package io.stealingdapenta.mc2048.config;


import static io.stealingdapenta.mc2048.MC2048.logger;

import io.stealingdapenta.mc2048.MC2048;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;

public enum ConfigKey {

    MSG_TOP_TEN("<gold>Top 10 High Scores on this server:</gold>"),
    MSG_TOP_SELF("<white>Your <bold>Position</bold>: %s</white>"),
    MSG_NO_PERMS("<red>You don't have the required <bold>permission</bold> to execute this command.</red>"),
    MSG_NOT_PLAYER("<light_purple>You can only execute this command as a <bold>player</bold>.</light_purple>"),
    MSG_RELOADED("<green>Successfully MSG_RELOADED the <italic>MC2048</italic> configuration file.</green>"),
    MSG_PARSING_ERROR("<yellow>Error parsing the value in the config file for <red><bold>%s</bold></red></yellow>"),
    MSG_GAME_STARTED("<aqua>Good luck and <bold>have fun</bold>!</aqua>"),
    MSG_INVALID_MOVE("<red>Sorry! <bold>That's not a valid move</bold>.</red>"),
    MSG_UNDID_LAST_MOVE("<green>Successfully undid the <bold>last move</bold>!</green>"),
    MSG_ATTEMPT_PROTECTION("<gray>The game wasn't saved because you didn't do anything. Your <bold>average score</bold> and <italic>attempts</italic> were protected.</gray>"),
    MSG_GAME_OVER("<dark_red><bold>Game over!</bold></dark_red>"),
    TITLE_GAME_OVER("<dark_red><bold>Game over!</bold></dark_red>"),
    TITLE_GAME_OVER_SUB("Score: %s | Playtime: %k"),

    GAME_GUI_TITLE("        <blue><b>MC 2048:</b></blue> <dark_gray>%s Points"),
    GAME_GUI_FILLER_MATERIAL("WHITE_STAINED_GLASS_PANE"),
    GAME_GUI_FILLER_MATERIAL_CMD("1000"),
    GAME_GUI_FILLER_ANIMATION("enabled"),
    GAME_GUI_FILLER_ANIMATION_MATERIAL("BLACK_STAINED_GLASS_PANE"),
    GAME_GUI_FILLER_ANIMATION_MATERIAL_CMD("1000"),
    MOVE_BUTTON_UP_NAME("<green><bold>            UP</bold></green>"),
    MOVE_BUTTON_UP_MATERIAL("LIGHTNING_ROD"),
    MOVE_BUTTON_UP_MATERIAL_CMD("1000"),
    MOVE_BUTTON_UP_SLOT("16"),
    MOVE_BUTTON_DOWN_NAME("<green><bold>          DOWN</bold></green>"),
    MOVE_BUTTON_DOWN_MATERIAL("LIGHTNING_ROD"),
    MOVE_BUTTON_DOWN_MATERIAL_CMD("1000"),
    MOVE_BUTTON_DOWN_SLOT("34"),
    MOVE_BUTTON_LEFT_NAME("<green><bold>          LEFT</bold></green>"),
    MOVE_BUTTON_LEFT_MATERIAL("LIGHTNING_ROD"),
    MOVE_BUTTON_LEFT_MATERIAL_CMD("1000"),
    MOVE_BUTTON_LEFT_SLOT("24"),
    MOVE_BUTTON_RIGHT_NAME("<green><bold>          RIGHT</bold></green>"),
    MOVE_BUTTON_RIGHT_MATERIAL("LIGHTNING_ROD"),
    MOVE_BUTTON_RIGHT_MATERIAL_CMD("1000"),
    MOVE_BUTTON_RIGHT_SLOT("26"),
    MOVE_BUTTON_LORE("<aqua>Click to move <bold>everything!</bold></aqua>"),

    UNDO_BUTTON_UNUSED_NAME("<green><bold>         UNDO</bold></green>"),
    UNDO_BUTTON_UNUSED_MATERIAL("AXOLOTL_BUCKET"),
    UNDO_BUTTON_UNUSED_MATERIAL_CMD("1000"),
    UNDO_BUTTON_UNUSED_LORE("<aqua>Click to undo the <bold>last move</bold>!</aqua>"),
    UNDO_BUTTON_UNUSED_USES("<blue>Uses left: "),
    UNDO_BUTTON_USED_NAME("<green><bold>         UNDO</bold></green>"),
    UNDO_BUTTON_USED_MATERIAL("BUCKET"),
    UNDO_BUTTON_USED_MATERIAL_CMD("1000"),
    UNDO_BUTTON_USED_LORE("<aqua>Click to undo the <bold>last move</bold>!</aqua>"),
    UNDO_BUTTON_USED_USES("<blue>Uses left: <red>0</red></blue>"),
    UNDO_BUTTON_USAGES("1"),
    UNDO_BUTTON_SLOT("42"),

