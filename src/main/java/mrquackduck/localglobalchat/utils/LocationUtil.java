package mrquackduck.localglobalchat.utils;

import org.bukkit.entity.Player;

public class LocationUtil {
    public static double getDistanceBetweenPlayers(Player player1, Player player2) {
        try { return player1.getLocation().distance(player2.getLocation()); }
        catch (IllegalArgumentException ex) { return Double.MAX_VALUE; } // When players are in different worlds
    }
}
