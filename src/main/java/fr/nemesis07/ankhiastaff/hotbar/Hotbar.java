package fr.nemesis07.ankhiastaff.hotbar;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class Hotbar {
    private Map<Integer, HotbarItem> items = new HashMap<>();

    public void setItem(int slot, HotbarItem item) {
        items.put(slot, item);
    }

    public HotbarItem getItem(int slot) {
        return items.get(slot);
    }

    public void give(Player player) {
        PlayerInventory inventory = player.getInventory();
        for (Entry<Integer, HotbarItem> entry : items.entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue().getItem());
        }
    }

    public Collection<Integer> getSlots() {
        return items.keySet();
    }
}