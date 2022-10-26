package net.mcson.supportbot.commands;

import com.google.gson.Gson;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.mcson.supportbot.Bot;
import net.mcson.supportbot.IssueResponder;
import net.mcson.supportbot.Keyword;
import net.mcson.supportbot.commands.util.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class AddKeywordCmd implements IPrefixCommand, ISlashCommand {
    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        Gson gson = new Gson();

        if (args.size() == 0 || args.size() == 1) {
            ctx.getMessage().reply("Missing arguments, please use " + Bot.prefix + getName() + " \"keyword\" \"response\"").queue();
            return;
        }

        String keywordString;
        String response;

        StringBuilder keywordSB = new StringBuilder();
        StringBuilder responseSB = new StringBuilder();
        int quoteNumber = 0;
        boolean inKeyword = false;
        boolean inResponse = false;
        boolean singleWord = false;
        for (String string : args.subList(0, args.size())) {
            boolean hasBeginningQuote = false;
            boolean hasEndingQuote = false;
            if (string.startsWith("\"")) {
                hasBeginningQuote = true;
                quoteNumber++;
            }
            if (string.endsWith("\"")) {
                hasEndingQuote = true;
                quoteNumber++;
            }
            if (string.startsWith("\"") && string.endsWith("\"")) {
                singleWord = true;
            }
            if (singleWord) {
                keywordSB.append(string, 1, string.length() - 1);
                singleWord = false;
                continue;
            }
            switch (quoteNumber) {
                case 1 -> inKeyword = true;
                case 2 -> inKeyword = false;
                case 3 -> inResponse = true;
            }
            if (inKeyword) {
                logic(keywordSB, string, hasBeginningQuote, hasEndingQuote);
            } else if (inResponse) {
                logic(responseSB, string, hasBeginningQuote, hasEndingQuote);
            }
        }
        if (quoteNumber != 4) {
            ctx.getMessage().reply("Error creating response, please make sure to use quotes around the keyword(s) and response").queue();
            return;
        }

        keywordString = keywordSB.toString();
        response = responseSB.toString();
        Keyword keyword = new Keyword(keywordString, response);
        saveToJson(ctx, gson, keyword);

        ctx.getMessage().reply("Successfully created new keyword response!").queue();
    }

    @Override
    public void handleSlash(CommandContext ctx) {
        SlashCommandEvent event = ctx.getSlashEvent();
        Gson gson = new Gson();
        Keyword keyword = new Keyword(event.getOption("keyword").getAsString(), event.getOption("response").getAsString());
        saveToJson(ctx, gson, keyword);

        ctx.getHook().setEphemeral(true).sendMessage("Successfully created new keyword response!").queue();

    }

    private void saveToJson(CommandContext ctx, Gson gson, Keyword keyword) {
        try {
            String json = gson.toJson(keyword);
            File responseFile = new File(Bot.config.getString("bot.response-path") + ctx.getSlashEvent().getOption("name").getAsString() + ".json");
            if (responseFile.exists()) {
                if (ctx.getArgs() == null) {
                    ctx.getHook().setEphemeral(true).sendMessage("Keyword with that name already exists!").queue();
                } else {
                    ctx.getMessage().reply("Keyword with that name already exists!").queue();
                }
                return;
            }
            Writer writer = new FileWriter(responseFile);
            writer.write(json);
            writer.flush();
            writer.close();
            IssueResponder.addKeyword(keyword);
        } catch (IOException e) {
            e.printStackTrace();
            if (ctx.getArgs() == null) {
                ctx.getHook().setEphemeral(true).sendMessage("An unexpected error occurred while saving response").queue();
            } else {
                ctx.getMessage().reply("An unexpected error occurred while saving response").queue();
            }
        }
    }

    private void logic(StringBuilder sb, String string, boolean hasBeginningQuote, boolean hasEndingQuote) {
        if (hasBeginningQuote) {
            sb.append(string.substring(1));
            sb.append(" ");
        } else if (hasEndingQuote) {
            sb.append(string, 0, string.length() - 1);
        } else {
            sb.append(string);
            sb.append(" ");
        }
    }

    @Override
    public String getName() {
        return "addkeyword";
    }

    @Override
    public int getPermissionLevel() {
        return 2;
    }

    @Override
    public String getHelp() {
        return "Creates new keyword to autorespond to";
    }

    @Override
    public String getDescription() {
        return getHelp();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(this.getName(), this.getDescription())
                .addOption(OptionType.STRING, "name", "keyword name", true)
                .addOption(OptionType.STRING, "keyword", "keyword(s) to respond to", true)
                .addOption(OptionType.STRING, "response", "response to when the keyword is detected", true);
    }
}
