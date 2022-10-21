package net.mcson.supportbot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.mcson.supportbot.commands.util.CommandContext;
import net.mcson.supportbot.commands.util.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.List;

public class Listener extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager = new CommandManager();

    public Listener() {}

    public CommandManager getCommandManager() {
        return manager;
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        String raw = event.getMessage().getContentRaw();
        List<Message.Attachment> attachments = event.getMessage().getAttachments();

        if (raw.startsWith(Bot.prefix)) {
            manager.handle(event);
        }

        if (attachments.size() > 0) {
            if (attachments.size() == 1) {
                AttachmentReader.onAttachmentSend(event);
            } else {
                event.getMessage().reply("Too many attachments, please send one at a time and wait for a response before sending another.").queue();
            }
        } else if (raw.startsWith("http") && (raw.toLowerCase().endsWith(".jpg") || raw.toLowerCase().endsWith(".png"))) {
            AttachmentReader.onUrlSend(event);
        }

        if (IssueResponder.containsKeyword(raw) && event.getChannel().getParent().getIdLong() == Bot.config.getLong("guild.supportCategoryId")) {
            event.getMessage().reply(IssueResponder.getResponse(raw)).queue();
        }
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        manager.handle(event);
    }
}
