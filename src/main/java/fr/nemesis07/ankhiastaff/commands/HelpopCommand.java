package fr.nemesis07.ankhiastaff.commands;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.modernlib.commands.ModernArguments;
import fr.nemesis07.ankhiastaff.modernlib.commands.ModernCommand;
import fr.nemesis07.ankhiastaff.player.StaffPlayer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpopCommand extends ModernCommand {
    private Map<String, Long> cooldowns = new HashMap();

    public HelpopCommand() {
        super("helpop");
    }

    public void onCommand(CommandSender sender, ModernArguments args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.only-players", new String[0]));
        } else if (!sender.hasPermission("staffmodex.helpop")) {
            sender.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.helpop.no-permission", new String[0]));
        } else {
            Player player = (Player)sender;
            long currentTime = System.currentTimeMillis();
            if (!player.hasPermission("staffmodex.bypass.cooldown")) {
                Long playerCooldown = (Long)this.cooldowns.get(player.getName());
                if (playerCooldown != null && playerCooldown > currentTime) {
                    long remainingTime = (playerCooldown - currentTime) / 1000L;
                    player.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.helpop.cooldown", new String[0]).replace("{time}", String.valueOf(remainingTime)));
                    return;
                }
            }

            if (!args.hasArg(0)) {
                player.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.helpop.usage", new String[0]));
            } else {
                long cooldown = AnkhiaStaff.getInstance().getCfg().getLong("helpop.cooldown", 60L) * 1000L;
                this.cooldowns.put(player.getName(), currentTime + cooldown);
                String playerName = player.getName();
                String message = String.join(" ", args.getArgs());
                String staffMessage = AnkhiaStaff.getInstance().getMessage("messages.helpop.receive", new String[]{"{message}", message, "{player}", playerName});
                player.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.helpop.sent", new String[]{"{message}", message, "{player}", playerName}));
                Iterator var11 = AnkhiaStaff.getInstance().getStaffPlayerManager().getStaffPlayers().values().iterator();

                while(var11.hasNext()) {
                    StaffPlayer staffPlayer = (StaffPlayer)var11.next();
                    if (staffPlayer.hasPermission("staffmodex.helpop.receive")) {
                        staffPlayer.sendMessage(staffMessage);
                    }
                }

            }
        }
    }
}
