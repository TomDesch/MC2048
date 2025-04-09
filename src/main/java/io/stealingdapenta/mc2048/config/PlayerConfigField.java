package io.stealingdapenta.mc2048.config;

import io.stealingdapenta.mc2048.MC2048;

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
     * @return the stored animation speed from the player's config as an integer
     */
    public int getAnimationSpeed() {
        String value = MC2048.getInstance().getConfig().getString(name().toLowerCase());
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return ConfigKey.DEFAULT_ANIMATION_SPEED.getIntValue();
        }
    }
}
