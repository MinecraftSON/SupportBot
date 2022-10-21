package net.mcson.supportbot.commands.util;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.sharding.ShardManager;

public interface ICommandContext {
    /**
     * Returns the {@link net.dv8tion.jda.api.entities.Guild} for the current command/event
     *
     * @return the {@link net.dv8tion.jda.api.entities.Guild} for this command/event
     */
    default Guild getGuild() {
        if (this.getEvent() == null) {
            return this.getSlashEvent().getGuild();
        } else if (this.getSlashEvent() == null) {
            return this.getEvent().getGuild();
        }
        return null;
    }

    /**
     * Returns the {@link net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent message event} that was received for this instance
     *
     * @return the {@link net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent message event} that was received for this instance
     */
    GuildMessageReceivedEvent getEvent();

    /**
     * Returns the {@link net.dv8tion.jda.api.events.interaction.SlashCommandEvent interaction event} that was received for this instance
     *
     * @returns the {@link net.dv8tion.jda.api.events.interaction.SlashCommandEvent interaction event} that was received for this instance
     */
    SlashCommandEvent getSlashEvent();

    /**
     * Returns the {@link net.dv8tion.jda.api.entities.TextChannel channel} that the message for this event was send in
     *
     * @return the {@link net.dv8tion.jda.api.entities.TextChannel channel} that the message for this event was send in
     */
    default TextChannel getChannel() {
        if (this.getEvent() == null) {
            return this.getSlashEvent().getTextChannel();
        } else if (this.getSlashEvent() == null) {
            return this.getEvent().getChannel();
        }
        return null;
    }

    /**
     * Returns the {@link net.dv8tion.jda.api.entities.Message message} that triggered this event
     *
     * @return the {@link net.dv8tion.jda.api.entities.Message message} that triggered this event
     */
    default Message getMessage() {
        if (this.getEvent() == null) {
            return null;
        } else if (this.getSlashEvent() == null) {
            return this.getEvent().getMessage();
        }
        return null;
    }

    default String getCommandString() {
        if (this.getEvent() == null) {
            return this.getSlashEvent().getCommandString();
        }
        return null;
    }

    /**
     * Returns the {@link net.dv8tion.jda.api.entities.User author} of the message as user
     *
     * @return the {@link net.dv8tion.jda.api.entities.User author} of the message as user
     */
    default User getAuthor() {
        if (this.getEvent() == null) {
            return this.getSlashEvent().getUser();
        } else if (this.getSlashEvent() == null) {
            return this.getEvent().getAuthor();
        }
        return null;
    }
    /**
     * Returns the {@link net.dv8tion.jda.api.entities.Member author} of the message as member
     *
     * @return the {@link net.dv8tion.jda.api.entities.Member author} of the message as member
     */
    default Member getMember() {
        if (this.getEvent() == null) {
            return this.getSlashEvent().getMember();
        } else if (this.getSlashEvent() == null) {
            return this.getEvent().getMember();
        }
        return null;
    }

    /**
     * Returns the {@link net.dv8tion.jda.api.interactions.InteractionHook interaction hook} of the slash command
     *
     * @return the {@link net.dv8tion.jda.api.interactions.InteractionHook interaction hook} of the slash command
     */
    default InteractionHook getHook() {
        return this.getSlashEvent().getHook();
    }

    /**
     * Returns the current {@link net.dv8tion.jda.api.JDA jda} instance
     *
     * @return the current {@link net.dv8tion.jda.api.JDA jda} instance
     */
    default JDA getJDA() {
        if (this.getEvent() == null) {
            return this.getSlashEvent().getJDA();
        } else if (this.getSlashEvent() == null) {
            return this.getEvent().getJDA();
        }
        return null;
    }

    /**
     * Returns the current {@link net.dv8tion.jda.api.sharding.ShardManager} instance
     *
     * @return the current {@link net.dv8tion.jda.api.sharding.ShardManager} instance
     */
    default ShardManager getShardManager() {
        return this.getJDA().getShardManager();
    }

    /**
     * Returns the {@link net.dv8tion.jda.api.entities.User user} for the currently logged in account
     *
     * @return the {@link net.dv8tion.jda.api.entities.User user} for the currently logged in account
     */
    default User getSelfUser() {
        return this.getJDA().getSelfUser();
    }

    /**
     * Returns the {@link net.dv8tion.jda.api.entities.Member member} in the guild for the currently logged in account
     *
     * @return the {@link net.dv8tion.jda.api.entities.Member member} in the guild for the currently logged in account
     */
    default Member getSelfMember() {
        return this.getGuild().getSelfMember();
    }
}
