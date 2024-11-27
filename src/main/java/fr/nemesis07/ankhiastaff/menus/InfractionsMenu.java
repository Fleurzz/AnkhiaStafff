package fr.nemesis07.ankhiastaff.menus;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.infractions.InfractionType;
import fr.nemesis07.ankhiastaff.modernlib.menus.Menu;
import fr.nemesis07.ankhiastaff.modernlib.menus.items.MenuItem;
import fr.nemesis07.ankhiastaff.modernlib.utils.Materials;
import fr.nemesis07.ankhiastaff.player.StaffPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InfractionsMenu extends Menu {
    public InfractionsMenu(final Menu oldMenu, final Player player, final StaffPlayer staffPlayer) {
        super(AnkhiaStaff.getInstance().getMsg().getText("menus.infractions.title"), 3);
        this.setItem(0, new MenuItem(Material.REDSTONE, AnkhiaStaff.getInstance().getMsg().getText("menus.infractions.warnings.title"), new String[]{AnkhiaStaff.getInstance().getMsg().getText("menus.infractions.warnings.description")}) {
            public void onClick() {
                player.sendMessage(AnkhiaStaff.getInstance().getMessage("menus.infractions.warnings.opening", new String[0]));
                (new InfractionsViewerMenu(InfractionType.WARNING, player, staffPlayer, InfractionsMenu.this)).openInventory(player);
            }
        });
        this.setItem(1, new MenuItem(Material.BOOK, AnkhiaStaff.getInstance().getMsg().getText("menus.infractions.reports.title"), new String[]{AnkhiaStaff.getInstance().getMsg().getText("menus.infractions.reports.description")}) {
            public void onClick() {
                player.sendMessage(AnkhiaStaff.getInstance().getMessage("menus.infractions.reports.opening", new String[0]));
                (new InfractionsViewerMenu(InfractionType.REPORT, player, staffPlayer, InfractionsMenu.this)).openInventory(player);
            }
        });
        if (oldMenu != null) {
            this.setItem(this.getSize() - 1, new MenuItem(Material.ARROW, AnkhiaStaff.getInstance().getMsg().getText("menus.infractions.back.title"), new String[]{AnkhiaStaff.getInstance().getMsg().getText("menus.infractions.back.description")}) {
                public void onClick() {
                    oldMenu.openInventory(player);
                }
            });
        }

        this.setBackground(Materials.get(AnkhiaStaff.getInstance().getCfg().getStringList("items.fill.material")), (short)AnkhiaStaff.getInstance().getCfg().getInt("items.fill.damage"), AnkhiaStaff.getInstance().getCfg().getText("items.fill.name"), new String[0]);
    }

    public void openInventory(Player player) {
        if (!player.hasPermission("staffmodex.infractions")) {
            player.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.infractions.no-permission", new String[0]));
        } else {
            super.openInventory(player);
        }
    }
}
