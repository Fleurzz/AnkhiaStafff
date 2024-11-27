package fr.nemesis07.ankhiastaff.commands;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.modernlib.commands.ModernArguments;
import fr.nemesis07.ankhiastaff.modernlib.commands.ModernCommand;
import fr.nemesis07.ankhiastaff.player.StaffPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand extends ModernCommand {
    public VanishCommand() {
        super("vanish", new String[]{"v"});
    }

    public void onCommand(CommandSender sender, ModernArguments args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
            staffPlayer.toggleVanish();
        } else {
            sender.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.only-players", new String[0]));
        }

    }
}
