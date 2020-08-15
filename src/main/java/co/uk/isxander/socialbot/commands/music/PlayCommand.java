package co.uk.isxander.socialbot.commands.music;

import co.uk.isxander.socialbot.commands.Embeds;
import co.uk.isxander.socialbot.handlers.music.GuildMusicManager;
import co.uk.isxander.socialbot.objects.ICommand;
import co.uk.isxander.socialbot.Indexes;
import co.uk.isxander.socialbot.Variables;
import co.uk.isxander.socialbot.handlers.music.PlayerManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

public class PlayCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Guild guildInstance = event.getGuild();
        Member member = event.getMember();
        MessageChannel channel = event.getChannel();
        Message msg = event.getMessage();
        if (Variables.getData(Indexes.DJROLE, guildInstance.getId()) == null) {
            channel.sendMessage(Embeds.warning("Tell an admin they need to set up a music role!")).queue();
            return;
        }
        Role musicRole = guildInstance.getRoleById(Variables.getData(Indexes.DJROLE, guildInstance.getId()));


        if (member.hasPermission(Permission.ADMINISTRATOR) || member.getRoles().contains(musicRole)) {

            if (args.isEmpty()) {
                PauseCommand pauseCommand = new PauseCommand();
                pauseCommand.handle(args, event);
                return;
            }

            msg.suppressEmbeds(true).queue();

            if (!member.getVoiceState().inVoiceChannel()) {
                channel.sendMessage(Embeds.error("You need to be in a voice channel to use this command!")).queue();
                return;
            }
            VoiceChannel voiceChannel = member.getVoiceState().getChannel();
            AudioManager manager = guildInstance.getAudioManager();

            if (!manager.isConnected())
                manager.openAudioConnection(voiceChannel);

            PlayerManager playerManager = PlayerManager.getInstance();
            playerManager.loadAndPlay(event.getChannel(), args.get(0));
            GuildMusicManager musicManager = playerManager.getGuildMusicManager(guildInstance);
            musicManager.player.setVolume(Integer.parseInt(Variables.getData(Indexes.VOLUME, guildInstance.getId())));
        }
        else {
            channel.sendMessage(Embeds.error("You do not have permission to use this command!")).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Plays music.";
    }

    @Override
    public String getInvoke() {
        return "play";
    }
}
