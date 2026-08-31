package io.stealingdapenta.mc2048.listeners;

import io.stealingdapenta.mc2048.GameManager;
import io.stealingdapenta.mc2048.MC2048;
import io.stealingdapenta.mc2048.config.ConfigKey;
import io.stealingdapenta.mc2048.utils.InventoryUtil;
import io.stealingdapenta.mc2048.utils.data.ActiveGame;
import io.stealingdapenta.mc2048.utils.data.ButtonAction;
import io.stealingdapenta.mc2048.utils.data.GameHolder;
import io.stealingdapenta.mc2048.utils.data.HelperHolder;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for the slot-based GUI click mapping.
 *
 * The bug report: translating button titles (config) broke move buttons after reload.
 * Root cause was name-based action identification.
 *
 * These tests ensure we never depend on translated item display names and instead use stable slots.
 */
class GameControlsListenerTest {

    private InventoryUtil inventoryUtil;
    private GameManager gameManager;
    private GameControlsListener listener;

    private Player player;
    private InventoryView view;
    private Inventory topInventory;
    private Inventory clickedInventory;

    private ActiveGame activeGame;
    private MockedStatic<MC2048> mc2048;

    @BeforeEach
    void setUp() {
        MC2048 plugin = mock(MC2048.class);
        FileConfiguration configuration = mock(FileConfiguration.class);
        mc2048 = mockStatic(MC2048.class);
        mc2048.when(MC2048::getInstance).thenReturn(plugin);
        when(plugin.getConfig()).thenReturn(configuration);
        when(configuration.getString(anyString())).thenAnswer(invocation -> {
            String key = invocation.getArgument(0, String.class);
            return key.equals(ConfigKey.MSG_INVALID_MOVE.name().toLowerCase())
                || key.equals(ConfigKey.MSG_GAME_PAUSED.name().toLowerCase())
                ? ""
                : ConfigKey.valueOf(key.toUpperCase()).getDefaultValue();
        });

        inventoryUtil = mock(InventoryUtil.class);
        gameManager = mock(GameManager.class);
        listener = new GameControlsListener(inventoryUtil, gameManager);

        player = mock(Player.class);
        view = mock(InventoryView.class);
        topInventory = mock(Inventory.class);
        clickedInventory = topInventory;

        when(view.getTopInventory()).thenReturn(topInventory);

        activeGame = mock(ActiveGame.class);
        when(activeGame.getGameWindow()).thenReturn(mock(Inventory.class));
        when(activeGame.isLocked()).thenReturn(false);
        when(gameManager.getActiveGame(player)).thenReturn(activeGame);

        // default: treat as game window
        when(inventoryUtil.isAnyGameWindow(view)).thenReturn(true);
        when(inventoryUtil.isGameWindow(view)).thenReturn(true);
        when(inventoryUtil.isHelpWindow(view)).thenReturn(false);
    }

    @AfterEach
    void tearDown() {
        mc2048.close();
    }

    private InventoryClickEvent clickEvent(int slot, Inventory clickedInventory) {
        InventoryClickEvent event = mock(InventoryClickEvent.class);
        when(event.getView()).thenReturn(view);
        when(event.getWhoClicked()).thenReturn(player);
        when(event.getSlot()).thenReturn(slot);
        when(event.getClickedInventory()).thenReturn(clickedInventory);
        return event;
    }

    @Test
    @DisplayName("Cancels clicks inside MC2048 windows")
    void cancelsClicksForPluginWindows() {
        InventoryClickEvent event = clickEvent(10, clickedInventory);

        listener.onButtonClick(event);

        verify(event).setCancelled(true);
    }

    @Test
    @DisplayName("Ignores clicks in the player's inventory (bottom) even if MC2048 GUI is open")
    void ignoresBottomInventoryClicks() {
        Inventory bottom = mock(Inventory.class);
        InventoryClickEvent event = clickEvent(16, bottom);

        listener.onButtonClick(event);

        // Event was still cancelled because any click in plugin window gets cancelled
        verify(event).setCancelled(true);
        // But it shouldn't attempt to use the game manager at all
        verify(gameManager, never()).getActiveGame(any());
        verifyNoInteractions(gameManager);
    }

