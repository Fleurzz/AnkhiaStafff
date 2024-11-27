package fr.nemesis07.ankhiastaff.modernlib.utils;

import java.util.List;
import org.bukkit.Material;

public class Materials {
    private Materials() {
    }

    public static Material get(String... names) {
        String[] var1 = names;
        int var2 = names.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String name = var1[var3];
            Material material = Material.getMaterial(name.toUpperCase());
            if (material != null) {
                return material;
            }
        }

        return Material.AIR;
    }

    public static Material get(List<String> names) {
        return get((String[])names.toArray(new String[0]));
    }
}
