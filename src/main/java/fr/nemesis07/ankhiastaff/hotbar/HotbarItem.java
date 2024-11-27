package fr.nemesis07.ankhiastaff.hotbar;

import fr.nemesis07.ankhiastaff.modernlib.utils.ChatColors;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HotbarItem {
    private ItemStack item = null;

    public ItemStack getItem() {
        return this.item;
    }

    public void onInteract(Player player) {
    }

    public void onInteract(Player player, Entity target) {
    }

    public void onInteract(Player player, Block clickedBlock) {
    }

    public HotbarItem(Material material, String name, int amount, short damage, List<String> lore) {
        this.item = new ItemStack(material, amount, damage);
        ItemMeta meta = this.item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColors.color(name));
            meta.setLore(ChatColors.color(lore));
            this.item.setItemMeta(meta);
        }

    }
}