package fr.nemesis07.ankhiastaff.modernlib.menus.items;

import fr.nemesis07.ankhiastaff.modernlib.menus.Menu;
import fr.nemesis07.ankhiastaff.modernlib.utils.ChatColors;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuItem extends ItemStack {
    private Menu menu;
    private int slot;

    public MenuItem(Material material, int amount, short damage, String displayName, String... lore) {
        super(material, amount);
        ItemMeta meta = this.getItemMeta();
        if (meta != null) {
            if (displayName != null) {
                meta.setDisplayName(ChatColors.color(displayName));
            }
            if (lore != null && lore.length > 0) {
                meta.setLore(ChatColors.color(Arrays.asList(lore)));
            }
            this.setItemMeta(meta);
        }
        this.setDurability(damage);
    }

    public MenuItem(Material material, int amount, short damage, String displayName, List<String> lore) {
        this(material, amount, damage, displayName, lore.toArray(new String[0]));
    }

    public MenuItem(Material material) {
        this(material, 1, (short)0, null, new String[0]);
    }

    public MenuItem(Material material, int amount, short damage) {
        this(material, amount, damage, null, new String[0]);
    }

    public MenuItem(Material material, String displayName, String... lore) {
        this(material, 1, (short)0, displayName, lore);
    }

    public MenuItem(ItemStack stack) {
        this(stack.getType(), stack.getAmount(), stack.getDurability(), null, new String[0]);
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            this.setItemMeta(meta);
        }
    }

    public void onClick() {}

    public void onClick(int slot) {}

    public void setMenu(Menu menu, int slot) {
        this.menu = menu;
        this.slot = slot;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public int getSlot() {
        return this.slot;
    }

    public void setLore(String... lore) {
        ItemMeta meta = this.getItemMeta();
        if (meta != null) {
            meta.setLore(ChatColors.color(Arrays.asList(lore)));
            this.setItemMeta(meta);
        }
        this.update();
    }

    public void update() {
        if (this.menu != null) {
            this.menu.setItem(this.getSlot(), this);
        }
    }
}