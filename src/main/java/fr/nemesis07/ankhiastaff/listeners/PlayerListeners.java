package fr.nemesis07.ankhiastaff.listeners;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.cps.CpsTestingManager;
import fr.nemesis07.ankhiastaff.hotbar.Hotbar;
import fr.nemesis07.ankhiastaff.hotbar.HotbarItem;
import fr.nemesis07.ankhiastaff.hotbar.components.items.KnockbackHotbarItem;
import fr.nemesis07.ankhiastaff.player.FreezablePlayer;
import fr.nemesis07.ankhiastaff.player.StaffNote;
import fr.nemesis07.ankhiastaff.player.StaffPlayer;
import fr.nemesis07.ankhiastaff.utils.Inventories;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListeners implements Listener {
    private Map<UUID, Long> cooldowns = new HashMap();

    public boolean isCooldownOver(UUID uuid) {
        long currentTime = System.currentTimeMillis();
        long cooldownTime = 100L;
        if (this.cooldowns.containsKey(uuid)) {
            long lastExecutionTime = (Long)this.cooldowns.get(uuid);
            if (currentTime - lastExecutionTime < cooldownTime) {
                return false;
            }
        }
        this.cooldowns.put(uuid, currentTime);
        return true;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (AnkhiaStaff.getInstance().getStaffModeManager().isStaff(player)) {
            String message = event.getMessage();
            String command = message.split(" ")[0];
            List<String> blockedCommands = AnkhiaStaff.getInstance().getCfg().getStringList("staffmode.blocked_commands");
            if (blockedCommands.contains(command)) {
                player.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.staffmode.cannot-use-command", new String[0]));
                event.setCancelled(true);
                return;
            }
        }

        StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
        if (staffPlayer != null && staffPlayer.isFrozen()) {
            player.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.freeze.cannot-use-commands", new String[0]));
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
        if (staffPlayer != null) {
            if (staffPlayer.isWritingNote()) {
                final String text = event.getMessage();
                new BukkitRunnable() {
                    public void run() {
                        staffPlayer.openWriteMenu(player, text);
                    }
                }.runTask(AnkhiaStaff.getInstance());
                StaffNote note = staffPlayer.writeNote(text);
                event.setCancelled(true);
                player.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.note-writing-success", new String[0]).replace("{player}", note.getName()));
            } else if (staffPlayer.getWarningProcess().isInProgress()) {
                String text = event.getMessage();
                String warnedName = staffPlayer.getWarningProcess().getTarget().getName();
                staffPlayer.getWarningProcess().complete(text);
                new BukkitRunnable() {
                    public void run() {
                        staffPlayer.getWarningProcess().openWarningMenu(player);
                    }
                }.runTask(AnkhiaStaff.getInstance());
                staffPlayer.getWarningProcess().clear();
                event.setCancelled(true);
                player.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.warning.success", new String[0]).replace("{player}", warnedName));
            } else if (staffPlayer.sendFreezeChat(event.getMessage())) {event.setCancelled(true);
            } else if (staffPlayer.isStaffChat()) {
                staffPlayer.sendStaffChat(event.getMessage());
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        AnkhiaStaff.getInstance().setVisible(player, true);
        StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
        if (staffPlayer.isVanished()) {
            event.setJoinMessage(null);
        }

        for (StaffPlayer toVanish : AnkhiaStaff.getInstance().getStaffPlayerManager().getStaffPlayers().values()) {
            if (toVanish.isVanished()) {
                staffPlayer.hidePlayer(toVanish.isForceVanish(), toVanish.getPlayer());
            }
        }

        AnkhiaStaff.getInstance().getHotbarManager().setHotbar(player, null);
        AnkhiaStaff.getInstance().getInventoryManager().loadPlayerInventory(player);
        AnkhiaStaff.getInstance().getInventoryManager().deletePlayerInventory(player);

        Bukkit.getScheduler().runTaskAsynchronously(AnkhiaStaff.getInstance(), () -> {
            staffPlayer.setIP(player.getAddress().getAddress().getHostAddress());
            staffPlayer.load();

            if (player.hasPermission("staffmodex.join")) {
                for (StaffPlayer staffPlayer2 : AnkhiaStaff.getInstance().getStaffPlayerManager().getStaffPlayers().values()) {
                    if (staffPlayer2.isViewJoins()) {
                        staffPlayer2.sendMessage(PlaceholderAPI.setPlaceholders(player,
                                AnkhiaStaff.getInstance().getMessage("messages.join.message",
                                        new String[]{"{player}", player.getName()})));
                    }
                }
            }

            if (player.hasPermission("staffmodex.view_joins")) {
                staffPlayer.setViewJoins(true);
            }

            if (player.hasPermission("staffmodex.staffmode")) {
                Bukkit.getScheduler().runTask(AnkhiaStaff.getInstance(), () -> {
                    AnkhiaStaff.getInstance().getStaffModeManager().addStaff(player);
                });
            }
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        AnkhiaStaff.getInstance().setVisible(player, true);
        StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getStaffPlayer(player);

        if (staffPlayer != null) {
            Bukkit.getScheduler().runTaskAsynchronously(AnkhiaStaff.getInstance(), () -> {
                staffPlayer.getStaffPlayerLoader().saveIP();
            });

            if (staffPlayer.isVanished()) {
                event.setQuitMessage(null);
            }

            if (staffPlayer.isFrozen()) {
                FreezablePlayer whoFroze = staffPlayer.getWhoFroze();
                whoFroze.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.freeze.quit_msg",
                        new String[]{"{player}", player.getName()}));
                staffPlayer.unfreeze();

                List<String> disconnectCmds = AnkhiaStaff.getInstance().getCfg().getTextList("freeze.commands.disconnect",
                        new String[]{"{player}", player.getName(), "{staff}", whoFroze.getName()});

                if (disconnectCmds != null && !disconnectCmds.isEmpty()) {
                    Player whoFrozePlayer = whoFroze.getPlayer();
                    Server server = AnkhiaStaff.getInstance().getServer();
                    if (whoFrozePlayer != null) {
                        for (String cmd : disconnectCmds) {
                            if (cmd != null && !cmd.isEmpty()) {
                                server.dispatchCommand(whoFrozePlayer, cmd);
                            }
                        }
                    }
                }
            }
        }

        AnkhiaStaff.getInstance().getStaffModeManager().removeStaff(player);
        AnkhiaStaff.getInstance().getStaffPlayerManager().removeStaffPlayer(player.getUniqueId());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (AnkhiaStaff.getInstance().getHotbarManager().getHotbar(player) != null ||
                AnkhiaStaff.getInstance().getStaffModeManager().isStaff(player)) {
            event.setCancelled(true);
            return;
        }

        StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
        if (staffPlayer != null && staffPlayer.isFrozen()) {
            staffPlayer.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.freeze.cannot-drop-items", new String[0]));
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if (AnkhiaStaff.getInstance().getStaffModeManager().isStaff(player)) {
            event.setCancelled(true);
            return;
        }

        StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
        if (staffPlayer != null && staffPlayer.isFrozen()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        boolean leftClick = action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK;
        boolean rightClick = action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK;

        if (leftClick) {
            CpsTestingManager.click(event.getPlayer());
        }

        Player player = event.getPlayer();

        if (rightClick && AnkhiaStaff.getInstance().getStaffModeManager().isStaff(player)) {
            Block clickedBlock = event.getClickedBlock();
            if (clickedBlock != null) {
                BlockState blockState = clickedBlock.getState();
                if (blockState instanceof Chest && player.hasPermission("staffmodex.chestpreview")) {
                    event.setCancelled(true);
                    Chest chest = (Chest)blockState;
                    Inventory inventory = chest.getBlockInventory();
                    Inventory copy = Bukkit.createInventory(null, inventory.getSize(), "Chest Contents");
                    copy.setContents(inventory.getContents());
                    player.openInventory(copy);
                    return;
                }
            }
        }

        if (leftClick || rightClick) {
            Hotbar hotbar = AnkhiaStaff.getInstance().getHotbarManager().getHotbar(player);
            if (hotbar != null) {
                PlayerInventory inventory = player.getInventory();
                int slot = inventory.getHeldItemSlot();
                HotbarItem hotbarItem = hotbar.getItem(slot);
                if (hotbarItem != null) {
                    if (this.isCooldownOver(player.getUniqueId())) {
                        hotbarItem.onInteract(player);
                        hotbarItem.onInteract(player, event.getClickedBlock());
                        Material clickedMaterial = hotbarItem.getItem().getType();
                        if (clickedMaterial.isSolid()) {
                            player.updateInventory();
                        }
                    }
                    event.setCancelled(true);
                }
            }

            StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
            if (staffPlayer == null) {
                return;
            }

            if (AnkhiaStaff.getInstance().getStaffModeManager().isStaff(player)) {
                event.setCancelled(true);
                if (rightClick) {
                    Block block = event.getClickedBlock();
                    if (block != null && block.getState() instanceof InventoryHolder) {
                        InventoryHolder holder = (InventoryHolder)block.getState();
                        player.closeInventory();
                        Inventory copy = Inventories.copyInventory(holder.getInventory());
                        if (copy != null) {
                            player.openInventory(copy);
                        }
                    }
                }
                return;
            }

            if (staffPlayer.isFrozen()) {
                staffPlayer.sendMessage(AnkhiaStaff.getInstance().getMessage("messages.freeze.cannot-interact", new String[0]));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Hotbar hotbar = AnkhiaStaff.getInstance().getHotbarManager().getHotbar(player);
        if (hotbar != null) {
            PlayerInventory inventory = player.getInventory();
            int slot = inventory.getHeldItemSlot();
            HotbarItem hotbarItem = hotbar.getItem(slot);
            if (hotbarItem != null && !(hotbarItem instanceof KnockbackHotbarItem)) {
                if (this.isCooldownOver(player.getUniqueId())) {
                    hotbarItem.onInteract(player);
                    hotbarItem.onInteract(player, event.getRightClicked());
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(event.getPlayer()).preventMovement(event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (AnkhiaStaff.getInstance().getStaffModeManager().isStaff(player)) {
            event.setKeepInventory(true);
            return;
        }

        StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
        if (staffPlayer != null && staffPlayer.isFrozen()) {
            event.setKeepInventory(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        HumanEntity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player)entity;
            if (AnkhiaStaff.getInstance().getStaffModeManager().isStaff(player)) {
                event.setCancelled(true);
            }
        }
    }
}