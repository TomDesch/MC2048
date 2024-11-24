package io.stealingdapenta.mc2048.utils;

import static io.stealingdapenta.mc2048.MC2048.logger;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_EIGHT;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_EIGHT_THOUSAND_ONE_HUNDRED_NINETY_TWO;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_FIVE_HUNDRED_TWELVE;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_FOUR;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_FOUR_THOUSAND_NINETY_SIX;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_HUNDRED_THIRTY_ONE_THOUSAND_SEVENTY_TWO;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_HUNDRED_TWENTY_EIGHT;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_INFINITY;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_ONE_THOUSAND_TWENTY_FOUR;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_SIXTEEN;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_SIXTEEN_THOUSAND_THREE_HUNDRED_EIGHTY_FOUR;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_SIXTY_FIVE_THOUSAND_FIVE_HUNDRED_THIRTY_SIX;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_SIXTY_FOUR;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_THIRTY_TWO;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_THIRTY_TWO_THOUSAND_SEVEN_HUNDRED_SIXTY_EIGHT;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_TWO;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_TWO_HUNDRED_FIFTY_SIX;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_TWO_THOUSAND_FORTY_EIGHT;

import java.util.Arrays;
import java.util.function.Supplier;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum NumberRepresentation {
    TWO(2, 2, MATERIAL_TWO::getMaterialValue),
    FOUR(4, 4, MATERIAL_FOUR::getMaterialValue),
    EIGHT(8, 8, MATERIAL_EIGHT::getMaterialValue),
    SIXTEEN(16, 16, MATERIAL_SIXTEEN::getMaterialValue),
    THIRTY_TWO(32, 32, MATERIAL_THIRTY_TWO::getMaterialValue),
    SIXTY_FOUR(64, 64, MATERIAL_SIXTY_FOUR::getMaterialValue),
    HUNDRED_TWENTY_EIGHT(2, 128, MATERIAL_HUNDRED_TWENTY_EIGHT::getMaterialValue),
    TWO_HUNDRED_FIFTY_SIX(4, 256, MATERIAL_TWO_HUNDRED_FIFTY_SIX::getMaterialValue),
    FIVE_HUNDRED_TWELVE(8, 512, MATERIAL_FIVE_HUNDRED_TWELVE::getMaterialValue),
    ONE_THOUSAND_TWENTY_FOUR(16, 1024, MATERIAL_ONE_THOUSAND_TWENTY_FOUR::getMaterialValue),
    TWO_THOUSAND_FORTY_EIGHT(32, 2048, MATERIAL_TWO_THOUSAND_FORTY_EIGHT::getMaterialValue),
    FOUR_THOUSAND_NINETY_SIX(64, 4096, MATERIAL_FOUR_THOUSAND_NINETY_SIX::getMaterialValue),
    EIGHT_THOUSAND_ONE_HUNDRED_NINETY_TWO(2, 8192, MATERIAL_EIGHT_THOUSAND_ONE_HUNDRED_NINETY_TWO::getMaterialValue),
    SIXTEEN_THOUSAND_THREE_HUNDRED_EIGHTY_FOUR(4, 16384, MATERIAL_SIXTEEN_THOUSAND_THREE_HUNDRED_EIGHTY_FOUR::getMaterialValue),
    THIRTY_TWO_THOUSAND_SEVEN_HUNDRED_SIXTY_EIGHT(8, 32768, MATERIAL_THIRTY_TWO_THOUSAND_SEVEN_HUNDRED_SIXTY_EIGHT::getMaterialValue),
    SIXTY_FIVE_THOUSAND_FIVE_HUNDRED_THIRTY_SIX(16, 65536, MATERIAL_SIXTY_FIVE_THOUSAND_FIVE_HUNDRED_THIRTY_SIX::getMaterialValue),
    HUNDRED_THIRTY_ONE_THOUSAND_SEVENTY_TWO(32, 131072, MATERIAL_HUNDRED_THIRTY_ONE_THOUSAND_SEVENTY_TWO::getMaterialValue),
    INFINITY(64, Integer.MAX_VALUE, MATERIAL_INFINITY::getMaterialValue);

    private static final String ERROR_REPRESENTATION = "Error getting representation for %s. Returning 0!";
    private static final String ERROR_NEXT_REPRESENTATION = "Error getting next representation for %s. Returning TWO!";
    private final int amount;
    private final int score;
    private final Supplier<Material> materialSupplier;

    NumberRepresentation(int amount, int score, Supplier<Material> materialSupplier) {
        this.amount = amount;
        this.score = score;
        this.materialSupplier = materialSupplier;
    }

    public static int getScoreFromItem(ItemStack itemStack) {
        return Arrays.stream(NumberRepresentation.values()).filter(representation -> representation.getRepresentation() == itemStack.getType()).findFirst()
                     .map(NumberRepresentation::getScore).orElseGet(() -> {
                    logger.severe(ERROR_REPRESENTATION.formatted(itemStack.getType()));
                    return 0;
                });
    }

    public static NumberRepresentation getNextRepresentation(int currentRepresentation) {
        NumberRepresentation[] representations = NumberRepresentation.values();

        for (int i = 0; i < representations.length - 1; i++) {
            if (representations[i].getScore() == currentRepresentation) {
                return representations[i + 1];
            }
        }

        logger.severe(ERROR_NEXT_REPRESENTATION.formatted(currentRepresentation));
        return TWO;
    }


    public int getScore() {
        return score;
    }

    public Material getRepresentation() {
        return materialSupplier.get();
    }

    public ItemStack getDisplayableBlock() {
        return new ItemStack(getRepresentation(), amount);
    }
}
