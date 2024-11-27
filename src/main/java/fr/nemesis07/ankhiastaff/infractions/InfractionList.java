package fr.nemesis07.ankhiastaff.infractions;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.configuration.ConfigurationSection;

public class InfractionList {
    private List<Infraction> infractions = new ArrayList();

    public List<Infraction> getInfractions() {
        return this.infractions;
    }

    public void addInfraction(Infraction infraction) {
        this.infractions.add(infraction);
    }

    public void setInfractions(List<Infraction> infractions) {
        this.infractions.clear();
        this.infractions.addAll(infractions);
    }

    public void load(ConfigurationSection infractionsSection) {
        this.infractions.clear();
        if (infractionsSection != null) {
            infractionsSection.getKeys(false).forEach((id) -> {
                try {
                    ConfigurationSection infractionSection = infractionsSection.getConfigurationSection(id);
                    if (infractionSection != null) {
                        String timestamp = infractionSection.getString("timestamp");
                        String reporterUUID = infractionSection.getString("reporter_uuid");
                        String reason = infractionSection.getString("reason");
                        String accusedUUID = infractionSection.getString("accused_uuid");
                        String type = infractionSection.getString("type");
                        this.addInfraction(new Infraction(id, timestamp, reporterUUID, reason, UUID.fromString(accusedUUID), UUID.fromString(reporterUUID), InfractionType.valueOf(type)));
                    }
                } catch (Exception var9) {
                    AnkhiaStaff.getInstance().getLogger().severe("Error while loading infraction from config");
                    var9.printStackTrace();
                }

            });
        }

    }

    public int count() {
        return this.infractions.size();
    }

    public String getLast() {
        return this.infractions.isEmpty() ? "No infractions" : ((Infraction)this.infractions.get(this.infractions.size() - 1)).getReason();
    }
}