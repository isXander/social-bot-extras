package co.uk.isxander.socialbot.commands.music;

import co.uk.isxander.socialbot.commands.Embeds;
import co.uk.isxander.socialbot.objects.ICommand;
import co.uk.isxander.socialbot.handlers.music.PlayerManager;
import co.uk.isxander.socialbot.Indexes;
import co.uk.isxander.socialbot.Variables;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class PauseCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Guild guildInstance = event.getGuild();
        MessageChannel channel = event.getChannel();
        Member member = event.getMember();
        if (Variables.getData(Indexes.DJROLE, guildInstance.getId()) == null) {
            channel.sendMessage(Embeds.warning("Tell an admin they need to set up a music role!")).queue();
            return;
        }
        Role musicRole = guildInstance.getRoleById(Variables.getData(Indexes.DJROLE, guildInstance.getId()));

        if (member.hasPermission(Permission.ADMINISTRATOR) || member.getRoles().contains(musicRole)) {
            PlayerManager playerManager = PlayerManager.getInstance();
            AudioPlayer player = playerManager.getGuildMusicManager(guildInstance).player;
            if (player.isPaused()) {
                player.setPaused(false);
                channel.sendMessage(Embeds.success("Now playing!")).queue();
            }
            else {
                player.setPaused(true);
                channel.sendMessage(Embeds.success("Now paused!")).queue();
            }
        }
        else {
            channel.sendMessage(Embeds.error("You do not have permission to use this command!")).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Pauses the radio.";
    }

    @Override
    public String getInvoke() {
        return "pause";
    }
}
