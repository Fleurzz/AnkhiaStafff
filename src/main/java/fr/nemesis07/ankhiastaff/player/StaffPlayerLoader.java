package fr.nemesis07.ankhiastaff.player;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.infractions.Infraction;
import fr.nemesis07.ankhiastaff.managers.DatabaseManager;
import fr.nemesis07.ankhiastaff.modernlib.config.ConfigWrapper;
import org.bukkit.configuration.ConfigurationSection;

public class StaffPlayerLoader {
    private StaffPlayer staffPlayer;
    private ConfigWrapper infractionsConfig;
    private ConfigWrapper ipsConfig;

    public StaffPlayerLoader(StaffPlayer staffPlayer, ConfigWrapper infractionsConfig, ConfigWrapper ipsConfig) {
        this.staffPlayer = staffPlayer;
        this.infractionsConfig = infractionsConfig;
        this.ipsConfig = ipsConfig;
    }

    public void load() {
        DatabaseManager mySQLManager = AnkhiaStaff.getInstance().getMySQLManager();
        if (mySQLManager.isInitializedSuccessfully()) {
            mySQLManager.loadInfractions(this.staffPlayer);
        } else {
            this.infractionsConfig.load();
            ConfigurationSection warningsSection = this.infractionsConfig.getConfig().getConfigurationSection("warnings");
            if (warningsSection != null) {
                this.staffPlayer.getWarnings().load(warningsSection);
            }

            ConfigurationSection reportsSection = this.infractionsConfig.getConfig().getConfigurationSection("reports");
            if (reportsSection != null) {
                this.staffPlayer.getReports().load(reportsSection);
            }

        }
    }

    public void save(Infraction infraction) {
        DatabaseManager mySQLManager = AnkhiaStaff.getInstance().getMySQLManager();
        if (mySQLManager.isInitializedSuccessfully()) {
            mySQLManager.saveInfraction(infraction);
        } else {
            infraction.save(this.infractionsConfig.getConfig());
            this.infractionsConfig.save();
        }
    }

    public void saveIP() {
        if (AnkhiaStaff.getInstance().getCfg().getBoolean("ip.enabled")) {
            DatabaseManager mySQLManager = AnkhiaStaff.getInstance().getMySQLManager();
            String ip = this.staffPlayer.getIP();
            if (mySQLManager.isInitializedSuccessfully()) {
                mySQLManager.saveIP(this.staffPlayer.getUUID(), ip);
            } else {
                this.ipsConfig.load();
                this.ipsConfig.getConfig().set("ip", ip);
                this.ipsConfig.save();
            }
        }
    }

    public void loadIP() {
        if (AnkhiaStaff.getInstance().getCfg().getBoolean("ip.enabled")) {
            DatabaseManager mySQLManager = AnkhiaStaff.getInstance().getMySQLManager();
            if (mySQLManager.isInitializedSuccessfully()) {
                mySQLManager.loadIP(this.staffPlayer);
            } else {
                this.ipsConfig.load();
                this.staffPlayer.setIP(this.ipsConfig.getConfig().getString("ip"));
            }
        }
    }
}