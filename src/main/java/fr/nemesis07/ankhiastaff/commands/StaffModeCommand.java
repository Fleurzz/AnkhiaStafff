package fr.nemesis07.ankhiastaff.commands;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.modernlib.commands.ModernArguments;
import fr.nemesis07.ankhiastaff.modernlib.commands.ModernCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffModeCommand extends ModernCommand {
    public StaffModeCommand() {
        super("staffmode", new String[]{"staff", "mod"});
    }

    public void onCommand(CommandSender sender, ModernArguments args) {
        if (!sender.hasPermission("staffmodex.staffmode")) {
            sender.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.no-permission", new String[0]));
        } else {
            if (sender instanceof Player) {
                Player player = (Player)sender;
                AnkhiaStaff.getInstance().getStaffModeManager().toggleStaff(player);
            } else {
                sender.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.only-players", new String[0]));
            }

        }
    }
}