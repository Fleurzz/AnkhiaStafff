package fr.nemesis07.ankhiastaff.commands;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.modernlib.commands.ModernArguments;
import fr.nemesis07.ankhiastaff.modernlib.commands.ModernCommand;
import fr.nemesis07.ankhiastaff.player.StaffPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IPCommand extends ModernCommand {
    public IPCommand() {
        super("ip", new String[]{"iplog"});
    }

    public void onCommand(CommandSender sender, ModernArguments args) {
        if (!sender.hasPermission("staffmodex.ip")) {
            sender.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.ip.no-permission", new String[0]));
            return;
        }

        String targetName = args.getText(0);
        if (targetName == null || targetName.isEmpty()) {
            sender.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.ip.usage", new String[0]));
            return;
        }

        OfflinePlayer targetPlayer = AnkhiaStaff.getInstance().getServer().getPlayer(targetName);
        if (targetPlayer == null) {
            targetPlayer = AnkhiaStaff.getInstance().getServer().getOfflinePlayer(targetName);
            StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(targetPlayer);

            Bukkit.getScheduler().runTaskAsynchronously(AnkhiaStaff.getInstance(), () -> {
                staffPlayer.getStaffPlayerLoader().loadIP();
                String ip = staffPlayer.getIP();
                if (ip != null) {
                    sender.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.ip.message",
                            new String[]{"{player}", targetName, "{ip}", ip}));
                } else {
                    sender.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.ip.no-ip",
                            new String[]{"{player}", targetName}));
                }
            });
        } else if (targetPlayer instanceof Player) {
            StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(targetPlayer);
            sender.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.ip.message",
                    new String[]{"{player}", targetName, "{ip}", staffPlayer.getIP()}));
        }
    }
}