package io.stealingdapenta.mc2048.utils.data;

import org.bukkit.inventory.ItemStack;

public class MovementInstruction {

    public final int fromRow;
    public final int fromColumn;
    public final int toRow;
    public final int toColumn;
    public final int step;               // The step number for this instruction (1-indexed)
    public int totalSteps;               // Total number of steps required for the full move
    public final ItemStack item;
    public final boolean merge;          // True if this step results in a merge
    public final ItemStack mergedItem;   // The new item after merging (if applicable)

    public MovementInstruction(int fromRow, int fromColumn, int toRow, int toColumn, 
                               ItemStack item, boolean merge, ItemStack mergedItem,
                               int step, int totalSteps) {
        this.fromRow = fromRow;
        this.fromColumn = fromColumn;
        this.toRow = toRow;
        this.toColumn = toColumn;
        this.item = item;
        this.merge = merge;
        this.mergedItem = mergedItem;
        this.step = step;
        this.totalSteps = totalSteps;
    }
}