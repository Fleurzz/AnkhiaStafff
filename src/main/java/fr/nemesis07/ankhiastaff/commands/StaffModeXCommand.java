package fr.nemesis07.ankhiastaff.commands;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.modernlib.commands.ModernArguments;
import fr.nemesis07.ankhiastaff.modernlib.commands.ModernCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class StaffModeXCommand extends ModernCommand {
    public StaffModeXCommand() {
        super("staffmodex");
    }

    public void onCommand(CommandSender sender, ModernArguments args) {
        if (!sender.hasPermission("staffmodex.admin")) {
            sender.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.no-permission", new String[0]));
        } else {
            String firstArg = args.getText(0);
            if ("reload".equalsIgnoreCase(firstArg)) {
                AnkhiaStaff.getInstance().onDisable();
                AnkhiaStaff.getInstance().onEnable();
                sender.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.reloaded", new String[0]));
            } else {
                sender.sendMessage(ChatColor.BLUE + "StaffModeX by LinsaFTW");
            }

        }
    }
}
