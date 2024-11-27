package fr.nemesis07.ankhiastaff.modernlib.utils;

import java.util.Iterator;
import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Players {
    public static void setFlying(Player player, boolean flying) {
        if (player.getAllowFlight() != flying) {
            player.setAllowFlight(flying);
        }

        if (player.isFlying() != flying) {
            player.setFlying(flying);
        }

    }

    public static void clearInventory(Player player) {
        player.getInventory().setArmorContents((ItemStack[])null);
        player.getInventory().clear();
    }

    public static void sendMessage(Player player, List<String> textList) {
        Iterator var2 = textList.iterator();

        while(var2.hasNext()) {
            String text = (String)var2.next();
            player.sendMessage(text);
        }

    }

    public static void heal(Player player) {
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
    }

    public static void setGameMode(Player player, String gameModeName) {
        try {
            setGameMode(player, GameMode.valueOf(gameModeName));
        } catch (IllegalStateException var3) {
        }

    }

    public static void setGameMode(Player player, GameMode gameMode) {
        player.setGameMode(gameMode);
    }

    public static String getIP(Player target, Player player) {
        return player.hasPermission("staffmodex.ip") ? target.getAddress().getAddress().getHostAddress() : "{...}";
    }
}