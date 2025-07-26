package mrquackduck.localglobalchat.server.renderers;

import io.papermc.paper.chat.ChatRenderer;
import mrquackduck.localglobalchat.LocalGlobalChatPlugin;
import mrquackduck.localglobalchat.configuration.Configuration;
import mrquackduck.localglobalchat.types.enums.ChatType;
import mrquackduck.localglobalchat.utils.LocationUtil;
import mrquackduck.localglobalchat.utils.MessageColorizer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LocalGlobalChatRenderer implements ChatRenderer {
    private final LocalGlobalChatPlugin plugin;
    private final ChatType chatType;
    private final String chatSymbolTemplate = "<chatSymbol>";
    private final Configuration config;

    public LocalGlobalChatRenderer(LocalGlobalChatPlugin plugin, Configuration config, ChatType chatType) {
        this.plugin = plugin;
        this.chatType = chatType;
        this.config = config;
    }

    @Override
    public @NotNull Component render(@NotNull Player sender, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
        Component formatComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(config.format());

        var prefixReplacementConfig = TextReplacementConfig.builder()
                .matchLiteral("<prefix>")
                .replacement(getPrefix(sender))
                .build();

        var nameReplacementConfig = TextReplacementConfig.builder()
                .matchLiteral("<name>")
                .replacement(sourceDisplayName)
                .build();

        var suffixReplacementConfig = TextReplacementConfig.builder()
                .matchLiteral("<suffix>")
                .replacement(getSuffix(sender))
                .build();

        var messageReplacementConfig = TextReplacementConfig.builder()
                .matchLiteral("<message>")
                .replacement(message)
                .build();

        if (chatType == ChatType.LOCAL) formatComponent = formatAsLocalChat(formatComponent, sender, viewer);
        else if (chatType == ChatType.GLOBAL) formatComponent = formatAsGlobalChat(formatComponent);

        // Replacing [localChatDistance] to "" if it wasn't replaced by previous filters
        var localChatDistanceReplacementConfig = TextReplacementConfig.builder()
                .matchLiteral("<localChatDistance>")
                .replacement("")
                .build();

        return formatComponent
                .replaceText(localChatDistanceReplacementConfig)
                .replaceText(prefixReplacementConfig)
                .replaceText(nameReplacementConfig)
                .replaceText(suffixReplacementConfig)
                .replaceText(messageReplacementConfig);
    }

    private Component formatAsLocalChat(Component formatComponent, Player sender, Audience viewer) {
        var localChatSymbol = getFormattedValue(config.localChatSymbol());

        if (viewer instanceof Player v && sender.getUniqueId() != v.getUniqueId()) {
            if (config.localChatDistanceAsHover()) {
                var localChatDistanceAsHover = HoverEvent.showText(getFormattedValue(Objects.requireNonNull(config.localChatDistanceHoverText()).replace("<blocks>", String.valueOf((long) LocationUtil.getDistanceBetweenPlayers(sender, (Player) viewer)))));
                localChatSymbol = localChatSymbol.hoverEvent(localChatDistanceAsHover);
            }
            else {
                var localChatDistance = getFormattedValue(config.localChatDistanceText().replace("<blocks>", String.valueOf((long) LocationUtil.getDistanceBetweenPlayers(sender, (Player) viewer))));
                var localChatDistanceReplacementConfig = TextReplacementConfig.builder()
                        .matchLiteral("<localChatDistance>")
                        .replacement(localChatDistance)
                        .build();

                formatComponent = formatComponent.replaceText(localChatDistanceReplacementConfig);
            }
        }

        var localChatSymbolReplacementConfig = TextReplacementConfig.builder()
                .matchLiteral(chatSymbolTemplate)
                .replacement(localChatSymbol)
                .build();

        return formatComponent.replaceText(localChatSymbolReplacementConfig);
    }

    private Component formatAsGlobalChat(Component formatComponent) {
        var globalChatSymbolReplacementConfig = TextReplacementConfig.builder()
                .matchLiteral(chatSymbolTemplate)
                .replacement(getFormattedValue(config.globalChatSymbol()))
                .build();

        return formatComponent.replaceText(globalChatSymbolReplacementConfig);
    }

    private String getPrefix(Player player) {
        if (plugin.getVaultChat() == null) return "";
        return MessageColorizer.colorize(plugin.getVaultChat().getPlayerPrefix(player));
    }

    private String getSuffix(Player player) {
        if (plugin.getVaultChat() == null) return "";
        return MessageColorizer.colorize(plugin.getVaultChat().getPlayerSuffix(player));
    }

    private Component getFormattedValue(String value) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(value);
    }
}
