package io.stealingdapenta.mc2048.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum NumberRepresentations {
    TWO(2, Material.BONE_BLOCK),
    FOUR(4, Material.HONEY_BLOCK),
    EIGHT(8, Material.HONEYCOMB_BLOCK),
    SIXTEEN(16, Material.HORN_CORAL_BLOCK),
    THIRTY_TWO(32, Material.RAW_GOLD_BLOCK),
    SIXTY_FOUR(64, Material.GOLD_BLOCK),
    HUNDRED_TWENTY_EIGHT(128, Material.BONE_BLOCK),
    TWO_HUNDRED_FIFTY_SIX(256, Material.OBSIDIAN),
    FIVE_HUNDRED_TWELVE(512, Material.NETHER_BRICK),
    ONE_THOUSAND_TWENTY_FOUR(1024, Material.NETHERITE_BLOCK),
    TWO_THOUSAND_FORTY_EIGHT(2048, Material.DIAMOND_BLOCK),
    FOUR_THOUSAND_NINETY_SIX(4096, Material.EMERALD_BLOCK),
    EIGHT_THOUSAND_ONE_HUNDRED_NINETY_TWO(8192, Material.DIAMOND_BLOCK),
    SIXTEEN_THOUSAND_THREE_HUNDRED_EIGHTY_FOUR(16384, Material.END_STONE);

    private final int number;
    private final Material representation;

    NumberRepresentations(int number, Material representation) {
        this.number = number;
        this.representation = representation;
    }

    public int getNumber() {
        return number;
    }

    public Material getRepresentation() {
        return representation;
    }

    public ItemStack getDisplayableBlock() {
        return new ItemStack(representation, number);
    }
}
