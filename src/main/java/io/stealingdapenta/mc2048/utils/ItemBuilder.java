package io.stealingdapenta.mc2048.utils;

import io.stealingdapenta.mc2048.config.ConfigKey;
import java.util.ArrayList;
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
    public static final String ERROR_SETTING_CMD = "Error setting custom model data for %s";

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
        this.itemMeta = this.itemStack.getItemMeta();
        this.loreList = (Objects.nonNull(itemMeta) && Objects.nonNull(itemMeta.getLore())) ? itemMeta.getLore() : new ArrayList<>();
    }

    public ItemBuilder(Material material) {
        this(new ItemStack(material, 1));
    }

    public ItemBuilder setDisplayName(ConfigKey name) {
        return setDisplayName(name.getFormattedValue());
    }

    public ItemBuilder setDisplayName(Component name) {
        return setDisplayName(LegacyComponentSerializer.legacySection()
                                                       .serialize(name));
    }

    public ItemBuilder setDisplayName(String name) {
        setItemMeta();
        itemMeta.setDisplayName(StringUtil.translate(name));
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

    public ItemBuilder addLoreList(List<Component> lore) {
        setItemMeta();
        List<String> currentLore = (Objects.nonNull(itemMeta.getLore())) ? itemMeta.getLore() : new ArrayList<>();

        lore.stream()
            .map(this::convertToString)
            .filter(Objects::nonNull)
            .forEach(currentLore::add);

        itemMeta.setLore(currentLore);
        return this;
    }

    public ItemBuilder addLore(Object lore) {
        setItemMeta();
        String loreString = convertToString(lore);
        if (Objects.isNull(loreString)) {
            return this;
        }

        loreList.add(loreString);
        itemMeta.setLore(loreList);
        return this;
    }

    private String convertToString(Object obj) {
        return switch (obj) {
            case ConfigKey configKey -> {
                Component formatted = configKey.getFormattedValue();
                String legacy = LegacyComponentSerializer.legacySection()
                                                         .serialize(formatted);
                if (legacy.isEmpty()) {
                    yield null;
                }
                yield LegacyComponentSerializer.legacySection()
                                               .serialize(StringUtil.processComponent(formatted));
            }
            case Component component -> {
                String legacy = LegacyComponentSerializer.legacySection()
                                                         .serialize(component);
                if (legacy.isEmpty()) {
                    yield null;
                }
                yield LegacyComponentSerializer.legacySection()
                                               .serialize(StringUtil.processComponent(component));
            }
            case String str -> {
                if (str.isEmpty()) {
                    yield null;
                }
                yield StringUtil.translate(str);
            }
            default -> throw new IllegalArgumentException("Unsupported lore type: " + obj.getClass());
        };
    }

    public static ItemStack setCustomModelDataTo(ItemStack itemStack, ConfigKey configKey) {
        return setCustomModelDataTo(itemStack, configKey.getIntValue());
    }

    public static ItemStack setCustomModelDataTo(ItemStack itemStack, NumberRepresentation numberRepresentation) {
        return setCustomModelDataTo(itemStack, numberRepresentation.getCustomModelData()
                                                                   .get());
    }

    public static ItemStack setCustomModelDataTo(ItemStack itemStack, Integer amount) {
        Objects.requireNonNull(itemStack.getItemMeta(), ERROR_SETTING_CMD.formatted(itemStack))
               .setCustomModelData(amount);
        return itemStack;
    }
}