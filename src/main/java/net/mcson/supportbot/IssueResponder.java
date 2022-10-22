package net.mcson.supportbot;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IssueResponder {
    private static final Logger LOGGER = LoggerFactory.getLogger(IssueResponder.class);
    private static List<Keyword> responses = new ArrayList<>();

    public static boolean containsKeyword(String message) {
        for (Keyword keyword : responses) {
            if (message.toLowerCase().contains(keyword.getKeyword())) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static String getResponse(String message) {
        for (Keyword keyword : responses) {
            if (message.toLowerCase().contains(keyword.getKeyword())) {
                return keyword.getResponse();
            }
        }
        return null;
    }

    public static void addKeyword(Keyword keyword) {
        responses.add(keyword);
        LOGGER.info("Keyword added to array");
    }

    public static void loadResponses() {
        try {
            File responsesDir = new File(Bot.config.getString("bot.response-path"));
            if (responsesDir.exists() && responsesDir.isDirectory()) {
                File[] files = responsesDir.listFiles();
                Gson gson = new Gson();
                for (File file : files) {
                    responses.add(gson.fromJson(new FileReader(file), Keyword.class));
                    LOGGER.info("Loaded keyword from: " + file.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
