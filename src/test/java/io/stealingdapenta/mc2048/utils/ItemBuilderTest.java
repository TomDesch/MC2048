package io.stealingdapenta.mc2048.utils;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.junit.jupiter.api.Test;

class ItemBuilderTest {

    @Test
    void setCustomModelDataToWithAmountUsesEquivalentFloatComponent() {
        ItemStack itemStack = mock(ItemStack.class);
        ItemMeta itemMeta = mock(ItemMeta.class);
        CustomModelDataComponent customModelData = mock(CustomModelDataComponent.class);
        when(itemStack.getItemMeta()).thenReturn(itemMeta);
        when(itemMeta.getCustomModelDataComponent()).thenReturn(customModelData);

        ItemStack result = ItemBuilder.setCustomModelDataTo(itemStack, 2048);

        assertSame(itemStack, result);
        verify(customModelData).setFloats(List.of(2048.0F));
        verify(itemMeta).setCustomModelDataComponent(customModelData);
        verify(itemStack).setItemMeta(itemMeta);
    }

    @Test
    void setCustomModelDataToWithNullClearsComponent() {
        ItemStack itemStack = mock(ItemStack.class);
        ItemMeta itemMeta = mock(ItemMeta.class);
        when(itemStack.getItemMeta()).thenReturn(itemMeta);

        ItemStack result = ItemBuilder.setCustomModelDataTo(itemStack, (Integer) null);

        assertSame(itemStack, result);
        verify(itemMeta).setCustomModelDataComponent(null);
        verify(itemStack).setItemMeta(itemMeta);
    }
}
