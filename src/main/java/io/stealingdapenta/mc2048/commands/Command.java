package io.stealingdapenta.mc2048.commands;

public enum Command {

    TOP_2048("top2048"),

    _2048("2048");


    private final String commandName;

    Command(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
