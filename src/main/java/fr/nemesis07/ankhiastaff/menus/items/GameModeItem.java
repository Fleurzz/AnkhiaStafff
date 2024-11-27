package fr.nemesis07.ankhiastaff.menus.items;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.modernlib.menus.items.MenuItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GameModeItem extends MenuItem {
    public GameModeItem(Player player) {
        super(Material.GRASS, AnkhiaStaff.getInstance().getMsg().getText("menus.gameMode.title"), new String[]{AnkhiaStaff.getInstance().getMsg().getText("menus.gameMode.gameMode").replace("{gamemode}", player.getGameMode().toString())});
    }
}