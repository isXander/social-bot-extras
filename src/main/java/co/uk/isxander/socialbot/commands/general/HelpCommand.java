package co.uk.isxander.socialbot.commands.general;

import co.uk.isxander.socialbot.commands.Embeds;
import co.uk.isxander.socialbot.objects.ICommand;
import co.uk.isxander.socialbot.Variables;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class HelpCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        Guild guildInstance = event.getGuild();

        String prefix = Variables.getData(0, guildInstance.getId());

        if (args.isEmpty()) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(new Color(250, 250, 250));
            embed.setTitle("Help");
            embed.addField("Music", prefix + "help music", true);
            channel.sendMessage(embed.build()).queue();
        }
        else if (args.get(0).equals("music")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(new Color(250, 250, 250));
            embed.setTitle("Music Commands");
            embed.addField(prefix + "play", "Queues songs\nUsage: " + prefix + "play [song]\nInfo: If there's no song, it will pause/play the song.", true);
            embed.addField(prefix + "leave", "Leaves the voice channel\nUsage: " + prefix + "leave", false);
            embed.addField(prefix + "pause", "Pauses/plays the current song\nUsage: " + prefix + "pause", false);
            embed.addField(prefix + "loop", "Loops the current track\nUsage: " + prefix + "loop", false);
            embed.addField(prefix + "queue", "Shows the whole queue\nUsage: " + prefix + "queue [page]", false);
            embed.addField(prefix + "playing", "Gives details about the current track\nUsage " + prefix + "playing", false);
            embed.addField(prefix + "skip", "Skips the current song\nUsage: " + prefix + "skip\nInfo: Bypasses the loop command", false);
            embed.addField(prefix + "shuffle", "Shuffles the queue\nUsage: " + prefix + "shuffle", false);
            embed.addField(prefix + "volume", "Changes the volume\nUsage: " + prefix + "volume <volume>\nInfo: 1 - 20", false);
            embed.addField(prefix + "clear", "Clears the queue and stops the current song\nUsage: " + prefix + "clear", false);
            channel.sendMessage(embed.build()).queue();
        }
        else {
            channel.sendMessage(Embeds.error("Unknown help category!")).queue();
        }
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getInvoke() {
        return "help";
    }
}
