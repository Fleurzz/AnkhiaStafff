package fr.nemesis07.ankhiastaff.hotbar.components;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.hotbar.Hotbar;
import fr.nemesis07.ankhiastaff.hotbar.components.items.CpsHotbarItem;
import fr.nemesis07.ankhiastaff.hotbar.components.items.ExamineHotbarItem;
import fr.nemesis07.ankhiastaff.hotbar.components.items.FollowHotbarItem;
import fr.nemesis07.ankhiastaff.hotbar.components.items.FreezeHotbarItem;
import fr.nemesis07.ankhiastaff.hotbar.components.items.KnockbackHotbarItem;
import fr.nemesis07.ankhiastaff.hotbar.components.items.PhaseHotbarItem;
import fr.nemesis07.ankhiastaff.hotbar.components.items.PlayersHotbarItem;
import fr.nemesis07.ankhiastaff.hotbar.components.items.RandomTeleportHotbarItem;
import fr.nemesis07.ankhiastaff.hotbar.components.items.StaffListHotbarItem;
import fr.nemesis07.ankhiastaff.hotbar.components.items.VanishHotbarItem;
import fr.nemesis07.ankhiastaff.modernlib.config.ConfigWrapper;
import fr.nemesis07.ankhiastaff.player.StaffPlayer;

public class StaffHotbar extends Hotbar {
    public StaffHotbar(StaffPlayer staffPlayer) {
        ConfigWrapper config = AnkhiaStaff.getInstance().getCfg();
        if (config.getBoolean("items.hotbar.phase.enabled")) {
            this.setItem(config.getInt("items.hotbar.phase.slot"), new PhaseHotbarItem());
        }

        if (config.getBoolean("items.hotbar.r-teleport.enabled")) {
            this.setItem(config.getInt("items.hotbar.r-teleport.slot"), new RandomTeleportHotbarItem());
        }

        if (config.getBoolean("items.hotbar.vanish.enabled")) {
            this.setItem(config.getInt("items.hotbar.vanish.slot"), new VanishHotbarItem(staffPlayer));
        }

        if (config.getBoolean("items.hotbar.players.enabled")) {
            this.setItem(config.getInt("items.hotbar.players.slot"), new PlayersHotbarItem());
        }

        if (config.getBoolean("items.hotbar.stafflist.enabled")) {
            this.setItem(config.getInt("items.hotbar.stafflist.slot"), new StaffListHotbarItem());
        }

        if (config.getBoolean("items.hotbar.freeze.enabled")) {
            this.setItem(config.getInt("items.hotbar.freeze.slot"), new FreezeHotbarItem());
        }

        if (config.getBoolean("items.hotbar.cps.enabled")) {
            this.setItem(config.getInt("items.hotbar.cps.slot"), new CpsHotbarItem());
        }

        if (config.getBoolean("items.hotbar.examine.enabled")) {
            this.setItem(config.getInt("items.hotbar.examine.slot"), new ExamineHotbarItem());
        }

        if (config.getBoolean("items.hotbar.follow.enabled")) {
            this.setItem(config.getInt("items.hotbar.follow.slot"), new FollowHotbarItem());
        }

        if (config.getBoolean("items.hotbar.knockback.enabled")) {
            this.setItem(config.getInt("items.hotbar.knockback.slot"), new KnockbackHotbarItem());
        }

    }
}
