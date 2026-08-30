package io.stealingdapenta.mc2048.config;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.EnumMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfigurationValidatorTest {

    private Map<ConfigKey, String> values;

    @BeforeEach
    void setUp() {
        values = new EnumMap<>(ConfigKey.class);
        for (ConfigKey key : ConfigKey.values()) {
            values.put(key, key.getDefaultValue());
        }
    }

    @Test
    void acceptsTheDefaultConfiguration() {
        assertDoesNotThrow(() -> ConfigurationValidator.validate(values));
    }

    @Test
    void rejectsInvalidMaterials() {
        values.put(ConfigKey.MATERIAL_TWO, "NOT_A_MATERIAL");

        assertThrows(IllegalArgumentException.class, () -> ConfigurationValidator.validate(values));
    }

    @Test
    void rejectsSlotsOnTheGameBoard() {
        values.put(ConfigKey.MOVE_BUTTON_UP_SLOT, "10");

        assertThrows(IllegalArgumentException.class, () -> ConfigurationValidator.validate(values));
    }

    @Test
    void rejectsConflictingSlots() {
        values.put(ConfigKey.MOVE_BUTTON_DOWN_SLOT, values.get(ConfigKey.MOVE_BUTTON_UP_SLOT));

        assertThrows(IllegalArgumentException.class, () -> ConfigurationValidator.validate(values));
    }

    @Test
    void permitsDisabledOptionalItems() {
        values.put(ConfigKey.UNDO_BUTTON_SLOT, "-1");
        values.put(ConfigKey.SPEED_BUTTON_SLOT, "-1");
        values.put(ConfigKey.PLAYER_ITEM_SLOT, "-1");

        assertDoesNotThrow(() -> ConfigurationValidator.validate(values));
    }
}
