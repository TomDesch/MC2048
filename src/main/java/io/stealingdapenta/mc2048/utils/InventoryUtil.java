package io.stealingdapenta.mc2048.utils;

import static io.stealingdapenta.mc2048.MC2048.logger;
import static io.stealingdapenta.mc2048.config.ConfigKey.AVERAGE_SCORE;
import static io.stealingdapenta.mc2048.config.ConfigKey.DOWN_BUTTON_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.GAMES_PLAYED;
import static io.stealingdapenta.mc2048.config.ConfigKey.GAME_TITLE;
import static io.stealingdapenta.mc2048.config.ConfigKey.HELP_GUI_GAME_SUMMARY_LORE_1;
import static io.stealingdapenta.mc2048.config.ConfigKey.HELP_GUI_GAME_SUMMARY_LORE_2;
import static io.stealingdapenta.mc2048.config.ConfigKey.HELP_GUI_GAME_SUMMARY_LORE_3;
import static io.stealingdapenta.mc2048.config.ConfigKey.HELP_GUI_GAME_SUMMARY_LORE_4;
import static io.stealingdapenta.mc2048.config.ConfigKey.HELP_GUI_GAME_SUMMARY_LORE_5;
import static io.stealingdapenta.mc2048.config.ConfigKey.HELP_GUI_INFO_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.HELP_GUI_PLAY_BUTTON_LORE;
import static io.stealingdapenta.mc2048.config.ConfigKey.HELP_GUI_PLAY_BUTTON_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.HELP_GUI_TITLE;
import static io.stealingdapenta.mc2048.config.ConfigKey.HIGH_SCORE;
import static io.stealingdapenta.mc2048.config.ConfigKey.LEFT_BUTTON_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_BUTTON_DOWN;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_BUTTON_DOWN_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_BUTTON_LEFT;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_BUTTON_LEFT_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_BUTTON_RIGHT;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_BUTTON_RIGHT_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_BUTTON_UNDO;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_BUTTON_UNDO_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_BUTTON_UNDO_USED;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_BUTTON_UNDO_USED_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_BUTTON_UP;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_BUTTON_UP_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_GUI_FILLER;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_GUI_FILLER_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_GUI_PLAYER;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_GUI_PLAYER_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_HELP_GUI_FILLER;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_HELP_GUI_FILLER_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_HELP_GUI_INFO;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_HELP_GUI_INFO_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_HELP_GUI_PLAY_BUTTON;
import static io.stealingdapenta.mc2048.config.ConfigKey.MATERIAL_HELP_GUI_PLAY_BUTTON_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MOVE_BUTTON_LORE;
import static io.stealingdapenta.mc2048.config.ConfigKey.NUMBER_OF_UNDO;
import static io.stealingdapenta.mc2048.config.ConfigKey.PLAYER_STATS_TITLE;
import static io.stealingdapenta.mc2048.config.ConfigKey.RIGHT_BUTTON_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.TOTAL_PLAYTIME;
import static io.stealingdapenta.mc2048.config.ConfigKey.UNDO_BUTTON_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.UNDO_BUTTON_UNUSED_LORE;
import static io.stealingdapenta.mc2048.config.ConfigKey.UNDO_BUTTON_UNUSED_USES;
import static io.stealingdapenta.mc2048.config.ConfigKey.UNDO_BUTTON_USED_USES;
import static io.stealingdapenta.mc2048.config.ConfigKey.UP_BUTTON_NAME;
import static io.stealingdapenta.mc2048.utils.ActiveGame.makeSecondsATimestamp;
import static io.stealingdapenta.mc2048.utils.FileManager.FILE_MANAGER;
import static io.stealingdapenta.mc2048.utils.ItemBuilder.setCustomModelDataTo;
import static org.bukkit.Bukkit.createInventory;

import io.stealingdapenta.mc2048.config.ConfigKey;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * Dear visitor, If you've found this class, and you're a programmer yourself, then I challenge you to rewrite the moveItem functions (e.g., moveItemsUp) to be DRY instead of this hot mess. (And make a pull request) I promise the current version
 * works, but man is it ugly. Kind regards. Edit: rewrite it whole. This is not SRP nor good Java in general.
 */
public class InventoryUtil {

    private final HighScoreManager highScoreManager;

    public static final int INVENTORY_ROWS = 6;
    public static final int INVENTORY_COLUMNS = 9;
    public static final int ROW_AND_COLUMN_SIZE = 4;
    public static final int REQUIRED_SIZE = INVENTORY_ROWS * INVENTORY_COLUMNS;
    private static final int[][] mergeSlots = {{10, 11, 12, 13}, {19, 20, 21, 22}, {28, 29, 30, 31}, {37, 38, 39, 40}};
    private static final String WRONG_SIZE = "Error filling sides of inventory: wrong size!";

