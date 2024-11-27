package fr.nemesis07.ankhiastaff.menus.items;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.menus.InfractionsMenu;
import fr.nemesis07.ankhiastaff.modernlib.menus.items.MenuItem;
import fr.nemesis07.ankhiastaff.player.StaffPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class InfractionItem extends MenuItem {
    private Player player;
    private Player target;
    private StaffPlayer staffPlayer;
    private int warnings;
    private int reports;
    private String lastWarning;
    private String lastReport;

    public InfractionItem(Player player, Player target) {
        super(Material.PAPER, AnkhiaStaff.getInstance().getMsg().getText("menus.infraction.title"), new String[]{AnkhiaStaff.getInstance().getMsg().getText("menus.infraction.loading")});
        this.player = player;
        this.target = target;
        this.asyncUpdate(target);
    }

    public void asyncUpdate(final Player player) {
        (new BukkitRunnable() {
            public void run() {
                InfractionItem.this.setStaffPlayer(AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player.getUniqueId()));
                InfractionItem.this.setWarnings(InfractionItem.this.staffPlayer.getWarnings().count());
                InfractionItem.this.setReports(InfractionItem.this.staffPlayer.getReports().count());
                InfractionItem.this.setLastWarning(InfractionItem.this.staffPlayer.getWarnings().getLast());
                InfractionItem.this.setLastReport(InfractionItem.this.staffPlayer.getReports().getLast());
                (new BukkitRunnable() {
                    public void run() {
                        InfractionItem.this.updateLore();
                    }
                }).runTask(AnkhiaStaff.getInstance());
            }
        }).runTaskAsynchronously(AnkhiaStaff.getInstance());
    }

    public void updateLore() {
        String warningsMsg = AnkhiaStaff.getInstance().getMsg().getText("menus.infraction.warnings").replace("{warnings}", String.valueOf(this.warnings));
        String reportsMsg = AnkhiaStaff.getInstance().getMsg().getText("menus.infraction.reports").replace("{reports}", String.valueOf(this.reports));
        String lastWarningMsg = AnkhiaStaff.getInstance().getMsg().getText("menus.infraction.lastWarning").replace("{lastWarning}", this.lastWarning != null ? this.lastWarning : "N/A");
        String lastReportMsg = AnkhiaStaff.getInstance().getMsg().getText("menus.infraction.lastReport").replace("{lastReport}", this.lastReport != null ? this.lastReport : "N/A");
        this.setLore(new String[]{warningsMsg, reportsMsg, lastWarningMsg, lastReportMsg});
    }

    public void setWarnings(int warnings) {
        this.warnings = warnings;
    }

    public void setReports(int reports) {
        this.reports = reports;
    }

    public void setLastWarning(String lastWarning) {
        this.lastWarning = lastWarning;
    }

    public void setLastReport(String lastReport) {
        this.lastReport = lastReport;
    }

    public void setStaffPlayer(StaffPlayer staffPlayer) {
        this.staffPlayer = staffPlayer;
    }

    public void onClick() {
        if (this.staffPlayer != null) {
            (new InfractionsMenu(this.getMenu(), this.player, this.staffPlayer)).openInventory(this.player);
        }
    }
}
