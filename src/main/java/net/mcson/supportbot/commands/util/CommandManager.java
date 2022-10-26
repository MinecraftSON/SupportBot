package net.mcson.supportbot.commands.util;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.mcson.supportbot.Bot;
import net.mcson.supportbot.commands.AddKeywordCmd;
import net.mcson.supportbot.commands.DebugCmd;
import net.mcson.supportbot.commands.HelpCmd;
import net.mcson.supportbot.commands.VerboseCmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static net.mcson.supportbot.Bot.jda;

public class CommandManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandManager.class);
    private final List<ICommand> commands = new ArrayList<>();
    private final List<IPrefixCommand> prefixCommands = new ArrayList<>();
    private final List<ISlashCommand> slashCommands = new ArrayList<>();

    public CommandManager() {
        //Add to this list in alphabetical order
        addCommand(new AddKeywordCmd());
        addCommand(new DebugCmd());
        addCommand(new HelpCmd(this));
        addCommand(new VerboseCmd());
    }

    private void addCommand(ICommand cmd) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present");
        }

        if (cmd instanceof IPrefixCommand) {
            prefixCommands.add((IPrefixCommand) cmd);
        }
        if (cmd instanceof ISlashCommand) {
            slashCommands.add((ISlashCommand) cmd);
        }
        commands.add(cmd);
    }

    public void registerSlashCommands() {
        if (slashCommands.size() > 0) {
            List<CommandData> cmdDataList = new ArrayList<>();
            for (ISlashCommand slashCmd : slashCommands) {
                cmdDataList.add(slashCmd.getCommandData());
            }
            jda.updateCommands().addCommands(cmdDataList).queue();
        }

    }

    @Nullable
    public IPrefixCommand getPrefixCommand(String search) {
        String searchLower = search.toLowerCase();

        for (IPrefixCommand cmd : prefixCommands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }
        LOGGER.warn("Returned null for command search: " + searchLower);
        return null;
    }

    @Nullable
    public ISlashCommand getSlashCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ISlashCommand cmd : slashCommands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }
        return null;
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    public List<IPrefixCommand> getPrefixCommands() {
        return prefixCommands;
    }

    public List<ISlashCommand> getSlashCommands() {
        return slashCommands;
    }

    public boolean hasPermission(Member member, ICommand cmd) {
        List<Role> memberRoles = member.getRoles();
        int permLevel = 0;

        for (Role memberRole : memberRoles) {
            switch (memberRole.getName().toLowerCase()) {
                case "helper", "1":
                    if (permLevel < 1) permLevel = 1;
                    continue;
                case "moderator", "mod", "2":
                    if (permLevel < 2) permLevel = 2;
                    continue;
                case "administrator", "admin", "3":
                    if (permLevel < 3) permLevel = 3;
                    continue;
                case "owner", "4":
                    if (permLevel < 4) permLevel = 4;
            }
        }
        return permLevel >= cmd.getPermissionLevel();
    }

    public void handle(GuildMessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(Bot.config.getString("bot.prefix")), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        IPrefixCommand cmd = getPrefixCommand(invoke);

        assert event.getMember() != null;
        assert cmd != null;
        if (!hasPermission(event.getMember(), cmd)) {
            event.getMessage().reply("You do not have permission to use this command.").queue();
            return;
        }

        event.getChannel().sendTyping().queue();
        List<String> args = Arrays.asList(split).subList(1, split.length);

        CommandContext ctx = new CommandContext(event, args);

        cmd.handle(ctx);
    }

    public void handle(SlashCommandEvent event) {
        String invoke = event.getName();
        ISlashCommand cmd = getSlashCommand(invoke);

        if (cmd != null) {
            event.deferReply().queue();

            assert event.getMember() != null;
            if (!hasPermission(event.getMember(), cmd)) {
                event.getHook().sendMessage("You do not have permission to use this command.").queue();
                return;
            }

            CommandContext ctx = new CommandContext(event);

            cmd.handleSlash(ctx);
        }
    }
}