    @Test
    @DisplayName("Maps the UP move by slot and calls processGameAction regardless of translated item names")
    void mapsUpMoveBySlot() {
        // slot 16 is default for UP in the shipped config (and common user configs)
        // We explicitly don't provide an ItemStack/display-name: mapping must not depend on it.
        InventoryClickEvent event = clickEvent(16, clickedInventory);

        listener.onButtonClick(event);

        verify(inventoryUtil).processGameAction(eq(activeGame), eq(ButtonAction.UP));
    }

    @Test
    @DisplayName("Maps the DOWN move by slot")
    void mapsDownMoveBySlot() {
        InventoryClickEvent event = clickEvent(34, clickedInventory);

        listener.onButtonClick(event);

        verify(inventoryUtil).processGameAction(eq(activeGame), eq(ButtonAction.DOWN));
    }

    @Test
    @DisplayName("Maps the LEFT move by slot")
    void mapsLeftMoveBySlot() {
        InventoryClickEvent event = clickEvent(24, clickedInventory);

        listener.onButtonClick(event);

        verify(inventoryUtil).processGameAction(eq(activeGame), eq(ButtonAction.LEFT));
    }

    @Test
    @DisplayName("Maps the RIGHT move by slot")
    void mapsRightMoveBySlot() {
        InventoryClickEvent event = clickEvent(26, clickedInventory);

        listener.onButtonClick(event);

        verify(inventoryUtil).processGameAction(eq(activeGame), eq(ButtonAction.RIGHT));
    }

    @Test
    @DisplayName("Clicking a non-action slot triggers invalid-move handling (processGameAction not called)")
    void nonActionSlotDoesNotMove() {
        InventoryClickEvent event = clickEvent(0, clickedInventory);

        listener.onButtonClick(event);

        verify(inventoryUtil, never()).processGameAction(any(), any());
    }

    @Test
    @DisplayName("Help menu START button is mapped by slot and activates a new game")
    void helpMenuStartBySlot() {
        when(inventoryUtil.isGameWindow(view)).thenReturn(false);
        when(inventoryUtil.isHelpWindow(view)).thenReturn(true);

        InventoryClickEvent event = clickEvent(49, clickedInventory);

        listener.onButtonClick(event);

        verify(gameManager).activateGame(player);
    }

    @Test
    @DisplayName("Non-START clicks in help menu do nothing")
    void helpMenuOtherSlotsDoNothing() {
        when(inventoryUtil.isGameWindow(view)).thenReturn(false);
        when(inventoryUtil.isHelpWindow(view)).thenReturn(true);

        InventoryClickEvent event = clickEvent(48, clickedInventory);

        listener.onButtonClick(event);

        verify(gameManager, never()).activateGame(any());
    }

    @Test
    @DisplayName("When active game is missing or locked, clicks are ignored")
    void gameMissingOrLockedIsIgnored() {
        when(gameManager.getActiveGame(player)).thenReturn(null);
        InventoryClickEvent event = clickEvent(16, clickedInventory);

        listener.onButtonClick(event);
        verify(inventoryUtil, never()).processGameAction(any(), any());

        when(gameManager.getActiveGame(player)).thenReturn(activeGame);
        when(activeGame.isLocked()).thenReturn(true);

        listener.onButtonClick(event);
        verify(inventoryUtil, never()).processGameAction(any(), any());
    }

    @Test
    @DisplayName("Defers closing a locked game until its animation finishes")
    void defersLockedGameClose() {
        when(inventoryUtil.isGameWindow(view)).thenReturn(true);
        when(activeGame.isLocked()).thenReturn(true);
        InventoryCloseEvent event = mock(InventoryCloseEvent.class);
        when(event.getView()).thenReturn(view);
        when(event.getPlayer()).thenReturn(player);

        listener.onGameClose(event);

        verify(activeGame).requestClose();
        verify(gameManager, never()).deactivateGameFor(player);
    }

