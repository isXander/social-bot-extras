package co.uk.isxander.socialbot.commands.moderation;

import co.uk.isxander.socialbot.SocialBot;
import co.uk.isxander.socialbot.objects.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UnbanCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Guild guildInstance = event.getGuild();
        Member member = event.getMember();
        TextChannel channel = event.getChannel();
        Message message = event.getMessage();

        if (!member.hasPermission(Permission.VOICE_MUTE_OTHERS)) {
            channel.sendMessage(":x: You do not have permission!").queue();
            return;
        }

        if(args.size() != 1) {
            channel.sendMessage(getHelp()).queue();
            return;
        }

        List<Member> mentionedMembers = message.getMentionedMembers();
        User victim = SocialBot.jda.getUserByTag(args.get(0));

        if (victim == null) {
            channel.sendMessage(":x: This user does not exist!").queue();
            return;
        }

        guildInstance.unban(victim).queue();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(0, 255, 0));
        embed.setTitle("**" + victim.getAsTag() + "** ban has been revoked!");
        channel.sendMessage(embed.build()).queue();
        victim.openPrivateChannel().queue(privateChannel -> {
            embed.setTitle("Your ban in **" + guildInstance.getName() + "** has been revoked!");
            privateChannel.sendMessage(embed.build()).queue();
        });
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getInvoke() {
        return null;
    }
}
