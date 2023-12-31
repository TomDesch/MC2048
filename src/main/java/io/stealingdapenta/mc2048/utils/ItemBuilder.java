package io.stealingdapenta.mc2048.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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

    public ItemBuilder setDisplayName(String name) {
        setItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        return this;
    }

    public ItemBuilder modifyMeta(Consumer<ItemMeta> consumer) {
        consumer.accept(itemMeta);
        return this;
    }

    public ItemBuilder addLore(String... lore) {
        setItemMeta();
        List<String> temp = Arrays.asList(lore);
        temp.forEach(s -> loreList.add(ChatColor.translateAlternateColorCodes('&', s)));
        itemMeta.setLore(loreList);
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

    public ItemBuilder setEnchantments(Map<Enchantment, Integer> enchants) {
        enchants.forEach(itemStack::addUnsafeEnchantment);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, Integer level) {
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder setGlowing(boolean glow) {
        if (!glow) {
            return this;
        }
        setItemMeta();
        itemMeta.addEnchant(Enchantment.LURE, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder setGlowing() {
        setGlowing(true);
        return this;
    }

    public ItemStack create() {
        setItemMeta();
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}