package fr.nemesis07.ankhiastaff.hotbar.components.items;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.hotbar.HotbarItem;
import fr.nemesis07.ankhiastaff.modernlib.utils.Materials;
import fr.nemesis07.ankhiastaff.player.StaffPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class VanishHotbarItem extends HotbarItem {
    private static Material getEnabledMaterial() {
        return Materials.get(AnkhiaStaff.getInstance().getCfg().getStringList("items.hotbar.vanish.turned_on.material"));
    }

    private static Material getDisabledMaterial() {
        return Materials.get(AnkhiaStaff.getInstance().getCfg().getStringList("items.hotbar.vanish.turned_off.material"));
    }

    private static short getEnabledDamage() {
        return (short)AnkhiaStaff.getInstance().getCfg().getInt("items.hotbar.vanish.turned_on.damage");
    }

    private static short getDisabledDamage() {
        return (short)AnkhiaStaff.getInstance().getCfg().getInt("items.hotbar.vanish.turned_off.damage");
    }

    public VanishHotbarItem(StaffPlayer staffPlayer) {
        super(staffPlayer.isVanished() ? getEnabledMaterial() : getDisabledMaterial(), AnkhiaStaff.getInstance().getMsg().getText("hotbar.vanish.name"), 1, staffPlayer.isVanished() ? getEnabledDamage() : getDisabledDamage(), AnkhiaStaff.getInstance().getMsg().getTextList("hotbar.vanish.lore"));
    }

    public void onInteract(Player player) {
        StaffPlayer staffPlayer = AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer(player);
        staffPlayer.toggleVanish();
        if (staffPlayer.isVanished()) {
            this.getItem().setType(getEnabledMaterial());
            this.getItem().setDurability(getEnabledDamage());
        } else {
            this.getItem().setType(getDisabledMaterial());
            this.getItem().setDurability(getDisabledDamage());
        }

        player.getInventory().setItem(AnkhiaStaff.getInstance().getCfg().getInt("items.hotbar.vanish.slot"), this.getItem());
    }
}