    SPEED_BUTTON_NAME("<green><bold>         SPEED</bold></green>"),
    SPEED_BUTTON_MATERIAL("STONE_BUTTON"),
    SPEED_BUTTON_MATERIAL_CMD("1000"),
    SPEED_BUTTON_LORE("<aqua>Click to change the <bold>animation speed</bold>!</aqua>"),
    SPEED_BUTTON_SPEED("<blue>Current speed: "),
    SPEED_BUTTON_SPEED_DEFAULT("3"),
    SPEED_BUTTON_SLOT("44"),

    PLAYER_ITEM_SLOT("25"),
    PLAYER_ITEM_NAME("<gold><bold>%s's Statistics</bold></gold>"),
    PLAYER_ITEM_MATERIAL("PLAYER_HEAD"),
    PLAYER_ITEM_MATERIAL_CMD("1000"),
    PLAYER_ITEM_LORE_TOTAL_PLAYTIME("<b>Total playtime: <green>%s</green></b>"),
    PLAYER_ITEM_LORE_GAMES_PLAYED("Amount of games played: <green>%s</green>"),
    PLAYER_ITEM_LORE_HIGH_SCORE("<b>HiScore: <green>%s</green></b>"),
    PLAYER_ITEM_LORE_AVERAGE_SCORE("Average Score: <green>%s</green>"),
    PLAYER_ITEM_LORE_CURRENT_SCORE("Current score: <green>%s</green>"),
    PLAYER_ITEM_LORE_CURRENT_PLAYTIME("<b>Current playtime: <green>%s</green></b>"),

    HELP_GUI_TITLE("     <blue><b>MC2048 HELP WINDOW"),
    HELP_GUI_FILLER_MATERIAL("LIGHT_BLUE_STAINED_GLASS_PANE"),
    HELP_GUI_FILLER_MATERIAL_CMD("1000"),
    HELP_GUI_LEGEND_ITEM_NAME_PREFIX("<b>Points: "),

    START_BUTTON_MATERIAL("NETHERITE_UPGRADE_SMITHING_TEMPLATE"),
    START_BUTTON_MATERIAL_CMD("1000"),
    START_BUTTON_NAME("Click to start game!"),
    START_BUTTON_LORE("Click to play the iconic 2048 game!"),

    HIGH_SCORE_ITEM_NAME("<blue><bold>High scores"),
    HIGH_SCORE_ITEM_MATERIAL("SKULL_BANNER_PATTERN"),
    HIGH_SCORE_ITEM_MATERIAL_CMD("1000"),
    HIGH_SCORE_ITEM_LORE_FORMAT("<aqua>%s</aqua>"),

    INFO_ITEM_NAME("<b>MC2048 Information"),
    INFO_ITEM_MATERIAL("AMETHYST_CLUSTER"),
    INFO_ITEM_MATERIAL_CMD("1000"),
    INFO_ITEM_LORE_1("MC2048 is a game where you match numbers."),
    INFO_ITEM_LORE_2("If you put two of the same numbers together, they combine into their sum."),
    INFO_ITEM_LORE_3("Keep combining numbers to make the biggest one you can."),
    INFO_ITEM_LORE_4("If the board fills up, the game is over."),
    INFO_ITEM_LORE_5("Aim for the highest number!"),

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
    MATERIAL_INFINITY_CMD("1000");

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
        String raw = getStringValue();

        if (raw.contains("&"))
            return LegacyComponentSerializer.legacyAmpersand().deserialize(raw);
        else
            return MiniMessage.miniMessage().deserialize(raw);
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
     * Replace %s in the component with the provided replacement value
     *
     * @param replacementOfPercentS the desired replacement text
     * @param replacementOfPercentK the desired replacement text
     * @return the original formatted component but with %s replaced
     */
    public Component getFormattedValue(String replacementOfPercentS, String replacementOfPercentK) {
        return getFormattedValue(replacementOfPercentS).replaceText(builder -> builder.match("%k")
                                                                 .replacement(replacementOfPercentK));
    }

    /**
     * @return the stored value from the config as an integer NumberFormatException safe
     */
    public int getIntValue() {
        String value = MC2048.getInstance().getConfig().getString(name().toLowerCase());
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.warning(MSG_PARSING_ERROR.getStringValue() + name().toLowerCase());
            return 0;
        }
    }
}