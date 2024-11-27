package fr.nemesis07.ankhiastaff.menus.items;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.menus.ExaminePlayerMenu;
import fr.nemesis07.ankhiastaff.modernlib.menus.items.MenuItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class FoodItem extends MenuItem {
    private Player player;
    private Player opener;

    public FoodItem(Player player, Player opener) {
        super(Material.BREAD, 1, (short)0, AnkhiaStaff.getInstance().getMsg().getText("menus.food.title"), AnkhiaStaff.getInstance().getMsg().getTextList("menus.food.lore", new String[]{"{health}", String.valueOf((int)player.getHealth()), "{maxHealth}", String.valueOf((int)player.getMaxHealth()), "{food}", String.valueOf(player.getFoodLevel())}));
        this.player = player;
        this.opener = opener;
    }

    public void onClick() {
        if (this.player != null && this.player.isOnline() && this.opener != null && this.opener.isOnline()) {
            if (this.opener.hasPermission("staffmodex.heal")) {
                boolean wasHealedOrFed = this.player.getHealth() < this.player.getMaxHealth() || this.player.getFoodLevel() < 20;
                if (wasHealedOrFed) {
                    this.player.setHealth(this.player.getMaxHealth());
                    this.player.setFoodLevel(20);
                    (new ExaminePlayerMenu(this.opener, this.player)).openInventory(this.opener);
                }

                this.player.sendMessage(AnkhiaStaff.getInstance().getMessage("menus.food.healed", new String[0]));
            } else {
                this.player.sendMessage(AnkhiaStaff.getInstance().getMessage("menus.food.no-permission-heal", new String[0]));
            }
        }

    }
}
