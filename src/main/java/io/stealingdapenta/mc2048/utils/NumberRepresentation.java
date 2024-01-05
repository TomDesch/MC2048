package io.stealingdapenta.mc2048.utils;

import static io.stealingdapenta.mc2048.MC2048.logger;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum NumberRepresentation {
    TWO(2, 2, Material.RAW_COPPER),
    FOUR(4, 4, Material.COPPER_INGOT),
    EIGHT(8, 8, Material.COPPER_BLOCK),
    SIXTEEN(16, 16, Material.RAW_IRON),
    THIRTY_TWO(32, 32, Material.IRON_NUGGET),
    SIXTY_FOUR(64, 64, Material.IRON_INGOT),
    HUNDRED_TWENTY_EIGHT(2, 128, Material.IRON_BLOCK),
    TWO_HUNDRED_FIFTY_SIX(4, 256, Material.RAW_GOLD),
    FIVE_HUNDRED_TWELVE(8, 512, Material.GOLD_NUGGET),
    ONE_THOUSAND_TWENTY_FOUR(16, 1024, Material.GOLD_INGOT),
    TWO_THOUSAND_FORTY_EIGHT(32, 2048, Material.GOLD_BLOCK),
    FOUR_THOUSAND_NINETY_SIX(64, 4096, Material.EMERALD),
    EIGHT_THOUSAND_ONE_HUNDRED_NINETY_TWO(2, 8192, Material.EMERALD_BLOCK),
    SIXTEEN_THOUSAND_THREE_HUNDRED_EIGHTY_FOUR(4, 16384, Material.LAPIS_LAZULI),
    THIRTY_TWO_THOUSAND_SEVEN_HUNDRED_SIXTY_EIGHT(8, 32768, Material.LAPIS_BLOCK),
    SIXTY_FIVE_THOUSAND_FIVE_HUNDRED_THIRTY_SIX(16, 65536, Material.DIAMOND),
    HUNDRED_THIRTY_ONE_THOUSAND_SEVENTY_TWO(32, 131072, Material.DIAMOND_BLOCK),
    INFINITY(64, Integer.MAX_VALUE, Material.END_STONE);

    private final int amount;
    private final int score;
    private final Material representation;

    NumberRepresentation(int amount, int score, Material representation) {
        this.amount = amount;
        this.score = score;
        this.representation = representation;
    }

    public static int getScoreFromItem(ItemStack itemStack) {
        for (NumberRepresentation representation : NumberRepresentation.values()) {
            if (representation.getRepresentation() == itemStack.getType()) {
                return representation.getScore();
            }
        }
        logger.severe("Error getting representation for %s. Returning 0!".formatted(itemStack.getType()));
        return 0;
    }

    public static NumberRepresentation getNextRepresentation(int currentRepresentation) {
        NumberRepresentation[] representations = NumberRepresentation.values();

        for (int i = 0; i < representations.length - 1; i++) {
            if (representations[i].getScore() == currentRepresentation) {
                return representations[i + 1];
            }
        }

        logger.severe("Error getting next representation for %s. Returning TWO!".formatted(currentRepresentation));
        return TWO;
    }


    public int getAmount() {
        return amount;
    }

    public int getScore() {
        return score;
    }

    public Material getRepresentation() {
        return representation;
    }

    public ItemStack getDisplayableBlock() {
        return new ItemStack(representation, amount);
    }
}
