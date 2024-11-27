package fr.nemesis07.ankhiastaff.hotbar.components.items;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.hotbar.HotbarItem;
import fr.nemesis07.ankhiastaff.modernlib.menus.Menu;
import fr.nemesis07.ankhiastaff.modernlib.menus.items.MenuItem;
import fr.nemesis07.ankhiastaff.modernlib.utils.Materials;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlayersHotbarItem extends HotbarItem {
    public PlayersHotbarItem() {
        super(Materials.get(AnkhiaStaff.getInstance().getCfg().getStringList("items.hotbar.players.material")), AnkhiaStaff.getInstance().getMsg().getText("hotbar.players.name"), 1, (short)0, AnkhiaStaff.getInstance().getMsg().getTextList("hotbar.players.lore"));
    }

    public void onInteract(final Player player) {
        Menu menu = new Menu(AnkhiaStaff.getInstance().getMsg().getText("hotbar.players.menu_title"), 4);
        int i = 0;
        Iterator var4 = Bukkit.getOnlinePlayers().iterator();

        while(var4.hasNext()) {
            final Player otherPlayer = (Player)var4.next();
            if (i > 26) {
                break;
            }

            if (player != otherPlayer) {
                menu.setItem(i++, new PlayerItem(player, otherPlayer) {
                    public void onClick() {
                        player.closeInventory();
                        player.teleport(otherPlayer.getLocation());
                        player.sendMessage(AnkhiaStaff.getInstance().getMsg().getText("hotbar.players.teleport").replace("{player}", otherPlayer.getName()));
                    }
                });
            }
        }

        menu.setItem(menu.getSize() - 9, new MenuItem(Material.ARROW, AnkhiaStaff.getInstance().getMsg().getText("hotbar.players.close"), new String[0]) {
            public void onClick() {
                player.closeInventory();
            }
        });
        menu.setBackground(Materials.get(AnkhiaStaff.getInstance().getCfg().getStringList("items.fill.material")), (short)AnkhiaStaff.getInstance().getCfg().getInt("items.fill.damage"), AnkhiaStaff.getInstance().getCfg().getText("items.fill.name"), new String[0]);
        menu.openInventory(player);
    }
}