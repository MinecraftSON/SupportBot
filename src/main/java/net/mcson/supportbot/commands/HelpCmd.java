package net.mcson.supportbot.commands;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.mcson.supportbot.Bot;
import net.mcson.supportbot.commands.util.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HelpCmd implements ISlashCommand {
    private final CommandManager manager;

    public HelpCmd(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void handleSlash(CommandContext ctx) {

        StringBuilder sb = new StringBuilder();
        EmbedBuilder eb = new EmbedBuilder();

        OptionMapping option = ctx.getSlashEvent().getOption("command");
        String cmd = null;
        if (option != null) {
            cmd = option.getAsString();
        }

        if (cmd == null){
            IPrefixCommand prefixCommand = manager.getPrefixCommand(ctx.getSlashEvent().getName());
            ISlashCommand slashCommand = manager.getSlashCommand(ctx.getSlashEvent().getName());

            if (prefixCommand == null && slashCommand == null) {
                ctx.getSlashEvent().getHook().sendMessage("Nothing found for " + ctx.getSlashEvent()).queue();
                return;
            }

            eb.setTitle("Commands");

            manager.getCommands().stream().map(ICommand::getName).forEach(
                    (it) -> sb.append('`').append(Bot.config.getString("bot.prefix")).append(it).append("`\n")
            );

            eb.addField("", sb.toString(), false);
        } else {
            ICommand command = manager.getSlashCommand(cmd);

            if (command != null) {
                eb.setTitle(command.getName());
                eb.addField("", command.getHelp(), false);
            } else {
                eb.setTitle("Error");
                eb.addField("", "Unknown command, run `/help` to see a list of available commands", false);
            }
        }

        ctx.getSlashEvent().getHook().sendMessageEmbeds(eb.build()).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "Shows the list of bot commands\n" +
                "Usage: `" + Bot.config.getString("bot.prefix") + "help [command]`";
    }


    @Override
    public String getDescription() {
        return "Lists bot commands";
    }

    @Override
    public @NotNull CommandData getCommandData() {
        List<Command.Choice> choices = new ArrayList<>();

        for (IPrefixCommand cmd : manager.getPrefixCommands()) {
            choices.add(new Command.Choice(cmd.getName(), cmd.getName()));
        }

        for (ISlashCommand cmd : manager.getSlashCommands()) {
            choices.add(new Command.Choice(cmd.getName(), cmd.getName()));
        }

        OptionData option = new OptionData(OptionType.STRING, "command", "Gives info on specified command", false);
        option.addChoices(choices);

        return new CommandData(this.getName(), this.getDescription())
                .addOptions(option);
    }
}
