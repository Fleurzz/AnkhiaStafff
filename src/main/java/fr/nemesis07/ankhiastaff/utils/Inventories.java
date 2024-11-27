package fr.nemesis07.ankhiastaff.utils;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class Inventories {
    public static Inventory copyInventory(Inventory source) {
        if (source.getType() == InventoryType.CHEST) {
            int originalSize = source.getSize();
            if (originalSize % 9 != 0) {
                return null;
            } else {
                Inventory copiedInventory = Bukkit.createInventory((InventoryHolder)null, originalSize, "Contents");
                if (originalSize != copiedInventory.getSize()) {
                    return copiedInventory;
                } else {
                    for(int i = 0; i < source.getSize(); ++i) {
                        ItemStack item = source.getItem(i);
                        if (item != null) {
                            copiedInventory.setItem(i, item.clone());
                        }
                    }

                    return copiedInventory;
                }
            }
        } else {
            return null;
        }
    }
}