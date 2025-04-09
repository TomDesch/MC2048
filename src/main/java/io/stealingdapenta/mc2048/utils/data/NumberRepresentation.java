package io.stealingdapenta.mc2048.utils.data;

import static io.stealingdapenta.mc2048.MC2048.logger;
import static io.stealingdapenta.mc2048.config.ConfigKey.HELP_GUI_LEGEND_ITEM_NAME_PREFIX;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_EIGHT;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_EIGHT_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_EIGHT_THOUSAND_ONE_HUNDRED_NINETY_TWO;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_EIGHT_THOUSAND_ONE_HUNDRED_NINETY_TWO_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_FIVE_HUNDRED_TWELVE;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_FIVE_HUNDRED_TWELVE_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_FOUR;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_FOUR_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_FOUR_THOUSAND_NINETY_SIX;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_FOUR_THOUSAND_NINETY_SIX_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_HUNDRED_THIRTY_ONE_THOUSAND_SEVENTY_TWO;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_HUNDRED_THIRTY_ONE_THOUSAND_SEVENTY_TWO_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_HUNDRED_TWENTY_EIGHT;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_HUNDRED_TWENTY_EIGHT_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_INFINITY;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_INFINITY_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_ONE_THOUSAND_TWENTY_FOUR;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_ONE_THOUSAND_TWENTY_FOUR_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_SIXTEEN;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_SIXTEEN_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_SIXTEEN_THOUSAND_THREE_HUNDRED_EIGHTY_FOUR;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_SIXTEEN_THOUSAND_THREE_HUNDRED_EIGHTY_FOUR_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_SIXTY_FIVE_THOUSAND_FIVE_HUNDRED_THIRTY_SIX;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_SIXTY_FIVE_THOUSAND_FIVE_HUNDRED_THIRTY_SIX_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_SIXTY_FOUR;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_SIXTY_FOUR_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_THIRTY_TWO;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_THIRTY_TWO_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_THIRTY_TWO_THOUSAND_SEVEN_HUNDRED_SIXTY_EIGHT;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_THIRTY_TWO_THOUSAND_SEVEN_HUNDRED_SIXTY_EIGHT_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_TWO;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_TWO_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_TWO_HUNDRED_FIFTY_SIX;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_TWO_HUNDRED_FIFTY_SIX_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_TWO_THOUSAND_FORTY_EIGHT;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_TWO_THOUSAND_FORTY_EIGHT_CMD;
import static io.stealingdapenta.mc2048.utils.ItemBuilder.setCustomModelDataTo;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum NumberRepresentation {
    TWO(2, 2, MATERIAL_TWO::getMaterialValue, MATERIAL_TWO_CMD::getIntValue),
    FOUR(4, 4, MATERIAL_FOUR::getMaterialValue, MATERIAL_FOUR_CMD::getIntValue),
    EIGHT(8, 8, MATERIAL_EIGHT::getMaterialValue, MATERIAL_EIGHT_CMD::getIntValue),
    SIXTEEN(16, 16, MATERIAL_SIXTEEN::getMaterialValue, MATERIAL_SIXTEEN_CMD::getIntValue),
    THIRTY_TWO(32, 32, MATERIAL_THIRTY_TWO::getMaterialValue, MATERIAL_THIRTY_TWO_CMD::getIntValue),
    SIXTY_FOUR(64, 64, MATERIAL_SIXTY_FOUR::getMaterialValue, MATERIAL_SIXTY_FOUR_CMD::getIntValue),
    HUNDRED_TWENTY_EIGHT(2, 128, MATERIAL_HUNDRED_TWENTY_EIGHT::getMaterialValue, MATERIAL_HUNDRED_TWENTY_EIGHT_CMD::getIntValue),
    TWO_HUNDRED_FIFTY_SIX(4, 256, MATERIAL_TWO_HUNDRED_FIFTY_SIX::getMaterialValue, MATERIAL_TWO_HUNDRED_FIFTY_SIX_CMD::getIntValue),
    FIVE_HUNDRED_TWELVE(8, 512, MATERIAL_FIVE_HUNDRED_TWELVE::getMaterialValue, MATERIAL_FIVE_HUNDRED_TWELVE_CMD::getIntValue),
    ONE_THOUSAND_TWENTY_FOUR(16, 1024, MATERIAL_ONE_THOUSAND_TWENTY_FOUR::getMaterialValue, MATERIAL_ONE_THOUSAND_TWENTY_FOUR_CMD::getIntValue),
    TWO_THOUSAND_FORTY_EIGHT(32, 2048, MATERIAL_TWO_THOUSAND_FORTY_EIGHT::getMaterialValue, MATERIAL_TWO_THOUSAND_FORTY_EIGHT_CMD::getIntValue),
    FOUR_THOUSAND_NINETY_SIX(64, 4096, MATERIAL_FOUR_THOUSAND_NINETY_SIX::getMaterialValue, MATERIAL_FOUR_THOUSAND_NINETY_SIX_CMD::getIntValue),
    EIGHT_THOUSAND_ONE_HUNDRED_NINETY_TWO(2, 8192, MATERIAL_EIGHT_THOUSAND_ONE_HUNDRED_NINETY_TWO::getMaterialValue, MATERIAL_EIGHT_THOUSAND_ONE_HUNDRED_NINETY_TWO_CMD::getIntValue),
    SIXTEEN_THOUSAND_THREE_HUNDRED_EIGHTY_FOUR(4, 16384, MATERIAL_SIXTEEN_THOUSAND_THREE_HUNDRED_EIGHTY_FOUR::getMaterialValue, MATERIAL_SIXTEEN_THOUSAND_THREE_HUNDRED_EIGHTY_FOUR_CMD::getIntValue),
    THIRTY_TWO_THOUSAND_SEVEN_HUNDRED_SIXTY_EIGHT(8, 32768, MATERIAL_THIRTY_TWO_THOUSAND_SEVEN_HUNDRED_SIXTY_EIGHT::getMaterialValue, MATERIAL_THIRTY_TWO_THOUSAND_SEVEN_HUNDRED_SIXTY_EIGHT_CMD::getIntValue),
    SIXTY_FIVE_THOUSAND_FIVE_HUNDRED_THIRTY_SIX(16, 65536, MATERIAL_SIXTY_FIVE_THOUSAND_FIVE_HUNDRED_THIRTY_SIX::getMaterialValue, MATERIAL_SIXTY_FIVE_THOUSAND_FIVE_HUNDRED_THIRTY_SIX_CMD::getIntValue),
    HUNDRED_THIRTY_ONE_THOUSAND_SEVENTY_TWO(32, 131072, MATERIAL_HUNDRED_THIRTY_ONE_THOUSAND_SEVENTY_TWO::getMaterialValue, MATERIAL_HUNDRED_THIRTY_ONE_THOUSAND_SEVENTY_TWO_CMD::getIntValue),
    INFINITY(64, Integer.MAX_VALUE, MATERIAL_INFINITY::getMaterialValue, MATERIAL_INFINITY_CMD::getIntValue);

    private static final String ERROR_REPRESENTATION = "Error getting representation for %s. Returning 0!";
    private static final String ERROR_NEXT_REPRESENTATION = "Error getting next representation for %s. Returning TWO!";
    private final int amount;
    private final int score;
    private final Supplier<Integer> customModelData;
    private final Supplier<Material> materialSupplier;

    NumberRepresentation(int amount, int score, Supplier<Material> materialSupplier, Supplier<Integer> customModelData) {
        this.amount = amount;
        this.score = score;
        this.customModelData = customModelData;
        this.materialSupplier = materialSupplier;
    }

    public static int getScoreFromItem(ItemStack itemStack) {
        return Arrays.stream(NumberRepresentation.values())
                     .filter(representation -> representation.getRepresentation() == itemStack.getType())
                     .findFirst()
                     .map(NumberRepresentation::getScore)
                     .orElseGet(() -> {
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

    public Supplier<Integer> getCustomModelData() {
        return customModelData;
    }

    public ItemStack getDisplayableBlock() {
        return setCustomModelDataTo(new ItemStack(getRepresentation(), amount), this);
    }

    public ItemStack getDisplayableLegendBlock() {
        ItemStack block = getDisplayableBlock();
        ItemMeta blockMeta = block.getItemMeta();
        assert Objects.nonNull(blockMeta);
        blockMeta.setDisplayName(LegacyComponentSerializer.legacySection()
                                                          .serialize(HELP_GUI_LEGEND_ITEM_NAME_PREFIX.getFormattedValue()) + getScore());
        block.setItemMeta(blockMeta);
        return block;
    }
}