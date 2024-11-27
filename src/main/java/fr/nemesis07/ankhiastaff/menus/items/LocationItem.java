package fr.nemesis07.ankhiastaff.menus.items;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.modernlib.menus.items.MenuItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class LocationItem extends MenuItem {
    private Player target;
    private Player opener;

    public LocationItem(Location location, Player target, Player opener) {
        super(Material.MAP, 1, (short)0, AnkhiaStaff.getInstance().getMsg().getText("menus.location.title"), AnkhiaStaff.getInstance().getMsg().getTextList("menus.location.lore", new String[]{"{world}", location.getWorld().getName(), "{x}", String.valueOf(location.getBlockX()), "{y}", String.valueOf(location.getBlockY()), "{z}", String.valueOf(location.getBlockZ())}));
        this.target = target;
        this.opener = opener;
    }

    public void onClick() {
        if (this.target != null && this.target.isOnline() && this.opener != null && this.opener.isOnline()) {
            if (this.opener.hasPermission("staffmodex.teleport")) {
                this.opener.teleport(this.target);
                this.opener.sendMessage(AnkhiaStaff.getInstance().getMessage("menus.location.teleported", new String[0]));
            } else {
                this.opener.sendMessage(AnkhiaStaff.getInstance().getMessage("menus.location.no-permission-teleport", new String[0]));
            }
        }

    }
}