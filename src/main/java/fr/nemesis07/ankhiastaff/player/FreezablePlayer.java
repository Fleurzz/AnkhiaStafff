package fr.nemesis07.ankhiastaff.player;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.modernlib.config.ConfigWrapper;
import fr.nemesis07.ankhiastaff.modernlib.utils.PotionEffects;
import fr.nemesis07.ankhiastaff.modernlib.utils.Sounds;
import fr.nemesis07.ankhiastaff.modernlib.utils.Titles;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class FreezablePlayer extends UUIDPlayer {
    private FreezeStatus freezeStatus = null;
    private Collection<FreezeStatus> frozenPlayersByMe = new HashSet();

    public FreezablePlayer(UUID uuid) {
        super(uuid);
    }

    public void toggleFreeze(Player player) {
        Player target = this.getPlayer();
        ConfigWrapper msg = AnkhiaStaff.getInstance().getMsg();
        if (!player.hasPermission("staffmodex.freeze")) {
            player.sendMessage(msg.getText("messages.freeze.no-permission"));
        } else if (target != null && target.hasPermission("staffmodex.freeze.bypass")) {
            player.sendMessage(msg.getText("messages.freeze.has_bypass"));
        } else if (!AnkhiaStaff.getInstance().getStaffModeManager().isStaff(player)) {
            player.sendMessage(msg.getText("messages.freeze.not-staff"));
        } else if (this.isFrozen()) {
            player.sendMessage(msg.getText("messages.freeze.unfreeze"));
            Sounds.play(player, 1.0F, 1.0F, new String[]{"ENDERMAN_TELEPORT", "ENTITY_ENDERMAN_TELEPORT"});
            this.unfreeze();
        } else {
            player.sendMessage(msg.getText("messages.freeze.freeze"));
            Sounds.play(player, 1.0F, 1.0F, new String[]{"SUCCESSFUL_HIT", "ENTITY_ARROW_HIT_PLAYER"});
            this.freeze(AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player).getFreezablePlayer());
        }

    }

    public FreezeStatus getFreezeStatus() {
        return this.freezeStatus;
    }

    public boolean isFrozen() {
        return this.freezeStatus != null;
    }

    public void preventMovement(PlayerMoveEvent event) {
        if (this.isFrozen()) {
            Location to = event.getTo();
            Location newTo = event.getFrom();
            float pitch = to.getPitch();
            float yaw = to.getYaw();
            newTo.setPitch(pitch);
            newTo.setYaw(yaw);
            event.setTo(newTo);
        }

    }

    public void freeze(FreezablePlayer origin) {
        if (this.freezeStatus != null) {
            this.unfreeze();
        } else {
            Player player = this.getPlayer();
            this.freezeStatus = new FreezeStatus(origin, this);
            origin.addFrozenPlayerByMe(this);
            Player originPlayer = origin.getPlayer();
            ConfigWrapper msg = AnkhiaStaff.getInstance().getMsg();
            if (player != null) {
                player.sendMessage(msg.getText("messages.freeze.frozen", new String[]{"{player}", originPlayer == null ? "" : originPlayer.getName(), "{time}", String.valueOf(AnkhiaStaff.getInstance().getCfg().getInt("freeze.time"))}));
                Titles.sendActionBar(player, msg.getText("messages.freeze.frozen_action"));
                Titles.sendTitle(player, AnkhiaStaff.getInstance().getMsg().getText("messages.freeze.frozen_title"), msg.getText("messages.freeze.frozen_subtitle"), 20, 60, 20);
                this.freezeStatus.setHelmet(player.getEquipment().getHelmet());
                PotionEffects.add(player, 0, 24000, new String[]{"BLINDNESS"});
                Sounds.play(player, 1.0F, 1.0F, new String[]{"ANVIL_LAND", "BLOCK_ANVIL_LAND"});
                player.getEquipment().setHelmet(new ItemStack(Material.PACKED_ICE));
            }

        }
    }

    public void unfreeze() {
        if (this.freezeStatus != null) {
            Player player = this.getPlayer();
            this.freezeStatus.getStaff().removeFrozenPlayerByMe(this);
            if (player != null) {
                ConfigWrapper msg = AnkhiaStaff.getInstance().getMsg();
                player.sendMessage(msg.getText("messages.freeze.unfrozen"));
                Titles.sendActionBar(player, msg.getText("messages.freeze.unfrozen_action"));
                Titles.sendTitle(player, msg.getText("messages.freeze.unfrozen_title"), msg.getText("messages.freeze.unfrozen_subtitle"), 20, 60, 20);
                player.getEquipment().setHelmet(this.freezeStatus.getHelmet());
                PotionEffects.remove(player, new String[]{"BLINDNESS"});
                Sounds.play(player, 1.0F, 1.0F, new String[]{"SUCCESSFUL_HIT", "ENTITY_ARROW_HIT_PLAYER"});
            }

            this.freezeStatus = null;
        }
    }

    public void addFrozenPlayerByMe(FreezablePlayer player) {
        this.frozenPlayersByMe.add(player.getFreezeStatus());
    }

    public void removeFrozenPlayerByMe(FreezablePlayer player) {
        this.frozenPlayersByMe.remove(player.getFreezeStatus());
    }

    public Collection<FreezeStatus> getFrozenPlayersByMe() {
        return this.frozenPlayersByMe;
    }

    public boolean sendFreezeChat(String message) {
        Player player = this.getPlayer();
        if (this.isFrozen()) {
            this.sendFrozenChatMessage(player, message);
            return true;
        } else if (!this.getFrozenPlayersByMe().isEmpty()) {
            this.sendStaffChatMessage(player, message);
            return true;
        } else {
            return false;
        }
    }

    private void sendFrozenChatMessage(Player player, String msg) {
        String message = AnkhiaStaff.getInstance().getMessage("messages.freeze.frozen_msg", new String[]{"{player}", player.getName(), "{message}", msg});
        FreezablePlayer whoFroze = this.getWhoFroze();
        whoFroze.sendMessage(message);
        player.sendMessage(message);
        AnkhiaStaff.getInstance().sendMessageToStaffPlayers(message, whoFroze.getUUID());
    }

    private void sendStaffChatMessage(Player player, String msg) {
        String message = AnkhiaStaff.getInstance().getMessage("messages.freeze.staff_msg", new String[]{"{player}", player.getName(), "{message}", msg});
        Iterator var4 = this.getFrozenPlayersByMe().iterator();

        while(var4.hasNext()) {
            FreezeStatus frozenByMe = (FreezeStatus)var4.next();
            frozenByMe.getTarget().sendMessage(message);
        }

        player.sendMessage(message);
        AnkhiaStaff.getInstance().sendMessageToStaffPlayers(message, player.getUniqueId());
    }

    public FreezablePlayer getWhoFroze() {
        return this.freezeStatus.getStaff();
    }
}