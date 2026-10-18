package io.stealingdapenta.mc2048.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfigurationFileManagerTest {

    private Map<ConfigKey, String> values;

    @BeforeEach
    void setUp() {
        values = new EnumMap<>(ConfigKey.class);
        for (ConfigKey key : ConfigKey.values()) {
            values.put(key, key.name());
        }
    }

    @Test
    void hasSlotAffectingChangesWithUnchangedSlotsReturnsFalse() {
        ConfigurationSnapshot current = ConfigurationSnapshot.from(values);
        Map<ConfigKey, String> changedValues = new EnumMap<>(values);
        changedValues.put(ConfigKey.MSG_GAME_STARTED, "changed");
        ConfigurationSnapshot candidate = ConfigurationSnapshot.from(changedValues);

        assertFalse(ConfigurationFileManager.hasSlotAffectingChanges(current, candidate));
    }

    @Test
    void hasSlotAffectingChangesWithChangedControlSlotReturnsTrue() {
        ConfigurationSnapshot current = ConfigurationSnapshot.from(values);
        Map<ConfigKey, String> changedValues = new EnumMap<>(values);
        changedValues.put(ConfigKey.MOVE_BUTTON_UP_SLOT, "45");
        ConfigurationSnapshot candidate = ConfigurationSnapshot.from(changedValues);

        assertTrue(ConfigurationFileManager.hasSlotAffectingChanges(current, candidate));
    }

    @Test
    void validateReloadWithChangedControlSlotAndActiveGameRejectsCandidate() {
        ConfigurationSnapshot current = ConfigurationSnapshot.from(values);
        Map<ConfigKey, String> changedValues = new EnumMap<>(values);
        changedValues.put(ConfigKey.MOVE_BUTTON_UP_SLOT, "45");
        ConfigurationSnapshot candidate = ConfigurationSnapshot.from(changedValues);

        assertThrows(
            IllegalStateException.class,
            () -> ConfigurationFileManager.validateReload(current, candidate, true)
        );
    }
}
