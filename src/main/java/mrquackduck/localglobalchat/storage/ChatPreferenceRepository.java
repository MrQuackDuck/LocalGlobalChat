package mrquackduck.localglobalchat.storage;

import mrquackduck.localglobalchat.types.enums.ChatType;
import mrquackduck.localglobalchat.utils.CustomConfigurationUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ChatPreferenceRepository {
    private final File preferenceStorageFile;
    private final FileConfiguration preferenceStorage;

    public ChatPreferenceRepository(JavaPlugin plugin) {
        this.preferenceStorageFile = CustomConfigurationUtil.loadOrCreateConfig(plugin, "storage.yml");
        this.preferenceStorage = CustomConfigurationUtil.asConfig(preferenceStorageFile);
    }

    public void setPreferredChat(Player player, ChatType chatType) {
        preferenceStorage.set(player.getUniqueId().toString(), chatType.toString());
        saveChanges();
    }

    public ChatType getPreferredChat(Player player) {
        var preferredChat = preferenceStorage.getString(player.getUniqueId().toString());
        if (preferredChat == null) return ChatType.LOCAL;

        return ChatType.valueOf(preferredChat);
    }

    private void saveChanges() {
        try { preferenceStorage.save(preferenceStorageFile); }
        catch (IOException e) { throw new RuntimeException(e); }
    }
}
