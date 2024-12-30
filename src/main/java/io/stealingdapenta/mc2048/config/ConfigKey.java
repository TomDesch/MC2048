package io.stealingdapenta.mc2048.config;


import static io.stealingdapenta.mc2048.MC2048.logger;

import io.stealingdapenta.mc2048.MC2048;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;

public enum ConfigKey {

    NO_PERMISSION("<red>You don't have the required <bold>permission</bold> to execute this command.</red>"),
    RELOADED("<green>Successfully reloaded the <italic>MC2048</italic> configuration file.</green>"),
    PARSING_ERROR("<yellow>Error parsing the value in the config file for <red><bold>%s</bold></red></yellow>"),
    NOT_PLAYER("<light_purple>You can only execute this command as a <bold>player</bold>.</light_purple>"),
    GOOD_LUCK("<aqua>Good luck and <bold>have fun</bold>!</aqua>"),
    TOP_TEN("<gold>Top 10 High Scores on this server:</gold>"),
    PLAYER_POSITION("<white>Your <bold>Position</bold>: </white>"),
    INVALID_MOVE("<red>Sorry! <bold>That's not a valid move</bold>.</red>"),
    UNDID_LAST_MOVE("<green>Successfully undid the <bold>last move</bold>!</green>"),
    GAME_OVER("<dark_red><bold>Game over!</bold></dark_red>"),
    ATTEMPT_PROTECTION("<gray>The game wasn't saved because you didn't do anything. Your <bold>average score</bold> and <italic>attempts</italic> were protected.</gray>"),
    NUMBER_OF_UNDO("1"),
    MATERIAL_TWO("RAW_COPPER"),
    MATERIAL_TWO_CMD("1000"),
    MATERIAL_FOUR("COPPER_INGOT"),
    MATERIAL_FOUR_CMD("1000"),
    MATERIAL_EIGHT("COPPER_BLOCK"),
    MATERIAL_EIGHT_CMD("1000"),
    MATERIAL_SIXTEEN("RAW_IRON"),
    MATERIAL_SIXTEEN_CMD("1000"),
    MATERIAL_THIRTY_TWO("IRON_NUGGET"),
    MATERIAL_THIRTY_TWO_CMD("1000"),
    MATERIAL_SIXTY_FOUR("IRON_INGOT"),
    MATERIAL_SIXTY_FOUR_CMD("1000"),
    MATERIAL_HUNDRED_TWENTY_EIGHT("IRON_BLOCK"),
    MATERIAL_HUNDRED_TWENTY_EIGHT_CMD("1000"),
    MATERIAL_TWO_HUNDRED_FIFTY_SIX("RAW_GOLD"),
    MATERIAL_TWO_HUNDRED_FIFTY_SIX_CMD("1000"),
    MATERIAL_FIVE_HUNDRED_TWELVE("GOLD_NUGGET"),
    MATERIAL_FIVE_HUNDRED_TWELVE_CMD("1000"),
    MATERIAL_ONE_THOUSAND_TWENTY_FOUR("GOLD_INGOT"),
    MATERIAL_ONE_THOUSAND_TWENTY_FOUR_CMD("1000"),
    MATERIAL_TWO_THOUSAND_FORTY_EIGHT("GOLD_BLOCK"),
    MATERIAL_TWO_THOUSAND_FORTY_EIGHT_CMD("1000"),
    MATERIAL_FOUR_THOUSAND_NINETY_SIX("EMERALD"),
    MATERIAL_FOUR_THOUSAND_NINETY_SIX_CMD("1000"),
    MATERIAL_EIGHT_THOUSAND_ONE_HUNDRED_NINETY_TWO("EMERALD_BLOCK"),
    MATERIAL_EIGHT_THOUSAND_ONE_HUNDRED_NINETY_TWO_CMD("1000"),
    MATERIAL_SIXTEEN_THOUSAND_THREE_HUNDRED_EIGHTY_FOUR("LAPIS_LAZULI"),
    MATERIAL_SIXTEEN_THOUSAND_THREE_HUNDRED_EIGHTY_FOUR_CMD("1000"),
    MATERIAL_THIRTY_TWO_THOUSAND_SEVEN_HUNDRED_SIXTY_EIGHT("LAPIS_BLOCK"),
    MATERIAL_THIRTY_TWO_THOUSAND_SEVEN_HUNDRED_SIXTY_EIGHT_CMD("1000"),
    MATERIAL_SIXTY_FIVE_THOUSAND_FIVE_HUNDRED_THIRTY_SIX("DIAMOND"),
    MATERIAL_SIXTY_FIVE_THOUSAND_FIVE_HUNDRED_THIRTY_SIX_CMD("1000"),
    MATERIAL_HUNDRED_THIRTY_ONE_THOUSAND_SEVENTY_TWO("DIAMOND_BLOCK"),
    MATERIAL_HUNDRED_THIRTY_ONE_THOUSAND_SEVENTY_TWO_CMD("1000"),
    MATERIAL_INFINITY("END_STONE"),
    MATERIAL_INFINITY_CMD("1000"),
    MATERIAL_GUI_FILLER("WHITE_STAINED_GLASS_PANE"),
    MATERIAL_GUI_FILLER_CMD("1000"),
    MATERIAL_BUTTON_UP("LIGHTNING_ROD"),
    MATERIAL_BUTTON_UP_CMD("1000"),
    MATERIAL_BUTTON_RIGHT("LIGHTNING_ROD"),
    MATERIAL_BUTTON_RIGHT_CMD("1000"),
    MATERIAL_BUTTON_LEFT("LIGHTNING_ROD"),
    MATERIAL_BUTTON_LEFT_CMD("1000"),
    MATERIAL_BUTTON_DOWN("LIGHTNING_ROD"),
    MATERIAL_BUTTON_DOWN_CMD("1000"),
    MATERIAL_BUTTON_UNDO("AXOLOTL_BUCKET"),
    MATERIAL_BUTTON_UNDO_CMD("1000"),
    MATERIAL_BUTTON_UNDO_USED("BUCKET"),
    MATERIAL_BUTTON_UNDO_USED_CMD("1000"),
    MATERIAL_GUI_PLAYER("PLAYER_HEAD"),
    MATERIAL_GUI_PLAYER_CMD("1000"),
    UP_BUTTON_NAME("<green><bold>            UP</bold></green>"),
    LEFT_BUTTON_NAME("<green><bold>          LEFT</bold></green>"),
    RIGHT_BUTTON_NAME("<green><bold>          RIGHT</bold></green>"),
    DOWN_BUTTON_NAME("<green><bold>          DOWN</bold></green>"),
    UNDO_BUTTON_NAME("<green><bold>         UNDO</bold></green>"),
    UNDO_BUTTON_UNUSED_LORE("<aqua>Click to undo the <bold>last move</bold>!</aqua>"),
    UNDO_BUTTON_UNUSED_USES("<blue>Uses left: "),
    UNDO_BUTTON_USED_LORE("<aqua>Click to undo the <bold>last move</bold>!</aqua>"),
    UNDO_BUTTON_USED_USES("<blue>Uses left: <red>0</red></blue>"),
    MOVE_BUTTON_LORE("<aqua>Click to move <bold>everything!</bold></aqua>"),
    PLAYER_STATS_TITLE("<gold><bold>%s's Statistics</bold></gold>"),
    TOTAL_PLAYTIME("<b>Total playtime: <green>%s</green></b>"),
    CURRENT_PLAYTIME("<b>Current playtime: <green>%s</green></b>"),
    HIGH_SCORE("<b>HiScore: <green>%s</green></b>"),
    CURRENT_SCORE("<b>Current score: <green>%s</green></b>"),
    GAMES_PLAYED("<b>Amount of games played: <green>%s</green></b>"),
    AVERAGE_SCORE("<b>Average Score: <green>%s</green></b>"),
    GAME_TITLE("              <blue><bold>MC 2048"),
    MATERIAL_HELP_GUI_FILLER("LIGHT_BLUE_STAINED_GLASS_PANE"),
    MATERIAL_HELP_GUI_FILLER_CMD("1000"),
    MATERIAL_HELP_GUI_INFO("AMETHYST_CLUSTER"),
    HELP_GUI_INFO_NAME("MC2048 Information"),
    MATERIAL_HELP_GUI_INFO_CMD("1000"),
    MATERIAL_HELP_GUI_PLAY_BUTTON("NETHERITE_UPGRADE_SMITHING_TEMPLATE"),
    MATERIAL_HELP_GUI_PLAY_BUTTON_CMD("1000"),
    HELP_GUI_PLAY_BUTTON_NAME("Click to play!"),
    MATERIAL_HELP_GUI_HIGH_SCORE("SKULL_BANNER_PATTERN"),
    HELP_GUI_HIGH_SCORE_NAME("High scores"),
    MATERIAL_HELP_GUI_HIGH_SCORE_CMD("1000"),
    HELP_GUI_TITLE("     <blue><bold>MC2048 HELP WINDOW");

