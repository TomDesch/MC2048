package io.stealingdapenta.mc2048.config;

public enum Permission {
    RELOAD("mc2048.reload");
    private final String node;

    Permission(String node) {
        this.node = node;
    }

    public String getNode() {
        return node;
    }
}
