package fr.nemesis07.ankhiastaff.player;

import fr.nemesis07.ankhiastaff.infractions.InfractionType;
import fr.nemesis07.ankhiastaff.menus.items.InfractionItem;
import org.bukkit.entity.Player;

public class WarningProcess {
    private StaffPlayer staffPlayer = null;
    private Player player = null;
    private StaffPlayer staffPlayerTarget = null;
    private Player target = null;
    private InfractionItem infractionItem = null;
    private boolean inProgress = false;

    public boolean isInProgress() {
        return this.inProgress;
    }

    public StaffPlayer getStaffPlayer() {
        return this.staffPlayer;
    }

    public Player getPlayer() {
        return this.player;
    }

    public StaffPlayer getStaffPlayerTarget() {
        return this.staffPlayerTarget;
    }

    public Player getTarget() {
        return this.target;
    }

    public InfractionItem getInfractionItem() {
        return this.infractionItem;
    }

    public void startWarning(InfractionItem item, Player player, Player target, StaffPlayer staffPlayer, StaffPlayer staffPlayerTarget) {
        this.infractionItem = item;
        this.player = player;
        this.target = target;
        this.staffPlayer = staffPlayer;
        this.staffPlayerTarget = staffPlayerTarget;
        this.inProgress = true;
    }

    public void clear() {
        this.infractionItem = null;
        this.player = null;
        this.target = null;
        this.staffPlayer = null;
        this.staffPlayerTarget = null;
    }

    public void complete(String reason) {
        this.inProgress = false;
        this.staffPlayerTarget.infraction(InfractionType.WARNING, this.staffPlayer, reason);
    }

    public void openWarningMenu(Player player) {
        if (this.infractionItem != null) {
            this.infractionItem.asyncUpdate(this.target);
            this.infractionItem.getMenu().openInventory(player);
        }
    }
}