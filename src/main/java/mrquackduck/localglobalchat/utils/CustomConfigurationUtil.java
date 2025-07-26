package mrquackduck.localglobalchat.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class CustomConfigurationUtil {
    public static File loadOrCreateConfig(JavaPlugin plugin, String configName) {
        File customConfigFile = new File(plugin.getDataFolder(), configName);
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            plugin.saveResource(configName, false);
        }

        return customConfigFile;
    }

    public static YamlConfiguration asConfig(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }
}
