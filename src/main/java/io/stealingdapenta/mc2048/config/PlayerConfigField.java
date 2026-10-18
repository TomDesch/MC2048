package io.stealingdapenta.mc2048.config;

public enum PlayerConfigField {
    HIGH_SCORE("high-score"),
    ATTEMPTS("Attempts"),
    PLAYTIME("playtime"),
    AVERAGE_SCORE("average"),
    ANIMATION_SPEED("speed-level");

    private final String key;

    PlayerConfigField(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
