package fr.nemesis07.ankhiastaff.menus.items;

import  fr.nemesis07.ankhiastaff.AnkhiaStaff;
import  fr.nemesis07.ankhiastaff.modernlib.menus.items.MenuItem;
import  fr.nemesis07.ankhiastaff.modernlib.utils.Players;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ConnectionItem extends MenuItem {
    public ConnectionItem(Player target, Player player) {
        super(Material.COMPASS, AnkhiaStaff.getInstance().getMsg().getText("menus.connection.title"), (String[])AnkhiaStaff.getInstance().getMsg().getTextList("menus.connection.lore", new String[]{"{ip}", Players.getIP(target, player), "{port}", String.valueOf(target.getAddress().getPort())}).toArray(new String[0]));
    }
}