package fr.nemesis07.ankhiastaff.hotbar.components.items;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.hotbar.HotbarItem;
import fr.nemesis07.ankhiastaff.modernlib.utils.Materials;
import fr.nemesis07.ankhiastaff.modernlib.utils.Sounds;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class RandomTeleportHotbarItem extends HotbarItem {
    public RandomTeleportHotbarItem() {
        super(Materials.get(AnkhiaStaff.getInstance().getCfg().getStringList("items.hotbar.r-teleport.material")), AnkhiaStaff.getInstance().getMsg().getText("hotbar.random_teleport.name"), 1, (short)0, AnkhiaStaff.getInstance().getMsg().getTextList("hotbar.random_teleport.lore"));
    }

    public void onInteract(Player player) {
        Server server = player.getServer();
        List<Player> otherPlayers = (List)server.getOnlinePlayers().stream().filter((p) -> {
            return !p.equals(player) && p.isOnline();
        }).collect(Collectors.toList());
        if (otherPlayers.isEmpty()) {
            player.sendMessage(AnkhiaStaff.getInstance().getMessage("hotbar.random_teleport.no_players", new String[0]));
        } else {
            Player targetPlayer = (Player)otherPlayers.get((new Random()).nextInt(otherPlayers.size()));
            player.teleport(targetPlayer.getLocation());
            player.sendMessage(AnkhiaStaff.getInstance().getMessage("hotbar.random_teleport.success", new String[0]).replace("{player}", targetPlayer.getDisplayName()));
            Sounds.play(player, 1.0F, 1.0F, new String[]{"ENTITY_ENDERMAN_TELEPORT", "ENDERMAN_TELEPORT"});
        }
    }
}
