package fr.nemesis07.ankhiastaff;

import fr.nemesis07.ankhiastaff.armor.ArmorManager;
import fr.nemesis07.ankhiastaff.commands.ExamineCommand;
import fr.nemesis07.ankhiastaff.commands.FreezeCommand;
import fr.nemesis07.ankhiastaff.commands.HelpopCommand;
import fr.nemesis07.ankhiastaff.commands.IPCommand;
import fr.nemesis07.ankhiastaff.commands.InfractionsCommand;
import fr.nemesis07.ankhiastaff.commands.ReportCommand;
import fr.nemesis07.ankhiastaff.commands.StaffChatCommand;
import fr.nemesis07.ankhiastaff.commands.StaffModeCommand;
import fr.nemesis07.ankhiastaff.commands.StaffModeXCommand;
import fr.nemesis07.ankhiastaff.commands.VanishCommand;
import fr.nemesis07.ankhiastaff.commands.WarnCommand;
import fr.nemesis07.ankhiastaff.expansion.StaffModePlaceholderExpansion;
import fr.nemesis07.ankhiastaff.hotbar.HotbarManager;
import fr.nemesis07.ankhiastaff.inventories.InventoryManager;
import fr.nemesis07.ankhiastaff.listeners.BlockListeners;
import fr.nemesis07.ankhiastaff.listeners.EntityListeners;
import fr.nemesis07.ankhiastaff.listeners.InventoryListeners;
import fr.nemesis07.ankhiastaff.listeners.PlayerListeners;
import fr.nemesis07.ankhiastaff.managers.DatabaseManager;
import fr.nemesis07.ankhiastaff.managers.StaffModeManager;
import fr.nemesis07.ankhiastaff.managers.VeinManager;
import fr.nemesis07.ankhiastaff.modernlib.commands.ModernCommand;
import fr.nemesis07.ankhiastaff.modernlib.config.ConfigWrapper;
import fr.nemesis07.ankhiastaff.modernlib.menus.listeners.MenuListener;
import fr.nemesis07.ankhiastaff.player.StaffPlayer;
import fr.nemesis07.ankhiastaff.player.StaffPlayerManager;
import fr.nemesis07.ankhiastaff.tasks.StaffActionBarTask;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AnkhiaStaff extends JavaPlugin {
    private HotbarManager hotbarManager = new HotbarManager();
    private InventoryManager inventoryManager = new InventoryManager(this, "inventories.yml");
    private StaffModeManager staffModeManager = new StaffModeManager();
    private StaffPlayerManager staffPlayerManager = new StaffPlayerManager();
    private DatabaseManager mySQLManager;
    private ConfigWrapper config;
    private ConfigWrapper messages;
    private Collection<ModernCommand> commands = new HashSet();
    private ArmorManager armorManager;
    private Collection<UUID> visiblePlayers = new HashSet();
    private static AnkhiaStaff instance;

    public ConfigWrapper getCfg() {
        return this.config;
    }

    public ConfigWrapper getMsg() {
        return this.messages;
    }

    public HotbarManager getHotbarManager() {
        return this.hotbarManager;
    }

    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }

    public StaffModeManager getStaffModeManager() {
        return this.staffModeManager;
    }

    public StaffPlayerManager getStaffPlayerManager() {
        return this.staffPlayerManager;
    }

    public ArmorManager getArmorManager() {
        return this.armorManager;
    }

    public void onEnable() {
        setInstance(this);
        this.reloadConfig();
        this.config = (new ConfigWrapper("config.yml")).saveDefault().load();
        this.messages = (new ConfigWrapper("messages.yml")).saveDefault().load();
        this.armorManager = new ArmorManager(this.config);
        this.mySQLManager = new DatabaseManager(this.config.getBoolean("mysql.enabled"), this.config.getString("mysql.url"), this.config.getString("mysql.username"), this.config.getString("mysql.password"));
        VeinManager veinManager = new VeinManager();
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new BlockListeners(veinManager), this);
        pluginManager.registerEvents(new EntityListeners(), this);
        pluginManager.registerEvents(new InventoryListeners(), this);
        pluginManager.registerEvents(new PlayerListeners(), this);
        pluginManager.registerEvents(new MenuListener(), this);
        if (getInstance().getCfg().getBoolean("examine.enabled")) {
            this.registerCommand(new ExamineCommand());
        }

        if (getInstance().getCfg().getBoolean("freeze.enabled")) {
            this.registerCommand(new FreezeCommand());
        }

        if (getInstance().getCfg().getBoolean("helpop.enabled")) {
            this.registerCommand(new HelpopCommand());
        }

        if (getInstance().getCfg().getBoolean("report.enabled") || getInstance().getCfg().getBoolean("warning.enabled")) {
            this.registerCommand(new InfractionsCommand());
        }

        if (getInstance().getCfg().getBoolean("ip.enabled")) {
            this.registerCommand(new IPCommand());
        }

        if (getInstance().getCfg().getBoolean("report.enabled")) {
            this.registerCommand(new ReportCommand());
        }

        if (getInstance().getCfg().getBoolean("staffchat.enabled")) {
            this.registerCommand(new StaffChatCommand());
        }

        if (getInstance().getCfg().getBoolean("staffmode.enabled")) {
            this.registerCommand(new StaffModeCommand());
        }

        this.registerCommand(new StaffModeXCommand());
        if (getInstance().getCfg().getBoolean("warning.enabled")) {
            this.registerCommand(new WarnCommand());
        }

        if (getInstance().getCfg().getBoolean("vanish.enabled")) {
            this.registerCommand(new VanishCommand());
        }

        if (getInstance().getCfg().getBoolean("action_bar.enabled")) {
            (new StaffActionBarTask()).register();
        }

        if (pluginManager.getPlugin("PlaceholderAPI") != null) {
            (new StaffModePlaceholderExpansion()).register();
        }

        this.getServer().getScheduler().runTaskAsynchronously(this, () -> {
            Iterator var1 = this.getServer().getOnlinePlayers().iterator();

            while(var1.hasNext()) {
                Player player = (Player)var1.next();
                this.getStaffPlayerManager().getOrCreateStaffPlayer(player).load();
            }

        });
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
        Iterator var1 = this.getServer().getOnlinePlayers().iterator();

        while(var1.hasNext()) {
            Player player = (Player)var1.next();
            StaffPlayer staffPlayer = this.getStaffPlayerManager().getStaffPlayer(player);
            if (staffPlayer != null) {
                staffPlayer.unfreeze();
            }

            this.getStaffModeManager().removeStaff(player);
            player.closeInventory();
        }

        this.unregisterCommands();
    }

    public void registerCommand(ModernCommand command) {
        this.commands.add(command);
        command.register();
    }

    public void unregisterCommands() {
        Iterator var1 = this.commands.iterator();

        while(var1.hasNext()) {
            ModernCommand command = (ModernCommand)var1.next();
            command.unregisterBukkitCommand();
        }

    }

    public static void setInstance(AnkhiaStaff instance) {
        AnkhiaStaff.instance = instance;
    }

    public static AnkhiaStaff getInstance() {
        return instance;
    }

    public DatabaseManager getMySQLManager() {
        return this.mySQLManager;
    }

    public void sendMessageToStaffPlayers(String message, UUID playerToSkip) {
        Iterator var3 = getInstance().getStaffPlayerManager().getStaffPlayers().values().iterator();

        while(var3.hasNext()) {
            StaffPlayer staffPlayer = (StaffPlayer)var3.next();
            if (staffPlayer.isStaffChatReceiver() && !staffPlayer.getUUID().equals(playerToSkip)) {
                staffPlayer.sendMessage(message);
            }
        }

    }

    public String getServerName() {
        return getInstance().getCfg().getString("server_name");
    }

    public int getVisiblePlayerCount() {
        return this.visiblePlayers.size();
    }

    public void setVisible(Player player, boolean visible) {
        if (visible) {
            this.visiblePlayers.add(player.getUniqueId());
        } else {
            this.visiblePlayers.remove(player.getUniqueId());
        }

    }

    public String getMessage(String path, String... placeholders) {
        return this.getMsg().getText("messages.prefix") + this.getMsg().getText(path, placeholders);
    }
}
