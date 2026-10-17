package io.stealingdapenta.mc2048.utils;

import static io.stealingdapenta.mc2048.MC2048.logger;
import static io.stealingdapenta.mc2048.config.ConfigKey.GAME_GUI_FILLER_ANIMATION_MATERIAL;
import static io.stealingdapenta.mc2048.config.ConfigKey.GAME_GUI_FILLER_ANIMATION_MATERIAL_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.GAME_GUI_FILLER_MATERIAL;
import static io.stealingdapenta.mc2048.config.ConfigKey.GAME_GUI_FILLER_MATERIAL_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.GAME_GUI_TITLE;
import static io.stealingdapenta.mc2048.config.ConfigKey.GENERATE_NEW_BLOCK_FOUR_PERCENT;
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
import static io.stealingdapenta.mc2048.config.ConfigKey.RESET_BUTTON_LORE;
import static io.stealingdapenta.mc2048.config.ConfigKey.RESET_BUTTON_MATERIAL;
import static io.stealingdapenta.mc2048.config.ConfigKey.RESET_BUTTON_MATERIAL_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.RESET_BUTTON_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.RESET_BUTTON_SLOT;
import static io.stealingdapenta.mc2048.config.ConfigKey.RESET_CONFIRM_NO_MATERIAL;
import static io.stealingdapenta.mc2048.config.ConfigKey.RESET_CONFIRM_NO_MATERIAL_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.RESET_CONFIRM_NO_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.RESET_CONFIRM_TITLE;
import static io.stealingdapenta.mc2048.config.ConfigKey.RESET_CONFIRM_YES_MATERIAL;
import static io.stealingdapenta.mc2048.config.ConfigKey.RESET_CONFIRM_YES_MATERIAL_CMD;
import static io.stealingdapenta.mc2048.config.ConfigKey.RESET_CONFIRM_YES_NAME;
import static io.stealingdapenta.mc2048.config.ConfigKey.START_BUTTON_LORE_1;
import static io.stealingdapenta.mc2048.config.ConfigKey.START_BUTTON_LORE_2;
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
import static io.stealingdapenta.mc2048.utils.ItemBuilder.setCustomModelDataTo;
import static io.stealingdapenta.mc2048.utils.data.ActiveGame.makeSecondsATimestamp;
import static org.bukkit.Bukkit.createInventory;

