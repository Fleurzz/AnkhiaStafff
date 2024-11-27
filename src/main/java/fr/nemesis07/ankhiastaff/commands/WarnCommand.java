package fr.nemesis07.ankhiastaff.commands;

import fr.nemesis07.ankhiastaff.infractions.InfractionType;

public class WarnCommand extends InfractionCommand {
    public WarnCommand() {
        super("warn", InfractionType.WARNING);
    }
}
