package io.stealingdapenta.mc2048.config;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public final class ConfigurationValidator {

    private static final Set<Integer> BOARD_SLOTS = Set.of(
        10, 11, 12, 13,
        19, 20, 21, 22,
        28, 29, 30, 31,
        37, 38, 39, 40
    );
    private static final List<ConfigKey> REQUIRED_SLOTS = List.of(
        ConfigKey.MOVE_BUTTON_UP_SLOT,
        ConfigKey.MOVE_BUTTON_DOWN_SLOT,
        ConfigKey.MOVE_BUTTON_LEFT_SLOT,
        ConfigKey.MOVE_BUTTON_RIGHT_SLOT
    );
    private static final List<ConfigKey> OPTIONAL_SLOTS = List.of(
        ConfigKey.UNDO_BUTTON_SLOT,
        ConfigKey.SPEED_BUTTON_SLOT,
        ConfigKey.RESET_BUTTON_SLOT,
        ConfigKey.PLAYER_ITEM_SLOT
    );

    private ConfigurationValidator() {
    }

    public static void validate(FileConfiguration configuration) {
        Map<ConfigKey, String> values = new EnumMap<>(ConfigKey.class);
        for (ConfigKey key : ConfigKey.values()) {
            values.put(key, configuration.getString(key.path()));
        }
        validate(values);
    }

    static void validate(Map<ConfigKey, String> values) {
        Map<ConfigKey, String> errors = new EnumMap<>(ConfigKey.class);

        for (ConfigKey key : ConfigKey.values()) {
            String value = values.get(key);
            if (value == null) {
                errors.put(key, key.path() + " is missing");
            } else if (isMaterialKey(key) && Material.matchMaterial(value) == null) {
                errors.put(key, key.path() + " is not a valid material: " + value);
            }
        }

        validateRange(values, ConfigKey.GENERATE_NEW_BLOCK_FOUR_PERCENT, 0, 100, errors);
        validateRange(values, ConfigKey.SPEED_BUTTON_SPEED_DEFAULT, 1, 6, errors);
        validateRange(values, ConfigKey.UNDO_BUTTON_USAGES, -1, Integer.MAX_VALUE, errors);

        Set<Integer> occupiedSlots = new HashSet<>();
        REQUIRED_SLOTS.forEach(key -> validateSlot(values, key, false, occupiedSlots, errors));
        OPTIONAL_SLOTS.forEach(key -> validateSlot(values, key, true, occupiedSlots, errors));

        if (!errors.isEmpty()) {
            throw new ConfigurationValidationException(errors);
        }
    }

    private static boolean isMaterialKey(ConfigKey key) {
        return !key.name().endsWith("_CMD")
            && (key.name().startsWith("MATERIAL_") || key.name().endsWith("_MATERIAL"));
    }

    private static void validateSlot(Map<ConfigKey, String> values, ConfigKey key, boolean optional,
                                     Set<Integer> occupiedSlots, Map<ConfigKey, String> errors) {
        Integer slot = parseInteger(values, key, errors);
        if (slot == null) {
            return;
        }
        if (optional && slot == -1) {
            return;
        }
        if (slot < 0 || slot >= 54) {
            errors.put(key, key.path() + " must be between 0 and 53" + (optional ? ", or -1" : ""));
        } else if (BOARD_SLOTS.contains(slot)) {
            errors.put(key, key.path() + " cannot use game-board slot " + slot);
        } else if (!occupiedSlots.add(slot)) {
            errors.put(key, key.path() + " conflicts with another item at slot " + slot);
        }
    }

    private static void validateRange(Map<ConfigKey, String> values, ConfigKey key, int minimum, int maximum,
                                      Map<ConfigKey, String> errors) {
        Integer value = parseInteger(values, key, errors);
        if (value != null && (value < minimum || value > maximum)) {
            errors.put(key, key.path() + " must be between " + minimum + " and " + maximum);
        }
    }

    private static Integer parseInteger(Map<ConfigKey, String> values, ConfigKey key, Map<ConfigKey, String> errors) {
        String value = values.get(key);
        if (value == null) {
            return null;
        }
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException exception) {
            errors.put(key, key.path() + " must be an integer: " + value);
            return null;
        }
    }
}
