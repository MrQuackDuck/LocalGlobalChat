package mrquackduck.localglobalchat.server.commands;

import mrquackduck.localglobalchat.LocalGlobalChatPlugin;
import mrquackduck.localglobalchat.configuration.Configuration;
import mrquackduck.localglobalchat.storage.ChatPreferenceRepository;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatCommand implements CommandExecutor, TabCompleter {
    private final LocalGlobalChatPlugin plugin;
    private final Configuration config;
    private final ChatPreferenceRepository chatPreferenceRepository;

    public ChatCommand(LocalGlobalChatPlugin plugin, Configuration config, ChatPreferenceRepository chatPreferenceRepository) {
        this.plugin = plugin;
        this.config = config;
        this.chatPreferenceRepository = chatPreferenceRepository;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(config.getMessage("usage-guide"));
            return true;
        }

        if (args[0].equalsIgnoreCase("local")) return new ChatLocalCommand(config, chatPreferenceRepository).onCommand(sender, command, s, Arrays.stream(args).skip(1).toArray(String[]::new));
        else if (args[0].equalsIgnoreCase("global")) return new ChatGlobalCommand(config, chatPreferenceRepository).onCommand(sender, command, s, Arrays.stream(args).skip(1).toArray(String[]::new));
        else if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("messagesonhead.admin"))
            return new ChatReloadCommand(plugin, config).onCommand(sender, command, s, args);

        sender.sendMessage(config.getMessage("usage-guide"));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> options = new ArrayList<>();
        List<String> completions = new ArrayList<>();

        if (args.length > 1) return completions;

        options.add("local");
        options.add("global");
        if (commandSender.hasPermission("localglobalchat.admin")) options.add("reload");

        StringUtil.copyPartialMatches(args[0], options, completions);
        return completions;
    }
}
