package fr.nemesis07.ankhiastaff.armor;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.modernlib.config.ConfigWrapper;
import fr.nemesis07.ankhiastaff.modernlib.utils.Materials;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ArmorManager {
    private Map<String, ArmorSet> armorSets = new HashMap();

    public ArmorManager(ConfigWrapper config) {
        this.loadArmorSets(config);
    }

    private void loadArmorSets(ConfigWrapper config) {
        if (config.isConfigurationSection("armors.sets")) {
            ConfigurationSection setsConfig = config.getConfigurationSection("armors.sets");
            Iterator var3 = setsConfig.getKeys(false).iterator();

            while(var3.hasNext()) {
                String key = (String)var3.next();
                ConfigurationSection setConfig = setsConfig.getConfigurationSection(key);
                String permission = setConfig.getString("permission");
                String colorName = setConfig.getString("color");
                String typeName = setConfig.getString("type");
                Color color = this.getColorFromString(colorName);
                if (permission != null && color != null) {
                    this.armorSets.put(key, new ArmorSet(permission, color, typeName));
                }
            }

        }
    }

    private Color getColorFromString(String colorName) {
        try {
            return (Color)Color.class.getField(colorName.toUpperCase()).get((Object)null);
        } catch (Exception var3) {
            return null;
        }
    }

    public void giveArmor(Player player) {
        if (AnkhiaStaff.getInstance().getCfg().getBoolean("armors.enabled")) {
            ArmorSet armor = this.getArmor(player);
            if (armor != null) {
                Color color = armor.getColor();
                String type = armor.getType();
                ItemStack helmet = this.createArmor(Materials.get(new String[]{type + "_HELMET"}), color);
                ItemStack chestplate = this.createArmor(Materials.get(new String[]{type + "_CHESTPLATE"}), color);
                ItemStack leggings = this.createArmor(Materials.get(new String[]{type + "_LEGGINGS"}), color);
                ItemStack boots = this.createArmor(Materials.get(new String[]{type + "_BOOTS"}), color);
                PlayerInventory inventory = player.getInventory();
                inventory.setHelmet(helmet);
                inventory.setChestplate(chestplate);
                inventory.setLeggings(leggings);
                inventory.setBoots(boots);
            }
        }
    }

    private ArmorSet getArmor(Player player) {
        Iterator var2 = this.armorSets.values().iterator();

        ArmorSet armorSet;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            armorSet = (ArmorSet)var2.next();
        } while(!player.hasPermission(armorSet.getPermission()));

        return armorSet;
    }

    private ItemStack createArmor(Material material, Color color) {
        if (material == null) {
            return null;
        } else {
            ItemStack armorPiece = new ItemStack(material);
            ItemMeta originalMeta = armorPiece.getItemMeta();
            if (originalMeta instanceof LeatherArmorMeta) {
                LeatherArmorMeta meta = (LeatherArmorMeta)originalMeta;
                if (color != null) {
                    meta.setColor(color);
                }

                armorPiece.setItemMeta(meta);
            }

            return armorPiece;
        }
    }
}