package me.youhavetrouble.customcontentmanager;

import me.youhavetrouble.customcontentmanager.exception.InvalidCustomItemException;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class CustomContentManager extends JavaPlugin {
    private static CustomContentManager plugin;
    public static NamespacedKey customItemTag = null;
    private static final HashMap<String, CustomItem> registeredItems = new HashMap<>();

    @Override
    public void onEnable() {
        customItemTag = new NamespacedKey(this, "customItem");
        plugin = this;
    }

    public static CustomContentManager getPlugin() {
        return plugin;
    }

    protected static void registerCustomItem(CustomItem customItem) throws InvalidCustomItemException {
        if (customItemTag == null) throw new RuntimeException("CCM not initialized");
        if (customItem == null)
            throw new InvalidCustomItemException("CustomItem cannot be null");
        if (customItem.getId() == null)
            throw new InvalidCustomItemException("Item id cannot be null");
        if (registeredItems.containsKey(customItem.getId()))
            throw new InvalidCustomItemException("Id already taken");
        if (customItem.createItemStack() == null)
            throw new InvalidCustomItemException("createItemStack() result cannot be null");
        if (!customItem.createItemStack().hasItemMeta())
            throw new InvalidCustomItemException("createItemStack() result needs to have its id in persistent data container");
        PersistentDataContainer pdc = customItem.createItemStack().getItemMeta().getPersistentDataContainer();
        if (!pdc.has(customItemTag, PersistentDataType.STRING))
            throw new InvalidCustomItemException("createItemStack() result needs to have its id in persistent data container");
        if (!customItem.getId().equals(pdc.get(customItemTag, PersistentDataType.STRING)))
            throw new InvalidCustomItemException("Id in persistent data container needs to be the same as the one in getId()");

        registeredItems.put(customItem.getId(), customItem);

        plugin.getServer().getPluginManager().registerEvents(customItem, plugin);
    }

    protected static boolean isCustomItem(ItemStack itemStack) {
        if (customItemTag == null) throw new RuntimeException("CCM not initialized");
        if (itemStack == null) return false;
        if (!itemStack.hasItemMeta()) return false;
        if (!itemStack.getItemMeta().getPersistentDataContainer().has(customItemTag, PersistentDataType.STRING)) return false;
        String id = itemStack.getItemMeta().getPersistentDataContainer().get(customItemTag, PersistentDataType.STRING);
        return registeredItems.containsKey(id);
    }

    protected static CustomItem getCustomItem(String id) {
        if (customItemTag == null) throw new RuntimeException("CCM not initialized");
        return registeredItems.get(id);
    }

    protected static String getCustomItemIdFromItemStack(ItemStack itemStack) {
        if (customItemTag == null) throw new RuntimeException("CCM not initialized");
        if (itemStack == null) return null;
        if (!itemStack.hasItemMeta()) return null;
        if (!itemStack.getItemMeta().getPersistentDataContainer().has(customItemTag, PersistentDataType.STRING)) return null;
        return itemStack.getItemMeta().getPersistentDataContainer().get(customItemTag, PersistentDataType.STRING);
    }

}
