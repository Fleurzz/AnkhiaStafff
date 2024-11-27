package fr.nemesis07.ankhiastaff.menus.items;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.modernlib.menus.items.MenuItem;
import fr.nemesis07.ankhiastaff.player.StaffPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class FreezePlayerItem extends MenuItem {
    private Player player;
    private Player target;

    public FreezePlayerItem(Player player, Player target) {
        super(Material.ICE, AnkhiaStaff.getInstance().getMsg().getText("menus.freezePlayer.title"), new String[]{AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(target).isFrozen() ? AnkhiaStaff.getInstance().getMsg().getText("menus.freezePlayer.frozen") : AnkhiaStaff.getInstance().getMsg().getText("menus.freezePlayer.notFrozen")});
        this.player = player;
        this.target = target;
    }

    public void onClick(int slot) {
        StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(this.target);
        staffPlayer.toggleFreeze(this.player);
        this.setLore(new String[]{staffPlayer.isFrozen() ? AnkhiaStaff.getInstance().getMsg().getText("menus.freezePlayer.frozen") : AnkhiaStaff.getInstance().getMsg().getText("menus.freezePlayer.notFrozen")});
    }
}
