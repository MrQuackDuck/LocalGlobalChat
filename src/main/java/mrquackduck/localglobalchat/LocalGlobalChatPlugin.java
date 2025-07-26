package mrquackduck.localglobalchat;

import com.tchristofferson.configupdater.ConfigUpdater;
import mrquackduck.localglobalchat.server.commands.ChatCommand;
import mrquackduck.localglobalchat.server.commands.ChatGlobalCommand;
import mrquackduck.localglobalchat.server.commands.ChatLocalCommand;
import mrquackduck.localglobalchat.configuration.Configuration;
import mrquackduck.localglobalchat.server.listeners.SendMessageListener;
import mrquackduck.localglobalchat.storage.ChatPreferenceRepository;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class LocalGlobalChatPlugin extends JavaPlugin {
    private final Configuration config = new Configuration(this);
    private final ChatPreferenceRepository chatPreferenceRepository = new ChatPreferenceRepository(this);
    private Logger logger;
    private Chat vaultChat;

    @Override
    public void onEnable() {
        // Setting up a logger
        logger = getLogger();

        // Registering listeners
        getServer().getPluginManager().registerEvents(new SendMessageListener(this, chatPreferenceRepository), this);

        // Starting the plugin
        try { start(); }
        catch (RuntimeException e) { getLogger().log(Level.SEVERE, e.getMessage()); }

        // Registering commands
        Objects.requireNonNull(getServer().getPluginCommand("chat")).setExecutor(new ChatCommand(this, config, chatPreferenceRepository));
        Objects.requireNonNull(getServer().getPluginCommand("local")).setExecutor(new ChatLocalCommand(config, chatPreferenceRepository));
        Objects.requireNonNull(getServer().getPluginCommand("global")).setExecutor(new ChatGlobalCommand(config, chatPreferenceRepository));
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void start() {
        // Saving the default config
        saveDefaultConfig();

        // Updating the config with missing key-pairs (and removing redundant ones if present)
        File configFile = new File(getDataFolder(), "config.yml");
        try { ConfigUpdater.update(this, "config.yml", configFile, new ArrayList<>()); }
        catch (IOException e) { e.printStackTrace(); }
    }

    public void reload() {
        // Reloading the config
        reloadConfig();

        // Starting the plugin again
        start();

        // Setting up the Vault chat
        setupVaultChat();

        logger.info("Plugin restarted!");
    }

    public Chat getVaultChat() {
        return vaultChat;
    }

    private void setupVaultChat() {
        try {
            vaultChat = Objects.requireNonNull(getServer().getServicesManager().getRegistration(Chat.class)).getProvider();
        }
        catch (NoClassDefFoundError | NullPointerException e) {
            vaultChat = null;
        }
    }
}
