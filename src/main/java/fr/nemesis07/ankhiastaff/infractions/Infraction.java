package fr.nemesis07.ankhiastaff.infractions;

import java.util.UUID;
import org.bukkit.configuration.ConfigurationSection;

public class Infraction {
    private UUID id;
    private String timestamp;
    private String reporterName;
    private String reason;
    private UUID accusedUUID;
    private UUID reporterUUID;
    private InfractionType type;

    public Infraction(UUID id, String timestamp, String reporterName, String reason, UUID accusedUUID, UUID reporterUUID, InfractionType type) {
        this.id = id;
        this.timestamp = timestamp;
        this.reporterName = reporterName;
        this.reason = reason;
        this.accusedUUID = accusedUUID;
        this.reporterUUID = reporterUUID;
        this.type = type;
    }

    public Infraction(String id, String timestamp, String reporterName, String reason, UUID accusedUUID, UUID reporterUUID, InfractionType type) {
        this(UUID.fromString(id), timestamp, reporterName, reason, accusedUUID, reporterUUID, type);
    }

    public Infraction(String timestamp, String reporterName, String reason, UUID accusedUUID, UUID reporterUUID, InfractionType type) {
        this(UUID.randomUUID(), timestamp, reporterName, reason, accusedUUID, reporterUUID, type);
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public String getReporterName() {
        return this.reporterName;
    }

    public String getReason() {
        return this.reason;
    }

    public UUID getId() {
        return this.id;
    }

    public void save(ConfigurationSection config) {
        String path = this.type == InfractionType.REPORT ? "reports" : "warnings." + this.id.toString() + ".";
        config.set(path + "timestamp", this.timestamp);
        config.set(path + "reporter_uuid", this.reporterUUID.toString());
        config.set(path + "reason", this.reason);
        config.set(path + "accused_uuid", this.accusedUUID.toString());
        config.set(path + "type", this.type.name());
    }

    public UUID getAccusedUUID() {
        return this.accusedUUID;
    }

    public UUID getReporterUUID() {
        return this.reporterUUID;
    }

    public InfractionType getType() {
        return this.type;
    }
}
