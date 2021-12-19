package net.mcson.supportbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.simpleyaml.configuration.file.YamlFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;

public class Bot {
    public static JDA jda;
    public static YamlFile config;
    public static String prefix;
    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);

    public static void main(String[] args) throws Exception {
        net.mcson.supportbot.Config.loadConfig();
        String token = net.mcson.supportbot.Config.getConfig().getString("bot.token");
        config = net.mcson.supportbot.Config.getConfig();
        prefix = net.mcson.supportbot.Config.getConfig().getString("bot.prefix");

        jda = JDABuilder.createDefault(token)
                .disableCache(EnumSet.of(
                        CacheFlag.CLIENT_STATUS,
                        CacheFlag.ACTIVITY,
                        CacheFlag.EMOTE
                ))
                .enableCache(CacheFlag.VOICE_STATE)
                .addEventListeners(new Listener())
                .build();
        getActivity();
    }

    public static void getActivity() {
        if (config.getString("bot.activity").equalsIgnoreCase("playing")) {
            jda.getPresence().setActivity(Activity.playing(config.getString("bot.action")));
        } else if (config.getString("bot.activity").equalsIgnoreCase("watching")) {
            jda.getPresence().setActivity(Activity.watching(config.getString("bot.action")));
        } else if (config.getString("bot.activity").equalsIgnoreCase("competing")) {
            jda.getPresence().setActivity(Activity.competing(config.getString("bot.action")));
        } else if (config.getString("bot.activity").equalsIgnoreCase("listening")) {
            jda.getPresence().setActivity(Activity.listening(config.getString("bot.action")));
        } else if (config.getString("bot.activity").equalsIgnoreCase("debug")) {
            jda.getPresence().setActivity(Activity.listening("my prefix " + prefix));
        } else {
            jda.getPresence().setActivity(Activity.playing("Minecraft"));
        }
    }
}
