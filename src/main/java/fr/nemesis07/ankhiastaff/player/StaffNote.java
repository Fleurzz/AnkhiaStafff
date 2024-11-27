package fr.nemesis07.ankhiastaff.player;

import java.util.UUID;

public class StaffNote {
    private UUID uuid;
    private String name;
    private String message;

    public StaffNote(UUID uuid, String name, String message) {
        this.uuid = uuid;
        this.name = name;
        this.message = message;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public String getMessage() {
        return this.message;
    }
}