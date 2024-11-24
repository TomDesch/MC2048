package io.stealingdapenta.mc2048.config;


import static io.stealingdapenta.mc2048.MC2048.logger;

import io.stealingdapenta.mc2048.MC2048;
import io.stealingdapenta.mc2048.utils.TextUtil;
import java.util.Objects;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public enum ConfigKey {

    NO_PERMISSION("You don't have the required permission to execute this command."),
    RELOADED("Successfully reloaded the DamageIndicator configuration file."),
    PARSING_ERROR("Error parsing the value in the config file for "),
    NOT_PLAYER("You can only execute this command as a player."),
    GOOD_LUCK("Good luck & have fun!"),
    TOP_TEN("Top 10 High Scores on this server:"),
    PLAYER_POSITION("Your Position: "),
    INVALID_MOVE("Sorry! That's not a valid move."),
    UNDID_LAST_MOVE("Successfully undid the last move!"),
    GAME_OVER("Game over!"),
    ATTEMPT_PROTECTION("The game wasn't saved because you didn't do anything. Your average score & attempts were protected."),
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
    MATERIAL_INFINITY("END_STONE");

    private static final TextUtil textUtil = TextUtil.getInstance();
    private final String defaultValue;

    ConfigKey(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }


    public Material getMaterialValue() {
        return Material.valueOf(name());
    }

    public String getStringValue() {
        return MC2048.getInstance().getConfig().getString(name().toLowerCase());
    }

    public TextComponent getFormattedValue() {
        return textUtil.parseFormattedString(getStringValue());
    }

    public String getFormattedStringValue() {
        return getFormattedValue().toString();
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
