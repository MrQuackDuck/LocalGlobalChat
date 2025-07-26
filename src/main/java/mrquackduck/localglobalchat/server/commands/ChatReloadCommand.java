package mrquackduck.localglobalchat.server.commands;

import mrquackduck.localglobalchat.LocalGlobalChatPlugin;
import mrquackduck.localglobalchat.configuration.Configuration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ChatReloadCommand implements CommandExecutor {
    private final LocalGlobalChatPlugin plugin;
    private final Configuration config;

    public ChatReloadCommand(LocalGlobalChatPlugin plugin, Configuration config) {
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        plugin.reload();
        sender.sendMessage(config.getMessage("reloaded"));
        return true;
    }
}
