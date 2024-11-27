package fr.nemesis07.ankhiastaff.player;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.infractions.Infraction;
import fr.nemesis07.ankhiastaff.infractions.InfractionList;
import fr.nemesis07.ankhiastaff.infractions.InfractionType;
import fr.nemesis07.ankhiastaff.menus.items.PlayerNotesItem;
import fr.nemesis07.ankhiastaff.modernlib.config.ConfigWrapper;
import fr.nemesis07.ankhiastaff.modernlib.utils.DateUtils;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class StaffPlayer extends UUIDPlayer {
    private InfractionList warnings = new InfractionList();
    private InfractionList reports = new InfractionList();
    private StaffNotes notes = new StaffNotes();
    private WarningProcess warningProcess = new WarningProcess();
    private Location restoreLocation = null;
    private GameMode restoreGameMode = null;
    private boolean restoreStaffChat = false;
    private StaffPlayerLoader staffPlayerLoader;
    private VanishPlayer vanishPlayer;
    private FreezablePlayer freezablePlayer;
    private boolean staffChat = false;
    private String ip = null;
    private boolean viewJoins = false;

    public StaffPlayerLoader getStaffPlayerLoader() {
        return this.staffPlayerLoader;
    }

    public VanishPlayer getVanishPlayer() {
        return this.vanishPlayer;
    }

    public FreezablePlayer getFreezablePlayer() {
        return this.freezablePlayer;
    }

    public StaffPlayer(UUID uuid, ConfigWrapper infractionsConfig, ConfigWrapper ipsConfig) {
        super(uuid);
        this.staffPlayerLoader = new StaffPlayerLoader(this, infractionsConfig, ipsConfig);
        this.vanishPlayer = new VanishPlayer(uuid);
        this.freezablePlayer = new FreezablePlayer(uuid);
    }

    public Location getRestoreLocation() {
        return this.restoreLocation;
    }

    public void setRestoreLocation(Location restoreLocation) {
        this.restoreLocation = restoreLocation;
    }

    public void restoreLocation() {
        Player player = this.getPlayer();
        if (this.restoreLocation != null) {
            player.teleport(this.restoreLocation);
        }
    }

    public GameMode getRestoreGameMode() {
        return this.restoreGameMode;
    }

    public void setRestoreGameMode(GameMode gameMode) {
        this.restoreGameMode = gameMode;
    }

    public void restoreGameMode() {
        Player player = this.getPlayer();
        if (this.restoreGameMode != null) {
            player.setGameMode(this.restoreGameMode);
        }
    }

    public void setRestoreStaffChat(boolean staffChat) {
        this.restoreStaffChat = staffChat;
    }

    public void restoreStaffChat() {
        if (this.staffChat) {
            this.setStaffChat(this.restoreStaffChat);
        }
    }

    public void toggleVanish() {
        this.vanishPlayer.toggleVanish();
    }

    public StaffPlayer load() {
        this.staffPlayerLoader.load();
        return this;
    }

    public void infraction(InfractionType type, StaffPlayer reporter, String reason) {
        String timestamp = DateUtils.getCurrentTimestamp();
        Infraction infraction = new Infraction(timestamp, reporter.getName(), reason, this.getUUID(), reporter.getUUID(), type);
        Iterator var6;
        StaffPlayer staffPlayer;
        if (type == InfractionType.WARNING) {
            this.warnings.addInfraction(infraction);
            this.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.warning.receive-self", new String[]{"{staff}", reporter.getName(), "{player}", this.getName(), "{reason}", reason}));
            var6 = AnkhiaStaff.getInstance().getStaffPlayerManager().getStaffPlayers().values().iterator();

            while(var6.hasNext()) {
                staffPlayer = (StaffPlayer)var6.next();
                if (staffPlayer.hasPermission("staffmodex.warning.receive")) {
                    staffPlayer.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.warning.receive", new String[]{"{staff}", reporter.getName(), "{player}", this.getName(), "{reason}", reason}));
                }
            }
        } else if (type == InfractionType.REPORT) {
            this.reports.addInfraction(infraction);
            var6 = AnkhiaStaff.getInstance().getStaffPlayerManager().getStaffPlayers().values().iterator();

            while(var6.hasNext()) {
                staffPlayer = (StaffPlayer)var6.next();
                if (staffPlayer.hasPermission("staffmodex.report.receive")) {
                    staffPlayer.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.report.receive", new String[]{"{staff}", reporter.getName(), "{player}", this.getName(), "{reason}", reason}));
                }
            }
        }

        this.staffPlayerLoader.save(infraction);
    }

    public InfractionList getWarnings() {
        return this.warnings;
    }

    public InfractionList getReports() {
        return this.reports;
    }

    public boolean isWritingNote() {
        return this.notes.isWritingNote();
    }

    public StaffNote writeNote(String message) {
        return this.notes.writeNote(message);
    }

    public void startWritingNote(PlayerNotesItem item, UUID uuid, String name) {
        this.notes.startWritingNote(item, uuid, name);
    }

    public String getNotes(UUID uniqueId) {
        return this.notes.getNote(uniqueId);
    }

    public void openWriteMenu(Player player, String notes) {
        this.notes.openWriteMenu(player, notes);
    }

    public WarningProcess getWarningProcess() {
        return this.warningProcess;
    }

    public void makeVisible() {
        this.vanishPlayer.makeVisible();
    }

    public void makeInvisible() {
        this.vanishPlayer.makeInvisible();
    }

    public boolean isVanished() {
        return this.vanishPlayer.isVanished();
    }

    public boolean isFrozen() {
        return this.freezablePlayer.isFrozen();
    }

    public void toggleFreeze(Player player) {
        this.freezablePlayer.toggleFreeze(player);
    }

    public FreezeStatus getFreezeStatus() {
        return this.freezablePlayer.getFreezeStatus();
    }

    public Collection<FreezeStatus> getFrozenPlayersByMe() {
        return this.freezablePlayer.getFrozenPlayersByMe();
    }

    public FreezablePlayer getWhoFroze() {
        return this.freezablePlayer.getWhoFroze();
    }

    public void unfreeze() {
        this.freezablePlayer.unfreeze();
    }

    public void freeze(StaffPlayer origin) {
        this.freezablePlayer.freeze(origin.getFreezablePlayer());
    }

    public void preventMovement(PlayerMoveEvent event) {
        this.freezablePlayer.preventMovement(event);
    }

    public boolean sendFreezeChat(String message) {
        return this.freezablePlayer.sendFreezeChat(message);
    }

    public boolean isStaffChat() {
        return this.staffChat;
    }

    public void setStaffChat(boolean staffChat) {
        this.staffChat = staffChat;
    }

    public boolean isStaffChatReceiver() {
        Player player = this.getPlayer();
        return player != null && player.hasPermission("staffmodex.staffchat");
    }

    public void sendStaffChat(String msg) {
        String message = PlaceholderAPI.setPlaceholders(this.getPlayer(),
                AnkhiaStaff.getInstance().getMsg().getText("messages.staffchat.chat",
                        new String[]{"{player}", this.getPlayer().getName(), "{server}", AnkhiaStaff.getInstance().getServerName()})
        ).replace("{message}", msg);
        AnkhiaStaff.getInstance().getStaffPlayerManager().sendStaffChat(message);
    }

    public void toggleStaffChat() {
        this.staffChat = !this.staffChat;
        ConfigWrapper msg = AnkhiaStaff.getInstance().getMsg();
        String toggleMessage = this.staffChat ? msg.getText("messages.staffchat.enabled") : msg.getText("messages.staffchat.disabled");
        this.sendMessage(toggleMessage);
    }

    public boolean hasPermission(String permission) {
        Player player = this.getPlayer();
        return player != null && player.hasPermission(permission);
    }

    public void hidePlayer(boolean force, Player player) {
        this.vanishPlayer.hidePlayer(force, player);
    }

    public boolean isForceVanish() {
        return this.vanishPlayer.isForceVanish();
    }

    public void setIP(String ip) {
        this.ip = ip;
    }

    public String getIP() {
        return this.ip;
    }

    public void setViewJoins(boolean viewJoins) {
        this.viewJoins = viewJoins;
    }

    public boolean isViewJoins() {
        return this.viewJoins;
    }
}