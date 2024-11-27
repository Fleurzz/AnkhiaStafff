package fr.nemesis07.ankhiastaff.hotbar.components.items;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.hotbar.HotbarItem;
import fr.nemesis07.ankhiastaff.menus.ExaminePlayerMenu;
import fr.nemesis07.ankhiastaff.modernlib.menus.Menu;
import fr.nemesis07.ankhiastaff.modernlib.utils.Materials;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ExamineHotbarItem extends HotbarItem {
    public ExamineHotbarItem() {
        super(Materials.get(AnkhiaStaff.getInstance().getCfg().getStringList("items.hotbar.examine.material")), AnkhiaStaff.getInstance().getMsg().getText("hotbar.examine.name"), 1, (short)0, AnkhiaStaff.getInstance().getMsg().getTextList("hotbar.examine.lore"));
    }

    public void onInteract(Player player, Entity target) {
        if (target instanceof Player) {
            Player targetPlayer = (Player)target;
            Menu menu = new ExaminePlayerMenu(player, targetPlayer);
            menu.openInventory(player);
        }

    }
}
