package io.stealingdapenta.mc2048.utils;

public enum ConfigField {
    HIGH_SCORE("high-score"),
    ATTEMPTS("Attempts"),
    TOTAL_PLAYTIME("playtime"),
    AVERAGE_SCORE("average");

    private final String key;

    ConfigField(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
