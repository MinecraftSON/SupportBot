package net.mcson.supportbot.commands;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.mcson.supportbot.Bot;
import net.mcson.supportbot.commands.util.CommandContext;
import net.mcson.supportbot.commands.util.ISlashCommand;

import java.io.IOException;

public class VerboseCmd implements ISlashCommand {
    @Override
    public String getName() {
        return "verbose";
    }

    @Override
    public int getPermissionLevel() {
        return 3;
    }

    @Override
    public String getHelp() {
        return "Sets the bot to verbose mode";
    }

    @Override
    public void handleSlash(CommandContext ctx) {
        boolean verbose = Bot.config.getBoolean("verbose");
        Bot.config.set("verbose", !verbose);
        try {
            Bot.config.save();
        } catch (IOException e) {
            ctx.getHook().sendMessage("Error saving config").queue();
            e.printStackTrace();
        }
        ctx.getHook().sendMessage("Verbose: " + !verbose).queue();
    }

    @Override
    public String getDescription() {
        return this.getHelp();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(this.getName(), this.getDescription());
    }
}