import io.stealingdapenta.mc2048.MC2048;
import io.stealingdapenta.mc2048.config.ConfigKey;
import io.stealingdapenta.mc2048.game.BoardEngine;
import io.stealingdapenta.mc2048.game.BoardEngine.Direction;
import io.stealingdapenta.mc2048.persistence.PlayerData;
import io.stealingdapenta.mc2048.utils.data.ActiveGame;
import io.stealingdapenta.mc2048.utils.data.ButtonAction;
import io.stealingdapenta.mc2048.utils.data.GameHolder;
import io.stealingdapenta.mc2048.utils.data.HelperHolder;
import io.stealingdapenta.mc2048.utils.data.MovementInstruction;
import io.stealingdapenta.mc2048.utils.data.NumberRepresentation;
import io.stealingdapenta.mc2048.utils.data.ResetConfirmationHolder;
import io.stealingdapenta.mc2048.utils.data.SavedGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
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
    public static final int RESET_CONFIRM_SLOT = 11;
    public static final int RESET_CANCEL_SLOT = 15;
    private static final int[][] mergeSlots = {{10, 11, 12, 13}, {19, 20, 21, 22}, {28, 29, 30, 31}, {37, 38, 39, 40}};
    private static final String WRONG_SIZE = "Error filling sides of inventory: wrong size!";

    private static final String ERROR_SKULL = "Error getting skull meta for %s.";
    private final Random random = new Random();
    private final BoardEngine boardEngine = new BoardEngine();

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

    public Inventory createResetConfirmationInventory(Player player) {
        Inventory inventory = createInventory(
            new ResetConfirmationHolder(player),
            27,
            LegacyComponentSerializer.legacySection().serialize(RESET_CONFIRM_TITLE.getFormattedValue())
        );
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            inventory.setItem(slot, getGameFillerItem());
        }
        inventory.setItem(RESET_CONFIRM_SLOT, createMenuButton(RESET_CONFIRM_YES_NAME, RESET_CONFIRM_YES_MATERIAL, RESET_CONFIRM_YES_MATERIAL_CMD));
        inventory.setItem(RESET_CANCEL_SLOT, createMenuButton(RESET_CONFIRM_NO_NAME, RESET_CONFIRM_NO_MATERIAL, RESET_CONFIRM_NO_MATERIAL_CMD));
        return inventory;
    }

    public Inventory createHelpInventory(Player player, PlayerData playerData) {
        Inventory helpGUI = createHelperGUIWindow(player, HELP_GUI_TITLE);

        final int playerStatsSlot = 28;
        final int highScoreSlot = 34;
        final int infoSlot = 31;
        final int playButtonSlot = 49;

        fillWithLegend(helpGUI);
        setItemInSlot(helpGUI, playerStatsSlot, getHelpGUIPlayerStatsHead(player, playerData));
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
                                                                                                                            .addLore(START_BUTTON_LORE_1.getFormattedValue())
                                                                                                                            .addLore(START_BUTTON_LORE_2.getFormattedValue())
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
        final int SLOT_RESET = RESET_BUTTON_SLOT.getIntValue();
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
            setItemInSlot(activeGame.getGameWindow(), SLOT_SPEED, getSpeedButton(activeGame.getAnimationSpeed()));
        }
        if (SLOT_RESET >= 0) {
            setItemInSlot(
                activeGame.getGameWindow(),
                SLOT_RESET,
                setCustomModelDataTo(
                    new ItemBuilder(RESET_BUTTON_MATERIAL.getMaterialValue())
                        .setDisplayName(RESET_BUTTON_NAME.getFormattedValue())
                        .addLore(RESET_BUTTON_LORE.getFormattedValue())
                        .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                        .create(),
                    RESET_BUTTON_MATERIAL_CMD
                )
            );
        }
    }

    private ItemStack createButton(ConfigKey buttonName, ConfigKey materialName, ConfigKey customMetaData) {
        return setCustomModelDataTo(new ItemBuilder(materialName.getMaterialValue()).setDisplayName(buttonName.getFormattedValue())
                                                                                    .addLore(MOVE_BUTTON_LORE.getFormattedValue())
                                                                                    .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                                                                                    .create(), customMetaData);
    }

    private ItemStack createMenuButton(ConfigKey buttonName, ConfigKey materialName, ConfigKey customMetaData) {
        return setCustomModelDataTo(
            new ItemBuilder(materialName.getMaterialValue())
                .setDisplayName(buttonName.getFormattedValue())
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .create(),
            customMetaData
        );
    }

    private ItemStack getUnblockedUndoButton(int numberOfUndoLeft) {
        return setCustomModelDataTo(new ItemBuilder(UNDO_BUTTON_UNUSED_MATERIAL.getMaterialValue()).setDisplayName(UNDO_BUTTON_UNUSED_NAME.getFormattedValue())
                                                                                                   .addLore(UNDO_BUTTON_UNUSED_LORE.getFormattedValue())
                                                                                                   .addLore(UNDO_BUTTON_UNUSED_USES.getFormattedValue()
                                                                                                                                   .append(Component.text(
                                                                                                                                            numberOfUndoLeft >= 0 ? StringUtil.formatInt(numberOfUndoLeft) : "∞"
                                                                                                                                    )))
                                                                                                   .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                                                                                                   .create(), UNDO_BUTTON_UNUSED_MATERIAL_CMD);
    }

    private ItemStack getBlockedUndoButton(int numberOfUndoLeft) {
        return setCustomModelDataTo(new ItemBuilder(UNDO_BUTTON_UNUSED_MATERIAL.getMaterialValue()).setDisplayName(UNDO_BUTTON_UNUSED_NAME.getFormattedValue())
                                                                                                   .addLore(UNDO_BUTTON_USED_LORE.getFormattedValue())
                                                                                                   .addLore(UNDO_BUTTON_UNUSED_USES.getFormattedValue()
                                                                                                                                    .append(Component.text(
                                                                                                                                        numberOfUndoLeft >= 0 ? StringUtil.formatInt(numberOfUndoLeft) : "∞"
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
                                                                                                             .serialize(title.getFormattedValue(StringUtil.formatInt(score))));
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
        return isGameWindow(inventoryView) || isHelpWindow(inventoryView) || isResetConfirmationWindow(inventoryView);
    }

    public boolean isResetConfirmationWindow(InventoryView inventoryView) {
        return inventoryView.getTopInventory().getHolder() instanceof ResetConfirmationHolder;
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
            tickDelay = moveItems(itemsInGame, activeGame, action);
        }

        // [debug] javaPlugin.getLogger().info("processGameAction.tickDelay == " + tickDelay);

        if (tickDelay < 0 || ButtonAction.UNDO.equals(action)) {
            copyItemArrayToGameWindow(gameWindow, itemsInGame);
        } else if (tickDelay > 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    activeGame.setLastMoveUndo(false);

                    copyItemArrayToGameWindow(gameWindow, itemsInGame);
                    activeGame.clearPendingBoard();
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
    private int moveItems(ItemStack[][] inventoryArray, ActiveGame activeGame, ButtonAction action) {

        // Create a deep copy of the current state before making moves
        ItemStack[][] lastPosition = new ItemStack[ROW_AND_COLUMN_SIZE][ROW_AND_COLUMN_SIZE];
        for (int i = 0; i < inventoryArray.length; i++) {
            lastPosition[i] = Arrays.copyOf(inventoryArray[i], inventoryArray[i].length);
        }

        int previousMoveScore = activeGame.getScoreGainedAfterLastMove();
        activeGame.resetGainedAfterLastMove();

        // Calculate moves (updates board simulation and scores)
        BoardEngine.MoveResult result = boardEngine.move(toValueBoard(inventoryArray), toDirection(action));
        if (!result.moved()) {
            activeGame.setScoreGainedAfterLastMove(previousMoveScore);
            return 0;
        }

        copyValueBoardToItems(result.board(), inventoryArray);
        activeGame.setPendingBoard(copyBoard(inventoryArray));
        activeGame.addToScore(result.scoreGain());
        activeGame.setScoreGainedAfterLastMove(result.scoreGain());
        List<MovementInstruction> instructions = result.movements().stream()
            .map(this::toMovementInstruction)
            .toList();

        // Since there will be movement, we can update the acitveGame's saved lastPosition
        activeGame.setLastPosition(lastPosition);
        // [debug] javaPlugin.getLogger().info("moveItems.lastPosition saved");

        // Animate the moves and get the maximum number of steps (ticks) needed
        int maxSteps = animateMovementsSimultaneous(activeGame, activeGame.getGameWindow(), instructions);

        // Compute tick delay for final board update (ensure at least 1 tick delay)
        return Math.max((maxSteps - 1) * activeGame.getAnimationDelay(), 1);
    }


    private Direction toDirection(ButtonAction action) {
        return switch (action) {
            case UP -> Direction.UP;
            case DOWN -> Direction.DOWN;
            case LEFT -> Direction.LEFT;
            case RIGHT -> Direction.RIGHT;
            default -> throw new IllegalArgumentException("Not a board movement: " + action);
        };
    }

    private MovementInstruction toMovementInstruction(BoardEngine.Movement movement) {
        ItemStack item = NumberRepresentation.fromScore(movement.value()).orElseThrow().getDisplayableBlock();
        ItemStack merged = movement.merge()
            ? NumberRepresentation.fromScore(movement.mergedValue()).orElseThrow().getDisplayableBlock()
            : null;
        return new MovementInstruction(
            movement.fromRow(),
            movement.fromColumn(),
            movement.toRow(),
            movement.toColumn(),
            item,
            movement.merge(),
            merged,
            movement.step(),
            movement.totalSteps()
        );
    }

    /**
     * Animate all movement instructions in synchronized steps.
     * <p>
     * Each step is scheduled as one global task so that all first-step updates occur on the same tick, all second-step updates occur on the next tick, and so on.
     *
     * @return max steps for this movement
     */
    public int animateMovementsSimultaneous(ActiveGame activeGame, Inventory menu,
                                            List<MovementInstruction> instructions) {
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
            }.runTaskLater(javaPlugin, (currentStep - 1L) * activeGame.getAnimationDelay());
        }
        return maxSteps;
    }

    public SavedGame createSavedGame(ActiveGame activeGame) {
        ItemStack[][] logicalBoard = currentLogicalBoard(activeGame);
        return new SavedGame(
            activeGame.getScore(),
            activeGame.getMillisecondsSinceStart(),
            activeGame.getUndoLastMoveCounter(),
            activeGame.getScoreGainedAfterLastMove(),
            activeGame.isLastMoveUndo(),
            flattenBoard(logicalBoard),
            flattenBoard(activeGame.getLastPosition())
        );
    }

    public SavedGame createShutdownSavedGame(ActiveGame activeGame) {
        ItemStack[][] logicalBoard = currentLogicalBoard(activeGame);
        if (activeGame.getPendingBoard() != null) {
            BoardEngine.SpawnResult spawn = boardEngine.spawn(
                toValueBoard(logicalBoard),
                GENERATE_NEW_BLOCK_FOUR_PERCENT.getIntValue(),
                random
            );
            copyValueBoardToItems(spawn.board(), logicalBoard);
        }
        return new SavedGame(
            activeGame.getScore(),
            activeGame.getMillisecondsSinceStart(),
            activeGame.getUndoLastMoveCounter(),
            activeGame.getScoreGainedAfterLastMove(),
            activeGame.isLastMoveUndo(),
            flattenBoard(logicalBoard),
            flattenBoard(activeGame.getLastPosition())
        );
    }

    public boolean noValidMovesLeft(ActiveGame activeGame) {
        return !boardEngine.hasLegalMove(toValueBoard(currentLogicalBoard(activeGame)));
    }

    public boolean noValidMovesLeft(SavedGame savedGame) {
        int[][] board = new int[ROW_AND_COLUMN_SIZE][ROW_AND_COLUMN_SIZE];
        for (int index = 0; index < savedGame.board().size(); index++) {
            board[index / ROW_AND_COLUMN_SIZE][index % ROW_AND_COLUMN_SIZE] = savedGame.board().get(index);
        }
        return !boardEngine.hasLegalMove(board);
    }

    public void restoreSavedGame(ActiveGame activeGame, SavedGame savedGame) {
        ItemStack[][] restoredBoard = toItemArray(savedGame.board());
        copyItemArrayToGameWindow(activeGame.getGameWindow(), restoredBoard);
        activeGame.setLastPosition(savedGame.previousBoard().isEmpty() ? null : toItemArray(savedGame.previousBoard()));
        updateUndoButton(activeGame);
    }

    private List<Integer> flattenBoard(Inventory inventory) {
        ItemStack[][] board = new ItemStack[ROW_AND_COLUMN_SIZE][ROW_AND_COLUMN_SIZE];
        copyGameWindowContentsToArray(inventory, board);
        return flattenBoard(board);
    }

    private ItemStack[][] currentLogicalBoard(ActiveGame activeGame) {
        if (activeGame.getPendingBoard() != null) {
            return copyBoard(activeGame.getPendingBoard());
        }
        ItemStack[][] board = new ItemStack[ROW_AND_COLUMN_SIZE][ROW_AND_COLUMN_SIZE];
        copyGameWindowContentsToArray(activeGame.getGameWindow(), board);
        return board;
    }

    private ItemStack[][] copyBoard(ItemStack[][] source) {
        ItemStack[][] copy = new ItemStack[ROW_AND_COLUMN_SIZE][ROW_AND_COLUMN_SIZE];
        for (int row = 0; row < ROW_AND_COLUMN_SIZE; row++) {
            copy[row] = Arrays.copyOf(source[row], source[row].length);
        }
        return copy;
    }

    private List<Integer> flattenBoard(ItemStack[][] board) {
        if (board == null) {
            return List.of();
        }
        return Arrays.stream(board)
                     .flatMap(Arrays::stream)
                     .map(item -> item == null ? 0 : NumberRepresentation.getScoreFromItem(item))
                     .toList();
    }

    private ItemStack[][] toItemArray(List<Integer> values) {
        ItemStack[][] board = new ItemStack[ROW_AND_COLUMN_SIZE][ROW_AND_COLUMN_SIZE];
        for (int index = 0; index < values.size(); index++) {
            int value = values.get(index);
            board[index / ROW_AND_COLUMN_SIZE][index % ROW_AND_COLUMN_SIZE] = value == 0
                ? null
                : NumberRepresentation.fromScore(value).orElseThrow().getDisplayableBlock();
        }
        return board;
    }

    public boolean noValidMovesLeft(Inventory gameWindow) {
        ItemStack[][] itemsInGame = new ItemStack[ROW_AND_COLUMN_SIZE][ROW_AND_COLUMN_SIZE];
        copyGameWindowContentsToArray(gameWindow, itemsInGame);
        return !boardEngine.hasLegalMove(toValueBoard(itemsInGame));
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

        BoardEngine.UndoResult undo = boardEngine.undo(
            toValueBoard(activeGame.getLastPosition()),
            activeGame.getScore(),
            activeGame.getScoreGainedAfterLastMove()
        );
        copyValueBoardToItems(undo.board(), inventoryArray);
        activeGame.setScore(undo.score());
        activeGame.decrementUndoLastMoveCounter();

        if (activeGame.hasNoUndoLastMoveLeft()) {
            setItemInSlot(activeGame.getGameWindow(), UNDO_BUTTON_SLOT.getIntValue(), getEmptyUndoButton());
        } else {
            setItemInSlot(activeGame.getGameWindow(), UNDO_BUTTON_SLOT.getIntValue(), getBlockedUndoButton(activeGame.getUndoLastMoveCounter()));
        }

        activeGame.setLastMoveUndo(true);
        return -1;
    }

    public void spawnNewBlock(Inventory inventory) {
        ItemStack[][] itemsInGame = new ItemStack[ROW_AND_COLUMN_SIZE][ROW_AND_COLUMN_SIZE];
        copyGameWindowContentsToArray(inventory, itemsInGame);
        BoardEngine.SpawnResult spawn = boardEngine.spawn(
            toValueBoard(itemsInGame),
            GENERATE_NEW_BLOCK_FOUR_PERCENT.getIntValue(),
            random
        );
        if (!spawn.spawned()) {
            return;
        }
        BoardEngine.Cell cell = spawn.cell();
        ItemStack tile = NumberRepresentation.fromScore(spawn.value()).orElseThrow().getDisplayableBlock();
        inventory.setItem(mergeSlots[cell.row()][cell.column()], new ItemBuilder(tile).create());
    }

    private int[][] toValueBoard(ItemStack[][] itemBoard) {
        int[][] values = new int[ROW_AND_COLUMN_SIZE][ROW_AND_COLUMN_SIZE];
        for (int row = 0; row < ROW_AND_COLUMN_SIZE; row++) {
            for (int column = 0; column < ROW_AND_COLUMN_SIZE; column++) {
                ItemStack item = itemBoard[row][column];
                values[row][column] = item == null ? 0 : NumberRepresentation.getScoreFromItem(item);
            }
        }
        return values;
    }

    private void copyValueBoardToItems(int[][] valueBoard, ItemStack[][] itemBoard) {
        for (int row = 0; row < ROW_AND_COLUMN_SIZE; row++) {
            for (int column = 0; column < ROW_AND_COLUMN_SIZE; column++) {
                int value = valueBoard[row][column];
                itemBoard[row][column] = value == 0
                    ? null
                    : NumberRepresentation.fromScore(value).orElseThrow().getDisplayableBlock();
            }
        }
    }

    private ItemStack getHelpGUIPlayerStatsHead(Player player, PlayerData playerData) {
        return (new ItemBuilder(getPlayerSkullItem(player))).addLore(PLAYER_ITEM_LORE_GAMES_PLAYED.getFormattedValue(StringUtil.formatLong(playerData.attempts())))
                                                            .addLore(PLAYER_ITEM_LORE_TOTAL_PLAYTIME.getFormattedValue(makeSecondsATimestamp(playerData.playtime())))
                                                            .addLore(PLAYER_ITEM_LORE_HIGH_SCORE.getFormattedValue(StringUtil.formatLong(playerData.highScore())))
                                                            .addLore(PLAYER_ITEM_LORE_AVERAGE_SCORE.getFormattedValue(StringUtil.formatLong(Math.round(playerData.averageScore()))))
                                                            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                                                            .create();
    }
}