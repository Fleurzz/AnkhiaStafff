package fr.nemesis07.ankhiastaff.commands;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.modernlib.commands.ModernArguments;
import fr.nemesis07.ankhiastaff.modernlib.commands.ModernCommand;
import fr.nemesis07.ankhiastaff.player.StaffPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChatCommand extends ModernCommand {
    public StaffChatCommand() {
        super("staffchat", new String[]{"sc"});
    }

    public void onCommand(CommandSender sender, ModernArguments args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (player.hasPermission("staffmodex.staffchat")) {
                StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
                if (args.hasArg(0)) {
                    staffPlayer.sendStaffChat(String.join(" ", args.getArgs(0)));
                } else {
                    staffPlayer.toggleStaffChat();
                }
            } else {
                sender.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.staffchat.no-permission", new String[0]));
            }
        } else {
            sender.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.only-players", new String[0]));
        }

    }
}