package net.mcson.supportbot.util;

import net.dv8tion.jda.api.entities.Message;
import net.mcson.supportbot.Bot;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class ImageReader {

    public static void downloadImage() {
        AttachmentReader.attachment.get(0).downloadToFile(Bot.config.getString("bot.dataPath") + "image." + AttachmentReader.attachment.get(0).getFileExtension());
    }

    public static String extractImage(String filePath) {
        File imageFile = new File(filePath);
        ITesseract instance = new Tesseract();

        try {
            String result = instance.doOCR(imageFile);
            return result;
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
            return "Error while reading image";
        }
    }
}
