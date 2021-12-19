package net.mcson.supportbot.util;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.mcson.supportbot.Bot;

import java.util.List;

public class AttachmentReader {

    public static List<Message.Attachment> attachment;
    private TextChannel channel;

    public void onAttachmentSend(GuildMessageReceivedEvent event) {
        channel = event.getChannel();
        attachment = event.getMessage().getAttachments();

        if (channel.getParent().getId().equals(Bot.config.getString("guild.supportCategoryId"))) {
            //if (!(attachment.size() < 1)) {
                if (attachment.get(0).isImage()) {
                    imageToString();
                } else if (attachment.get(0).getFileExtension().equalsIgnoreCase("txt") | attachment.get(0).getFileExtension().equalsIgnoreCase("log")) {
                    channel.sendMessage("^ is a text or log file!").queue();
                } else {
                    channel.sendMessage("^ is not a valid attachment!").queue();
                }
            //} else {
            //    channel.sendMessage("Too many attachments, please send one at a time and wait for a response before sending another.").queue();
            //}
        }
    }

    public void imageToString() {
        ImageReader.downloadImage();

        channel.sendMessage(ImageReader.extractImage(Bot.config.getString("bot.dataPath") + "image." + attachment.get(0).getFileExtension())).queue();

        //return ocrText;
    }
}
