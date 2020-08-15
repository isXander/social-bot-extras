package co.uk.isxander.socialbot.commands.music;

import co.uk.isxander.socialbot.commands.Embeds;
import co.uk.isxander.socialbot.objects.ICommand;
import co.uk.isxander.socialbot.Indexes;
import co.uk.isxander.socialbot.Variables;
import co.uk.isxander.socialbot.handlers.music.PlayerManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class VolumeCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Guild guildInstance = event.getGuild();
        PlayerManager playerManager = PlayerManager.getInstance();
        MessageChannel channel = event.getChannel();
        Member member = event.getMember();
        if (Variables.getData(1, guildInstance.getId()) == null) {
            channel.sendMessage(":warning: Tell an admin that they need to set up a music role!").queue();
            return;
        }
        Role musicRole = guildInstance.getRoleById(Variables.getData(1, guildInstance.getId()));

        if (member.hasPermission(Permission.ADMINISTRATOR) || member.getRoles().contains(musicRole)) {
            if (Variables.getData(Indexes.DJROLE, guildInstance.getId()) == null) {
                channel.sendMessage(Embeds.warning("Tell an admin they need to set up a music role!")).queue();
                return;
            }
            if (!args.isEmpty()) {
                int volume;
                try {
                    volume = Integer.parseInt(args.get(0));
                }
                catch (NumberFormatException e) {
                    channel.sendMessage(Embeds.error("You cannot set the volume to this!")).queue();
                    return;
                }
                if (volume > 20 || volume < 1) {
                    channel.sendMessage(Embeds.error("You cannot set the volume to this!")).queue();
                    return;
                }
                Variables.setData(2, args.get(0), guildInstance.getId());
                playerManager.getGuildMusicManager(guildInstance).player.setVolume(volume);

                channel.sendMessage(Embeds.success("Volume has now been set to " + volume)).queue();
            }
            else {
                channel.sendMessage(Embeds.info("My volume is **" + playerManager.getGuildMusicManager(guildInstance).player.getVolume() + "**")).queue();
            }
        }
        else {
            channel.sendMessage(Embeds.error("You do not have permission to use this command!")).queue();
        }
    }

    @Override
    public String getHelp() {
        return "Changes volume of music.";
    }

    @Override
    public String getInvoke() {
        return "volume";
    }

}
