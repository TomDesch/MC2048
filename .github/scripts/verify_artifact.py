"""Check the deployable shaded JAR."""

from pathlib import Path
import re
import struct
import zipfile

ARTIFACT_GLOB = "MC2048-*-shaded.jar"
PLUGIN_PACKAGE = "io/stealingdapenta/mc2048/"
RELOCATED_ROOT = PLUGIN_PACKAGE + "internal/"
BYTECODE_MAJOR = 65
API_VERSION_RE = r"(?m)^api-version:\s*['\"]26\.3['\"]\s*$"
REQUIRED_RESOURCES = (
    "plugin.yml",
    "config.yml",
    "META-INF/LICENSE",
    "META-INF/THIRD_PARTY_NOTICES.txt",
    "META-INF/licenses/kyori-MIT.txt",
    "META-INF/licenses/Apache-2.0.txt",
    "META-INF/licenses/jspecify-1.0.1-AUTHORS.txt",
)
RELOCATED_PREFIXES = (
    RELOCATED_ROOT + "kyori/",
    RELOCATED_ROOT + "jetbrains/annotations/",
    RELOCATED_ROOT + "jspecify/annotations/",
)
FORBIDDEN_PACKAGES = (
    "org/bukkit/",
    "io/papermc/",
    "com/destroystokyo/paper/",
    "net/minecraft/",
    "org/mockito/",
    "net/bytebuddy/",
    "org/objenesis/",
    "org/junit/",
    "com/google/",
    "org/yaml/",
    "net/kyori/",
    "org/jetbrains/",
    "org/intellij/",
    "org/jspecify/",
)


def require(condition, message):
    if not condition:
        raise RuntimeError(message)


artifacts = [
    path
    for path in Path("target").glob(ARTIFACT_GLOB)
    if not path.name.startswith("original-")
    and not path.stem.endswith(("-sources", "-javadoc", "-tests"))
]
require(len(artifacts) == 1, f"Expected one deployable shaded JAR, found {artifacts}")
artifact = artifacts[0]

with zipfile.ZipFile(artifact) as jar:
    entries = jar.namelist()
    for resource in REQUIRED_RESOURCES:
        require(resource in entries, f"Missing required resource: {resource}")

    own_classes = 0
    for entry in entries:
        if not entry.endswith(".class"):
            continue
        require(not entry.startswith(FORBIDDEN_PACKAGES), f"Bundled forbidden class: {entry}")
        require(not entry.endswith("module-info.class"), f"Module descriptor survived: {entry}")
        if entry.startswith(PLUGIN_PACKAGE) and not entry.startswith(RELOCATED_ROOT):
            own_classes += 1
            major = struct.unpack(">H", jar.read(entry)[6:8])[0]
            require(major == BYTECODE_MAJOR, f"Unexpected bytecode {major}: {entry}")

    require(own_classes > 0, "No plugin classes found")
    for prefix in RELOCATED_PREFIXES:
        require(
            any(entry.startswith(prefix) and entry.endswith(".class") for entry in entries),
            f"Missing relocated implementation: {prefix}",
        )

    metadata = jar.read("plugin.yml").decode("utf-8")
    require(re.search(API_VERSION_RE, metadata), "api-version is not quoted 26.3")

    project_license = jar.read("META-INF/LICENSE").decode("utf-8")
    require("permission notice shall be included" in project_license, "Project MIT license is incomplete")
    kyori_license = jar.read("META-INF/licenses/kyori-MIT.txt").decode("utf-8")
    require("KyoriPowered" in kyori_license, "Kyori attribution is missing")
    apache_license = jar.read("META-INF/licenses/Apache-2.0.txt").decode("utf-8")
    require("END OF TERMS AND CONDITIONS" in apache_license, "Apache-2.0 terms are incomplete")

    notices = jar.read("META-INF/THIRD_PARTY_NOTICES.txt").decode("utf-8")
    for coordinate in (
        "net.kyori:adventure-platform-bukkit:4.4.1",
        "net.kyori:adventure-api:4.21.0",
        "net.kyori:adventure-text-minimessage:4.21.0",
        "net.kyori:option:1.1.0",
        "org.jetbrains:annotations:26.0.2",
        "org.jspecify:jspecify:1.0.1",
    ):
        require(coordinate in notices, f"Bundled dependency attribution is stale: {coordinate}")

print(f"Verified deployable artifact: {artifact} ({artifact.stat().st_size:,} bytes)")
