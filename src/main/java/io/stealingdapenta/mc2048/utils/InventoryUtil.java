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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * Dear visitor If you've found this class, and you're a programmer yourself, then I challenge you to rewrite the moveItem functions (e.g. moveItemsUp) to be
 * DRY instead of this hot mess. (And make a pull request) I promise the current version works, but man is it ugly. Kind regards.
 */
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
    private static final int SLOT_UNDO = 52;
    private static final String ERROR_SKULL = "Error getting skull meta for %s.";
    private final Random random = new Random();

    public Inventory createGameInventory(ActiveGame activeGame) {
        Inventory inventory = createChestInventory(activeGame.getPlayer());
        fillSides(inventory);
        activeGame.setGameWindow(inventory);
        setButtonsAndStats(activeGame);
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
        return new ItemBuilder(new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1)).setDisplayName(" ").create();
    }

    private void setButtonsAndStats(ActiveGame activeGame) {
        setItemInSlot(activeGame.getGameWindow(), SLOT_UP, createButton("&2&l            UP"));
        setItemInSlot(activeGame.getGameWindow(), SLOT_LEFT, createButton("&2&l          LEFT"));
        setItemInSlot(activeGame.getGameWindow(), SLOT_RIGHT, createButton("&2&l          RIGHT"));
        setItemInSlot(activeGame.getGameWindow(), SLOT_DOWN, createButton("&2&l          DOWN"));
        setItemInSlot(activeGame.getGameWindow(), SLOT_UNDO, createUnusedUndoButton());
        updateStatisticItem(activeGame);
    }

    private ItemStack createButton(String buttonName) {
        return new ItemBuilder(Material.LIGHTNING_ROD).setDisplayName(buttonName).addLore("&aClick to move everything!").addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                                                      .create();
    }

    private ItemStack createUnusedUndoButton() {
        return new ItemBuilder(Material.AXOLOTL_BUCKET).setDisplayName("&2&l         UNDO").addLore("&aClick to undo the last move!")
                                                       .addLore("&bUses left: &2 1").addItemFlags(ItemFlag.HIDE_ATTRIBUTES).create();
    }

    private ItemStack createUsedUndoButton() {
        return new ItemBuilder(Material.BUCKET).setDisplayName("&2&l         UNDO").addLore("&aClick to undo the last move!").addLore("&bUses left: &2 0")
                                               .addItemFlags(ItemFlag.HIDE_ATTRIBUTES).create();
    }

    private void setItemInSlot(Inventory inventory, int slot, ItemStack itemStack) {
        inventory.setItem(slot, itemStack);
    }

    public boolean isGameWindow(InventoryView inventoryView) {
        return inventoryView.getTitle().contains(ChatColor.translateAlternateColorCodes('&', GAME_TITLE.strip()));
    }

    private void setLegend(Inventory gameWindow) {
        // todo alter this in a new window.
        ItemStack currentItem = NumberRepresentation.TWO.getDisplayableBlock();

        for (int row = INVENTORY_ROWS - 2; row < INVENTORY_ROWS; row++) {
            for (int column = 0; column < INVENTORY_COLUMNS; column++) {
                int slot = row * 9 + column;
                gameWindow.setItem(slot, currentItem);
                currentItem = getNextRepresentation(currentItem);
            }
        }
    }

    public boolean moveItemsInDirection(ActiveGame activeGame, Direction direction) {
        Inventory gameWindow = activeGame.getGameWindow();
        ItemStack[][] itemsInGame = new ItemStack[ROW_AND_COLUMN_SIZE][ROW_AND_COLUMN_SIZE];

        copyGameWindowContentsToArray(gameWindow, itemsInGame);

        if (!direction.equals(Direction.UNDO)) {
            // Create a deep copy of the current state before making moves
            ItemStack[][] lastPosition = new ItemStack[ROW_AND_COLUMN_SIZE][ROW_AND_COLUMN_SIZE];
            for (int i = 0; i < itemsInGame.length; i++) {
                lastPosition[i] = Arrays.copyOf(itemsInGame[i], itemsInGame[i].length);
            }
            activeGame.setLastPosition(lastPosition);
            activeGame.resetGainedAfterLastMove();
        }

        boolean anythingMoved = switch (direction) {
            case UP -> moveItemsUp(itemsInGame, activeGame);
            case DOWN -> moveItemsDown(itemsInGame, activeGame);
            case LEFT -> moveItemsLeft(itemsInGame, activeGame);
            case RIGHT -> moveItemsRight(itemsInGame, activeGame);
            case UNDO -> undoLastMove(itemsInGame, activeGame);
        };

        if (!anythingMoved) {
            return false;
        }

        copyItemArrayToGameWindow(gameWindow, itemsInGame);
        return true;
    }

    public boolean noValidMovesLeft(Inventory gameWindow) {
        ItemStack[][] itemsInGame = new ItemStack[ROW_AND_COLUMN_SIZE][ROW_AND_COLUMN_SIZE];
        copyGameWindowContentsToArray(gameWindow, itemsInGame);
        if (Arrays.stream(itemsInGame).flatMap(Arrays::stream).noneMatch(Objects::isNull)) {
            return !hasValidMoves(itemsInGame);
        }

        return false;
    }

    /**
     * Checks if there are valid moves left by examining adjacent items in the given 2D matrix.
     *
     * @param itemsInGame The 2D matrix representing the game state.
     * @return {@code true} if there are valid moves left, {@code false} otherwise.
     */
    private boolean hasValidMoves(ItemStack[][] itemsInGame) {
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
    private void copyItemArrayToGameWindow(Inventory gameWindow, ItemStack[][] itemsInGame) {
        for (int row = 0; row < ROW_AND_COLUMN_SIZE; row++) {
            for (int column = 0; column < ROW_AND_COLUMN_SIZE; column++) {
                gameWindow.setItem(mergeSlots[row][column], itemsInGame[row][column]);
            }
        }
    }

    // Copy contents from actual items to the 2D array based on mergeSlots numbers
    private void copyGameWindowContentsToArray(Inventory gameWindow, ItemStack[][] inventoryArray) {
        for (int row = 0; row < ROW_AND_COLUMN_SIZE; row++) {
            for (int column = 0; column < ROW_AND_COLUMN_SIZE; column++) {
                inventoryArray[row][column] = gameWindow.getItem(mergeSlots[row][column]);
            }
        }
    }

    private boolean undoLastMove(ItemStack[][] inventoryArray, ActiveGame activeGame) {
        if (Objects.isNull(activeGame.getLastPosition()) || activeGame.hasNoUndoLastMoveLeft()) {
            return false;
        }

        ItemStack[][] lastPosition = activeGame.getLastPosition();

        for (int i = 0; i < inventoryArray.length; i++) {
            System.arraycopy(lastPosition[i], 0, inventoryArray[i], 0, inventoryArray[i].length);
        }

        activeGame.decrementUndoLastMoveCounter();
        activeGame.addToScore(-activeGame.getScoreGainedAfterLastMove());
        setItemInSlot(activeGame.getGameWindow(), SLOT_UNDO, createUsedUndoButton());
        return true;
    }

    private boolean moveItemsUp(ItemStack[][] inventoryArray, ActiveGame activeGame) {
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
                                activeGame.addToScore(NumberRepresentation.getScoreFromItem(nextItem));
                                activeGame.addToGainedAfterLastMove(NumberRepresentation.getScoreFromItem(nextItem));
                            }
                        }
                    }
                }
            }
        }
        return moved;
    }

    private boolean moveItemsRight(ItemStack[][] inventoryArray, ActiveGame activeGame) {
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
                                activeGame.addToScore(NumberRepresentation.getScoreFromItem(nextItem));
                                activeGame.addToGainedAfterLastMove(NumberRepresentation.getScoreFromItem(nextItem));
                            }
                        }
                    }
                }
            }
        }
        return moved;
    }

    private boolean moveItemsLeft(ItemStack[][] inventoryArray, ActiveGame activeGame) {
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
                                activeGame.addToScore(NumberRepresentation.getScoreFromItem(nextItem));
                                activeGame.addToGainedAfterLastMove(NumberRepresentation.getScoreFromItem(nextItem));
                            }
                        }
                    }
                }
            }
        }

        return moved;
    }

    private boolean moveItemsDown(ItemStack[][] inventoryArray, ActiveGame activeGame) {
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
                                activeGame.addToScore(NumberRepresentation.getScoreFromItem(nextItem));
                                activeGame.addToGainedAfterLastMove(NumberRepresentation.getScoreFromItem(nextItem));
                            }
                        }
                    }
                }
            }
        }
        return moved;
    }

    private ItemStack getNextRepresentation(ItemStack itemStack) {
        int currentRepresentation = NumberRepresentation.getScoreFromItem(itemStack);
        return NumberRepresentation.getNextRepresentation(currentRepresentation).getDisplayableBlock();
    }

    public void spawnNewBlock(Inventory inventory) {
        List<Integer> emptySlots = IntStream.range(0, inventory.getSize()).filter(i -> Objects.isNull(inventory.getItem(i))).boxed().toList();

        if (!emptySlots.isEmpty()) {
            inventory.setItem(emptySlots.get(new Random().nextInt(emptySlots.size())), generateNewBlock());
        }
    }

    private ItemStack generateNewBlock() {
        NumberRepresentation chosenNumber = random.nextInt(2) == 0 ? NumberRepresentation.TWO : NumberRepresentation.FOUR;
        return new ItemBuilder(chosenNumber.getDisplayableBlock()).create();
    }

    public ItemStack getPlayerSkullItem(Player player) {
        ItemStack playerHead = (new ItemBuilder(Material.PLAYER_HEAD)).create();
        SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
        if (Objects.isNull(skullMeta)) {
            logger.severe(ERROR_SKULL.formatted(player.getName()));
            return playerHead;
        }
        skullMeta.setOwningPlayer(player);
        skullMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6%s's Statistics".formatted(player.getName())));
        playerHead.setItemMeta(skullMeta);
        return playerHead;
    }

    public void updateStatisticItem(ActiveGame activeGame) {
        activeGame.getGameWindow().setItem(SLOT_STATS, getPlayerStatsHead(activeGame));
    }

    private ItemStack getPlayerStatsHead(ActiveGame activeGame) {
        return (new ItemBuilder(getPlayerSkullItem(activeGame.getPlayer()))).addLore(
                                                                                    "&bTotal playtime: &2%s".formatted(activeGame.getTotalPlusCurrentPlayTimeFormatted())).addLore(
                                                                                    "&bCurrent playtime: &2%s".formatted(activeGame.getCurrentPlayTimeFormatted())).addLore("&bHiScore: &2%s".formatted(activeGame.getHiScore()))
                                                                            .addLore("&bCurrent score: &2%s".formatted(activeGame.getScore()))
                                                                            .addLore("&bAmount of games played: &2%s".formatted(activeGame.getAttempts()))
                                                                            .addLore(
                                                                                    "&bAverage Score: &2%s".formatted(Math.round(activeGame.getAverageScore())))
                                                                            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES).create();
    }
}