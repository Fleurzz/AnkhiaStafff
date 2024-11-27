package fr.nemesis07.ankhiastaff.hotbar.components.items;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.modernlib.menus.items.MenuItem;
import fr.nemesis07.ankhiastaff.modernlib.utils.Materials;
import fr.nemesis07.ankhiastaff.modernlib.utils.Players;
import java.util.List;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PlayerItem extends MenuItem {
    public static String[] getPlaceholders(Player player, Player miner) {
        return new String[]{"{playerName}", miner.getName(), "{blockX}", String.valueOf(miner.getLocation().getBlockX()), "{blockY}", String.valueOf(miner.getLocation().getBlockY()), "{blockZ}", String.valueOf(miner.getLocation().getBlockZ()), "{health}", String.valueOf(miner.getHealth()), "{maxHealth}", String.valueOf(miner.getMaxHealth()), "{foodLevel}", String.valueOf(miner.getFoodLevel()), "{exp}", String.valueOf(miner.getExp()), "{expToLevel}", String.valueOf(miner.getExpToLevel()), "{level}", String.valueOf(miner.getLevel()), "{gameMode}", miner.getGameMode().name(), "{ip}", Players.getIP(miner, player), "{uuid}", miner.getUniqueId().toString()};
    }

    public static List<String> getDescription(Player player, Player miner) {
        List<String> description = AnkhiaStaff.getInstance().getMsg().getTextList("hotbar.playerItem.description", getPlaceholders(player, miner));
        description = PlaceholderAPI.setPlaceholders(miner, description);
        return description;
    }

    public static String getTitle(Player player, Player miner) {
        String title = AnkhiaStaff.getInstance().getMsg().getText("hotbar.playerItem.title", getPlaceholders(player, miner));
        title = PlaceholderAPI.setPlaceholders(miner, title);
        return title;
    }

    public PlayerItem(Player player, Player miner) {
        super(Materials.get(new String[]{"SKULL_ITEM", "PLAYER_HEAD"}), 1, (short)3, getTitle(player, miner), getDescription(player, miner));
    }
}