    private static final String ERROR_SKULL = "Error getting skull meta for %s.";
    private final Random random = new Random();

    public InventoryUtil(HighScoreManager highScoreManager) {
        this.highScoreManager = highScoreManager;
    }

    public static ItemStack getPlayerSkullItem(Player player) {
        ItemStack playerHead = setCustomModelDataTo((new ItemBuilder(MATERIAL_GUI_PLAYER.getMaterialValue())).create(), MATERIAL_GUI_PLAYER_CMD);
        SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
        if (Objects.isNull(skullMeta)) {
            logger.warning(ERROR_SKULL.formatted(player.getName()));
            return playerHead;
        }
        skullMeta.setOwningPlayer(player);
        skullMeta.setDisplayName(LegacyComponentSerializer.legacySection()
                                                          .serialize(PLAYER_STATS_TITLE.getFormattedValue(player.getName())));
        playerHead.setItemMeta(skullMeta);
        return playerHead;
    }

    public Inventory createGameInventory(ActiveGame activeGame) {
        Inventory inventory = createGUIWindow(activeGame.getPlayer(), GAME_TITLE);
        fillSides(inventory);
        activeGame.setGameWindow(inventory);
        setButtonsAndStats(activeGame);
        return inventory;
    }

    public Inventory createHelpInventory(Player player) {
        Inventory helpGUI = createGUIWindow(player, HELP_GUI_TITLE);

        final int playerStatsSlot = 28;
        final int highScoreSlot = 34;
        final int infoSlot = 31;
        final int playButtonSlot = 49;

        fillWithLegend(helpGUI);
        setItemInSlot(helpGUI, playerStatsSlot, getHelpGUIPlayerStatsHead(player));
        setItemInSlot(helpGUI, highScoreSlot, highScoreManager.getHighScoresItem());
        setItemInSlot(helpGUI, infoSlot, setCustomModelDataTo(new ItemBuilder(MATERIAL_HELP_GUI_INFO.getMaterialValue()).setDisplayName(HELP_GUI_INFO_NAME.getFormattedValue())
                                                                                                                        .addLore(HELP_GUI_GAME_SUMMARY_LORE_1.getFormattedValue())
                                                                                                                        .addLore(HELP_GUI_GAME_SUMMARY_LORE_2.getFormattedValue())
                                                                                                                        .addLore(HELP_GUI_GAME_SUMMARY_LORE_3.getFormattedValue())
                                                                                                                        .addLore(HELP_GUI_GAME_SUMMARY_LORE_4.getFormattedValue())
                                                                                                                        .addLore(HELP_GUI_GAME_SUMMARY_LORE_5.getFormattedValue())
                                                                                                                        .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                                                                                                                        .create(), MATERIAL_HELP_GUI_INFO_CMD));
        setItemInSlot(helpGUI, playButtonSlot, setCustomModelDataTo(new ItemBuilder(MATERIAL_HELP_GUI_PLAY_BUTTON.getMaterialValue()).setDisplayName(HELP_GUI_PLAY_BUTTON_NAME.getFormattedValue())
                                                                                                                                     .addLore(HELP_GUI_PLAY_BUTTON_LORE)
                                                                                                                                     .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                                                                                                                                     .create(), MATERIAL_HELP_GUI_PLAY_BUTTON_CMD));
        fillEmptySlots(helpGUI);

        return helpGUI;
    }

    private void fillEmptySlots(Inventory inventory) {
        if (inventory.getSize() != REQUIRED_SIZE) {
            logger.warning(WRONG_SIZE);
            return;
        }

        for (int i = 0; i < REQUIRED_SIZE; i++) {
            if (Objects.isNull(inventory.getItem(i))) {
                inventory.setItem(i, getHelpGuiFillerItem());
            }
        }
    }

    private void fillWithLegend(Inventory inventory) {
        Arrays.stream(NumberRepresentation.values())
              .forEach(numberRepresentation -> inventory.setItem(numberRepresentation.ordinal(), numberRepresentation.getDisplayableLegendBlock()));
    }

