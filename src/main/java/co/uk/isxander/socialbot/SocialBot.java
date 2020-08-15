package co.uk.isxander.socialbot;

import co.uk.isxander.socialbot.handlers.CommandHandler;
import co.uk.isxander.socialbot.handlers.Listener;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.Console;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SocialBot extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger("SocialBot Init");
    public static String id;
    public static JDA jda;

    private SocialBot(String token, String presence) throws LoginException, InterruptedException {
        CommandHandler commandHandler = new CommandHandler();
        Listener listener = new Listener(commandHandler);

        jda = JDABuilder.create(token,
                GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS)
                .addEventListeners(listener)
                .setActivity(Activity.watching("undefined"))
                .build();

        jda.awaitReady();

        id = jda.getSelfUser().getId();

        presence = presence.replace("%guildcount%", Integer.toString(jda.getGuilds().size()));
        jda.getPresence().setActivity(Activity.watching(presence));
    }

    public static void main(String[] args) throws LoginException, InterruptedException, IOException {
//        Console console = System.console();
//        if (console == null && !GraphicsEnvironment.isHeadless()) {
//            String filename = SocialBot.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
//            Runtime.getRuntime().exec(new String[] { "cmd", "/c", "start", "cmd", "/k", "java -jar \"" + filename + "\""} );
//            logger.debug("Console not open");
//            System.exit(0);
//        }

        JSONParser parser = new JSONParser();
        String token = null;
        String presence = null;

        try {
            JSONArray a = (JSONArray) parser.parse(new FileReader("./config/socialbotconfig.json"));

            for (Object o : a) {
                JSONObject prop = (JSONObject) o;

                token = (String) prop.get("token");
                presence = (String) prop.get("presence");

            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("Could not find JSON! Shutting down!");
            System.exit(-1);
        }
        catch (ParseException e) {
            e.printStackTrace();
            logger.error( "Could not parse JSON correctly! Shutting down!");
            System.exit(-2);
        }
        catch (IOException e) {
            e.printStackTrace();
            logger.error("IOException was triggered while trying to parse JSON! Shutting down!");
            System.exit(-3);
        }

        new SocialBot(token, presence);
    }

}
