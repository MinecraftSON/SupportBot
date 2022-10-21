package net.mcson.supportbot.commands.util;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.mcson.supportbot.commands.util.ICommandContext;

import java.util.List;

public class CommandContext implements ICommandContext {
    private final GuildMessageReceivedEvent event;
    private final SlashCommandEvent slashEvent;
    private final List<String> args;

    public CommandContext(GuildMessageReceivedEvent event, List<String> args) {
        this.event = event;
        this.slashEvent = null;
        this.args = args;
    }

    public CommandContext(SlashCommandEvent event) {
        this.event = null;
        this.slashEvent = event;
        this.args = null;
    }

    @Override
    public GuildMessageReceivedEvent getEvent() {
        return this.event;
    }

    @Override
    public SlashCommandEvent getSlashEvent() {
        return this.slashEvent;
    }

    public List<String> getArgs() {
        return this.args;
    }
}