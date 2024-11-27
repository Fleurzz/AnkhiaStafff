package fr.nemesis07.ankhiastaff.commands;

import fr.nemesis07.ankhiastaff.infractions.InfractionType;

public class ReportCommand extends InfractionCommand {
    public ReportCommand() {
        super("report", InfractionType.REPORT);
    }
}
