package io.stealingdapenta.mc2048.persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

/**
 * Replaces a data file only after its complete new contents have been written successfully.
 *
 * <p>The system implementation writes a sibling temporary file and moves it over the target,
 * preventing an interrupted save from leaving a partially written player file.
 */
@FunctionalInterface
interface AtomicFileWriter {

    void write(Path target, String content) throws IOException;

    static AtomicFileWriter system() {
        return (target, content) -> {
            Files.createDirectories(target.getParent());
            Path temporary = target.resolveSibling(target.getFileName() + "." + UUID.randomUUID() + ".tmp");
            try {
                Files.writeString(
                    temporary,
                    content,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE_NEW,
                    StandardOpenOption.WRITE
                );
                try {
                    Files.move(
                        temporary,
                        target,
                        StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING
                    );
                } catch (AtomicMoveNotSupportedException exception) {
                    Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING);
                }
            } finally {
                Files.deleteIfExists(temporary);
            }
        };
    }
}
