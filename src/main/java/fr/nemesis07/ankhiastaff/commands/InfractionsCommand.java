package fr.nemesis07.ankhiastaff.commands;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.menus.InfractionsMenu;
import fr.nemesis07.ankhiastaff.modernlib.commands.ModernArguments;
import fr.nemesis07.ankhiastaff.modernlib.commands.ModernCommand;
import fr.nemesis07.ankhiastaff.modernlib.menus.Menu;
import fr.nemesis07.ankhiastaff.player.StaffPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfractionsCommand extends ModernCommand {
    public InfractionsCommand() {
        super("infractions", new String[]{"warnings"});
    }

    public void onCommand(CommandSender sender, ModernArguments args) {
        Bukkit.getScheduler().runTaskAsynchronously(AnkhiaStaff.getInstance(), () -> {
            if (!(sender instanceof Player)) {
                sender.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.only-players", new String[0]));
            } else {
                Player player = (Player)sender;
                OfflinePlayer target = null;
                String targetName = args.getText(0);
                if (targetName != null && !targetName.isEmpty()) {
                    target = AnkhiaStaff.getInstance().getServer().getOfflinePlayer(targetName);
                }

                if (target == null) {
                    sender.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.infractions.usage", new String[0]));
                } else {
                    StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(target);
                    if (!target.isOnline()) {
                        staffPlayer.load();
                    }

                    Bukkit.getScheduler().runTask(AnkhiaStaff.getInstance(), () -> {
                        (new InfractionsMenu((Menu)null, player, staffPlayer)).openInventory(player);
                    });
                }
            }
        });
    }
}