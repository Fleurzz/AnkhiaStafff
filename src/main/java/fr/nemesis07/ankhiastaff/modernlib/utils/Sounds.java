package fr.nemesis07.ankhiastaff.modernlib.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Sounds {
    private Sounds() {
    }

    public static Sound get(String... names) {
        String[] var1 = names;
        int var2 = names.length;
        int var3 = 0;

        while(var3 < var2) {
            String name = var1[var3];

            try {
                return Sound.valueOf(name.toUpperCase());
            } catch (IllegalArgumentException var6) {
                ++var3;
            }
        }

        return null;
    }

    public static void play(Player player, float volume, float pitch, String... sounds) {
        Sound sound = get(sounds);
        if (sound != null) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }

    }
}