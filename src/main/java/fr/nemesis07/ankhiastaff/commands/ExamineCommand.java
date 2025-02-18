package fr.nemesis07.ankhiastaff.commands;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.menus.ExaminePlayerMenu;
import fr.nemesis07.ankhiastaff.modernlib.commands.ModernArguments;
import fr.nemesis07.ankhiastaff.modernlib.commands.ModernCommand;
import fr.nemesis07.ankhiastaff.modernlib.menus.Menu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExamineCommand extends ModernCommand {
    public ExamineCommand() {
        super("examine", new String[]{"invsee"});
    }

    public void onCommand(CommandSender sender, ModernArguments args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.only-players", new String[0]));
        } else {
            Player player = (Player)sender;
            Player target = null;
            String targetName = args.getText(0);
            if (targetName != null && !targetName.isEmpty()) {
                target = AnkhiaStaff.getInstance().getServer().getPlayer(targetName);
                if (target == null) {
                    sender.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.examine.player-not-online", new String[]{"{player}", targetName}));
                    return;
                }
            }

            if (target == null) {
                sender.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.examine.usage", new String[0]));
            } else {
                Menu menu = new ExaminePlayerMenu(player, target);
                menu.openInventory(player);
            }
        }
    }
}