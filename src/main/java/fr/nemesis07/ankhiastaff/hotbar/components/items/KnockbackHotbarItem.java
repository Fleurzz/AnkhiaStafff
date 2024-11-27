package fr.nemesis07.ankhiastaff.hotbar.components.items;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.hotbar.HotbarItem;
import fr.nemesis07.ankhiastaff.modernlib.utils.Materials;
import org.bukkit.enchantments.Enchantment;

public class KnockbackHotbarItem extends HotbarItem {
    public KnockbackHotbarItem() {
        super(Materials.get(AnkhiaStaff.getInstance().getCfg().getStringList("items.hotbar.knockback.material")), AnkhiaStaff.getInstance().getMsg().getText("hotbar.knockback.name"), 1, (short)0, AnkhiaStaff.getInstance().getMsg().getTextList("hotbar.knockback.lore"));

        try {
            this.getItem().addEnchantment(Enchantment.KNOCKBACK, 1);
        } catch (Exception var2) {
        }

    }
}
