package io.stealingdapenta.mc2048.utils;

import static io.stealingdapenta.mc2048.MC2048.logger;
import static io.stealingdapenta.mc2048.config.ConfigKey.GAME_GUI_FILLER_ANIMATION_MATERIAL;
import static io.stealingdapenta.mc2048.config.ConfigKey.GAME_GUI_FILLER_ANIMATION_MATERIAL_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.GAME_GUI_FILLER_MATERIAL;
import static io.stealingdapenta.mc2048.config.ConfigKey.GAME_GUI_FILLER_MATERIAL_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.GAME_GUI_TITLE;
import static io.stealingdapenta.mc2048.config.ConfigKey.HELP_GUI_FILLER_MATERIAL;
import static io.stealingdapenta.mc2048.config.ConfigKey.HELP_GUI_FILLER_MATERIAL_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.HELP_GUI_TITLE;
import static io.stealingdapenta.mc2048.config.ConfigKey.INFO_ITEM_LORE_1;
import static io.stealingdapenta.mc2048.config.ConfigKey.INFO_ITEM_LORE_2;
import static io.stealingdapenta.mc2048.config.ConfigKey.INFO_ITEM_LORE_3;
import static io.stealingdapenta.mc2048.config.ConfigKey.INFO_ITEM_LORE_4;
import static io.stealingdapenta.mc2048.config.ConfigKey.INFO_ITEM_LORE_5;
import static io.stealingdapenta.mc2048.config.ConfigKey.INFO_ITEM_MATERIAL;
import static io.stealingdapenta.mc2048.config.ConfigKey.INFO_ITEM_MATERIAL_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.INFO_ITEM_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.MOVE_BUTTON_DOWN_MATERIAL;
import static io.stealingdapenta.mc2048.config.ConfigKey.MOVE_BUTTON_DOWN_MATERIAL_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MOVE_BUTTON_DOWN_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.MOVE_BUTTON_DOWN_SLOT;
import static io.stealingdapenta.mc2048.config.ConfigKey.MOVE_BUTTON_LEFT_MATERIAL;
import static io.stealingdapenta.mc2048.config.ConfigKey.MOVE_BUTTON_LEFT_MATERIAL_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MOVE_BUTTON_LEFT_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.MOVE_BUTTON_LEFT_SLOT;
import static io.stealingdapenta.mc2048.config.ConfigKey.MOVE_BUTTON_LORE;
import static io.stealingdapenta.mc2048.config.ConfigKey.MOVE_BUTTON_RIGHT_MATERIAL;
import static io.stealingdapenta.mc2048.config.ConfigKey.MOVE_BUTTON_RIGHT_MATERIAL_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MOVE_BUTTON_RIGHT_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.MOVE_BUTTON_RIGHT_SLOT;
import static io.stealingdapenta.mc2048.config.ConfigKey.MOVE_BUTTON_UP_MATERIAL;
import static io.stealingdapenta.mc2048.config.ConfigKey.MOVE_BUTTON_UP_MATERIAL_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.MOVE_BUTTON_UP_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.MOVE_BUTTON_UP_SLOT;
import static io.stealingdapenta.mc2048.config.ConfigKey.PLAYER_ITEM_LORE_AVERAGE_SCORE;
import static io.stealingdapenta.mc2048.config.ConfigKey.PLAYER_ITEM_LORE_GAMES_PLAYED;
import static io.stealingdapenta.mc2048.config.ConfigKey.PLAYER_ITEM_LORE_HIGH_SCORE;
import static io.stealingdapenta.mc2048.config.ConfigKey.PLAYER_ITEM_LORE_TOTAL_PLAYTIME;
import static io.stealingdapenta.mc2048.config.ConfigKey.PLAYER_ITEM_MATERIAL;
import static io.stealingdapenta.mc2048.config.ConfigKey.PLAYER_ITEM_MATERIAL_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.PLAYER_ITEM_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.PLAYER_ITEM_SLOT;
import static io.stealingdapenta.mc2048.config.ConfigKey.START_BUTTON_LORE;
import static io.stealingdapenta.mc2048.config.ConfigKey.START_BUTTON_MATERIAL;
import static io.stealingdapenta.mc2048.config.ConfigKey.START_BUTTON_MATERIAL_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.START_BUTTON_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.SPEED_BUTTON_LORE;
import static io.stealingdapenta.mc2048.config.ConfigKey.SPEED_BUTTON_MATERIAL;
import static io.stealingdapenta.mc2048.config.ConfigKey.SPEED_BUTTON_MATERIAL_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.SPEED_BUTTON_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.SPEED_BUTTON_SLOT;
import static io.stealingdapenta.mc2048.config.ConfigKey.SPEED_BUTTON_SPEED;
import static io.stealingdapenta.mc2048.config.ConfigKey.UNDO_BUTTON_SLOT;
import static io.stealingdapenta.mc2048.config.ConfigKey.UNDO_BUTTON_UNUSED_LORE;
import static io.stealingdapenta.mc2048.config.ConfigKey.UNDO_BUTTON_UNUSED_MATERIAL;
import static io.stealingdapenta.mc2048.config.ConfigKey.UNDO_BUTTON_UNUSED_MATERIAL_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.UNDO_BUTTON_UNUSED_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.UNDO_BUTTON_UNUSED_USES;
import static io.stealingdapenta.mc2048.config.ConfigKey.UNDO_BUTTON_USAGES;
import static io.stealingdapenta.mc2048.config.ConfigKey.UNDO_BUTTON_USED_LORE;
import static io.stealingdapenta.mc2048.config.ConfigKey.UNDO_BUTTON_USED_MATERIAL;
import static io.stealingdapenta.mc2048.config.ConfigKey.UNDO_BUTTON_USED_MATERIAL_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.UNDO_BUTTON_USED_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.UNDO_BUTTON_USED_USES;
import static io.stealingdapenta.mc2048.utils.FileManager.FILE_MANAGER;
import static io.stealingdapenta.mc2048.utils.ItemBuilder.setCustomModelDataTo;
import static io.stealingdapenta.mc2048.utils.data.ActiveGame.makeSecondsATimestamp;
import static org.bukkit.Bukkit.createInventory;

