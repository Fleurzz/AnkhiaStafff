package fr.nemesis07.ankhiastaff.menus.items;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.modernlib.menus.items.MenuItem;
import fr.nemesis07.ankhiastaff.player.StaffPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerNotesItem extends MenuItem {
    private Player player;
    private Player target;

    public PlayerNotesItem(final Player player, final Player target) {
        super(Material.PAPER, AnkhiaStaff.getInstance().getMsg().getText("menus.playerNotes.title"), new String[]{"&bNotes: &7" + AnkhiaStaff.getInstance().getMsg().getText("menus.playerNotes.loading")});
        this.player = player;
        this.target = target;
        (new BukkitRunnable() {
            public void run() {
                StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player.getUniqueId());
                if (staffPlayer != null) {
                    final String notes = staffPlayer.getNotes(target.getUniqueId());
                    (new BukkitRunnable() {
                        public void run() {
                            PlayerNotesItem.this.updateLore(notes);
                        }
                    }).runTask(AnkhiaStaff.getInstance());
                }

            }
        }).runTaskAsynchronously(AnkhiaStaff.getInstance());
    }

    public void onClick() {
        StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(this.player.getUniqueId());
        staffPlayer.startWritingNote(this, this.target.getUniqueId(), this.target.getName());
        this.player.closeInventory();
        this.player.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.note-started", new String[0]).replace("{player}", this.target.getName()));
    }

    public void updateLore(String notes) {
        if (notes == null) {
            this.setLore(new String[]{AnkhiaStaff.getInstance().getMsg().getText("menus.playerNotes.noNotes")});
        } else {
            this.setLore(new String[]{"&bNotes: &7" + notes});
        }

    }
}