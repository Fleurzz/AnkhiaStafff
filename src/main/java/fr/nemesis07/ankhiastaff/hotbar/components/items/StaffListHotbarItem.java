package fr.nemesis07.ankhiastaff.hotbar.components.items;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.hotbar.HotbarItem;
import fr.nemesis07.ankhiastaff.modernlib.config.ConfigWrapper;
import fr.nemesis07.ankhiastaff.modernlib.menus.Menu;
import fr.nemesis07.ankhiastaff.modernlib.menus.items.MenuItem;
import fr.nemesis07.ankhiastaff.modernlib.utils.Materials;
import org.bukkit.Material;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class StaffListHotbarItem extends HotbarItem {

    public StaffListHotbarItem() {
        super(
                Materials.get(AnkhiaStaff.getInstance().getCfg().getStringList("items.hotbar.stafflist.material")),
                AnkhiaStaff.getInstance().getMsg().getText("hotbar.stafflist.name"),
                1,
                (short)0,
                AnkhiaStaff.getInstance().getMsg().getTextList("hotbar.stafflist.lore")
        );
    }

    public void onInteract(final Player player) {
        // Create menu
        Menu menu = new Menu(AnkhiaStaff.getInstance().getMsg().getText("hotbar.stafflist.menu_title"), 3);

        // Add close button
        menu.setItem(18, new MenuItem(
                Material.ARROW,
                AnkhiaStaff.getInstance().getMsg().getText("hotbar.stafflist.close"),
                new String[0]
        ) {
            @Override
            public void onClick() {
                player.closeInventory();
            }
        });

        // Set background
        menu.setBackground(
                Materials.get(AnkhiaStaff.getInstance().getCfg().getStringList("items.fill.material")),
                (short)AnkhiaStaff.getInstance().getCfg().getInt("items.fill.damage"),
                AnkhiaStaff.getInstance().getCfg().getText("items.fill.name"),
                new String[0]
        );

        menu.openInventory(player);

        // Fill menu asynchronously
        Bukkit.getScheduler().runTaskAsynchronously(AnkhiaStaff.getInstance(), () -> {
            List<String> staffPlayers = new ArrayList<>();

            // Get online staff players
            for(Player p : Bukkit.getOnlinePlayers()) {
                if(p.hasPermission("staffmodex.staffmode")) {
                    staffPlayers.add(p.getName());
                }
            }

            // Add staff player heads to menu
            int slot = 0;
            for(String playerName : staffPlayers) {
                if(slot >= 18) break;
                if(playerName == null) continue;

                final int currentSlot = slot++;

                // Update menu synchronously
                Bukkit.getScheduler().runTask(AnkhiaStaff.getInstance(), () -> {
                    ConfigWrapper msg = AnkhiaStaff.getInstance().getMsg();
                    String isHere = Bukkit.getPlayer(playerName) != null ?
                            msg.getText("hotbar.staff_player_item.here") : "";

                    menu.setItem(currentSlot, new MenuItem(
                            Materials.get(new String[]{"SKULL_ITEM", "PLAYER_HEAD"}),
                            1,
                            (short)3,
                            msg.getText("hotbar.staff_player_item.title",
                                    new String[]{"{playerName}", playerName}),
                            msg.getTextList("hotbar.staff_player_item.description",
                                    new String[]{
                                            "{playerName}", playerName,
                                            "{serverName}", AnkhiaStaff.getInstance().getServerName(),
                                            "{here}", isHere
                                    })
                    ));
                });
            }

            player.updateInventory();
        });
    }
}