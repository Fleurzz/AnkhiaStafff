package fr.nemesis07.ankhiastaff.player;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.modernlib.config.ConfigWrapper;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class StaffPlayerManager {
    private Map<UUID, StaffPlayer> staffPlayers = new ConcurrentHashMap<>();

    public StaffPlayer getStaffPlayer(UUID uuid) {
        return staffPlayers.getOrDefault(uuid, null);
    }

    public void addStaffPlayer(StaffPlayer staffPlayer) {
        staffPlayers.put(staffPlayer.getUUID(), staffPlayer);
    }

    public void setStaffPlayers(Map<UUID, StaffPlayer> staffPlayers) {
        this.staffPlayers.clear();
        this.staffPlayers.putAll(staffPlayers);
    }

    public Map<UUID, StaffPlayer> getStaffPlayers() {
        return staffPlayers;
    }

    public void removeStaffPlayer(UUID uuid) {
        staffPlayers.remove(uuid);
    }

    public StaffPlayer getOrCreateStaffPlayer(UUID uuid) {
        ConfigWrapper infractionsConfig = new ConfigWrapper("infractions/" + uuid + ".yml");
        ConfigWrapper ipsConfig = new ConfigWrapper("ips/" + uuid + ".yml");

        StaffPlayer staffPlayer = staffPlayers.get(uuid);
        if (staffPlayer == null) {
            staffPlayer = new StaffPlayer(uuid, infractionsConfig, ipsConfig);
            staffPlayers.put(uuid, staffPlayer);
        }
        return staffPlayer;
    }

    public StaffPlayer getOrCreateStaffPlayer(OfflinePlayer player) {
        return getOrCreateStaffPlayer(player.getUniqueId());
    }

    public StaffPlayer getStaffPlayer(OfflinePlayer player) {
        return getStaffPlayer(player.getUniqueId());
    }

    public void sendStaffChat(String message) {
        for (StaffPlayer staffPlayer : AnkhiaStaff.getInstance().getStaffPlayerManager().getStaffPlayers().values()) {
            if (staffPlayer.isStaffChatReceiver()) {
                staffPlayer.sendMessage(message);
            }
        }
        Bukkit.getConsoleSender().sendMessage(message);
    }
}