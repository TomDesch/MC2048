package io.stealingdapenta.mc2048.commands;

public enum Command {

    _2048("2048"),
    TOP("2048top"),
    RELOAD("2048reload"),
    HELP("2048help");

    private final String commandName;

    Command(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
