package fr.nemesis07.ankhiastaff.listeners;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.hotbar.Hotbar;
import fr.nemesis07.ankhiastaff.hotbar.HotbarItem;
import fr.nemesis07.ankhiastaff.player.StaffPlayer;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryListeners implements Listener {
    @EventHandler(
            ignoreCancelled = true
    )
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity human = event.getWhoClicked();
        if (human instanceof Player) {
            Player player = (Player)human;
            if (AnkhiaStaff.getInstance().getStaffModeManager().isStaff(player)) {
                event.setCancelled(true);
                return;
            }

            StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
            if (staffPlayer != null && staffPlayer.isFrozen()) {
                staffPlayer.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.freeze.cannot-click-inventory", new String[0]));
                event.setCancelled(true);
                return;
            }
        }

    }

    @EventHandler(
            ignoreCancelled = true
    )
    public void onInventoryDrag(InventoryDragEvent event) {
        HumanEntity human = event.getWhoClicked();
        if (human instanceof Player) {
            Player player = (Player)human;
            if (AnkhiaStaff.getInstance().getStaffModeManager().isStaff(player)) {
                event.setCancelled(true);
                return;
            }

            StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
            if (staffPlayer != null && staffPlayer.isFrozen()) {
                staffPlayer.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.freeze.cannot-drag-inventory", new String[0]));
                event.setCancelled(true);
                return;
            }

            Hotbar hotbar = AnkhiaStaff.getInstance().getHotbarManager().getHotbar(player);
            if (hotbar != null) {
                Set<Integer> slots = event.getInventorySlots();
                Iterator var7 = slots.iterator();

                while(var7.hasNext()) {
                    int slot = (Integer)var7.next();
                    HotbarItem hotbarItem = hotbar.getItem(slot);
                    if (hotbarItem != null) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }

    }
}