package io.stealingdapenta.mc2048.utils;

import static io.stealingdapenta.mc2048.MC2048.logger;

import java.util.Arrays;
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

    public boolean moveItemsInDirection(Inventory gameWindow, Direction direction) {
        ItemStack[][] itemsInGame = new ItemStack[ROW_AND_COLUMN_SIZE][ROW_AND_COLUMN_SIZE];

        copyGameContents(gameWindow, itemsInGame);

        boolean anythingMoved = switch (direction) {
            case UP -> moveItemsUp(itemsInGame);
            case DOWN -> moveItemsDown(itemsInGame);
            case LEFT -> moveItemsLeft(itemsInGame);
            case RIGHT -> moveItemsRight(itemsInGame);
        };

        if (!anythingMoved) {
            return false;
        }

        copyToGameWindow(gameWindow, itemsInGame);
        return true;
    }

    public boolean noValidMovesLeft(Inventory gameWindow) {
        ItemStack[][] itemsInGame = new ItemStack[ROW_AND_COLUMN_SIZE][ROW_AND_COLUMN_SIZE];
        copyGameContents(gameWindow, itemsInGame);
        if (Arrays.stream(itemsInGame)
                  .flatMap(Arrays::stream)
                  .noneMatch(Objects::isNull)) {
            return !hasValidMovesLeft(itemsInGame);
        }

        return false;
    }

    /**
     * Checks if there are valid moves left by examining adjacent items in the given 2D matrix.
     *
     * @param itemsInGame The 2D matrix representing the game state.
     * @return {@code true} if there are valid moves left, {@code false} otherwise.
     */
    private boolean hasValidMovesLeft(ItemStack[][] itemsInGame) {
        for (int row = 0; row < ROW_AND_COLUMN_SIZE; row++) {
            for (int column = 0; column < ROW_AND_COLUMN_SIZE; column++) {
                // Check right and down for each position
                ItemStack currentItem = itemsInGame[row][column];
                ItemStack rightItem = null;
                if (column + 1 < ROW_AND_COLUMN_SIZE) {
                    rightItem = itemsInGame[row][column + 1];
                }

                ItemStack belowItem = null;
                if (row + 1 < ROW_AND_COLUMN_SIZE) {
                    belowItem = itemsInGame[row + 1][column];
                }

                if ((column < ROW_AND_COLUMN_SIZE - 1 && currentItem.isSimilar(rightItem)) || (row < ROW_AND_COLUMN_SIZE - 1 && currentItem.isSimilar(
                        belowItem))) {
                    return true;
                }
            }
        }
        return false;
    }

    // Update the gameWindow inventory with the modified items arrays
    private void copyToGameWindow(Inventory gameWindow, ItemStack[][] itemsInGame) {
        for (int row = 0; row < ROW_AND_COLUMN_SIZE; row++) {
            for (int column = 0; column < ROW_AND_COLUMN_SIZE; column++) {
                gameWindow.setItem(mergeSlots[row][column], itemsInGame[row][column]);
            }
        }
    }

    // Copy contents from actual items to the 2D array based on mergeSlots numbers
    private void copyGameContents(Inventory gameWindow, ItemStack[][] inventoryArray) {
        for (int row = 0; row < ROW_AND_COLUMN_SIZE; row++) {
            for (int column = 0; column < ROW_AND_COLUMN_SIZE; column++) {
                inventoryArray[row][column] = gameWindow.getItem(mergeSlots[row][column]);
            }
        }
    }

    private boolean moveItemsUp(ItemStack[][] inventoryArray) {
        boolean moved = false;

        for (int row = 1; row < ROW_AND_COLUMN_SIZE; row++) {
            for (int column = 0; column < ROW_AND_COLUMN_SIZE; column++) {
                if (Objects.nonNull(inventoryArray[row][column])) {
                    int currentRowIndex = row;
                    int aboveRowIndex = currentRowIndex - 1;

                    while (aboveRowIndex >= 0 && Objects.isNull(inventoryArray[aboveRowIndex][column])) {
                        inventoryArray[aboveRowIndex][column] = inventoryArray[currentRowIndex][column];
                        inventoryArray[currentRowIndex][column] = null;
                        currentRowIndex--;
                        aboveRowIndex = currentRowIndex - 1;
                        moved = true;
                    }

                    if (aboveRowIndex >= 0) {
                        ItemStack itemAbove = inventoryArray[aboveRowIndex][column];
                        if (Objects.nonNull(itemAbove)) {
                            ItemStack currentItem = inventoryArray[currentRowIndex][column];
                            if (itemAbove.isSimilar(currentItem)) {
                                ItemStack nextItem = getNextRepresentation(currentItem);
                                inventoryArray[aboveRowIndex][column] = nextItem;
                                inventoryArray[currentRowIndex][column] = null;
                                moved = true;
                            }
                        }
                    }
                }
            }
        }
        return moved;
    }

    private boolean moveItemsRight(ItemStack[][] inventoryArray) {
        boolean moved = false;

        for (int column = ROW_AND_COLUMN_SIZE - 2; column >= 0; column--) {
            for (int row = 0; row < ROW_AND_COLUMN_SIZE; row++) {
                if (Objects.nonNull(inventoryArray[row][column])) {
                    int currentColumnIndex = column;
                    int rightColumnIndex = currentColumnIndex + 1;

                    while (rightColumnIndex < ROW_AND_COLUMN_SIZE && Objects.isNull(inventoryArray[row][rightColumnIndex])) {
                        inventoryArray[row][rightColumnIndex] = inventoryArray[row][currentColumnIndex];
                        inventoryArray[row][currentColumnIndex] = null;
                        currentColumnIndex++;
                        rightColumnIndex = currentColumnIndex + 1;
                        moved = true;
                    }

                    if (rightColumnIndex < ROW_AND_COLUMN_SIZE) {
                        ItemStack rightItem = inventoryArray[row][rightColumnIndex];
                        if (Objects.nonNull(rightItem)) {
                            ItemStack currentItem = inventoryArray[row][currentColumnIndex];
                            if (rightItem.isSimilar(currentItem)) {
                                ItemStack nextItem = getNextRepresentation(currentItem);
                                inventoryArray[row][rightColumnIndex] = nextItem;
                                inventoryArray[row][currentColumnIndex] = null;
                                moved = true;
                            }
                        }
                    }
                }
            }
        }
        return moved;
    }

    private boolean moveItemsLeft(ItemStack[][] inventoryArray) {
        boolean moved = false;

        for (int column = 1; column < ROW_AND_COLUMN_SIZE; column++) {
            for (int row = 0; row < ROW_AND_COLUMN_SIZE; row++) {
                if (Objects.nonNull(inventoryArray[row][column])) {
                    int currentColumnIndex = column;
                    int leftColumnIndex = currentColumnIndex - 1;

                    while (leftColumnIndex >= 0 && Objects.isNull(inventoryArray[row][leftColumnIndex])) {
                        inventoryArray[row][leftColumnIndex] = inventoryArray[row][currentColumnIndex];
                        inventoryArray[row][currentColumnIndex] = null;
                        currentColumnIndex--;
                        leftColumnIndex = currentColumnIndex - 1;
                        moved = true;
                    }

                    if (leftColumnIndex >= 0) {
                        ItemStack rightItem = inventoryArray[row][leftColumnIndex];
                        if (Objects.nonNull(rightItem)) {
                            ItemStack currentItem = inventoryArray[row][currentColumnIndex];
                            if (rightItem.isSimilar(currentItem)) {
                                ItemStack nextItem = getNextRepresentation(currentItem);
                                inventoryArray[row][leftColumnIndex] = nextItem;
                                inventoryArray[row][currentColumnIndex] = null;
                                moved = true;
                            }
                        }
                    }
                }
            }
        }

        return moved;
    }

    private boolean moveItemsDown(ItemStack[][] inventoryArray) {
        boolean moved = false;

        for (int row = ROW_AND_COLUMN_SIZE - 2; row >= 0; row--) {
            for (int column = 0; column < ROW_AND_COLUMN_SIZE; column++) {
                if (Objects.nonNull(inventoryArray[row][column])) {
                    int currentRowIndex = row;
                    int belowRowIndex = currentRowIndex + 1;

                    while (belowRowIndex < ROW_AND_COLUMN_SIZE && Objects.isNull(inventoryArray[belowRowIndex][column])) {
                        inventoryArray[belowRowIndex][column] = inventoryArray[currentRowIndex][column];
                        inventoryArray[currentRowIndex][column] = null;
                        currentRowIndex++;
                        belowRowIndex = currentRowIndex + 1;
                        moved = true;
                    }

                    if (belowRowIndex < ROW_AND_COLUMN_SIZE) {
                        ItemStack rightItem = inventoryArray[belowRowIndex][column];
                        if (Objects.nonNull(rightItem)) {
                            ItemStack currentItem = inventoryArray[currentRowIndex][column];
                            if (rightItem.isSimilar(currentItem)) {
                                ItemStack nextItem = getNextRepresentation(currentItem);
                                inventoryArray[belowRowIndex][column] = nextItem;
                                inventoryArray[currentRowIndex][column] = null;
                                moved = true;
                            }
                        }
                    }
                }
            }
        }
        return moved;
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
