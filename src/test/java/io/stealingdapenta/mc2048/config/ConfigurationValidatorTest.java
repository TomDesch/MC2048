package io.stealingdapenta.mc2048.config;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfigurationValidatorTest {

    private Map<ConfigKey, String> values;

    @BeforeEach
    void setUp() {
        values = loadPackagedDefaults();
    }

    @Test
    void validateWithPackagedDefaultsDoesNotThrow() {
        assertDoesNotThrow(() -> ConfigurationValidator.validate(values));
    }

    @Test
    void validateWithInvalidMaterialThrows() {
        values.put(ConfigKey.MATERIAL_TWO, "NOT_A_MATERIAL");

        assertThrows(IllegalArgumentException.class, () -> ConfigurationValidator.validate(values));
    }

    @Test
    void validateWithBoardSlotThrows() {
        values.put(ConfigKey.MOVE_BUTTON_UP_SLOT, "10");

        assertThrows(IllegalArgumentException.class, () -> ConfigurationValidator.validate(values));
    }

    @Test
    void validateWithConflictingSlotsThrows() {
        values.put(ConfigKey.MOVE_BUTTON_DOWN_SLOT, values.get(ConfigKey.MOVE_BUTTON_UP_SLOT));

        assertThrows(IllegalArgumentException.class, () -> ConfigurationValidator.validate(values));
    }

    @Test
    void validateWithDisabledOptionalItemsDoesNotThrow() {
        values.put(ConfigKey.UNDO_BUTTON_SLOT, "-1");
        values.put(ConfigKey.SPEED_BUTTON_SLOT, "-1");
        values.put(ConfigKey.PLAYER_ITEM_SLOT, "-1");

        assertDoesNotThrow(() -> ConfigurationValidator.validate(values));
    }

    private Map<ConfigKey, String> loadPackagedDefaults() {
        InputStream input = Objects.requireNonNull(
            getClass().getClassLoader().getResourceAsStream("config.yml"));
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(
            new InputStreamReader(input, StandardCharsets.UTF_8));
        Map<ConfigKey, String> defaults = new EnumMap<>(ConfigKey.class);
        for (ConfigKey key : ConfigKey.values()) {
            defaults.put(key, configuration.getString(key.path()));
        }
        return defaults;
    }
}
