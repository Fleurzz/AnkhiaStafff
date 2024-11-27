package fr.nemesis07.ankhiastaff.modernlib.utils;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Effects {
    private Effects() {
    }

    public static Object get(String... names) {
        String[] var1 = names;
        int var2 = names.length;
        int var3 = 0;

        while(var3 < var2) {
            String name = var1[var3];

            try {
                return Effect.valueOf(name.toUpperCase());
            } catch (Exception var6) {
                ++var3;
            }
        }

        return null;
    }

    public static void play(Player player, String... effects) {
        Object effectObject = get(effects);
        if (effectObject instanceof Effect) {
            Effect effect = (Effect)effectObject;
            player.playEffect(player.getLocation(), effect, 0);
        }

    }

    public static void play(Location loc, String... effects) {
        Object effectObject = get(effects);
        if (effectObject instanceof Effect) {
            Effect effect = (Effect)effectObject;
            loc.getWorld().playEffect(loc, effect, 0);
        }

    }
}