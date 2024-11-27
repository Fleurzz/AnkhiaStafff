package fr.nemesis07.ankhiastaff.listeners;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.managers.VeinManager;
import fr.nemesis07.ankhiastaff.player.StaffPlayer;
import java.util.Collection;
import java.util.UUID;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListeners implements Listener {
    private VeinManager veinManager;

    public BlockListeners(VeinManager veinManager) {
        this.veinManager = veinManager;
    }

    @EventHandler(
            ignoreCancelled = true
    )
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (AnkhiaStaff.getInstance().getStaffModeManager().isStaff(player)) {
            event.setCancelled(true);
        } else {
            StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
            if (staffPlayer != null) {
                if (staffPlayer.isFrozen()) {
                    staffPlayer.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.freeze.cannot-interact", new String[0]));
                    event.setCancelled(true);
                }

                Block block = event.getBlock();
                if (AnkhiaStaff.getInstance().getCfg().getBoolean("diamond-finder.enabled")) {
                    Collection<Block> vein = this.veinManager.getConnectedDiamondOres(player, block);
                    if (vein != null) {
                        int minStreak = AnkhiaStaff.getInstance().getCfg().getInt("diamond-finder.min-streak");
                        int streak = this.veinManager.getStreak(player);
                        if (streak >= minStreak) {
                            UUID uuid = player.getUniqueId();
                            String size = String.valueOf(vein.size());
                            AnkhiaStaff.getInstance().sendMessageToStaffPlayers(AnkhiaStaff.getInstance().getMessage("messages.diamond-finder.found", new String[]{"{player}", player.getName(), "{streak}", String.valueOf(streak), "{size}", size}), uuid);
                        }
                    }
                }

            }
        }
    }

    @EventHandler(
            ignoreCancelled = true
    )
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (AnkhiaStaff.getInstance().getStaffModeManager().isStaff(player) && !player.hasPermission("staffmodex.build")) {
            event.setCancelled(true);
        } else {
            StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
            if (staffPlayer != null) {
                if (staffPlayer.isFrozen()) {
                    staffPlayer.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.freeze.cannot-interact", new String[0]));
                    event.setCancelled(true);
                }

            }
        }
    }
}