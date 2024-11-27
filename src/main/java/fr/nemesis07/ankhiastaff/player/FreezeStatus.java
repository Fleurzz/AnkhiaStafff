package fr.nemesis07.ankhiastaff.player;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import java.util.concurrent.TimeUnit;
import org.bukkit.inventory.ItemStack;

public class FreezeStatus {
    private FreezablePlayer staff;
    private FreezablePlayer target;
    private long startTime;
    private long duration;
    private ItemStack helmet = null;

    public FreezeStatus(FreezablePlayer staff, FreezablePlayer target) {
        this.staff = staff;
        this.target = target;
        this.startTime = System.currentTimeMillis();
        this.duration = TimeUnit.MINUTES.toMillis((long)AnkhiaStaff.getInstance().getCfg().getInt("freeze.time", 5));
    }

    public FreezablePlayer getStaff() {
        return this.staff;
    }

    public FreezablePlayer getTarget() {
        return this.target;
    }

    public long getTimeElapsed() {
        return System.currentTimeMillis() - this.startTime;
    }

    public String getTimeFormatted() {
        long timeElapsed = this.getTimeElapsed() / 1000L;
        long minutes = timeElapsed / 60L;
        long seconds = timeElapsed % 60L;
        return String.format("%dm %ds", minutes, seconds);
    }

    public String getCountdownFormatted() {
        long timeRemaining = Math.max(0L, this.duration - this.getTimeElapsed());
        long minutes = timeRemaining / 60000L;
        long seconds = timeRemaining % 60000L / 1000L;
        return String.format("%dm %ds", minutes, seconds);
    }

    public boolean isCountdownFinished() {
        return this.getTimeElapsed() >= this.duration;
    }

    public ItemStack getHelmet() {
        return this.helmet;
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;
    }
}