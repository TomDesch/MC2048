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

    private void moveItemsInDirection(Inventory gameWindow, int start, int end, int increment, boolean isVertical) {
        ItemStack[][] inventoryArray = new ItemStack[ROW_AND_COLUMN_SIZE][ROW_AND_COLUMN_SIZE];

        // Copy contents from actual items to the 2D array based on mergeSlots numbers
        for (int row = 0; row < ROW_AND_COLUMN_SIZE; row++) {
            for (int column = 0; column < ROW_AND_COLUMN_SIZE; column++) {
                logger.info("Setting [%s, %s] to item %s".formatted(row, column, gameWindow.getItem(mergeSlots[row][column])));
                inventoryArray[row][column] = gameWindow.getItem(mergeSlots[row][column]);
            }
        }

        for (int movingIndex = start; movingIndex != end; movingIndex += increment) {
            for (int fixedIndex = 0; fixedIndex < ROW_AND_COLUMN_SIZE; fixedIndex++) {
                if (inventoryArray[isVertical ? movingIndex : fixedIndex][isVertical ? fixedIndex : movingIndex] != null) {
                    int currentIndex = movingIndex;
                    while (currentIndex >= end
                            && inventoryArray[isVertical ? currentIndex - increment : fixedIndex][isVertical ? fixedIndex : currentIndex - increment] == null) {
                        inventoryArray[isVertical ? currentIndex - increment : fixedIndex][isVertical ? fixedIndex : currentIndex - increment] = inventoryArray[
                                isVertical ? currentIndex : fixedIndex][isVertical ? fixedIndex : currentIndex];
                        inventoryArray[isVertical ? currentIndex : fixedIndex][isVertical ? fixedIndex : currentIndex] = null;
                        currentIndex -= increment;
                    }
                    if (currentIndex != end
                            && inventoryArray[isVertical ? currentIndex - increment : fixedIndex][isVertical ? fixedIndex : currentIndex - increment] != null) {
                        logger.info("COLLIDED!");
                    }
                }
            }
        }

        // Update the gameWindow inventory with the modified items
        for (int i = 0; i < ROW_AND_COLUMN_SIZE; i++) {
            for (int j = 0; j < ROW_AND_COLUMN_SIZE; j++) {
                gameWindow.setItem(mergeSlots[i][j], inventoryArray[i][j]);
            }
        }
    }

    public void moveUp(Inventory gameWindow) {
        moveItemsInDirection(gameWindow, 1, ROW_AND_COLUMN_SIZE, 1, true);
    }

    public void moveDown(Inventory gameWindow) {
        moveItemsInDirection(gameWindow, ROW_AND_COLUMN_SIZE - 2, -1, -1, true);
    }

    public void moveLeft(Inventory gameWindow) {
        moveItemsInDirection(gameWindow, 1, ROW_AND_COLUMN_SIZE, 1, false);
    }

    public void moveRight(Inventory gameWindow) {
        moveItemsInDirection(gameWindow, ROW_AND_COLUMN_SIZE - 2, -1, -1, false);
    }


    private void moveItem(ItemStack[][] array, int sourceRow, int sourceColumn, int destRow, int destColumn) {
        array[destRow][destColumn] = array[sourceRow][sourceColumn];
        array[sourceRow][sourceColumn] = null;
    }

    private int getNextRow(int row, boolean isReverse) {
        return isReverse ? row - 1 : row + 1;
    }

    private int getNextColumn(int column, boolean isReverse) {
        return isReverse ? column - 1 : column + 1;
    }

    private ItemStack getNextRepresentation(ItemStack itemStack) {
        int currentRepresentation = NumberRepresentations.getRepresentationFromItem(itemStack);
        return NumberRepresentations.getNextRepresentation(currentRepresentation)
                                    .getDisplayableBlock();
    }

    public Inventory spawnNewBlock(Inventory inventory) {
        List<Integer> emptySlots = IntStream.range(0, inventory.getSize())
                                            .filter(i -> Objects.isNull(inventory.getItem(i)))
                                            .boxed()
                                            .toList();

        if (!emptySlots.isEmpty()) {
            inventory.setItem(emptySlots.get(new Random().nextInt(emptySlots.size())), generateNewBlock());
        }
        return inventory;
    }

    private ItemStack generateNewBlock() {
        NumberRepresentations chosenNumber = random.nextInt(2) == 0 ? NumberRepresentations.TWO : NumberRepresentations.FOUR;
        return new ItemBuilder(chosenNumber.getDisplayableBlock()).create();
    }
}
