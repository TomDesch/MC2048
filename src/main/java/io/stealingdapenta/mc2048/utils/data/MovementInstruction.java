package io.stealingdapenta.mc2048.utils.data;

import org.bukkit.inventory.ItemStack;

public record MovementInstruction(
    int fromRow,
    int fromColumn,
    int toRow,
    int toColumn,
    ItemStack item,
    boolean merge,
    ItemStack mergedItem,
    int step,
    int totalSteps
) {
}