package co.uk.isxander.socialbot.commands.music;

import co.uk.isxander.socialbot.commands.Embeds;
import co.uk.isxander.socialbot.handlers.music.PlayerManager;
import co.uk.isxander.socialbot.objects.ICommand;
import co.uk.isxander.socialbot.Indexes;
import co.uk.isxander.socialbot.Variables;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class ClearCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Guild guildInstance = event.getGuild();
        Member member = event.getMember();
        MessageChannel channel = event.getChannel();

        if (Variables.getData(Indexes.DJROLE, guildInstance.getId()) == null) {
            channel.sendMessage(Embeds.warning("Tell an admin they need to set up a music role!")).queue();
            return;
        }
        Role musicRole = guildInstance.getRoleById(Variables.getData(Indexes.DJROLE, guildInstance.getId()));

        if (member.hasPermission(Permission.ADMINISTRATOR) || member.getRoles().contains(musicRole)) {
            PlayerManager playerManager = PlayerManager.getInstance();

            playerManager.getGuildMusicManager(guildInstance).scheduler.setLoop(false);
            playerManager.getGuildMusicManager(guildInstance).scheduler.clearQueue();
            playerManager.getGuildMusicManager(guildInstance).player.stopTrack();

            channel.sendMessage(Embeds.success("Queue has been cleared!")).queue();
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
        return "clear";
    }
}
