package co.uk.isxander.socialbot.commands.music;

import co.uk.isxander.socialbot.commands.Embeds;
import co.uk.isxander.socialbot.handlers.Listener;
import co.uk.isxander.socialbot.handlers.music.GuildMusicManager;
import co.uk.isxander.socialbot.handlers.music.PlayerManager;
import co.uk.isxander.socialbot.handlers.music.TrackScheduler;
import co.uk.isxander.socialbot.objects.ICommand;
import co.uk.isxander.socialbot.Variables;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class QueueCommand implements ICommand {
    private static Message queueMessage;
    public static int page;
    public static GuildMessageReceivedEvent previousEvent;
    public static Message previousMsg;

    @Override
    public void handle(final List<String> args, final GuildMessageReceivedEvent event) {
        if (args.isEmpty()) {
            QueueCommand.page = 1;
        }
        else {
            QueueCommand.page = Integer.parseInt(args.get(0));
        }
        final Guild guildInstance = event.getGuild();
        final PlayerManager playerManager = PlayerManager.getInstance();
        final MessageChannel channel = event.getChannel();
        QueueCommand.previousEvent = event;
        QueueCommand.queueMessage = event.getMessage();
        final Member member = event.getMember();
        final GuildMusicManager musicManager = playerManager.getGuildMusicManager(guildInstance);
        if (Variables.getData(1, guildInstance.getId()) == null) {
            channel.sendMessage(":warning: Tell an admin that they need to set up a music role!").queue();
            return;
        }
        final Role musicRole = guildInstance.getRoleById(Variables.getData(1, guildInstance.getId()));
        if (member.hasPermission(Permission.ADMINISTRATOR) || member.getRoles().contains(musicRole)) {
            channel.sendMessage("**Here's your queue**\n" + getQueue(event)).queue(message -> {
                QueueCommand.previousMsg = message;
                Listener.reactionHandler.addReactionListener(message, "\u2b06\ufe0f", QueueCommand::upArrowEvent);
                Listener.reactionHandler.addReactionListener(message, "\u2b07\ufe0f", QueueCommand::downArrowEvent);
            });
        }
        else {
            channel.sendMessage(Embeds.error("You do not have permission to use this command!")).queue();
        }
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getInvoke() {
        return "queue";
    }

    public static String getQueue(final GuildMessageReceivedEvent event) {
        final Guild guildInstance = event.getGuild();
        final PlayerManager playerManager = PlayerManager.getInstance();
        final GuildMusicManager musicManager = playerManager.getGuildMusicManager(guildInstance);
        final TrackScheduler scheduler = musicManager.scheduler;
        final byte authorIndex = 60;
        final byte timeIndex = 75;
        final byte maxLines = 11;
        final byte threshold = 0;
        final int maxTitleLength = 50 - threshold;
        final int maxAuthorLength = 50 - threshold;
        final StringBuilder sb = new StringBuilder();
        sb.append("```nimrod\n");
        sb.append("Page ").append(QueueCommand.page).append("\n");
        AudioTrackInfo currentTrackInfo = musicManager.player.getPlayingTrack().getInfo();
        long length = currentTrackInfo.length;
        long minutes = length / 1000L / 60L;
        long seconds = length / 1000L % 60L;
        String second = Long.toString(seconds);
        if (second.length() == 1) {
            second = "0" + second;
        }
        String time = minutes + "m " + second + "s";
        String title = "(current track) " + currentTrackInfo.title;
        String author = currentTrackInfo.author;
        if (title.length() > maxTitleLength) {
            if (title.charAt(maxAuthorLength - threshold - 1) == ' ') {
                title = title.substring(0, maxTitleLength - threshold - 1) + "... ";
            }
            else {
                title = title.substring(0, maxTitleLength - threshold) + "...";
            }
        }
        if (author.length() > maxAuthorLength) {
            if (author.charAt(maxAuthorLength - threshold - 1) == ' ') {
                author = author.substring(0, maxAuthorLength - threshold - 1);
            }
            else {
                author = author.substring(0, maxAuthorLength - threshold) + "...";
            }
        }
        StringBuilder authorAlign = new StringBuilder();
        while (title.length() + authorAlign.length() < authorIndex) {
            authorAlign.append(" ");
        }
        String titleSpace = authorAlign.toString();
        StringBuilder timeAlign = new StringBuilder();
        while (author.length() + timeAlign.length() < timeIndex) {
            timeAlign.append(" ");
        }
        String authorSpace = timeAlign.toString();
        String text = title + titleSpace + author + authorSpace + time + "\n";
        sb.append(text);
        if (scheduler.getTrackList().size() <= maxLines) {
            for (final AudioTrack track : scheduler.getTrackList()) {
                currentTrackInfo = track.getInfo();
                length = currentTrackInfo.length;
                minutes = length / 1000L / 60L;
                seconds = length / 1000L % 60L;
                second = Long.toString(seconds);
                if (second.length() == 1) {
                    second = "0" + second;
                }
                time = minutes + "m " + second + "s";
                title = currentTrackInfo.title;
                author = currentTrackInfo.author;
                if (title.length() > maxTitleLength) {
                    title = title.substring(0, maxTitleLength - threshold) + "...  ";
                }
                if (author.length() > maxAuthorLength) {
                    author = author.substring(0, maxAuthorLength - threshold) + "...  ";
                }
                authorAlign = new StringBuilder();
                while (title.length() + authorAlign.length() < authorIndex) {
                    authorAlign.append(" ");
                }
                titleSpace = authorAlign.toString();
                timeAlign = new StringBuilder();
                while (author.length() + timeAlign.length() < timeIndex) {
                    timeAlign.append(" ");
                }
                authorSpace = timeAlign.toString();
                text = title + titleSpace + author + authorSpace + time + "\n";
                sb.append(text);
            }
        }
        else {
            final List<AudioTrack> trackList = scheduler.getTrackList();
            for (int currentIndex = QueueCommand.page * maxLines; currentIndex < QueueCommand.page * maxLines + maxLines; ++currentIndex) {
                try {
                    currentTrackInfo = trackList.get(currentIndex).getInfo();
                }
                catch (IndexOutOfBoundsException e) {
                    break;
                }
                length = currentTrackInfo.length;
                minutes = length / 1000L / 60L;
                second = Long.toString(seconds);
                if (second.length() == 1) {
                    second = "0" + second;
                }
                time = minutes + "m " + second + "s";
                title = currentTrackInfo.title;
                author = currentTrackInfo.author;
                if (title.length() > maxTitleLength) {
                    title = title.substring(0, maxTitleLength - threshold) + "...  ";
                }
                if (author.length() > maxAuthorLength) {
                    author = author.substring(0, maxAuthorLength - threshold) + "...  ";
                }
                authorAlign = new StringBuilder();
                while (title.length() + authorAlign.length() < authorIndex) {
                    authorAlign.append(" ");
                }
                titleSpace = authorAlign.toString();
                timeAlign = new StringBuilder();
                while (author.length() + timeAlign.length() < timeIndex) {
                    timeAlign.append(" ");
                }
                authorSpace = timeAlign.toString();
                text = title + titleSpace + author + authorSpace + time + "\n";
                sb.append(text);
            }
            sb.append("\nAnd ").append(trackList.size() - maxLines).append(" more...\n");
        }
        sb.append("```");
        return sb.toString();
    }

    public static void upArrowEvent() {
        if (QueueCommand.page == 1) {
            return;
        }
        QueueCommand.page--;
        Listener.reactionHandler.removeReactionListener(QueueCommand.previousEvent.getMessage(), "\u2b06\ufe0f", QueueCommand::upArrowEvent);
        System.out.println(QueueCommand.page);
        QueueCommand.previousMsg.editMessage("**Here's your queue**\n" + getQueue(QueueCommand.previousEvent)).queue(message -> {
            Listener.reactionHandler.addReactionListener(message, "\u2b06\ufe0f", QueueCommand::upArrowEvent);
            Listener.reactionHandler.addReactionListener(message, "\u2b07\ufe0f", QueueCommand::downArrowEvent);
        });
    }

    public static void downArrowEvent() {
        QueueCommand.page++;
        Listener.reactionHandler.removeReactionListener(QueueCommand.previousEvent.getMessage(), "\u2b07\ufe0f", QueueCommand::upArrowEvent);
        System.out.println(QueueCommand.page);
        QueueCommand.previousMsg.editMessage("**Here's your queue**\n" + getQueue(QueueCommand.previousEvent)).queue(message -> {
            Listener.reactionHandler.addReactionListener(message, "\u2b06\ufe0f", QueueCommand::upArrowEvent);
            Listener.reactionHandler.addReactionListener(message, "\u2b07\ufe0f", QueueCommand::downArrowEvent);
        });
    }
}