    private void fillSides(Inventory inventory) {
        if (inventory.getSize() != REQUIRED_SIZE) {
            logger.warning(WRONG_SIZE);
            return;
        }

        for (int i = 0; i < REQUIRED_SIZE; i++) {
            if (isInvalidGameSlot(i)) {
                inventory.setItem(i, getGameFillerItem());
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

    private ItemStack getGameFillerItem() {
        return new ItemBuilder(setCustomModelDataTo(new ItemStack(MATERIAL_GUI_FILLER.getMaterialValue(), 1), MATERIAL_GUI_FILLER_CMD)).setDisplayName(" ")
                                                                                                                                       .create();
    }

    private ItemStack getHelpGuiFillerItem() {
        return new ItemBuilder(setCustomModelDataTo(new ItemStack(MATERIAL_HELP_GUI_FILLER.getMaterialValue(), 1), MATERIAL_HELP_GUI_FILLER_CMD)).setDisplayName(" ")
                                                                                                                                                 .create();
    }

    private void setButtonsAndStats(ActiveGame activeGame) {
        final int SLOT_UP = 16;
        final int SLOT_LEFT = 24;
        final int SLOT_RIGHT = 26;
        final int SLOT_DOWN = 34;
        final int SLOT_UNDO = 52;
        setItemInSlot(activeGame.getGameWindow(), SLOT_UP, createButton(UP_BUTTON_NAME, MATERIAL_BUTTON_UP, MATERIAL_BUTTON_UP_CMD));
        setItemInSlot(activeGame.getGameWindow(), SLOT_LEFT, createButton(LEFT_BUTTON_NAME, MATERIAL_BUTTON_LEFT, MATERIAL_BUTTON_LEFT_CMD));
        setItemInSlot(activeGame.getGameWindow(), SLOT_RIGHT, createButton(RIGHT_BUTTON_NAME, MATERIAL_BUTTON_RIGHT, MATERIAL_BUTTON_RIGHT_CMD));
        setItemInSlot(activeGame.getGameWindow(), SLOT_DOWN, createButton(DOWN_BUTTON_NAME, MATERIAL_BUTTON_DOWN, MATERIAL_BUTTON_DOWN_CMD));
        setItemInSlot(activeGame.getGameWindow(), SLOT_UNDO, createUndoButton(NUMBER_OF_UNDO.getIntValue()));
    }

    private ItemStack createButton(ConfigKey buttonName, ConfigKey materialName, ConfigKey customMetaData) {
        return setCustomModelDataTo(new ItemBuilder(materialName.getMaterialValue()).setDisplayName(buttonName.getFormattedValue())
                                                                                    .addLore(MOVE_BUTTON_LORE)
                                                                                    .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                                                                                    .create(), customMetaData);
    }

    private ItemStack createUndoButton(int numberOfUndoLeft) {
        return setCustomModelDataTo(new ItemBuilder(MATERIAL_BUTTON_UNDO.getMaterialValue()).setDisplayName(UNDO_BUTTON_NAME)
                                                                                            .addLore(UNDO_BUTTON_UNUSED_LORE)
                                                                                            .addLore(UNDO_BUTTON_UNUSED_USES.getFormattedValue()
                                                                                                                            .append(Component.text(numberOfUndoLeft)))
                                                                                            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                                                                                            .create(), MATERIAL_BUTTON_UNDO_CMD);
    }

    private ItemStack createUsedUndoButton() {
        return setCustomModelDataTo(new ItemBuilder(MATERIAL_BUTTON_UNDO_USED.getMaterialValue()).setDisplayName(UNDO_BUTTON_NAME)
                                                                                                 .addLore(UNDO_BUTTON_UNUSED_LORE)
                                                                                                 .addLore(UNDO_BUTTON_USED_USES)
                                                                                                 .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                                                                                                 .create(), MATERIAL_BUTTON_UNDO_USED_CMD);
    }

    private void setItemInSlot(Inventory inventory, int slot, ItemStack itemStack) {
        inventory.setItem(slot, itemStack);
    }

    private Inventory createGUIWindow(Player player, ConfigKey title) {
        return createInventory(player, InventoryUtil.REQUIRED_SIZE, LegacyComponentSerializer.legacySection()
                                                                                             .serialize(title.getFormattedValue()));
    }

    public boolean isGameWindow(InventoryView inventoryView) {
        return isKnownWindow(inventoryView, GAME_TITLE);
    }

    public boolean isHelpWindow(InventoryView inventoryView) {
        return isKnownWindow(inventoryView, HELP_GUI_TITLE);
    }

    /**
     * @return whether the open inventory resembles ANY possible GUI window from this plugin
     */
    public boolean isAnyGameWindow(InventoryView inventoryView) {
        return isGameWindow(inventoryView) || isHelpWindow(inventoryView);
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

        if (!direction.equals(Direction.UNDO)) {
            activeGame.setLastMoveUndo(false);
        }

        copyItemArrayToGameWindow(gameWindow, itemsInGame);
        return true;
    }

    public boolean noValidMovesLeft(Inventory gameWindow) {
        ItemStack[][] itemsInGame = new ItemStack[ROW_AND_COLUMN_SIZE][ROW_AND_COLUMN_SIZE];
        copyGameWindowContentsToArray(gameWindow, itemsInGame);
        if (Arrays.stream(itemsInGame)
                  .flatMap(Arrays::stream)
                  .noneMatch(Objects::isNull)) {
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

                if ((column < ROW_AND_COLUMN_SIZE - 1 && currentItem.isSimilar(rightItem)) || (row < ROW_AND_COLUMN_SIZE - 1 && currentItem.isSimilar(belowItem))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Update the gameWindow inventory with the modified items arrays
     */
    private void copyItemArrayToGameWindow(Inventory gameWindow, ItemStack[][] itemsInGame) {
        for (int row = 0; row < ROW_AND_COLUMN_SIZE; row++) {
            for (int column = 0; column < ROW_AND_COLUMN_SIZE; column++) {
                gameWindow.setItem(mergeSlots[row][column], itemsInGame[row][column]);
            }
        }
    }

    /**
     * Copy contents from actual items to the 2D array based on mergeSlots numbers
     */
    private void copyGameWindowContentsToArray(Inventory gameWindow, ItemStack[][] inventoryArray) {
        for (int row = 0; row < ROW_AND_COLUMN_SIZE; row++) {
            for (int column = 0; column < ROW_AND_COLUMN_SIZE; column++) {
                inventoryArray[row][column] = gameWindow.getItem(mergeSlots[row][column]);
            }
        }
    }

    private boolean undoLastMove(ItemStack[][] inventoryArray, ActiveGame activeGame) {
        if (Objects.isNull(activeGame.getLastPosition()) || activeGame.hasNoUndoLastMoveLeft() || activeGame.isLastMoveUndo()) {
            return false;
        }

        ItemStack[][] lastPosition = activeGame.getLastPosition();

        for (int i = 0; i < inventoryArray.length; i++) {
            System.arraycopy(lastPosition[i], 0, inventoryArray[i], 0, inventoryArray[i].length);
        }

        activeGame.addToScore(-activeGame.getScoreGainedAfterLastMove());
        activeGame.decrementUndoLastMoveCounter();

        final int SLOT_UNDO = 52; // FIXME not DRY
        setItemInSlot(activeGame.getGameWindow(), SLOT_UNDO, activeGame.hasNoUndoLastMoveLeft() ? createUsedUndoButton() : createUndoButton(activeGame.getUndoLastMoveCounter()));
        activeGame.setLastMoveUndo(true);
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
        return NumberRepresentation.getNextRepresentation(currentRepresentation)
                                   .getDisplayableBlock();
    }

    /**
     * Sets a freshly generated ItemStack block (2 or 4) at any random open slot within the game window. If there's no empty slots: do nothing (shouldn't be possible because that would mean Game Over)
     */
    public void spawnNewBlock(Inventory inventory) {
        List<Integer> emptySlots = IntStream.range(0, inventory.getSize())
                                            .filter(i -> Objects.isNull(inventory.getItem(i)))
                                            .boxed()
                                            .toList();

        if (emptySlots.isEmpty()) {
            return;
        }

        inventory.setItem(emptySlots.get(new Random().nextInt(emptySlots.size())), generateNewBlock());
    }

    private ItemStack generateNewBlock() {
        NumberRepresentation chosenNumber = random.nextInt(2) == 0 ? NumberRepresentation.TWO : NumberRepresentation.FOUR;
        return new ItemBuilder(chosenNumber.getDisplayableBlock()).create();
    }

    private boolean isKnownWindow(InventoryView inventoryView, ConfigKey titleKey) {
        return inventoryView.getTitle()
                            .contains(LegacyComponentSerializer.legacySection()
                                                               .serialize(titleKey.getFormattedValue()));
    }

    private ItemStack getHelpGUIPlayerStatsHead(Player player) {
        return (new ItemBuilder(getPlayerSkullItem(player))).addLore(TOTAL_PLAYTIME.getFormattedValue(makeSecondsATimestamp(FILE_MANAGER.getLongByKey(player, PlayerConfigField.TOTAL_PLAYTIME.getKey()))))
                                                            .addLore(HIGH_SCORE.getFormattedValue(String.valueOf(FILE_MANAGER.getLongByKey(player, PlayerConfigField.HIGH_SCORE.getKey()))))
                                                            .addLore(GAMES_PLAYED.getFormattedValue(String.valueOf(FILE_MANAGER.getLongByKey(player, PlayerConfigField.ATTEMPTS.getKey()))))
                                                            .addLore(AVERAGE_SCORE.getFormattedValue(String.valueOf(Math.round(FILE_MANAGER.getLongByKey(player, PlayerConfigField.AVERAGE_SCORE.getKey())))))
                                                            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                                                            .create();
    }


}