package fr.nemesis07.ankhiastaff.menus.items;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.modernlib.menus.items.MenuItem;
import fr.nemesis07.ankhiastaff.player.StaffPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class WarnPlayerItem extends MenuItem {
    private InfractionItem infractionItem;
    private StaffPlayer staffPlayer;
    private StaffPlayer staffPlayerTarget;
    private Player player;
    private Player target;

    public WarnPlayerItem(InfractionItem infractionItem, final Player player, final Player target) {
        super(Material.PAPER, AnkhiaStaff.getInstance().getMsg().getText("menus.warnPlayer.title"), new String[]{AnkhiaStaff.getInstance().getMsg().getText("menus.warnPlayer.description")});
        this.infractionItem = infractionItem;
        this.player = player;
        this.target = target;
        (new BukkitRunnable() {
            public void run() {
                WarnPlayerItem.this.setStaffPlayer(AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player.getUniqueId()));
                WarnPlayerItem.this.setStaffPlayerTarget(AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(target.getUniqueId()));
            }
        }).runTaskAsynchronously(AnkhiaStaff.getInstance());
    }

    public void onClick() {
        if (!this.player.hasPermission("staffmodex.warning")) {
            this.player.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.no-permission", new String[0]));
        } else {
            this.player.closeInventory();
            this.staffPlayer.getWarningProcess().startWarning(this.infractionItem, this.player, this.target, this.staffPlayer, this.staffPlayerTarget);
            this.player.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.warning.started", new String[0]).replace("{player}", this.target.getName()));
        }
    }

    public StaffPlayer getStaffPlayerTarget() {
        return this.staffPlayerTarget;
    }

    public void setStaffPlayerTarget(StaffPlayer staffPlayer) {
        this.staffPlayerTarget = staffPlayer;
    }

    public StaffPlayer getStaffPlayer() {
        return this.staffPlayer;
    }

    public void setStaffPlayer(StaffPlayer staffPlayer) {
        this.staffPlayer = staffPlayer;
    }
}