package fr.nemesis07.ankhiastaff.player;

import fr.nemesis07.ankhiastaff.menus.items.PlayerNotesItem;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StaffNotes {
    private Map<UUID, String> notes = new HashMap<>();
    private StaffPendingNote pendingNote = new StaffPendingNote(this);

    public String getNote(UUID uuid) {
        return notes.getOrDefault(uuid, null);
    }

    public void setNote(UUID uuid, String staffNote) {
        notes.put(uuid, staffNote);
    }

    public boolean isWritingNote() {
        return pendingNote.isWritingNote();
    }

    public StaffNote writeNote(String message) {
        return pendingNote.writeNote(message);
    }

    public void startWritingNote(PlayerNotesItem item, UUID uuid, String name) {
        pendingNote.startWritingNote(item, uuid, name);
    }

    public void openWriteMenu(Player player, String notes) {
        pendingNote.openWriteMenu(player, notes);
    }
}