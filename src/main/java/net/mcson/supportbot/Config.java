package net.mcson.supportbot;

import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;

public class Config {
    private static final YamlFile ymlFile = new YamlFile("config.yml");

    public static void loadConfig() {
        try {
            if (!ymlFile.exists()) {
                System.out.println("Configuration file has been created\n");
                ymlFile.createNewFile(true);
            } else {
                System.out.println("config.yml already exists, loading configuration file...\n");
            }
            ymlFile.loadWithComments();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        ymlFile.addDefault("bot.prefix", "!");
        ymlFile.addDefault("bot.token", "BotToken122333444455555666666777777788888888999999999");
        ymlFile.addDefault("bot.activity", "playing");
        ymlFile.addDefault("bot.action", "Minecraft");
        ymlFile.addDefault("bot.dataPath", "./data/");
        ymlFile.addDefault("bot.response-path", "./responses/");
        ymlFile.addDefault("permission.founder-role-id", "000000000000000000");
        ymlFile.addDefault("permission.admin-role-id", "000000000000000000");
        ymlFile.addDefault("permission.mod-role-id", "000000000000000000");
        ymlFile.addDefault("guild.supportCategoryId", "000000000000000000");
        ymlFile.addDefault("verbose", false);

        try {
            ymlFile.save();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public static YamlFile getConfig() {
        return ymlFile;
    }
}
