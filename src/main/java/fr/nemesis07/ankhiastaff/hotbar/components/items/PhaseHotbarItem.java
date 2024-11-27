package fr.nemesis07.ankhiastaff.hotbar.components.items;

import fr.nemesis07.ankhiastaff.AnkhiaStaff;
import fr.nemesis07.ankhiastaff.hotbar.HotbarItem;
import fr.nemesis07.ankhiastaff.modernlib.utils.Materials;
import fr.nemesis07.ankhiastaff.modernlib.utils.Sounds;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class PhaseHotbarItem extends HotbarItem {
    private static int MAX_DISTANCE = 5;
    private static Set<Material> WALKABLE_MATERIALS = new HashSet();

    public PhaseHotbarItem() {
        super(Materials.get(AnkhiaStaff.getInstance().getCfg().getStringList("items.hotbar.phase.material")), AnkhiaStaff.getInstance().getMsg().getText("hotbar.phase.name"), 1, (short)0, AnkhiaStaff.getInstance().getMsg().getTextList("hotbar.phase.lore"));
    }

    public void onInteract(Player player, Block block) {
        Location playerLocation = player.getLocation();
        Vector direction = playerLocation.getDirection().normalize();
        Location newLocation = playerLocation.clone();

        for(int i = 0; i < MAX_DISTANCE; ++i) {
            newLocation.add(direction);
            if (WALKABLE_MATERIALS.contains(newLocation.getBlock().getType()) && WALKABLE_MATERIALS.contains(newLocation.getBlock().getRelative(0, 1, 0).getType())) {
                player.teleport(newLocation);
                Sounds.play(player, 1.0F, 1.0F, new String[]{"ENTITY_ENDERMAN_TELEPORT", "ENDERMAN_TELEPORT"});
                return;
            }
        }

        player.sendMessage(AnkhiaStaff.getInstance().getMessage("hotbar.phase.no_safe_location", new String[0]));
    }

    static {
        WALKABLE_MATERIALS.add(Material.AIR);
        WALKABLE_MATERIALS.add(Material.WATER);
        WALKABLE_MATERIALS.add(Material.LAVA);
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"CARPET"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"RED_CARPET"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"BLUE_CARPET"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"GREEN_CARPET"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"YELLOW_CARPET"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"ORANGE_CARPET"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"PINK_CARPET"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"PURPLE_CARPET"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"CYAN_CARPET"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"LIGHT_BLUE_CARPET"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"MAGENTA_CARPET"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"BLACK_CARPET"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"WHITE_CARPET"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"BROWN_CARPET"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"GRAY_CARPET"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"LIGHT_GRAY_CARPET"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"OAK_PRESSURE_PLATE"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"SPRUCE_PRESSURE_PLATE"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"BIRCH_PRESSURE_PLATE"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"JUNGLE_PRESSURE_PLATE"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"ACACIA_PRESSURE_PLATE"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"DARK_OAK_PRESSURE_PLATE"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"STONE_PRESSURE_PLATE"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"LIGHT_WEIGHTED_PRESSURE_PLATE"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"HEAVY_WEIGHTED_PRESSURE_PLATE"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"CRIMSON_PRESSURE_PLATE"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"WARPED_PRESSURE_PLATE"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"POLISHED_BLACKSTONE_PRESSURE_PLATE"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"IRON_DOOR"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"OAK_DOOR"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"IRON_TRAPDOOR"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"OAK_TRAPDOOR"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"IRON_DOOR_BLOCK"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"OAK_DOOR_BLOCK"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"IRON_TRAPDOOR_BLOCK"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"OAK_TRAPDOOR_BLOCK"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"LADDER"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"VINE"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"SUGAR_CANE"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"SEAGRASS"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"TALL_SEAGRASS"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"BAMBOO"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"BAMBOO_SAPLING"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"LILY_PAD"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"OAK_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"SPRUCE_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"BIRCH_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"JUNGLE_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"ACACIA_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"DARK_OAK_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"STONE_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"SMOOTH_STONE_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"SANDSTONE_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"PETRIFIED_OAK_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"COBBLESTONE_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"BRICK_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"STONE_BRICK_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"NETHER_BRICK_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"QUARTZ_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"RED_SANDSTONE_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"PURPUR_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"PRISMARINE_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"PRISMARINE_BRICK_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"DARK_PRISMARINE_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"POLISHED_GRANITE_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"SMOOTH_RED_SANDSTONE_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"MOSSY_STONE_BRICK_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"POLISHED_DIORITE_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"MOSSY_COBBLESTONE_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"END_STONE_BRICK_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"SMOOTH_SANDSTONE_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"SMOOTH_QUARTZ_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"GRANITE_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"ANDESITE_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"RED_NETHER_BRICK_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"POLISHED_ANDESITE_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"DIORITE_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"CRIMSON_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"WARPED_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"PETRIFIED_OAK_SLAB"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"WARPED_PRESSURE_PLATE"}));
        WALKABLE_MATERIALS.add(Materials.get(new String[]{"CRIMSON_PRESSURE_PLATE"}));
    }
}
