package fr.nemesis07.ankhiastaff.tasks;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.modernlib.tasks.ModernTask;
import fr.nemesis07.ankhiastaff.modernlib.utils.ServerUtils;
import fr.nemesis07.ankhiastaff.modernlib.utils.Titles;
import fr.nemesis07.ankhiastaff.player.FreezeStatus;
import fr.nemesis07.ankhiastaff.player.StaffPlayer;
import java.util.Iterator;
import org.bukkit.entity.Player;

public class StaffActionBarTask extends ModernTask {
    public StaffActionBarTask() {
        super(AnkhiaStaff.getInstance(), 5L, false);
    }

    public void run() {
        Iterator var1 = AnkhiaStaff.getInstance().getStaffModeManager().getStaffPlayers().iterator();

        while(true) {
            Player player;
            StaffPlayer staffPlayer;
            String msg;
            label39:
            do {
                while(var1.hasNext()) {
                    player = (Player)var1.next();
                    staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
                    if (!staffPlayer.getFrozenPlayersByMe().isEmpty()) {
                        continue label39;
                    }

                    if (AnkhiaStaff.getInstance().getCfg().getBoolean("action_bar.on_staff")) {
                        String vanished = staffPlayer.isVanished() ? "&a✔" : "&c✖";
                        String staffChat = staffPlayer.isStaffChat() ? "&a✔" : "&c✖";
                        String tps = ServerUtils.getTPSFormatted(0);
                        msg = AnkhiaStaff.getInstance().getMsg().getText("staffmode.action_bar", new String[]{"{vanished}", vanished, "{staffchat}", staffChat, "{tps}", tps});
                        Titles.sendActionBar(player, msg);
                    }
                }

                return;
            } while(!AnkhiaStaff.getInstance().getCfg().getBoolean("action_bar.on_freeze"));

            Iterator var8 = staffPlayer.getFrozenPlayersByMe().iterator();

            while(var8.hasNext()) {
                FreezeStatus freezeStatus = (FreezeStatus)var8.next();
                Player otherPlayer = freezeStatus.getTarget().getPlayer();
                msg = AnkhiaStaff.getInstance().getMsg().getText("messages.freeze.action_bar", new String[]{"{countdown}", freezeStatus.getCountdownFormatted(), "{timeElapsed}", freezeStatus.getTimeFormatted()});
                Titles.sendActionBar(player, msg);
                if (otherPlayer != null) {
                    Titles.sendActionBar(otherPlayer, msg);
                }
            }
        }
    }
}