package mrquackduck.localglobalchat.configuration;

import org.bukkit.plugin.java.JavaPlugin;

public class Configuration extends MessageConfigurationBase {
    public Configuration(JavaPlugin plugin) {
        super(plugin, "messages");
    }

    public int localChatRadius() {
        return getInt("localChatRadius");
    }

    public String format() {
        return getString("format");
    }

    public String localChatSymbol() {
        return getString("localChatSymbol");
    }

    public String globalChatSymbol() {
        return getString("globalChatSymbol");
    }

    public String localChatDistanceText() {
        return getString("localChatDistanceText");
    }

    public boolean localChatDistanceAsHover() {
        return getBoolean("localChatDistanceAsHover");
    }

    public String localChatDistanceHoverText() {
        return getString("localChatDistanceHoverText");
    }

    public String globalChatPrefix() {
        return getString("globalChatPrefix");
    }

    public boolean hideGlobalPrefixInOutputAnyway() {
        return getBoolean("hideGlobalPrefixInOutputAnyway");
    }

    public boolean showNobodyHeardYou() {
        return getBoolean("showNobodyHeardYou");
    }
}
