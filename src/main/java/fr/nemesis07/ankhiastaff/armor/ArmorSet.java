package fr.nemesis07.ankhiastaff.armor;

import org.bukkit.Color;

public class ArmorSet {
    private String permission;
    private Color color;
    private String type;

    public ArmorSet(String permission, Color color, String type) {
        this.permission = permission;
        this.color = color;
        this.type = type;
    }

    public String getPermission() {
        return this.permission;
    }

    public Color getColor() {
        return this.color;
    }

    public String getType() {
        return this.type == null ? "LEATHER" : this.type;
    }
}