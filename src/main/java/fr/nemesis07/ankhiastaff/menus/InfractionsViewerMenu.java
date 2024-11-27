package fr.nemesis07.ankhiastaff.menus;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.infractions.Infraction;
import fr.nemesis07.ankhiastaff.infractions.InfractionType;
import fr.nemesis07.ankhiastaff.modernlib.menus.Menu;
import fr.nemesis07.ankhiastaff.modernlib.menus.items.MenuItem;
import fr.nemesis07.ankhiastaff.modernlib.utils.Materials;
import fr.nemesis07.ankhiastaff.player.StaffPlayer;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InfractionsViewerMenu extends Menu {
    public InfractionsViewerMenu(InfractionType type, Player player, StaffPlayer staffPlayer, Menu oldMenu) {
        super(AnkhiaStaff.getInstance().getMsg().getText("messages.infractions-menu-title"), 3);
        this.addInfractionsItems(type, player, staffPlayer, oldMenu);
        this.setBackground(Materials.get(AnkhiaStaff.getInstance().getCfg().getStringList("items.fill.material")), (short)AnkhiaStaff.getInstance().getCfg().getInt("items.fill.damage"), AnkhiaStaff.getInstance().getCfg().getText("items.fill.name"), new String[0]);
    }

    private void addInfractionsItems(InfractionType type, final Player player, StaffPlayer staffPlayer, final Menu oldMenu) {
        List<Infraction> infractions = type == InfractionType.REPORT ? staffPlayer.getReports().getInfractions() : staffPlayer.getWarnings().getInfractions();
        int slot = 0;

        for(int i = infractions.size() - 1; i >= 0; --i) {
            Infraction infraction = (Infraction)infractions.get(i);
            Material material = type == InfractionType.REPORT ? Material.BOOK : Material.REDSTONE;
            String itemName = type == InfractionType.REPORT ? AnkhiaStaff.getInstance().getMsg().getText("messages.report.title") : AnkhiaStaff.getInstance().getMsg().getText("messages.warning.title");
            this.setItem(slot, new MenuItem(material, itemName, new String[]{AnkhiaStaff.getInstance().getMsg().getText("messages.reason-label", new String[]{"{reason}", infraction.getReason()}), AnkhiaStaff.getInstance().getMsg().getText("messages.reporter-label", new String[]{"{reporter}", infraction.getReporterName()}), AnkhiaStaff.getInstance().getMsg().getText("messages.time-label", new String[]{"{time}", infraction.getTimestamp()})}));
            ++slot;
        }

        this.setItem(this.getSize() - 1, new MenuItem(Material.ARROW, AnkhiaStaff.getInstance().getMsg().getText("messages.back-button"), new String[]{AnkhiaStaff.getInstance().getMsg().getText("messages.return-to-previous-menu")}) {
            public void onClick() {
                oldMenu.openInventory(player);
            }
        });
    }
}
