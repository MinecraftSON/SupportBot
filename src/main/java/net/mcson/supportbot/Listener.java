package net.mcson.supportbot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.mcson.supportbot.commands.CommandManager;
import net.mcson.supportbot.util.AttachmentReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.List;

public class Listener extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager = new CommandManager();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();
        AttachmentReader ar = new AttachmentReader();

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        String raw = event.getMessage().getContentRaw();
        List<Message.Attachment> attachment = event.getMessage().getAttachments();

        if (raw.startsWith(Bot.prefix)) {
            manager.handle(event);
        }
        if (attachment.size() > 0) {
            ar.onAttachmentSend(event);
        }
    }
}
