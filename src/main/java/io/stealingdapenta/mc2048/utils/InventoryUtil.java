package io.stealingdapenta.mc2048.utils;

import static io.stealingdapenta.mc2048.MC2048.logger;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class InventoryUtil {

    public static final int INVENTORY_ROWS = 6;
    public static final int INVENTORY_COLUMNS = 9;
    public static final int ROW_AND_COLUMN_SIZE = 4;
    public static final int REQUIRED_SIZE = INVENTORY_ROWS * INVENTORY_COLUMNS;
    private static final int[][] mergeSlots = {{10, 11, 12, 13}, {19, 20, 21, 22}, {28, 29, 30, 31}, {37, 38, 39, 40}};
    private static final int GAME_ROWS = 4;
    private static final int GAME_COLUMNS = 4;

    private static final String WRONG_SIZE = "Error filling sides of inventory: wrong size!";
    private static final String GAME_TITLE = "               &4&lMC 2048";
    private static final int SLOT_UP = 16;
    private static final int SLOT_LEFT = 24;
    private static final int SLOT_RIGHT = 26;
    private static final int SLOT_DOWN = 34;
    private static final int SLOT_STATS = 25;

    private final Random random = new Random();


    public Inventory getGameInventory(Player player) {
        Inventory inventory = createChestInventory(player);
        fillSides(inventory);
        setButtonsAndStats(inventory, player);
        return inventory;
    }

    private Inventory createChestInventory(Player player) {
        return Bukkit.createInventory(player, REQUIRED_SIZE, ChatColor.translateAlternateColorCodes('&', GAME_TITLE));
    }

    private void fillSides(Inventory inventory) {
        if (inventory.getSize() != REQUIRED_SIZE) {
            logger.warning(WRONG_SIZE);
            return;
        }

        for (int i = 0; i < REQUIRED_SIZE; i++) {
            if (isInvalidGameSlot(i)) {
                inventory.setItem(i, getStainedGlassPaneItem());
            }
        }
    }

    public boolean isInvalidGameSlot(int slot) {
        for (int[] row : mergeSlots) {
            for (int validSlot : row) {
                if (validSlot == slot) {
                    return false;
                }
            }
        }
        return true;
    }

    private ItemStack getStainedGlassPaneItem() {
        return new ItemBuilder(new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1)).setDisplayName(" ")
                                                                                   .create();
    }

    private void setButtonsAndStats(Inventory inventory, Player player) {
        setItemInSlot(inventory, SLOT_UP, createButton("&2&lUP"));
        setItemInSlot(inventory, SLOT_LEFT, createButton("&2&lLEFT"));
        setItemInSlot(inventory, SLOT_RIGHT, createButton("&2&lRIGHT"));
        setItemInSlot(inventory, SLOT_DOWN, createButton("&2&lDOWN"));
        setItemInSlot(inventory, SLOT_STATS, createStatsItem(player));
    }

    private ItemStack createButton(String buttonName) {
        return new ItemBuilder(Material.LIGHTNING_ROD).setDisplayName(buttonName)
                                                      .addLore("&aClick to move everything!")
                                                      .create();
    }

    private ItemStack createStatsItem(Player player) {
        return new ItemBuilder(Material.RECOVERY_COMPASS).setDisplayName("&6&l" + player.getName() + "s stats")
                                                         .addLore("&e>> Playtime: 11:12:13s")
                                                         .addLore("&e>> HiScore: 2048")
                                                         .addLore("&e>> Current score: 1024")
                                                         .create();
    }

    private void setItemInSlot(Inventory inventory, int slot, ItemStack itemStack) {
        inventory.setItem(slot, itemStack);
    }

    public boolean isGameWindow(InventoryView inventoryView) {
        return inventoryView.getTitle()
                            .contains(ChatColor.translateAlternateColorCodes('&', GAME_TITLE.strip()));
    }

    public void moveItemsInDirection(Inventory gameWindow, Direction direction) {
        ItemStack[][] inventoryArray = new ItemStack[ROW_AND_COLUMN_SIZE][ROW_AND_COLUMN_SIZE];

        // Copy contents from actual items to the 2D array based on mergeSlots numbers
        for (int row = 0; row < ROW_AND_COLUMN_SIZE; row++) {
            for (int column = 0; column < ROW_AND_COLUMN_SIZE; column++) {
                inventoryArray[row][column] = gameWindow.getItem(mergeSlots[row][column]);
            }
        }
        // ============================================================================

        switch (direction) {
            case UP -> {
                for (int row = 1; row < ROW_AND_COLUMN_SIZE; row++) {
                    for (int column = 0; column < ROW_AND_COLUMN_SIZE; column++) {
                        if (Objects.nonNull(inventoryArray[row][column])) {
                            int currentIndex = row;

                            while (currentIndex - 1 >= 0 && Objects.isNull(inventoryArray[currentIndex - 1][column])) {
                                inventoryArray[currentIndex - 1][column] = inventoryArray[currentIndex][column];
                                inventoryArray[currentIndex][column] = null;
                                currentIndex--;
                            }

                            if (currentIndex != 0 && inventoryArray[currentIndex - 1][column] != null) {
                                logger.info("COLLIDED UP!");
                            }
                        }
                    }
                }
            }
            case DOWN -> {
                for (int row = ROW_AND_COLUMN_SIZE - 2; row >= 0; row--) {
                    for (int column = 0; column < ROW_AND_COLUMN_SIZE; column++) {
                        if (Objects.nonNull(inventoryArray[row][column])) {
                            int currentIndex = row;

                            while (currentIndex + 1 < ROW_AND_COLUMN_SIZE && Objects.isNull(inventoryArray[currentIndex + 1][column])) {
                                inventoryArray[currentIndex + 1][column] = inventoryArray[currentIndex][column];
                                inventoryArray[currentIndex][column] = null;
                                currentIndex++;
                            }

                            if (currentIndex != ROW_AND_COLUMN_SIZE - 1 && inventoryArray[currentIndex + 1][column] != null) {
                                logger.info("COLLIDED DOWN!");
                            }
                        }
                    }
                }
            }
            case LEFT -> {
                for (int column = 1; column < ROW_AND_COLUMN_SIZE; column++) {
                    for (int row = 0; row < ROW_AND_COLUMN_SIZE; row++) {
                        if (Objects.nonNull(inventoryArray[row][column])) {
                            int currentIndex = column;

                            while (currentIndex - 1 >= 0 && Objects.isNull(inventoryArray[row][currentIndex - 1])) {
                                inventoryArray[row][currentIndex - 1] = inventoryArray[row][currentIndex];
                                inventoryArray[row][currentIndex] = null;
                                currentIndex--;
                            }

                            if (currentIndex != 0 && inventoryArray[row][currentIndex - 1] != null) {
                                logger.info("COLLIDED LEFT!");
                            }
                        }
                    }
                }

            }
            case RIGHT -> {
                for (int column = ROW_AND_COLUMN_SIZE - 2; column >= 0; column--) {
                    for (int row = 0; row < ROW_AND_COLUMN_SIZE; row++) {
                        if (Objects.nonNull(inventoryArray[row][column])) {
                            int currentIndex = column;

                            while (currentIndex + 1 < ROW_AND_COLUMN_SIZE && Objects.isNull(inventoryArray[row][currentIndex + 1])) {
                                inventoryArray[row][currentIndex + 1] = inventoryArray[row][currentIndex];
                                inventoryArray[row][currentIndex] = null;
                                currentIndex++;
                            }

                            if (currentIndex != ROW_AND_COLUMN_SIZE - 1 && inventoryArray[row][currentIndex + 1] != null) {
                                logger.info("COLLIDED RIGHT!");
                            }
                        }
                    }
                }
            }
        }

        // ============================================================================

        // Update the gameWindow inventory with the modified items arrays
        for (int row = 0; row < ROW_AND_COLUMN_SIZE; row++) {
            for (int column = 0; column < ROW_AND_COLUMN_SIZE; column++) {
                gameWindow.setItem(mergeSlots[row][column], inventoryArray[row][column]);
            }
        }
    }

    private ItemStack getNextRepresentation(ItemStack itemStack) {
        int currentRepresentation = NumberRepresentations.getRepresentationFromItem(itemStack);
        return NumberRepresentations.getNextRepresentation(currentRepresentation)
                                    .getDisplayableBlock();
    }

    public void spawnNewBlock(Inventory inventory) {
        List<Integer> emptySlots = IntStream.range(0, inventory.getSize())
                                            .filter(i -> Objects.isNull(inventory.getItem(i)))
                                            .boxed()
                                            .toList();

        if (!emptySlots.isEmpty()) {
            inventory.setItem(emptySlots.get(new Random().nextInt(emptySlots.size())), generateNewBlock());
        }
    }

    private ItemStack generateNewBlock() {
        NumberRepresentations chosenNumber = random.nextInt(2) == 0 ? NumberRepresentations.TWO : NumberRepresentations.FOUR;
        return new ItemBuilder(chosenNumber.getDisplayableBlock()).create();
    }
}