    @Test
    @DisplayName("Closing an unlocked game pauses it instead of completing it")
    void pausesUnlockedGameClose() {
        InventoryCloseEvent event = mock(InventoryCloseEvent.class);
        when(event.getView()).thenReturn(view);
        when(event.getPlayer()).thenReturn(player);

        listener.onGameClose(event);

        verify(gameManager).pauseGame(activeGame);
        verify(gameManager, never()).completeGame(any());
    }

    @Test
    @DisplayName("Opening reset confirmation does not pause the game")
    void resetTransitionDoesNotPauseGame() {
        when(activeGame.isResetConfirmationOpen()).thenReturn(true);
        InventoryCloseEvent event = mock(InventoryCloseEvent.class);
        when(event.getView()).thenReturn(view);
        when(event.getPlayer()).thenReturn(player);

        listener.onGameClose(event);

        verify(gameManager, never()).pauseGame(any());
    }

    @Test
    @DisplayName("Reset button opens confirmation without abandoning the game")
    void resetButtonOpensConfirmation() {
        Inventory confirmation = mock(Inventory.class);
        when(inventoryUtil.createResetConfirmationInventory(player)).thenReturn(confirmation);
        InventoryClickEvent event = clickEvent(43, clickedInventory);

        listener.onButtonClick(event);

        verify(activeGame).setResetConfirmationOpen(true);
        verify(player).openInventory(confirmation);
        verify(gameManager, never()).resetGame(any());
    }

    @Test
    @DisplayName("Confirming reset abandons the old game and starts a new one")
    void confirmingResetStartsNewGame() {
        when(inventoryUtil.isGameWindow(view)).thenReturn(false);
        when(inventoryUtil.isResetConfirmationWindow(view)).thenReturn(true);
        InventoryClickEvent event = clickEvent(InventoryUtil.RESET_CONFIRM_SLOT, clickedInventory);

        listener.onButtonClick(event);

        verify(activeGame).setResetConfirmationOpen(false);
        verify(gameManager).resetGame(activeGame);
        verify(gameManager).activateGame(player);
    }

    @Test
    @DisplayName("Cancelling reset returns to the unfinished game")
    void cancellingResetReturnsToGame() {
        when(inventoryUtil.isGameWindow(view)).thenReturn(false);
        when(inventoryUtil.isResetConfirmationWindow(view)).thenReturn(true);
        Inventory gameInventory = activeGame.getGameWindow();
        InventoryClickEvent event = clickEvent(InventoryUtil.RESET_CANCEL_SLOT, clickedInventory);

        listener.onButtonClick(event);

        verify(gameManager, never()).resetGame(any());
        verify(player).openInventory(gameInventory);
    }

    @Test
    @DisplayName("Closing reset confirmation pauses the unfinished game")
    void closingResetConfirmationPausesGame() {
        when(inventoryUtil.isGameWindow(view)).thenReturn(false);
        when(inventoryUtil.isResetConfirmationWindow(view)).thenReturn(true);
        when(activeGame.isResetConfirmationOpen()).thenReturn(true);
        InventoryCloseEvent event = mock(InventoryCloseEvent.class);
        when(event.getView()).thenReturn(view);
        when(event.getPlayer()).thenReturn(player);

        listener.onGameClose(event);

        verify(activeGame).setResetConfirmationOpen(false);
        verify(gameManager).pauseGame(activeGame);
    }

    @Test
    @DisplayName("Does nothing for non-plugin inventories")
    void ignoresNonPluginInventories() {
        when(inventoryUtil.isAnyGameWindow(view)).thenReturn(false);
        when(inventoryUtil.isHelpWindow(view)).thenReturn(false);

        InventoryClickEvent event = clickEvent(16, clickedInventory);
        listener.onButtonClick(event);

        verify(event, never()).setCancelled(true);
        verifyNoInteractions(gameManager);
    }
}
