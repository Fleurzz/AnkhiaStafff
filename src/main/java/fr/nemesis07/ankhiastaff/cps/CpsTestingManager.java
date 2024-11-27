package fr.nemesis07.ankhiastaff.cps;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;

public class CpsTestingManager {
    private static Map<Player, CpsTesting> testingPlayers = new HashMap();

    public static void startCpsTesting(Player player) {
        testingPlayers.put(player, new CpsTesting());
    }

    public static boolean isTesting(Player player) {
        return testingPlayers.containsKey(player);
    }

    public static double getAverageCps(Player player) {
        return !isTesting(player) ? 0.0D : ((CpsTesting)testingPlayers.get(player)).getAverageCps();
    }

    public static void click(Player player) {
        if (isTesting(player)) {
            ((CpsTesting)testingPlayers.get(player)).click();
        }
    }

    public static void stopCpsTesting(Player testedPlayer) {
        if (isTesting(testedPlayer)) {
            testingPlayers.remove(testedPlayer);
        }
    }
}
