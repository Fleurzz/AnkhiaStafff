package fr.nemesis07.ankhiastaff.modernlib.tasks;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public abstract class ModernTask implements Runnable {
    private Plugin plugin;
    private long time;
    private boolean async;

    public ModernTask(Plugin plugin, long time, boolean async) {
        this.plugin = plugin;
        this.time = time;
        this.async = async;
    }

    public void register() {
        BukkitScheduler scheduler = this.plugin.getServer().getScheduler();
        if (this.async) {
            scheduler.runTaskTimerAsynchronously(this.plugin, this, this.time, this.time);
        } else {
            scheduler.runTaskTimer(this.plugin, this, this.time, this.time);
        }

    }
}
