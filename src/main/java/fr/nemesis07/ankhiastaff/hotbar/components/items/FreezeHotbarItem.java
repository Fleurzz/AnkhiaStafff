package fr.nemesis07.ankhiastaff.hotbar.components.items;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.hotbar.HotbarItem;
import fr.nemesis07.ankhiastaff.modernlib.utils.Materials;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class FreezeHotbarItem extends HotbarItem {
    public FreezeHotbarItem() {
        super(Materials.get(AnkhiaStaff.getInstance().getCfg().getStringList("items.hotbar.freeze.material")), AnkhiaStaff.getInstance().getMsg().getText("hotbar.freeze.name"), 1, (short)0, AnkhiaStaff.getInstance().getMsg().getTextList("hotbar.freeze.lore"));
    }

    public void onInteract(Player player, Entity target) {
        if (!(target instanceof Player)) {
            player.sendMessage(AnkhiaStaff.getInstance().getMessage("hotbar.freeze.invalid", new String[0]));
        } else {
            AnkhiaStaff.getInstance().getStaffPlayerManager().getOrCreateStaffPlayer((Player)target).toggleFreeze(player);
        }
    }
}