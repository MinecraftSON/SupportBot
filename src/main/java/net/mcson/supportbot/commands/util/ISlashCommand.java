package net.mcson.supportbot.commands.util;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface ISlashCommand extends ICommand {

    void handleSlash(CommandContext ctx);

    String getDescription();

    CommandData getCommandData();

}