import io.stealingdapenta.mc2048.MC2048;
import io.stealingdapenta.mc2048.config.ConfigKey;
import io.stealingdapenta.mc2048.config.PlayerConfigField;
import io.stealingdapenta.mc2048.utils.data.ActiveGame;
import io.stealingdapenta.mc2048.utils.data.ButtonAction;
import io.stealingdapenta.mc2048.utils.data.GameHolder;
import io.stealingdapenta.mc2048.utils.data.HelperHolder;
import io.stealingdapenta.mc2048.utils.data.MovementInstruction;
import io.stealingdapenta.mc2048.utils.data.NumberRepresentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryUtil {

    public final MC2048 javaPlugin;
    private final HighScoreManager highScoreManager;

    public static final int INVENTORY_ROWS = 6;
    public static final int INVENTORY_COLUMNS = 9;
    public static final int ROW_AND_COLUMN_SIZE = 4;
    public static final int REQUIRED_SIZE = INVENTORY_ROWS * INVENTORY_COLUMNS;
    private static final int[][] mergeSlots = {{10, 11, 12, 13}, {19, 20, 21, 22}, {28, 29, 30, 31}, {37, 38, 39, 40}};
    private static final String WRONG_SIZE = "Error filling sides of inventory: wrong size!";

    private static final String ERROR_SKULL = "Error getting skull meta for %s.";
    private final Random random = new Random();

    public InventoryUtil(MC2048 javaPlugin, HighScoreManager highScoreManager) {
        this.javaPlugin = javaPlugin;
        this.highScoreManager = highScoreManager;
    }

    public static ItemStack getPlayerSkullItem(Player player) {
        ItemStack playerHead = setCustomModelDataTo((new ItemBuilder(PLAYER_ITEM_MATERIAL.getMaterialValue())).create(), PLAYER_ITEM_MATERIAL_CMD);
        SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
        if (Objects.isNull(skullMeta)) {
            logger.warning(ERROR_SKULL.formatted(player.getName()));
            return playerHead;
        }
        skullMeta.setOwningPlayer(player);
        skullMeta.setDisplayName(LegacyComponentSerializer.legacySection()
                                                          .serialize(PLAYER_ITEM_NAME.getFormattedValue(player.getName())));
        playerHead.setItemMeta(skullMeta);
        return playerHead;
    }

    public void showEndAnimation(Inventory inventory, long delay) {
        if (inventory.getSize() != REQUIRED_SIZE) {
            logger.warning(WRONG_SIZE);
            return;
        }

        for (int i = 0; i < REQUIRED_SIZE; i++) {
            if (isInvalidGameSlot(i) && i!=PLAYER_ITEM_SLOT.getIntValue()) {
                inventory.setItem(i, getGameEndItem());
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < REQUIRED_SIZE; i++) {
                    if (isInvalidGameSlot(i) && i!=PLAYER_ITEM_SLOT.getIntValue()) {
                        inventory.setItem(i, getGameFillerItem());
                    }
                }
            }
        }.runTaskLater(javaPlugin, delay);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < REQUIRED_SIZE; i++) {
                    if (isInvalidGameSlot(i) && i!=PLAYER_ITEM_SLOT.getIntValue()) {
                        inventory.setItem(i, getGameEndItem());
                    }
                }
            }
        }.runTaskLater(javaPlugin, delay*2);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < REQUIRED_SIZE; i++) {
                    if (isInvalidGameSlot(i) && i!=PLAYER_ITEM_SLOT.getIntValue()) {
                        inventory.setItem(i, getGameFillerItem());
                    }
                }
            }
        }.runTaskLater(javaPlugin, delay*3);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < REQUIRED_SIZE; i++) {
                    if (isInvalidGameSlot(i) && i!=PLAYER_ITEM_SLOT.getIntValue()) {
                        inventory.setItem(i, getGameEndItem());
                    }
                }
            }
        }.runTaskLater(javaPlugin, delay*4);
    }

    public Inventory createGameInventory(ActiveGame activeGame) {
        Inventory inventory = createGameGUIWindow(activeGame.getPlayer(), GAME_GUI_TITLE, activeGame.getScore());
        fillSides(inventory);
        activeGame.setGameWindow(inventory);
        setButtonsAndStats(activeGame);
        return inventory;
    }

    public Inventory createHelpInventory(Player player) {
        Inventory helpGUI = createHelperGUIWindow(player, HELP_GUI_TITLE);

        final int playerStatsSlot = 28;
        final int highScoreSlot = 34;
        final int infoSlot = 31;
        final int playButtonSlot = 49;

        fillWithLegend(helpGUI);
        setItemInSlot(helpGUI, playerStatsSlot, getHelpGUIPlayerStatsHead(player));
        setItemInSlot(helpGUI, highScoreSlot, highScoreManager.getHighScoresItem());
        setItemInSlot(helpGUI, infoSlot, setCustomModelDataTo(new ItemBuilder(INFO_ITEM_MATERIAL.getMaterialValue()).setDisplayName(INFO_ITEM_NAME.getFormattedValue())
                                                                                                                    .addLore(INFO_ITEM_LORE_1.getFormattedValue())
                                                                                                                    .addLore(INFO_ITEM_LORE_2.getFormattedValue())
                                                                                                                    .addLore(INFO_ITEM_LORE_3.getFormattedValue())
                                                                                                                    .addLore(INFO_ITEM_LORE_4.getFormattedValue())
                                                                                                                    .addLore(INFO_ITEM_LORE_5.getFormattedValue())
                                                                                                                    .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                                                                                                                    .create(), INFO_ITEM_MATERIAL_CMD));
        setItemInSlot(helpGUI, playButtonSlot, setCustomModelDataTo(new ItemBuilder(START_BUTTON_MATERIAL.getMaterialValue()).setDisplayName(START_BUTTON_NAME.getFormattedValue())
                                                                                                                            .addLore(START_BUTTON_LORE.getFormattedValue())
                                                                                                                            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                                                                                                                            .create(), START_BUTTON_MATERIAL_CMD));
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

    private boolean isInvalidGameSlot(int slot) {
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
        return new ItemBuilder(setCustomModelDataTo(new ItemStack(GAME_GUI_FILLER_MATERIAL.getMaterialValue(), 1), GAME_GUI_FILLER_MATERIAL_CMD)).setDisplayName(" ")
                                                                                                                                                 .create();
    }

    private ItemStack getHelpGuiFillerItem() {
        return new ItemBuilder(setCustomModelDataTo(new ItemStack(HELP_GUI_FILLER_MATERIAL.getMaterialValue(), 1), HELP_GUI_FILLER_MATERIAL_CMD)).setDisplayName(" ")
                                                                                                                                                 .create();
    }

    private ItemStack getGameEndItem() {
        return new ItemBuilder(setCustomModelDataTo(new ItemStack(GAME_GUI_FILLER_ANIMATION_MATERIAL.getMaterialValue(), 1), GAME_GUI_FILLER_ANIMATION_MATERIAL_CMD)).setDisplayName(" ")
                                                                                                                                                 .create();
    }

    private void setButtonsAndStats(ActiveGame activeGame) {
        final int SLOT_UP = MOVE_BUTTON_UP_SLOT.getIntValue();
        final int SLOT_DOWN = MOVE_BUTTON_DOWN_SLOT.getIntValue();
        final int SLOT_LEFT = MOVE_BUTTON_LEFT_SLOT.getIntValue();
        final int SLOT_RIGHT = MOVE_BUTTON_RIGHT_SLOT.getIntValue();
        final int SLOT_UNDO = UNDO_BUTTON_SLOT.getIntValue();
        final int SLOT_SPEED = SPEED_BUTTON_SLOT.getIntValue();
        setItemInSlot(activeGame.getGameWindow(), SLOT_UP, createButton(MOVE_BUTTON_UP_NAME, MOVE_BUTTON_UP_MATERIAL, MOVE_BUTTON_UP_MATERIAL_CMD));
        setItemInSlot(activeGame.getGameWindow(), SLOT_DOWN, createButton(MOVE_BUTTON_DOWN_NAME, MOVE_BUTTON_DOWN_MATERIAL, MOVE_BUTTON_DOWN_MATERIAL_CMD));
        setItemInSlot(activeGame.getGameWindow(), SLOT_LEFT, createButton(MOVE_BUTTON_LEFT_NAME, MOVE_BUTTON_LEFT_MATERIAL, MOVE_BUTTON_LEFT_MATERIAL_CMD));
        setItemInSlot(activeGame.getGameWindow(), SLOT_RIGHT, createButton(MOVE_BUTTON_RIGHT_NAME, MOVE_BUTTON_RIGHT_MATERIAL, MOVE_BUTTON_RIGHT_MATERIAL_CMD));

        if (SLOT_UNDO >= 0) {
            if (activeGame.hasNoUndoLastMoveLeft()) {
                setItemInSlot(activeGame.getGameWindow(), SLOT_UNDO, getEmptyUndoButton());
            } else {
                if (!activeGame.isLastMoveUndo()) {
                    setItemInSlot(activeGame.getGameWindow(), SLOT_UNDO, getUnblockedUndoButton(UNDO_BUTTON_USAGES.getIntValue()));
                } else {
                    setItemInSlot(activeGame.getGameWindow(), SLOT_UNDO, getBlockedUndoButton(UNDO_BUTTON_USAGES.getIntValue()));
                }
            }
        }

        if (SLOT_SPEED >= 0) {
            setItemInSlot(activeGame.getGameWindow(), SLOT_SPEED, getSpeedButton(FILE_MANAGER.getAnimationSpeed(activeGame.getPlayer())));
        }
    }

    private ItemStack createButton(ConfigKey buttonName, ConfigKey materialName, ConfigKey customMetaData) {
        return setCustomModelDataTo(new ItemBuilder(materialName.getMaterialValue()).setDisplayName(buttonName.getFormattedValue())
                                                                                    .addLore(MOVE_BUTTON_LORE.getFormattedValue())
                                                                                    .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                                                                                    .create(), customMetaData);
    }

    private ItemStack getUnblockedUndoButton(int numberOfUndoLeft) {
        return setCustomModelDataTo(new ItemBuilder(UNDO_BUTTON_UNUSED_MATERIAL.getMaterialValue()).setDisplayName(UNDO_BUTTON_UNUSED_NAME.getFormattedValue())
                                                                                                   .addLore(UNDO_BUTTON_UNUSED_LORE.getFormattedValue())
                                                                                                   .addLore(UNDO_BUTTON_UNUSED_USES.getFormattedValue()
                                                                                                                                   .append(Component.text(
                                                                                                                                            numberOfUndoLeft >= 0 ? numberOfUndoLeft+"" : "∞"
                                                                                                                                    )))
                                                                                                   .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                                                                                                   .create(), UNDO_BUTTON_UNUSED_MATERIAL_CMD);
    }

    private ItemStack getBlockedUndoButton(int numberOfUndoLeft) {
        return setCustomModelDataTo(new ItemBuilder(UNDO_BUTTON_UNUSED_MATERIAL.getMaterialValue()).setDisplayName(UNDO_BUTTON_UNUSED_NAME.getFormattedValue())
                                                                                                   .addLore(UNDO_BUTTON_USED_LORE.getFormattedValue())
                                                                                                   .addLore(UNDO_BUTTON_UNUSED_USES.getFormattedValue()
                                                                                                                                    .append(Component.text(
                                                                                                                                        numberOfUndoLeft >= 0 ? numberOfUndoLeft+"" : "∞"
                                                                                                                                    )))
                                                                                                   .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                                                                                                   .create(), UNDO_BUTTON_UNUSED_MATERIAL_CMD);
    }

    private ItemStack getEmptyUndoButton() {
        return setCustomModelDataTo(new ItemBuilder(UNDO_BUTTON_USED_MATERIAL.getMaterialValue()).setDisplayName(UNDO_BUTTON_USED_NAME.getFormattedValue())
                                                                                                 .addLore(UNDO_BUTTON_USED_LORE.getFormattedValue())
                                                                                                 .addLore(UNDO_BUTTON_USED_USES.getFormattedValue())
                                                                                                 .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                                                                                                 .create(), UNDO_BUTTON_USED_MATERIAL_CMD);
    }

    private ItemStack getSpeedButton(int currentSpeed) {
        return setCustomModelDataTo(new ItemBuilder(SPEED_BUTTON_MATERIAL.getMaterialValue()).setDisplayName(SPEED_BUTTON_NAME.getFormattedValue())
                                                                                             .addLore(SPEED_BUTTON_LORE.getFormattedValue())
                                                                                             .addLore(SPEED_BUTTON_SPEED.getFormattedValue()
                                                                                                                        .append(Component.text(currentSpeed)))
                                                                                             .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                                                                                             .create(), SPEED_BUTTON_MATERIAL_CMD);
    }

    public void updateUndoButton(ActiveGame activeGame) {
        final int SLOT_UNDO = UNDO_BUTTON_SLOT.getIntValue();
        if (SLOT_UNDO >= 0) {
            if (activeGame.hasNoUndoLastMoveLeft()) {
                activeGame.getGameWindow().setItem(SLOT_UNDO, null);
                setItemInSlot(activeGame.getGameWindow(), SLOT_UNDO, getEmptyUndoButton());
            } else {
                if (!activeGame.isLastMoveUndo()) {
                    activeGame.getGameWindow().setItem(SLOT_UNDO, null);
                    setItemInSlot(activeGame.getGameWindow(), SLOT_UNDO, getUnblockedUndoButton(activeGame.getUndoLastMoveCounter()));
                } else {
                    activeGame.getGameWindow().setItem(SLOT_UNDO, null);
                    setItemInSlot(activeGame.getGameWindow(), SLOT_UNDO, getBlockedUndoButton(activeGame.getUndoLastMoveCounter()));
                }
            }
        }
    }

    // Doesn't need a disabled check as the caller method implies its enabled
    public void updateSpeedButton(ActiveGame activeGame, int newSpeed) {
        setItemInSlot(activeGame.getGameWindow(), SPEED_BUTTON_SLOT.getIntValue(), getSpeedButton(newSpeed));
    }

    private void setItemInSlot(Inventory inventory, int slot, ItemStack itemStack) {
        inventory.setItem(slot, itemStack);
    }

    private Inventory createHelperGUIWindow(Player player, ConfigKey title) {
        return createInventory(new HelperHolder(player), InventoryUtil.REQUIRED_SIZE, LegacyComponentSerializer.legacySection()
                                                                                                             .serialize(title.getFormattedValue()));
    }

    private Inventory createGameGUIWindow(Player player, ConfigKey title, int score) {
        return createInventory(new GameHolder(player), InventoryUtil.REQUIRED_SIZE, LegacyComponentSerializer.legacySection()
                                                                                                             .serialize(title.getFormattedValue(score + "")));
    }

    public boolean isGameWindow(InventoryView inventoryView) {
        InventoryHolder holder = inventoryView.getTopInventory()
                                              .getHolder();
        return holder instanceof GameHolder;
    }

    public boolean isHelpWindow(InventoryView inventoryView) {
        InventoryHolder holder = inventoryView.getTopInventory()
                                              .getHolder();
        return holder instanceof HelperHolder;
    }

    /**
     * @return whether the open inventory resembles ANY possible GUI window from this plugin
     */
    public boolean isAnyGameWindow(InventoryView inventoryView) {
        return isGameWindow(inventoryView) || isHelpWindow(inventoryView);
    }

    /**
     * @return tick delay for caller method continuation
     */
    public int processGameAction(ActiveGame activeGame, ButtonAction action) {
        Inventory gameWindow = activeGame.getGameWindow();
        ItemStack[][] itemsInGame = new ItemStack[ROW_AND_COLUMN_SIZE][ROW_AND_COLUMN_SIZE];

        copyGameWindowContentsToArray(gameWindow, itemsInGame);

        int tickDelay;
        if (action.equals(ButtonAction.UNDO)) {
            // [debug] javaPlugin.getLogger().info("processGameAction: undoing last move");
            tickDelay = undoLastMove(itemsInGame, activeGame);
        } else {
            // [debug] javaPlugin.getLogger().info("processGameAction: moving items");
            tickDelay = moveItems(activeGame.getPlayer(), itemsInGame, activeGame, action);
        }

        // [debug] javaPlugin.getLogger().info("processGameAction.tickDelay == " + tickDelay);

        if (tickDelay < 0 || ButtonAction.UNDO.equals(action)) {
            copyItemArrayToGameWindow(gameWindow, itemsInGame);
            activeGame.updateInventoryTitle(activeGame.getScore());
        } else if (tickDelay > 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    activeGame.setLastMoveUndo(false);

                    copyItemArrayToGameWindow(gameWindow, itemsInGame);
                    activeGame.updateInventoryTitle(activeGame.getScore());
                }
            }.runTaskLater(javaPlugin, tickDelay);
        }

        return tickDelay;
    }

    /**
     * Moves items (calculates, merges, and scores) with animation.
     *
     * @return tick delay for caller method continuation
     */
    private int moveItems(Player player, ItemStack[][] inventoryArray, ActiveGame activeGame, ButtonAction action) {

        // Create a deep copy of the current state before making moves
        ItemStack[][] lastPosition = new ItemStack[ROW_AND_COLUMN_SIZE][ROW_AND_COLUMN_SIZE];
        for (int i = 0; i < inventoryArray.length; i++) {
            lastPosition[i] = Arrays.copyOf(inventoryArray[i], inventoryArray[i].length);
        }

        // Calculate moves (updates board simulation and scores)
        List<MovementInstruction> instructions = calculateMoves(inventoryArray, activeGame, action);
        if ((Objects.isNull(instructions)) || instructions.isEmpty()) {
            // [debug] javaPlugin.getLogger().info("moveItems.instructions.isEmpty() == true");
            return 0;
        }
        
        // [debug] javaPlugin.getLogger().info("moveItems.instructions.isEmpty() == false");

        // Since there will be movement, we can update the acitveGame's saved lastPosition
        activeGame.setLastPosition(lastPosition);
        activeGame.resetGainedAfterLastMove();
        // [debug] javaPlugin.getLogger().info("moveItems.lastPosition saved");

        // Animate the moves and get the maximum number of steps (ticks) needed
        int maxSteps = animateMovementsSimultaneous(player, activeGame.getGameWindow(), instructions);

        // Compute tick delay for final board update (ensure at least 1 tick delay)
        return Math.max((maxSteps - 1) * FILE_MANAGER.getAnimationSpeed(player), 1);
    }


    /**
     * Calculates the moves and merges for the board. Updates the game's score.
     *
     * @return list of movements to preform, or null if none
     */
    private List<MovementInstruction> calculateMoves(ItemStack[][] board, ActiveGame activeGame, ButtonAction action) {
        List<MovementInstruction> instructions = new ArrayList<>();

        switch (action) {
            case UP:
                for (int row = 1; row < ROW_AND_COLUMN_SIZE; row++) {
                    for (int col = 0; col < ROW_AND_COLUMN_SIZE; col++) {
                        if (Objects.nonNull(board[row][col])) {
                            // [debug] javaPlugin.getLogger().info("calculateMoves.processCell(UP row: " + row + " col: " + col + ")");
                            processCell(row, col, -1, 0, board, activeGame, instructions);
                        }
                    }
                }
                break;
            case DOWN:
                for (int row = ROW_AND_COLUMN_SIZE - 2; row >= 0; row--) {
                    for (int col = 0; col < ROW_AND_COLUMN_SIZE; col++) {
                        if (Objects.nonNull(board[row][col])) {
                            // [debug] javaPlugin.getLogger().info("calculateMoves.processCell(DOWN row: " + row + " col: " + col + ")");
                            processCell(row, col, 1, 0, board, activeGame, instructions);
                        }
                    }
                }
                break;
            case LEFT:
                for (int col = 1; col < ROW_AND_COLUMN_SIZE; col++) {
                    for (int row = 0; row < ROW_AND_COLUMN_SIZE; row++) {
                        if (Objects.nonNull(board[row][col])) {
                            // [debug] javaPlugin.getLogger().info("calculateMoves.processCell(LEFT row: " + row + " col: " + col + ")");
                            processCell(row, col, 0, -1, board, activeGame, instructions);
                        }
                    }
                }
                break;
            case RIGHT:
                for (int col = ROW_AND_COLUMN_SIZE - 2; col >= 0; col--) {
                    for (int row = 0; row < ROW_AND_COLUMN_SIZE; row++) {
                        if (Objects.nonNull(board[row][col])) {
                            // [debug] javaPlugin.getLogger().info("calculateMoves.processCell(RIGHT row: " + row + " col: " + col + ")");
                            processCell(row, col, 0, 1, board, activeGame, instructions);
                        }
                    }
                }
                break;
            case UNDO:
            case SPEED:
            case START:
                return null;
        }
        return instructions;
    }

    /**
     * Processes a single cell by sliding it as far as possible and then merging if possible.
     *
     * @param row          the starting row of the cell
     * @param col          the starting column of the cell
     * @param rowDelta     the row increment (e.g., -1 for up, 1 for down, 0 for neither)
     * @param colDelta     the column increment (e.g., -1 for left, 1 for right, 0 for neither)
     * @param board        the game board
     * @param activeGame   the active game instance for updating scores
     * @param instructions the list to add movement instructions to
     */
    private void processCell(int row, int col, int rowDelta, int colDelta, ItemStack[][] board, ActiveGame activeGame, List<MovementInstruction> instructions) {
        int currentRow = row;
        int currentCol = col;
        int stepCount = 0;

        // Temporary list to hold all instructions for this tile’s movement
        List<MovementInstruction> tileInstructions = new ArrayList<>();

        // Slide one spot at a time while the next cell is empty
        while (isInBounds(currentRow + rowDelta, currentCol + colDelta) && Objects.isNull(board[currentRow + rowDelta][currentCol + colDelta])) {
            int fromRow = currentRow;
            int fromCol = currentCol;
            currentRow += rowDelta;
            currentCol += colDelta;
            stepCount++;
            // Record this one-step movement; temporarily totalSteps is 0 (will be updated later)
            tileInstructions.add(new MovementInstruction(fromRow, fromCol, currentRow, currentCol, board[fromRow][fromCol], false, null, stepCount, 0));
            // Update the simulation: move the tile one step
            board[currentRow][currentCol] = board[fromRow][fromCol];
            board[fromRow][fromCol] = null;
        }

        // If a merge is possible from the current position, do it as an extra step.
        if (isInBounds(currentRow + rowDelta, currentCol + colDelta) && Objects.nonNull(board[currentRow + rowDelta][currentCol + colDelta])) {
            if (board[currentRow + rowDelta][currentCol + colDelta].isSimilar(board[currentRow][currentCol])) {
                stepCount++; // merging counts as an extra step
                ItemStack currentItem = board[currentRow][currentCol];
                ItemStack merged = getNextRepresentation(currentItem);
                tileInstructions.add(new MovementInstruction(currentRow, currentCol, currentRow + rowDelta, currentCol + colDelta, currentItem, true, merged, stepCount, 0));
                board[currentRow + rowDelta][currentCol + colDelta] = merged;
                board[currentRow][currentCol] = null;
                activeGame.addToScore(NumberRepresentation.getScoreFromItem(merged));
                activeGame.addToGainedAfterLastMove(NumberRepresentation.getScoreFromItem(merged));
            }
        }

        // Update all instructions with the final total step count, then add them to the overall list
        for (MovementInstruction instruction : tileInstructions) {
            instruction.totalSteps = stepCount;
            instructions.add(instruction);
        }
    }

    /**
     * Checks if the given row and column indices are within the board boundaries.
     *
     * @return true if the indices are valid; false otherwise
     */
    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < ROW_AND_COLUMN_SIZE && col >= 0 && col < ROW_AND_COLUMN_SIZE;
    }

    /**
     * Animate all movement instructions in synchronized steps.
     * <p>
     * Each step is scheduled as one global task so that all first-step updates occur on the same tick, all second-step updates occur on the next tick, and so on.
     *
     * @return max steps for this movement
     */
    public int animateMovementsSimultaneous(Player player, Inventory menu, List<MovementInstruction> instructions) {
        // Compute the overall maximum steps from the instructions
        int maxSteps = instructions.stream()
                                   .mapToInt(instruction -> instruction.totalSteps)
                                   .max()
                                   .orElse(0);
        // [debug] javaPlugin.getLogger().info("animateMovementsSimultaneous.maxSteps == " + maxSteps);

        // For each step, schedule a task to update the GUI accordingly
        for (int step = 1; step <= maxSteps; step++) {
            final int currentStep = step;
            new BukkitRunnable() {
                @Override
                public void run() {
                    // [debug] javaPlugin.getLogger().info("animateMovementsSimultaneous.currentStep == __" + currentStep + "__");
                    // Process all instructions whose step matches this tick.
                    for (MovementInstruction instruction : instructions) {
                        if (instruction.step == currentStep) {
                            // Clear the previous slot (i.e. the from slot for this step)
                            int clearSlot = mergeSlots[instruction.fromRow][instruction.fromColumn];
                            menu.setItem(clearSlot, null);
                            // Place the item into the destination slot for this step
                            int newSlot = mergeSlots[instruction.toRow][instruction.toColumn];
                            if (instruction.merge && currentStep == instruction.totalSteps) {
                                menu.setItem(newSlot, instruction.mergedItem);
                            } else {
                                menu.setItem(newSlot, instruction.item);
                            }
                        }
                    }
                }
            }.runTaskLater(javaPlugin, (currentStep - 1L) * FILE_MANAGER.getAnimationSpeed(player));
        }
        return maxSteps;
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

        // [debug] javaPlugin.getLogger().info("ran copyItemArrayToGameWindow()");
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

    private int undoLastMove(ItemStack[][] inventoryArray, ActiveGame activeGame) {
        if (Objects.isNull(activeGame.getLastPosition()) || activeGame.hasNoUndoLastMoveLeft() || activeGame.isLastMoveUndo()) {
            return 0;
        }

        ItemStack[][] lastPosition = activeGame.getLastPosition();

        for (int i = 0; i < inventoryArray.length; i++) {
            System.arraycopy(lastPosition[i], 0, inventoryArray[i], 0, inventoryArray[i].length);
        }

        activeGame.removeFromScore(activeGame.getScoreGainedAfterLastMove());
        activeGame.decrementUndoLastMoveCounter();

        if (activeGame.hasNoUndoLastMoveLeft()) {
            setItemInSlot(activeGame.getGameWindow(), UNDO_BUTTON_SLOT.getIntValue(), getEmptyUndoButton());
        } else {
            setItemInSlot(activeGame.getGameWindow(), UNDO_BUTTON_SLOT.getIntValue(), getBlockedUndoButton(activeGame.getUndoLastMoveCounter()));
        }

        activeGame.setLastMoveUndo(true);
        return -1;
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

    // [debug]
    /*
        public void spawnNewBlock1(Inventory inventory) {
            List<Integer> emptySlots = IntStream.range(0, inventory.getSize())
                                                .filter(i -> Objects.isNull(inventory.getItem(i)))
                                                .boxed()
                                                .toList();
            if (emptySlots.isEmpty()) return;
            inventory.setItem(37, generateNewBlock());
        }
        
        public void spawnNewBlock2(Inventory inventory) {
            List<Integer> emptySlots = IntStream.range(0, inventory.getSize())
                                                .filter(i -> Objects.isNull(inventory.getItem(i)))
                                                .boxed()
                                                .toList();
            if (emptySlots.isEmpty()) return;
            inventory.setItem(40, generateNewBlock());
        }
    */

    private ItemStack generateNewBlock() {
        NumberRepresentation chosenNumber = random.nextInt(2) == 0 ? NumberRepresentation.TWO : NumberRepresentation.FOUR;
        return new ItemBuilder(chosenNumber.getDisplayableBlock()).create();
    }

    private ItemStack getHelpGUIPlayerStatsHead(Player player) {
        return (new ItemBuilder(getPlayerSkullItem(player))).addLore(PLAYER_ITEM_LORE_GAMES_PLAYED.getFormattedValue(String.valueOf(FILE_MANAGER.getLongByKey(player, PlayerConfigField.ATTEMPTS.getKey()))))
                                                            .addLore(PLAYER_ITEM_LORE_TOTAL_PLAYTIME.getFormattedValue(makeSecondsATimestamp(FILE_MANAGER.getLongByKey(player, PlayerConfigField.PLAYTIME.getKey()))))
                                                            .addLore(PLAYER_ITEM_LORE_HIGH_SCORE.getFormattedValue(String.valueOf(FILE_MANAGER.getLongByKey(player, PlayerConfigField.HIGH_SCORE.getKey()))))
                                                            .addLore(PLAYER_ITEM_LORE_AVERAGE_SCORE.getFormattedValue(String.valueOf(Math.round(FILE_MANAGER.getLongByKey(player, PlayerConfigField.AVERAGE_SCORE.getKey())))))
                                                            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                                                            .create();
    }
}