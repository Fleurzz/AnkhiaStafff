package fr.nemesis07.ankhiastaff.expansion;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.player.FreezablePlayer;
import fr.nemesis07.ankhiastaff.player.FreezeStatus;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class StaffModePlaceholderExpansion extends PlaceholderExpansion {
    public String getAuthor() {
        return "LinsaFTW";
    }

    public String getIdentifier() {
        return "staffmodex";
    }

    public String getVersion() {
        return AnkhiaStaff.getInstance().getDescription().getVersion();
    }

    public String onRequest(OfflinePlayer player, String params) {
        byte var4 = -1;
        switch(params.hashCode()) {
            case -1609594047:
                if (params.equals("enabled")) {
                    var4 = 0;
                }
                break;
            case -135704224:
                if (params.equals("frozen_player")) {
                    var4 = 3;
                }
                break;
            case 560683726:
                if (params.equals("playercount")) {
                    var4 = 4;
                }
                break;
            case 2059631241:
                if (params.equals("freeze_countdown")) {
                    var4 = 1;
                }
                break;
            case 2116867957:
                if (params.equals("freeze_time")) {
                    var4 = 2;
                }
        }

        FreezeStatus freezeStatus;
        switch(var4) {
            case 0:
                boolean status = false;
                if (player instanceof Player) {
                    status = AnkhiaStaff.getInstance().getStaffModeManager().isStaff((Player)player);
                }

                return String.valueOf(status);
            case 1:
                if (player instanceof Player) {
                    freezeStatus = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player).getFreezeStatus();
                    return freezeStatus.getCountdownFormatted();
                }

                return "0m 0s";
            case 2:
                if (player instanceof Player) {
                    freezeStatus = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player).getFreezeStatus();
                    return freezeStatus.getTimeFormatted();
                }

                return "0m 0s";
            case 3:
                if (player instanceof Player) {
                    freezeStatus = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player).getFreezeStatus();
                    if (freezeStatus != null) {
                        FreezablePlayer freezablePlayer = freezeStatus.getTarget();
                        if (freezablePlayer != null) {
                            return freezablePlayer.getName();
                        }
                    }
                }

                return "";
            case 4:
                return String.valueOf(AnkhiaStaff.getInstance().getVisiblePlayerCount());
            default:
                return null;
        }
    }
}