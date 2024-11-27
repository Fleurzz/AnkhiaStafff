package fr.nemesis07.ankhiastaff.managers;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class VeinManager {
    private Set<String> detectedBlocks = new HashSet();
    private Map<UUID, Integer> streaks = new HashMap();

    public int getStreak(UUID uuid) {
        return (Integer)this.streaks.getOrDefault(uuid, 0);
    }

    public int getStreak(Player player) {
        return this.getStreak(player.getUniqueId());
    }

    public void incrementStreak(UUID uuid) {
        int currentStreak = (Integer)this.streaks.getOrDefault(uuid, 0);
        this.streaks.put(uuid, currentStreak + 1);
    }

    public void incrementStreak(Player player) {
        this.incrementStreak(player.getUniqueId());
    }

    public boolean isDetected(Block block) {
        return this.detectedBlocks.contains(block.getLocation().toString());
    }

    public void addDetected(Block block) {
        this.detectedBlocks.add(block.getLocation().toString());
    }

    private void addDetected(Set<Block> vein) {
        Iterator var2 = vein.iterator();

        while(var2.hasNext()) {
            Block block = (Block)var2.next();
            this.addDetected(block);
        }

    }

    public Set<Block> getConnectedDiamondOres(Player player, Block startBlock) {
        Set<Block> vein = null;
        if (startBlock.getType() != Material.DIAMOND_ORE) {
            return vein;
        } else if (this.isDetected(startBlock)) {
            return vein;
        } else {
            vein = new HashSet();
            this.incrementStreak(player);
            Deque<Block> stack = new ArrayDeque();
            stack.push(startBlock);

            while(!stack.isEmpty()) {
                Block currentBlock = (Block)stack.pop();
                if (vein.add(currentBlock)) {
                    this.addAdjacentDiamondOresToStack(currentBlock, stack, vein);
                }
            }

            this.addDetected((Set)vein);
            return vein;
        }
    }

    private void addAdjacentDiamondOresToStack(Block currentBlock, Deque<Block> stack, Set<Block> vein) {
        for(int xOffset = -1; xOffset <= 1; ++xOffset) {
            for(int yOffset = -1; yOffset <= 1; ++yOffset) {
                for(int zOffset = -1; zOffset <= 1; ++zOffset) {
                    if (Math.abs(xOffset) + Math.abs(yOffset) + Math.abs(zOffset) == 1) {
                        Block adjacentBlock = currentBlock.getRelative(xOffset, yOffset, zOffset);
                        if (!this.isDetected(adjacentBlock) && adjacentBlock.getType() == Material.DIAMOND_ORE && !vein.contains(adjacentBlock)) {
                            stack.push(adjacentBlock);
                        }
                    }
                }
            }
        }

    }
}