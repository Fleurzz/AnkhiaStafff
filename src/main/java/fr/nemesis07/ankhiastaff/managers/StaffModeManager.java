package fr.nemesis07.ankhiastaff.managers;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.hotbar.Hotbar;
import fr.nemesis07.ankhiastaff.hotbar.HotbarManager;
import fr.nemesis07.ankhiastaff.hotbar.components.StaffHotbar;
import fr.nemesis07.ankhiastaff.modernlib.config.ConfigWrapper;
import fr.nemesis07.ankhiastaff.modernlib.utils.Players;
import fr.nemesis07.ankhiastaff.modernlib.utils.PotionEffects;
import fr.nemesis07.ankhiastaff.player.StaffPlayer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class StaffModeManager {
    private Collection<Player> staffPlayers = new HashSet();

    public void toggleStaff(Player player) {
        if (this.isStaff(player)) {
            this.removeStaff(player);
        } else {
            this.addStaff(player);
        }
    }

    public void applyPotionEffects(Player player) {
        ConfigWrapper cfg = AnkhiaStaff.getInstance().getCfg();
        ConfigurationSection effectsSection = cfg.getConfigurationSection("staffmode.effects");
        if (effectsSection != null) {
            Iterator var4 = effectsSection.getKeys(false).iterator();

            while(var4.hasNext()) {
                String effectName = (String)var4.next();
                ConfigurationSection effectSection = effectsSection.getConfigurationSection(effectName);
                if (effectSection != null) {
                    int amplifier = effectSection.getInt("amplifier", 1);
                    int duration = effectSection.getInt("duration", Integer.MAX_VALUE);
                    List<String> aliases = effectSection.getStringList("aliases");
                    String[] effects = (String[])aliases.toArray(new String[0]);
                    PotionEffects.add(player, amplifier, duration, effects);
                } else {
                    AnkhiaStaff.getInstance().getLogger().warning("Invalid configuration section for effect: " + effectName);
                }
            }
        } else {
            AnkhiaStaff.getInstance().getLogger().warning("No potion effects found in the configuration.");
        }

    }

    public void removePotionEffects(Player player) {
        ConfigWrapper cfg = AnkhiaStaff.getInstance().getCfg();
        ConfigurationSection effectsSection = cfg.getConfigurationSection("staffmode.effects");
        if (effectsSection != null) {
            Iterator var4 = effectsSection.getKeys(false).iterator();

            while(var4.hasNext()) {
                String effectName = (String)var4.next();
                ConfigurationSection effectSection = effectsSection.getConfigurationSection(effectName);
                if (effectSection != null) {
                    List<String> aliases = effectSection.getStringList("aliases");
                    String[] effects = (String[])aliases.toArray(new String[0]);
                    PotionEffects.remove(player, effects);
                } else {
                    AnkhiaStaff.getInstance().getLogger().warning("Invalid configuration section for effect: " + effectName);
                }
            }
        } else {
            AnkhiaStaff.getInstance().getLogger().warning("No potion effects found in the configuration.");
        }

    }

    public void addStaff(Player player) {
        if (!this.isStaff(player)) {
            HotbarManager hotbarManager = AnkhiaStaff.getInstance().getHotbarManager();
            StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
            if (staffPlayer != null) {
                staffPlayer.setRestoreLocation(player.getLocation());
                staffPlayer.setRestoreGameMode(player.getGameMode());
                staffPlayer.setRestoreStaffChat(staffPlayer.isStaffChat());
                if (AnkhiaStaff.getInstance().getCfg().getBoolean("vanish.enabled") && AnkhiaStaff.getInstance().getCfg().getBoolean("vanish.on_staff_mode")) {
                    staffPlayer.makeInvisible();
                }

                if (AnkhiaStaff.getInstance().getCfg().getBoolean("staffchat.enabled") && AnkhiaStaff.getInstance().getCfg().getBoolean("staffchat.on_staff_mode")) {
                    staffPlayer.setStaffChat(true);
                }
            }

            AnkhiaStaff.getInstance().getInventoryManager().savePlayerInventory(player);
            Players.clearInventory(player);
            Players.heal(player);
            if (AnkhiaStaff.getInstance().getCfg().getBoolean("gamemode.enabled")) {
                Players.setGameMode(player, AnkhiaStaff.getInstance().getCfg().getString("gamemode.mode"));
            }

            hotbarManager.setHotbar(player, new StaffHotbar(staffPlayer));
            Players.setFlying(player, true);
            player.sendMessage(AnkhiaStaff.getInstance().getMessage("staffmode.enter", new String[0]));
            this.staffPlayers.add(player);
            this.applyPotionEffects(player);
            AnkhiaStaff.getInstance().getArmorManager().giveArmor(player);
        }
    }

    public void removeStaff(Player player) {
        if (this.isStaff(player)) {
            HotbarManager hotbarManager = AnkhiaStaff.getInstance().getHotbarManager();
            hotbarManager.setHotbar(player, (Hotbar)null);
            Players.clearInventory(player);
            AnkhiaStaff.getInstance().getInventoryManager().loadPlayerInventory(player);
            AnkhiaStaff.getInstance().getInventoryManager().deletePlayerInventory(player);
            player.setFallDistance(0.0F);
            player.sendMessage(AnkhiaStaff.getInstance().getMessage("staffmode.leave", new String[0]));
            this.staffPlayers.remove(player);
            this.removePotionEffects(player);
            StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
            if (staffPlayer != null) {
                if (AnkhiaStaff.getInstance().getCfg().getBoolean("staffmode.restore_location")) {
                    staffPlayer.restoreLocation();
                }

                if (AnkhiaStaff.getInstance().getCfg().getBoolean("gamemode.enabled")) {
                    staffPlayer.restoreGameMode();
                }

                staffPlayer.restoreStaffChat();
                if (AnkhiaStaff.getInstance().getCfg().getBoolean("vanish.enabled") && AnkhiaStaff.getInstance().getCfg().getBoolean("vanish.on_staff_mode")) {
                    staffPlayer.makeVisible();
                }
            }

        }
    }

    public boolean isStaff(Player player) {
        return this.staffPlayers.contains(player);
    }

    public Collection<Player> getStaffPlayers() {
        return this.staffPlayers;
    }
}