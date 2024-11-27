package fr.nemesis07.ankhiastaff.menus;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.menus.items.ConnectionItem;
import fr.nemesis07.ankhiastaff.menus.items.FoodItem;
import fr.nemesis07.ankhiastaff.menus.items.FreezePlayerItem;
import fr.nemesis07.ankhiastaff.menus.items.GameModeItem;
import fr.nemesis07.ankhiastaff.menus.items.InfractionItem;
import fr.nemesis07.ankhiastaff.menus.items.LocationItem;
import fr.nemesis07.ankhiastaff.menus.items.PlayerInventoryItem;
import fr.nemesis07.ankhiastaff.menus.items.PlayerNotesItem;
import fr.nemesis07.ankhiastaff.menus.items.WarnPlayerItem;
import fr.nemesis07.ankhiastaff.modernlib.menus.Menu;
import fr.nemesis07.ankhiastaff.modernlib.utils.Materials;
import org.bukkit.entity.Player;

public class ExaminePlayerMenu extends Menu {
    public ExaminePlayerMenu(Player player, Player target) {
        super("&6Examining " + target.getName(), 3);
        InfractionItem infractionItem = new InfractionItem(player, target);
        this.setItem(9, new FoodItem(target, player));
        this.setItem(10, new ConnectionItem(target, player));
        this.setItem(11, new GameModeItem(target));
        if (AnkhiaStaff.getInstance().getCfg().getBoolean("warning.enabled") || AnkhiaStaff.getInstance().getCfg().getBoolean("report.enabled")) {
            this.setItem(12, infractionItem);
        }

        this.setItem(13, new LocationItem(target.getLocation(), target, player));
        this.setItem(14, new PlayerNotesItem(player, target));
        if (AnkhiaStaff.getInstance().getCfg().getBoolean("freeze.enabled")) {
            this.setItem(15, new FreezePlayerItem(player, target));
        }

        if (AnkhiaStaff.getInstance().getCfg().getBoolean("warning.enabled")) {
            this.setItem(16, new WarnPlayerItem(infractionItem, player, target));
        }

        this.setItem(17, new PlayerInventoryItem(player, target, this));
        this.setBackground(Materials.get(AnkhiaStaff.getInstance().getCfg().getStringList("items.fill.material")), (short)AnkhiaStaff.getInstance().getCfg().getInt("items.fill.damage"), AnkhiaStaff.getInstance().getCfg().getText("items.fill.name"), new String[0]);
    }

    public void openInventory(Player player) {
        if (!player.hasPermission("staffmodex.examine")) {
            player.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.examine.no-permission", new String[0]));
        } else {
            super.openInventory(player);
        }
    }
}