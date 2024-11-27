package fr.nemesis07.ankhiastaff.commands;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.infractions.InfractionType;
import fr.nemesis07.ankhiastaff.modernlib.commands.ModernArguments;
import fr.nemesis07.ankhiastaff.modernlib.commands.ModernCommand;
import fr.nemesis07.ankhiastaff.player.StaffPlayer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class InfractionCommand extends ModernCommand {
    private InfractionType infractionType;
    private Map<String, Long> cooldowns;

    public InfractionCommand(String name, InfractionType infractionType) {
        super(name);
        this.infractionType = infractionType;
        this.cooldowns = new HashMap();
    }

    public void onCommand(CommandSender sender, ModernArguments args) {
        if (!sender.hasPermission("staffmodex." + this.getName())) {
            sender.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.no-permission", new String[0]));
        } else if (!(sender instanceof Player)) {
            sender.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.only-players", new String[0]));
        } else {
            final Player player = (Player)sender;
            final long currentTime = System.currentTimeMillis();
            if (!player.hasPermission("staffmodex.bypass.cooldown")) {
                Long playerCooldown = (Long)this.cooldowns.get(player.getName());
                if (playerCooldown != null && playerCooldown > currentTime) {
                    long remainingTime = (playerCooldown - currentTime) / 1000L;
                    player.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.cooldown", new String[0]).replace("{time}", String.valueOf(remainingTime)));
                    return;
                }
            }

            if (!args.hasArg(1)) {
                player.sendMessage(AnkhiaStaff.getInstance().getMessage("messages." + this.infractionType.name().toLowerCase() + ".usage", new String[0]));
            } else {
                final String targetPlayerName = args.getText(0);
                final String reason = String.join(" ", (CharSequence[])Arrays.copyOfRange(args.getArgs(), 1, args.getArgs().length));
                (new BukkitRunnable() {
                    public void run() {
                        Player target = Bukkit.getPlayer(targetPlayerName);
                        if (target == null) {
                            player.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.player-not-found", new String[0]));
                        } else {
                            StaffPlayer targetStaffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(target);
                            StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
                            targetStaffPlayer.infraction(InfractionCommand.this.infractionType, staffPlayer, reason);
                            player.sendMessage(AnkhiaStaff.getInstance().getMessage("messages." + InfractionCommand.this.infractionType.name().toLowerCase() + ".success", new String[]{"{player}", targetPlayerName, "{reason}", reason}));
                            long cooldown = AnkhiaStaff.getInstance().getCfg().getLong(InfractionCommand.this.infractionType.name().toLowerCase() + ".cooldown", 60L) * 1000L;
                            InfractionCommand.this.cooldowns.put(player.getName(), currentTime + cooldown);
                        }
                    }
                }).runTaskAsynchronously(AnkhiaStaff.getInstance());
            }
        }
    }
}
