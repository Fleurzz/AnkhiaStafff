package fr.nemesis07.ankhiastaff.modernlib.utils;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionEffects {
    public static void add(Player player, int amplifier, int duration, String... effects) {
        String[] var4 = effects;
        int var5 = effects.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String effect = var4[var6];
            PotionEffectType effectType = PotionEffectType.getByName(effect);
            if (effectType != null) {
                PotionEffect potionEffect = new PotionEffect(effectType, duration, amplifier);
                player.addPotionEffect(potionEffect);
            }
        }

    }

    public static void remove(Player player, String... effects) {
        String[] var2 = effects;
        int var3 = effects.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String effect = var2[var4];
            PotionEffectType effectType = PotionEffectType.getByName(effect);
            if (effectType != null) {
                player.removePotionEffect(effectType);
            }
        }

    }
}