package co.uk.isxander.socialbot.commands.music;

import co.uk.isxander.socialbot.objects.ICommand;
import co.uk.isxander.socialbot.handlers.music.GuildMusicManager;
import co.uk.isxander.socialbot.handlers.music.PlayerManager;
//import com.sun.jndi.toolkit.url.Uri;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PlayingCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Guild guildInstance = event.getGuild();
        PlayerManager playerManager = PlayerManager.getInstance();
        MessageChannel channel = event.getChannel();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(guildInstance);

        try {
            musicManager.player.getPlayingTrack();
        }
        catch (NullPointerException e) {
            channel.sendMessage(":x: There is nothing playing right now!").queue();
        }

        String title = musicManager.player.getPlayingTrack().getInfo().title;
        String author = musicManager.player.getPlayingTrack().getInfo().author;

        long length = musicManager.player.getPlayingTrack().getInfo().length;
        long minutes = (length / 1000) / 60;
        long seconds = (length / 1000) % 60;
        String second = Long.toString(seconds);
        if (second.length() == 1) second = "0" + second;
        String time = minutes + "m " + second + "s";

        String uriString = musicManager.player.getPlayingTrack().getInfo().uri;
        boolean isStream = musicManager.player.getPlayingTrack().getInfo().isStream;

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Now playing");
        if (!isStream) {
            embed.setDescription("Title: " + title + "\nAuthor: " + author + "\nLength: " + time);
        }
        else {
            embed.setDescription("Title: " + title + "\nAuthor: " + author + "\nLength: Livestream. Unknown Length");
        }
        embed.setFooter(new Date(System.currentTimeMillis()).toString());

        int r = rollColor();
        int g = rollColor();
        int b = rollColor();
        embed.setColor(new Color(r, g, b));

        channel.sendMessage(embed.build()).queue();

        
    }

    @Override
    public String getHelp() {
        return "Gets the current song details.";
    }

    @Override
    public String getInvoke() {
        return "playing";
    }

    public int rollColor() {
        return ThreadLocalRandom.current().nextInt(80, 256);
    }
}
