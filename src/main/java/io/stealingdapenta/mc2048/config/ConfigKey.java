package io.stealingdapenta.mc2048.config;


import static io.stealingdapenta.mc2048.MC2048.logger;

import io.stealingdapenta.mc2048.MC2048;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public enum ConfigKey {

    NO_PERMISSION("<red>You don't have the required <bold>permission</bold> to execute this command.</red>"),
    RELOADED("<green>Successfully reloaded the <italic>DamageIndicator</italic> configuration file.</green>"),
    PARSING_ERROR("<yellow>Error parsing the value in the config file for <red><bold>%s</bold></red></yellow>"),
    NOT_PLAYER("<light_purple>You can only execute this command as a <bold>player</bold>.</light_purple>"),
    GOOD_LUCK("<aqua>Good luck and <bold>have fun</bold>!</aqua>"),
    TOP_TEN("<gold>Top 10 High Scores on this server:</gold>"),
    PLAYER_POSITION("<white>Your <bold>Position</bold>: </white>"),
    INVALID_MOVE("<red>Sorry! <bold>That's not a valid move</bold>.</red>"),
    UNDID_LAST_MOVE("<green>Successfully undid the <bold>last move</bold>!</green>"),
    GAME_OVER("<dark_red><bold>Game over!</bold></dark_red>"),
    ATTEMPT_PROTECTION(
            "<gray>The game wasn't saved because you didn't do anything. Your <bold>average score</bold> and <italic>attempts</italic> were protected.</gray>"),
    NUMBER_OF_UNDO("1"),
    MATERIAL_TWO("RAW_COPPER"),
    MATERIAL_FOUR("COPPER_INGOT"),
    MATERIAL_EIGHT("COPPER_BLOCK"),
    MATERIAL_SIXTEEN("RAW_IRON"),
    MATERIAL_THIRTY_TWO("IRON_NUGGET"),
    MATERIAL_SIXTY_FOUR("IRON_INGOT"),
    MATERIAL_HUNDRED_TWENTY_EIGHT("IRON_BLOCK"),
    MATERIAL_TWO_HUNDRED_FIFTY_SIX("RAW_GOLD"),
    MATERIAL_FIVE_HUNDRED_TWELVE("GOLD_NUGGET"),
    MATERIAL_ONE_THOUSAND_TWENTY_FOUR("GOLD_INGOT"),
    MATERIAL_TWO_THOUSAND_FORTY_EIGHT("GOLD_BLOCK"),
    MATERIAL_FOUR_THOUSAND_NINETY_SIX("EMERALD"),
    MATERIAL_EIGHT_THOUSAND_ONE_HUNDRED_NINETY_TWO("EMERALD_BLOCK"),
    MATERIAL_SIXTEEN_THOUSAND_THREE_HUNDRED_EIGHTY_FOUR("LAPIS_LAZULI"),
    MATERIAL_THIRTY_TWO_THOUSAND_SEVEN_HUNDRED_SIXTY_EIGHT("LAPIS_BLOCK"),
    MATERIAL_SIXTY_FIVE_THOUSAND_FIVE_HUNDRED_THIRTY_SIX("DIAMOND"),
    MATERIAL_HUNDRED_THIRTY_ONE_THOUSAND_SEVENTY_TWO("DIAMOND_BLOCK"),
    MATERIAL_INFINITY("END_STONE"),
    UP_BUTTON_NAME("<green><bold>            UP</bold></green>"),
    LEFT_BUTTON_NAME("<green><bold>          LEFT</bold></green>"),
    RIGHT_BUTTON_NAME("<green><bold>          RIGHT</bold></green>"),
    DOWN_BUTTON_NAME("<green><bold>          DOWN</bold></green>"),
    UNDO_BUTTON_NAME("<green><bold>         UNDO</bold></green>"),
    UNDO_BUTTON_UNUSED_LORE("<aqua>Click to undo the <bold>last move</bold>!</aqua>"),
    UNDO_BUTTON_UNUSED_USES("<blue>Uses left: "),
    UNDO_BUTTON_USED_LORE("<aqua>Click to undo the <bold>last move</bold>!</aqua>"),
    UNDO_BUTTON_USED_USES("<blue>Uses left: <red>0</red></blue>"),
    MOVE_BUTTON_LORE("<aqua>Click to move <bold>everything!</bold></aqua>");

    private final String defaultValue;

    ConfigKey(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public Material getMaterialValue() {
        return Material.valueOf(getStringValue());
    }

    public String getStringValue() {
        return MC2048.getInstance().getConfig().getString(name().toLowerCase());
    }

    public Component getFormattedValue() {
        return MiniMessage.miniMessage().deserialize(getStringValue());
    }

    public int getIntValue() {
        return getIntValue(name().toLowerCase());
    }

    private int getIntValue(String key) {
        JavaPlugin plugin = MC2048.getInstance();
        String valueAsString = plugin.getConfig().getString(key);
        int result;
        if (Objects.isNull(valueAsString)) {
            valueAsString = "0";
        }
        try {
            result = Integer.parseInt(valueAsString);
        } catch (NumberFormatException numberFormatException) {
            logger.warning(PARSING_ERROR.getStringValue() + key);
            result = 0;
        }
        return result;
    }
}