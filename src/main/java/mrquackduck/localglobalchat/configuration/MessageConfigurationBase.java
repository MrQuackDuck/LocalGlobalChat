package mrquackduck.localglobalchat.configuration;

import mrquackduck.localglobalchat.utils.MessageColorizer;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageConfigurationBase extends ConfigurationBase {
    private final String messagesConfigurationSectionName;

    public MessageConfigurationBase(JavaPlugin plugin, String messagesConfigurationSectionName) {
        super(plugin);
        this.messagesConfigurationSectionName = messagesConfigurationSectionName;
    }

    /**
     * Returns a plain message from configuration by key without formatting
     */
    public String getPlainMessage(String key) {
        var message = getString(messagesConfigurationSectionName + '.' + key);
        if (message == null) return String.format("Message %s wasn't found", key);

        return message;
    }

    /**
     * Returns a formatted message from configuration by key
     */
    public String getMessage(String key) {
        var message = getPlainMessage(key);

        message = message.replace("<prefix>", MessageColorizer.colorize(getPlainMessage("prefix")));

        return MessageColorizer.colorize(message);
    }
}