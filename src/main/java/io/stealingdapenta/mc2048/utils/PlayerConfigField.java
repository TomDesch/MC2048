package io.stealingdapenta.mc2048.utils;

import io.stealingdapenta.mc2048.MC2048;
import io.stealingdapenta.mc2048.config.ConfigKey;

public enum PlayerConfigField {
    HIGH_SCORE("high-score"),
    ATTEMPTS("Attempts"),
    PLAYTIME("playtime"),
    AVERAGE_SCORE("average"),
    ANIMATION_SPEED("speed");

    private final String key;

    PlayerConfigField(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    /**
     * @return the stored value from the config as an integer NumberFormatException safe
     */
    public int getIntValue() {
        String value = MC2048.getInstance().getConfig().getString(name().toLowerCase());
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return ConfigKey.DEFAULT_ANIMATION_SPEED.getIntValue();
        }
    }
}
