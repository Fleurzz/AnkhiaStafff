package fr.nemesis07.ankhiastaff.hotbar.components.items;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.hotbar.HotbarItem;
import fr.nemesis07.ankhiastaff.modernlib.utils.Materials;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class FollowHotbarItem extends HotbarItem {
    public FollowHotbarItem() {
        super(Materials.get(AnkhiaStaff.getInstance().getCfg().getStringList("items.hotbar.follow.material")), AnkhiaStaff.getInstance().getMsg().getText("hotbar.follow.name"), 1, (short)0, AnkhiaStaff.getInstance().getMsg().getTextList("hotbar.follow.lore"));
    }

    public void onInteract(Player player, Entity target) {
        if (!(target instanceof Player)) {
            player.sendMessage(AnkhiaStaff.getInstance().getMessage("hotbar.follow.invalid", new String[0]));
        } else {
            Player targetPlayer = (Player)target;
            if (player.isInsideVehicle()) {
                player.leaveVehicle();
            }

            targetPlayer.setPassenger(player);
            player.sendMessage(AnkhiaStaff.getInstance().getMessage("hotbar.follow.success", new String[0]).replace("{player}", targetPlayer.getDisplayName()));
        }
    }
}