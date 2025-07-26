package mrquackduck.localglobalchat.server.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import mrquackduck.localglobalchat.LocalGlobalChatPlugin;
import mrquackduck.localglobalchat.configuration.Configuration;
import mrquackduck.localglobalchat.types.enums.ChatType;
import mrquackduck.localglobalchat.server.renderers.LocalGlobalChatRenderer;
import mrquackduck.localglobalchat.storage.ChatPreferenceRepository;
import mrquackduck.localglobalchat.utils.LocationUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;

public class SendMessageListener implements Listener {
    private final LocalGlobalChatPlugin plugin;
    private final Configuration config;
    private final ChatPreferenceRepository chatPreferenceRepository;

    public SendMessageListener(LocalGlobalChatPlugin plugin, ChatPreferenceRepository chatPreferenceRepository) {
        this.plugin = plugin;
        this.config = new Configuration(plugin);
        this.chatPreferenceRepository = chatPreferenceRepository;
    }

    @EventHandler
    public void onMessageSent(AsyncChatEvent event) {
        var sender = event.getPlayer();
        var recipients = event.viewers();
        var playersToExclude = new ArrayList<Audience>();

        var chatType = chatPreferenceRepository.getPreferredChat(sender);

        var globalChatPrefix = config.globalChatPrefix();
        boolean isGlobalChatPrefixAtStart = ((TextComponent) event.message()).content().startsWith(Objects.requireNonNull(globalChatPrefix));
        boolean doesMessageContainOnlyOneSymbol = ((TextComponent) event.message()).content().length() == 1;
        if ((isGlobalChatPrefixAtStart && chatType == ChatType.LOCAL || isGlobalChatPrefixAtStart && config.hideGlobalPrefixInOutputAnyway()) && !doesMessageContainOnlyOneSymbol) {
            chatType = ChatType.GLOBAL;

            // Construct regex to match the prefix followed optionally by a space
            String regex = "^" + Pattern.quote(globalChatPrefix) + "\\s?";
            var globalChatPrefixReplacementConfig = TextReplacementConfig.builder()
                    .match(Pattern.compile(regex))
                    .replacement("") // Replace with an empty string
                    .once()
                    .build();

            event.message(event.message().replaceText(globalChatPrefixReplacementConfig));
        }
        else if (chatType == ChatType.LOCAL) {
            for (Audience audience : recipients) {
                if (audience instanceof Player recipient) {
                    if (LocationUtil.getDistanceBetweenPlayers(sender, recipient) > config.localChatRadius()) {
                        playersToExclude.add(recipient);
                    }
                }
            }
        }

        playersToExclude.forEach(recipients::remove);

        // 2 is the minimum amount of recipients (console + the sender itself)
        if (config.showNobodyHeardYou() && chatType != ChatType.GLOBAL && recipients.size() <= 2) {
            sender.sendActionBar(config.getMessage("nobody-heard-you"));
        }

        var renderer = new LocalGlobalChatRenderer(plugin, config, chatType);
        event.renderer(renderer);
    }
}
