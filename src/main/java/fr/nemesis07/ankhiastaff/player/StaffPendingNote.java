package fr.nemesis07.ankhiastaff.player;

import fr.nemesis07.ankhiastaff.menus.items.PlayerNotesItem;
import fr.nemesis07.ankhiastaff.modernlib.menus.Menu;
import java.util.UUID;
import org.bukkit.entity.Player;

public class StaffPendingNote {
    private StaffNotes notes;
    private boolean isWriting = false;
    private UUID writingUUID = null;
    private String writingName = null;
    private PlayerNotesItem oldItem = null;

    public StaffPendingNote(StaffNotes notes) {
        this.notes = notes;
    }

    public boolean isWritingNote() {
        return this.isWriting;
    }

    public StaffNote writeNote(String message) {
        StaffNote note = new StaffNote(this.writingUUID, this.writingName, message);
        this.isWriting = false;
        this.writingUUID = null;
        this.writingName = null;
        this.oldItem = null;
        this.notes.setNote(note.getUuid(), message);
        return note;
    }

    public void startWritingNote(PlayerNotesItem item, UUID uuid, String name) {
        this.isWriting = true;
        this.writingUUID = uuid;
        this.writingName = name;
        this.oldItem = item;
    }

    public PlayerNotesItem getNotesItem() {
        return this.oldItem;
    }

    public void openWriteMenu(Player player, String notes) {
        PlayerNotesItem notesItem = this.getNotesItem();
        if (notesItem != null) {
            Menu menu = notesItem.getMenu();
            if (menu != null) {
                notesItem.updateLore(notes);
                menu.openInventory(player);
            }

        }
    }
}