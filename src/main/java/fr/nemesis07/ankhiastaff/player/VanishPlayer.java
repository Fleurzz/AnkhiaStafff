package fr.nemesis07.ankhiastaff.player;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.modernlib.config.ConfigWrapper;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VanishPlayer extends UUIDPlayer {
    private boolean vanished = false;

    public VanishPlayer(UUID uuid) {
        super(uuid);
    }

    public void toggleVanish() {
        ConfigWrapper msg = AnkhiaStaff.getInstance().getMsg();
        Player player = this.getPlayer();
        if (!player.hasPermission("staffmodex.vanish")) {
            player.sendMessage(msg.getText("messages.vanish.no-permission"));
        } else if (!AnkhiaStaff.getInstance().getStaffModeManager().isStaff(player)) {
            player.sendMessage(msg.getText("messages.vanish.not-staff"));
        } else if (this.isVanished()) {
            this.makeVisible();
            player.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.vanish.unvanished", new String[0]));
        } else {
            this.makeInvisible();
            player.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.vanish.vanished", new String[0]));
        }
    }

    public boolean isForceVanish() {
        return this.getPlayer().hasPermission("staffmodex.vanish.force");
    }

    public void hidePlayer(boolean force, Player toBeHidden) {
        Player player = this.getPlayer();
        if (player != null) {
            if (player != toBeHidden) {
                if (force || !player.hasPermission("staffmodex.vanish.bypass")) {
                    player.hidePlayer(toBeHidden);
                }
            }
        }
    }

    public void makeInvisible() {
        Player player = this.getPlayer();
        boolean force = this.isForceVanish();
        Iterator var3 = AnkhiaStaff.getInstance().getStaffPlayerManager().getStaffPlayers().values().iterator();

        while(var3.hasNext()) {
            StaffPlayer staffPlayer = (StaffPlayer)var3.next();
            staffPlayer.hidePlayer(force, player);
        }

        // Apply invisibility effect
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));

        this.vanished = true;
        AnkhiaStaff.getInstance().setVisible(player, false);
    }

    public void makeVisible() {
        Player player = this.getPlayer();
        Iterator var2 = Bukkit.getOnlinePlayers().iterator();

        while(var2.hasNext()) {
            Player otherPlayer = (Player)var2.next();
            otherPlayer.showPlayer(player);
        }

        // Remove invisibility effect
        player.removePotionEffect(PotionEffectType.INVISIBILITY);

        this.vanished = false;
        AnkhiaStaff.getInstance().setVisible(player, true);
    }

    public boolean isVanished() {
        return this.vanished;
    }
}