    private final String defaultValue;

    ConfigKey(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Used for first time config initialisation
     *
     * @return the hardcoded value from this ENUM config
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * @return the stored value from the config as a Material object
     * @throws IllegalArgumentException if the Bukkit Material enum has no constant with the specified name
     */
    public Material getMaterialValue() {
        return Material.valueOf(getStringValue());
    }

    /**
     * @return the stored value from the config as a String literal
     */
    public String getStringValue() {
        return MC2048.getInstance()
                     .getConfig()
                     .getString(name().toLowerCase());
    }

    /**
     * @return the stored value from the config as a Spigot 1.18+ formatted component
     */
    public Component getFormattedValue() {
        return MiniMessage.miniMessage()
                          .deserialize(getStringValue());
    }


    /**
     * Replace %s in the component with the provided replacement value
     *
     * @param replacementOfPercentS the desired replacement text
     * @return the original formatted component but with %s replaced
     */
    public Component getFormattedValue(String replacementOfPercentS) {
        return getFormattedValue().replaceText(builder -> builder.match("%s")
                                                                 .replacement(replacementOfPercentS));
    }

    /**
     * @return the stored value from the config as an integer NumberFormatException safe
     */
    public int getIntValue() {
        try {
            return MC2048.getInstance()
                         .getConfig()
                         .getInt(name().toLowerCase());
        } catch (NumberFormatException numberFormatException) {
            logger.warning(PARSING_ERROR.getStringValue() + name().toLowerCase());
            return 0;
        }
    }
}