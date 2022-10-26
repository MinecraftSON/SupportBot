package net.mcson.supportbot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.sourceforge.tess4j.Tesseract;
import org.simpleyaml.configuration.file.YamlFile;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.UUID;

public class AttachmentReader {
    private static final YamlFile CONFIG = Bot.config;
    private static final Tesseract tesseract = new Tesseract();

    {
        tesseract.setDatapath(System.getenv("TESSDATA_PATH"));
    }

    public static void onAttachmentSend(@Nonnull GuildMessageReceivedEvent event) {
        try {
            TextChannel channel = event.getChannel();
            List<Message.Attachment> attachments = event.getMessage().getAttachments();
            if (channel.getParent().getId().equals(CONFIG.getString("guild.supportCategoryId"))) {
                if (attachments.get(0).isImage()) {
                    Message.Attachment attachment = attachments.get(0);
                    File imgFile = new File(CONFIG.getString("bot.dataPath") + (UUID.randomUUID().toString().replace("-", "")) + ".png");
                    if (attachment.getFileExtension().equalsIgnoreCase("jpg")) {
                        BufferedImage jpg = ImageIO.read(attachment.retrieveInputStream().get());
                        ImageIO.write(jpg, "png", imgFile);
                    } else {
                        attachment.downloadToFile(imgFile);
                    }
                    String ocrText = tesseract.doOCR(imgFile);
                    if (CONFIG.getBoolean("verbose")) {
                        event.getMessage().reply(ocrText).queue(); // DEBUG: Sends OCR text in chat
                    }
                    imgFile.delete();
                    String response = IssueResponder.getResponse(ocrText);
                    if (response == null) {
                        return;
                    }
                    event.getMessage().reply(response).queue();
                } else if (attachments.get(0).getFileExtension().equalsIgnoreCase("txt") | attachments.get(0).getFileExtension().equalsIgnoreCase("log")) {
                    channel.sendMessage("^ is a text or log file!").queue();
                } else {
                    channel.sendMessage("^ is not a valid attachment!").queue();
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void onUrlSend(@Nonnull GuildMessageReceivedEvent event) {
        try {
            String message = event.getMessage().getContentRaw();
            File imgFile = new File(CONFIG.getString("bot.dataPath") + (UUID.randomUUID().toString().replace("-", "")) + ".png");
            BufferedImage urlImg = ImageIO.read(new URL(message));
            ImageIO.write(urlImg, "png", imgFile);
            String ocrText = tesseract.doOCR(imgFile);
            if (CONFIG.getBoolean("verbose")) {
                event.getMessage().reply(ocrText).queue(); // DEBUG: Sends OCR text in chat
            }
            imgFile.delete();
            String response = IssueResponder.getResponse(ocrText);
            if (response == null) {
                return;
            }
            event.getMessage().reply(response).queue();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
