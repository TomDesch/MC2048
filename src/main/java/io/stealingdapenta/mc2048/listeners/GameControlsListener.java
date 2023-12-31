package io.stealingdapenta.mc2048.listeners;

import io.stealingdapenta.mc2048.GameManager;
import io.stealingdapenta.mc2048.utils.InventoryUtil;
import java.util.Objects;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GameControlsListener implements Listener {

    private final InventoryUtil inventoryUtil;
    private final GameManager gameManager;

    public GameControlsListener(InventoryUtil inventoryUtil, GameManager gameManager) {
        this.inventoryUtil = inventoryUtil;
        this.gameManager = gameManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onButtonClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();

        InventoryView clickedInventoryView = event.getView();
        if (!inventoryUtil.isGameWindow(clickedInventoryView)) {
            return;
        }

        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (Objects.isNull(clickedItem)) {
            return;
        }
        ItemMeta itemMeta = clickedItem.getItemMeta();
        if (Objects.isNull(itemMeta)) {
            return;
        }

        String pressed = "You clicked on %s.";

        String displayName = itemMeta.getDisplayName();
        if (displayName.contains("UP")) {
            pressed = pressed.formatted("UP");
        } else if (displayName.contains("LEFT")) {
            pressed = pressed.formatted("LEFT");
        } else if (displayName.contains("RIGHT")) {
            pressed = pressed.formatted("RIGHT");
        } else if (displayName.contains("DOWN")) {
            pressed = pressed.formatted("DOWN");
        } else {
            return;
        }
        event.getWhoClicked()
             .sendMessage(pressed);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGameClose(InventoryCloseEvent event) {
        InventoryView clickedInventoryView = event.getView();
        if (!inventoryUtil.isGameWindow(clickedInventoryView)) {
            return;
        }

        event.getPlayer()
             .sendMessage("game window closed!!!");
        gameManager.deactivateGameFor((Player) event.getPlayer());
    }
}
