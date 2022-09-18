package me.youhavetrouble.customcontentmanager;

import me.youhavetrouble.customcontentmanager.exception.InvalidCustomItemException;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public interface CustomItem extends Listener {
    Material getBaseMaterial();

    int getCustomModelData();

    String getId();

    /**
     * Creates ItemStack of custom item. ItemStack returned has to have String custom item tag with item id
     * in its PersistentDataContainer.
     */
    default ItemStack createItemStack() {
        ItemStack item = new ItemStack(getBaseMaterial(), 1);
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(CustomContentManager.customItemTag, PersistentDataType.STRING, getId());
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Returns true if the item is currently registered custom item.
     */
    static boolean isCustomItem(ItemStack itemStack) {
        return CustomContentManager.isCustomItem(itemStack);
    }

    /**
     * Returns id of custom item, null if id doesn't exist or the item is not currently registered.
     */
    static String getCustomItemIdFromItemStack(ItemStack itemStack) {
        return CustomContentManager.getCustomItemIdFromItemStack(itemStack);
    }

    /**
     * Gets custom item from id.
     */
    static CustomItem getCustomItem(String id) {
        return CustomContentManager.getCustomItem(id);
    }

    /**
     * Registers custom item. Requirements below need to be met, otherwise InvalidCustomItemException will be thrown.
     * <li>argument cannot be null</li>
     * <li>id has to be unique and cannot already be registered</li>
     * <li>createItemStack() method has to return an item with PDC tag with id of the item</li>
     */
    static void registerCustomItem(CustomItem customItem) throws InvalidCustomItemException {
        CustomContentManager.registerCustomItem(customItem);
    }

}
