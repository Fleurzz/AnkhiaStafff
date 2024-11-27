package fr.nemesis07.ankhiastaff.hotbar.components.items;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.cps.CpsTestingManager;
import fr.nemesis07.ankhiastaff.hotbar.HotbarItem;
import fr.nemesis07.ankhiastaff.modernlib.utils.ChatColors;
import fr.nemesis07.ankhiastaff.modernlib.utils.Materials;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CpsHotbarItem extends HotbarItem {
    public CpsHotbarItem() {
        super(Materials.get(AnkhiaStaff.getInstance().getCfg().getStringList("items.hotbar.cps.material")), AnkhiaStaff.getInstance().getMsg().getText("hotbar.cps.name"), 1, (short)0, AnkhiaStaff.getInstance().getMsg().getTextList("hotbar.cps.lore"));
    }

    public void onInteract(Player player, Entity target) {
        if (!(target instanceof Player)) {
            player.sendMessage(AnkhiaStaff.getInstance().getMessage("hotbar.cps.invalid", new String[0]));
        } else {
            Player testedPlayer = (Player)target;
            if (CpsTestingManager.isTesting(testedPlayer)) {
                player.sendMessage(AnkhiaStaff.getInstance().getMessage("hotbar.cps.already_testing", new String[0]).replace("{player}", testedPlayer.getName()));
            } else {
                CpsTestingManager.startCpsTesting(testedPlayer);
                player.sendMessage(AnkhiaStaff.getInstance().getMessage("hotbar.cps.start_testing", new String[0]).replace("{player}", testedPlayer.getName()));
                Bukkit.getScheduler().runTaskLater(AnkhiaStaff.getInstance(), () -> {
                    double averageCps = CpsTestingManager.getAverageCps(testedPlayer);
                    CpsTestingManager.stopCpsTesting(testedPlayer);
                    String averageCpsString = String.format("%.1f", averageCps);
                    if (averageCps > 10.0D) {
                        player.sendMessage(ChatColors.color(AnkhiaStaff.getInstance().getMessage("hotbar.cps.dangerous", new String[0]).replace("{player}", testedPlayer.getName()).replace("{cps}", averageCpsString)));
                    } else {
                        player.sendMessage(ChatColors.color(AnkhiaStaff.getInstance().getMessage("hotbar.cps.safe", new String[0]).replace("{player}", testedPlayer.getName()).replace("{cps}", averageCpsString)));
                    }

                }, (long)AnkhiaStaff.getInstance().getCfg().getInt("cps.time") * 20L);
            }
        }
    }
}