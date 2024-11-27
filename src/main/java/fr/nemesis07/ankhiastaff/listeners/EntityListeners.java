package fr.nemesis07.ankhiastaff.listeners;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.hotbar.Hotbar;
import fr.nemesis07.ankhiastaff.hotbar.HotbarItem;
import fr.nemesis07.ankhiastaff.hotbar.components.items.KnockbackHotbarItem;
import fr.nemesis07.ankhiastaff.player.StaffPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityListeners implements Listener {
    @EventHandler(
            ignoreCancelled = true
    )
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player)entity;
            StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
            if (staffPlayer.isFrozen() || AnkhiaStaff.getInstance().getStaffModeManager().isStaff(player)) {
                event.setCancelled(true);
            }
        }

    }

    @EventHandler(
            ignoreCancelled = true
    )
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (damager instanceof Player) {
            Player player = (Player)damager;
            StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
            if (staffPlayer.isFrozen() || AnkhiaStaff.getInstance().getStaffModeManager().isStaff(player)) {
                Hotbar hotbar = AnkhiaStaff.getInstance().getHotbarManager().getHotbar(player);
                if (hotbar != null) {
                    HotbarItem hotbarItem = hotbar.getItem(player.getInventory().getHeldItemSlot());
                    if (hotbarItem != null && hotbarItem instanceof KnockbackHotbarItem) {
                        return;
                    }
                }

                event.setCancelled(true);
                return;
            }
        }

        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player)entity;
            StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
            if (staffPlayer.isFrozen() || AnkhiaStaff.getInstance().getStaffModeManager().isStaff(player)) {
                event.setCancelled(true);
                return;
            }
        }

    }

    @EventHandler(
            ignoreCancelled = true
    )
    public void onEntityTarget(EntityTargetEvent event) {
        Entity target = event.getTarget();
        if (target instanceof Player) {
            Player player = (Player)target;
            boolean staff = AnkhiaStaff.getInstance().getStaffModeManager().isStaff(player);
            if (staff) {
                event.setCancelled(true);
            }

            StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
            if (staffPlayer == null) {
                return;
            }

            if (staffPlayer.isFrozen()) {
                event.setCancelled(true);
            }
        }

    }
}
