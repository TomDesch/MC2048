package io.stealingdapenta.mc2048.commands;

public enum Command {

    TOP("top"),
    _2048("2048"),
    RELOAD("reload");

    private final String commandName;

    Command(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
