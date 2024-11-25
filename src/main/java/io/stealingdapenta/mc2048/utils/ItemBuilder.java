package io.stealingdapenta.mc2048.utils;

import io.stealingdapenta.mc2048.config.ConfigKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Code based on original code from XTens Prison Core
 */
public class ItemBuilder {

    private final ItemStack itemStack;
    private final List<String> loreList;
    private ItemMeta itemMeta;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
        this.itemMeta = this.itemStack.getItemMeta();
        this.loreList = Objects.nonNull(itemMeta) && itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();
    }

    public ItemBuilder(Material material) {
        this(new ItemStack(material, 1));
    }

    public ItemBuilder setDisplayName(ConfigKey name) {
        return setDisplayName(name.getFormattedValue());
    }

    public ItemBuilder setDisplayName(Component name) {
        return setDisplayName(LegacyComponentSerializer.legacySection().serialize(name));
    }

    public ItemBuilder setDisplayName(String name) {
        setItemMeta();
        itemMeta.setDisplayName(name);
        return this;
    }

    private void setItemMeta() {
        itemMeta = (Objects.isNull(itemMeta)) ? itemStack.getItemMeta() : itemMeta;
    }

    public ItemBuilder addItemFlags(ItemFlag... itemFlags) {
        setItemMeta();
        itemMeta.addItemFlags(itemFlags);
        return this;
    }

    public ItemStack create() {
        setItemMeta();
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemBuilder addLore(Object... lore) {
        setItemMeta();
        Arrays.stream(lore).map(this::convertToString)  // Convert each input to String
              .forEach(loreList::add);     // Add to lore list
        itemMeta.setLore(loreList);
        return this;
    }

    private String convertToString(Object obj) {
        return switch (obj) {
            case ConfigKey configKey -> LegacyComponentSerializer.legacySection().serialize(configKey.getFormattedValue());
            case Component component -> LegacyComponentSerializer.legacySection().serialize(component);
            case String str -> str;
            default -> throw new IllegalArgumentException("Unsupported lore type: " + obj.getClass());
        };
    }
}