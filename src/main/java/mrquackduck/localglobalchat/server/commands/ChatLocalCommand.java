package mrquackduck.localglobalchat.server.commands;

import mrquackduck.localglobalchat.configuration.Configuration;
import mrquackduck.localglobalchat.types.enums.ChatType;
import mrquackduck.localglobalchat.storage.ChatPreferenceRepository;
import mrquackduck.localglobalchat.utils.StringBuilderUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChatLocalCommand implements CommandExecutor {
    private final Configuration config;
    private final ChatPreferenceRepository chatPreferenceRepository;

    public ChatLocalCommand(Configuration config, ChatPreferenceRepository chatPreferenceRepository) {
        this.config = config;
        this.chatPreferenceRepository = chatPreferenceRepository;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(config.getMessage("only-players"));
            return true;
        }

        if (chatPreferenceRepository.getPreferredChat((Player)sender) != ChatType.LOCAL || args.length == 0) {
            sender.sendMessage(config.getMessage("default-chat-set-to-local"));
            chatPreferenceRepository.setPreferredChat((Player)sender, ChatType.LOCAL);
        }

        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i != 0) messageBuilder.append(" ");
            messageBuilder.append(args[i]);
        }

        StringBuilderUtil.stripLeadingSlashes(messageBuilder);

        ((Player) sender).chat(messageBuilder.toString());
        return true;
    }
}
