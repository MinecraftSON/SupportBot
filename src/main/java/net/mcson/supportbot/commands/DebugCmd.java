package net.mcson.supportbot.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.mcson.supportbot.IssueResponder;
import net.mcson.supportbot.Keyword;
import net.mcson.supportbot.commands.util.CommandContext;
import net.mcson.supportbot.commands.util.ISlashCommand;

import java.util.List;

public class DebugCmd implements ISlashCommand {
    @Override
    public String getName() {
        return "debug";
    }

    @Override
    public int getPermissionLevel() {
        return 3;
    }

    @Override
    public String getHelp() {
        return "Helps debugging";
    }

    @Override
    public void handleSlash(CommandContext ctx) {
        SlashCommandEvent event = ctx.getSlashEvent();
        switch (event.getOption("option").getAsString()) {
            case "envs" -> {
                String path = System.getenv("PATH");
                String javaHome = System.getenv("JAVA_HOME");
                String tessPath = System.getenv("TESSDATA_PATH");
                String tessPrefix = System.getenv("TESSDATA_PREFIX");
                ctx.getHook().setEphemeral(true).sendMessage(path + "\n" + javaHome + "\n" + tessPath + "\n" + tessPrefix).queue();
            }
            case "responses" -> {
                List<Keyword> responses = IssueResponder.getResponses();
                StringBuilder sb = new StringBuilder();
                for (Keyword keyword : responses) {
                    sb.append(keyword.getKeyword());
                    sb.append("\n");
                }
                ctx.getHook().setEphemeral(true).sendMessage(sb.toString()).queue();
            }
            default -> ctx.getHook().sendMessage("Unknown option, please use a provided option").queue();
        }
    }

    @Override
    public String getDescription() {
        return this.getHelp();
    }

    @Override
    public CommandData getCommandData() {
        Command.Choice[] choices = new Command.Choice[]{
                new Command.Choice("envs", "envs"),
                new Command.Choice("responses", "responses")
                //new Command.Choice("", "")
        };
        return new CommandData(this.getName(), this.getDescription())
                .addOptions(new OptionData(OptionType.STRING, "option", "debugging options", true)
                        .addChoices(choices));
    }
}
