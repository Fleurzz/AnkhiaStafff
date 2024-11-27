package fr.nemesis07.ankhiastaff.inventories;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryManager {
    private File inventoryFile;
    private FileConfiguration inventoryConfig;

    public InventoryManager(AnkhiaStaff plugin, String fileName) {
        this.inventoryFile = new File(plugin.getDataFolder(), fileName);
        if (!this.inventoryFile.exists()) {
            try {
                if (!this.inventoryFile.getParentFile().exists()) {
                    this.inventoryFile.getParentFile().mkdirs();
                }

                this.inventoryFile.createNewFile();
            } catch (IOException var4) {
                var4.printStackTrace();
            }
        }

        this.inventoryConfig = YamlConfiguration.loadConfiguration(this.inventoryFile);
    }

    public void savePlayerInventory(Player player) {
        if (player != null) {
            this.inventoryConfig.createSection(player.getUniqueId().toString() + ".inventory");
            PlayerInventory inventory = player.getInventory();

            for(int i = 0; i < inventory.getSize(); ++i) {
                ItemStack item = inventory.getItem(i);
                if (item != null) {
                    this.inventoryConfig.set(player.getUniqueId().toString() + ".inventory." + i, item);
                }
            }

            ItemStack[] armorContents = inventory.getArmorContents();

            for(int i = 0; i < armorContents.length; ++i) {
                ItemStack item = armorContents[i];
                if (item != null) {
                    this.inventoryConfig.set(player.getUniqueId().toString() + ".armor." + i, item);
                }
            }

            this.inventoryConfig.set(player.getUniqueId().toString() + ".health", player.getHealth());
            this.inventoryConfig.set(player.getUniqueId().toString() + ".food", player.getFoodLevel());
            this.inventoryConfig.set(player.getUniqueId().toString() + ".flying", player.isFlying());
            this.inventoryConfig.set(player.getUniqueId().toString() + ".allow-flight", player.getAllowFlight());

            try {
                this.inventoryConfig.save(this.inventoryFile);
            } catch (IOException var6) {
                var6.printStackTrace();
            }

        }
    }

    public void loadPlayerInventory(Player player) {
        if (player != null) {
            String uuidString = player.getUniqueId().toString();
            if (this.inventoryConfig.contains(uuidString + ".inventory")) {
                PlayerInventory inventory = player.getInventory();
                inventory.clear();
                ConfigurationSection invSection = this.inventoryConfig.getConfigurationSection(uuidString + ".inventory");
                if (invSection != null) {
                    Iterator var5 = invSection.getKeys(false).iterator();

                    while(var5.hasNext()) {
                        String key = (String)var5.next();
                        int slot = Integer.parseInt(key);
                        ItemStack value = invSection.getItemStack(key);
                        inventory.setItem(slot, value);
                    }
                }

                ConfigurationSection armorSection = this.inventoryConfig.getConfigurationSection(uuidString + ".armor");
                if (armorSection != null) {
                    ItemStack[] armorContents = inventory.getArmorContents();
                    Iterator var14 = armorSection.getKeys(false).iterator();

                    while(var14.hasNext()) {
                        String key = (String)var14.next();

                        try {
                            int slot = Integer.parseInt(key);
                            ItemStack value = armorSection.getItemStack(key);
                            armorContents[slot] = value;
                        } catch (Exception var11) {
                        }
                    }

                    inventory.setArmorContents(armorContents);
                }

                player.setHealth(Math.min(player.getMaxHealth(), this.inventoryConfig.getDouble(uuidString + ".health", player.getHealth())));
                player.setFoodLevel(this.inventoryConfig.getInt(uuidString + ".food", player.getFoodLevel()));
                player.setAllowFlight(this.inventoryConfig.getBoolean(uuidString + ".allow-flight", player.getAllowFlight()));
                player.setFlying(this.inventoryConfig.getBoolean(uuidString + ".flying", player.isFlying()));
            }
        }
    }

    public void deletePlayerInventory(Player player) {
        if (player != null) {
            this.inventoryConfig.set(player.getUniqueId().toString(), (Object)null);

            try {
                this.inventoryConfig.save(this.inventoryFile);
            } catch (IOException var3) {
                var3.printStackTrace();
            }

        }
    }
}
