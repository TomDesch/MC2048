# MC2048

MC2048 is a stand-alone plugin that allows you to play the iconic game on your Minecraft server.

It targets the official Spigot API 26.3 and supports both Spigot and Paper servers. The plugin bytecode targets Java 21.
Run the server with a JDK supported by the selected Spigot or Paper release. The build JDK is separate from the server
runtime JDK. In particular, Spigot 26.3 uses Java 25 or 26 rather than the JDK 27 used to build this project.

## Installation

Download the plugin for free from [Spigot](https://www.spigotmc.org/resources/mc2048-%E2%AD%95-1-21%E2%9C%85.114328/) and drop it into your plugin folder.

Deploy `target/MC2048-1.4.0-shaded.jar`. Do not deploy the unshaded JAR.

## Usage

Plug and play. On first launch, the plugin creates the config file.

```
/2048 : Opens the game window
/2048top : Shows the top 10, and your position in that list
/2048reload : Reloads the plugin config (required permission: mc2048.reload)
/2048help :  Show the legend and general information.
```

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update / add tests as appropriate.

## Building

Build with JDK 27 and the checked-in Maven wrapper:

```text
.\mvnw.cmd -B -ntp clean verify
python .github\scripts\verify_artifact.py
```

The compiler emits Java 21 bytecode. The Maven wrapper uses Maven 3.10.0.

## License

[MIT](https://choosealicense.com/licenses/mit/)
