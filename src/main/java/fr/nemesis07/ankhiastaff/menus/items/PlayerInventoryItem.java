package fr.nemesis07.ankhiastaff.menus.items;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.modernlib.menus.Menu;
import fr.nemesis07.ankhiastaff.modernlib.menus.items.MenuItem;
import fr.nemesis07.ankhiastaff.modernlib.utils.Materials;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class PlayerInventoryItem extends MenuItem {
    private Player player;
    private Player target;
    private Menu examinePlayerMenu;

    public PlayerInventoryItem(Player player, Player target, Menu examinePlayerMenu) {
        super(Material.CHEST, AnkhiaStaff.getInstance().getMsg().getText("menus.playerInventory.title"), new String[]{AnkhiaStaff.getInstance().getMsg().getText("menus.playerInventory.viewInventory")});
        this.player = player;
        this.target = target;
        this.examinePlayerMenu = examinePlayerMenu;
    }

    public void onClick() {
        Menu menu = new Menu(this.target.getName() + "'s inventory", 5);

        ItemStack offHandItem;
        for(int i = 0; i < 36; ++i) {
            offHandItem = this.target.getInventory().getItem(i);
            if (offHandItem != null && offHandItem.getType() != Material.AIR) {
                MenuItem item = new MenuItem(offHandItem);
                menu.setItem(i, item);
            }
        }

        try {
            EquipmentSlot offHand = EquipmentSlot.valueOf("OFF_HAND");
            offHandItem = this.player.getInventory().getItem(offHand);
            if (offHandItem != null && offHandItem.getType() != Material.AIR) {
                menu.setItem(39, new MenuItem(offHandItem));
            }
        } catch (Exception var7) {
        }

        menu.setItem(36, new MenuItem(Material.ARROW, AnkhiaStaff.getInstance().getMsg().getText("menus.playerInventory.back"), new String[0]) {
            public void onClick() {
                PlayerInventoryItem.this.examinePlayerMenu.openInventory(PlayerInventoryItem.this.player);
            }
        });
        menu.openInventory(this.player);
        Collection<PotionEffect> effects = this.target.getActivePotionEffects();
        List<String> loreEffects = effects.isEmpty() ? Arrays.asList(AnkhiaStaff.getInstance().getMsg().getText("menus.playerInventory.noEffects")) : (List)effects.stream().map(this::formatEffect).collect(Collectors.toList());
        menu.setItem(40, new MenuItem(Material.WATER_BUCKET, AnkhiaStaff.getInstance().getMsg().getText("menus.playerInventory.effects"), (String[])loreEffects.toArray(new String[0])));

        for(int i = 0; i < 4; ++i) {
            ItemStack stack = this.target.getInventory().getArmorContents()[i];
            if (stack != null && stack.getType() != Material.AIR) {
                MenuItem item = new MenuItem(stack);
                menu.setItem(45 - i - 1, item);
            } else {
                menu.setItem(45 - i - 1, new MenuItem(Materials.get(new String[]{"STAINED_GLASS_PANE", "RED_STAINED_GLASS_PANE"}), 1, (short)14, AnkhiaStaff.getInstance().getMsg().getText("menus.playerInventory.emptySlot"), new String[0]));
            }
        }

        menu.setBackground(Materials.get(AnkhiaStaff.getInstance().getCfg().getStringList("items.fill.material")), (short)AnkhiaStaff.getInstance().getCfg().getInt("items.fill.damage"), AnkhiaStaff.getInstance().getCfg().getText("items.fill.name"), new String[0]);
    }

    private String toRomanNumber(int amplifier) {
        if (amplifier == 0) {
            return "I";
        } else if (amplifier == 1) {
            return "II";
        } else if (amplifier == 2) {
            return "III";
        } else if (amplifier == 3) {
            return "IV";
        } else if (amplifier == 4) {
            return "V";
        } else if (amplifier == 5) {
            return "VI";
        } else if (amplifier == 6) {
            return "VII";
        } else if (amplifier == 7) {
            return "VIII";
        } else {
            return amplifier == 8 ? "IX" : "X";
        }
    }

    private String formatEffect(PotionEffect effect) {
        return "&7" + effect.getType().getName() + " &8(" + effect.getDuration() + "s, " + this.toRomanNumber(effect.getAmplifier()) + ")";
    }
}