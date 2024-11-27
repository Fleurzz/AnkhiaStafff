package fr.nemesis07.ankhiastaff.player;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class UUIDPlayer {
    private UUID uuid;
    private String name = null;

    public UUIDPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public void sendMessage(String msg) {
        Player player = this.getPlayer();
        if (player != null) {
            player.sendMessage(msg);
        }

    }

    public String getName() {
        if (this.name != null) {
            return this.name;
        } else {
            Player player = this.getPlayer();
            if (player != null) {
                this.name = player.getName();
                return this.name;
            } else {
                return "N/A";
            }
        }
    }
}