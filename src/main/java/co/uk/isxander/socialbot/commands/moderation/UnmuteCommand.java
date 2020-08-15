package co.uk.isxander.socialbot.commands.moderation;

import co.uk.isxander.socialbot.SocialBot;
import co.uk.isxander.socialbot.objects.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UnmuteCommand implements ICommand {
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

        if (args.size() != 1) {
            channel.sendMessage(getHelp()).queue();
            return;
        }

        List<Member> mentionedMembers = message.getMentionedMembers();
        Member victim = mentionedMembers.get(0);

        if (victim.getId().equals(member.getId())) {
            channel.sendMessage(":x: You can't revoke your punishment!").queue();
            return;
        }
        if (victim.getRoles().get(0).getPosition() > member.getRoles().get(0).getPosition()) {
            channel.sendMessage(":x: You cannot revoke the punishment of this player!").queue();
            return;
        }
        if (guildInstance.getMemberById(SocialBot.id).getRoles().get(0).getPosition() < victim.getRoles().get(0).getPosition()) {
            channel.sendMessage(":x: I cannot revoke the punishment of this player!").queue();
            return;
        }

        List<String> roleNames = new ArrayList<>();
        int mutedIndex = 0;
        for (int i = 0; i < guildInstance.getRoles().size(); i++) {
            String roleName = guildInstance.getRoles().get(i).getName();
            roleNames.add(roleName);
            if (roleName.equals("muted")) mutedIndex = i;
        }
        if (!roleNames.contains("muted")) {
            channel.sendMessage(":x: Nobody is muted!").queue();
            return;
        }
        else if (!victim.getRoles().contains(guildInstance.getRoles().get(mutedIndex))) {
            channel.sendMessage(":x: This user is not muted!").queue();
            return;
        }
        else {
            guildInstance.removeRoleFromMember(victim, guildInstance.getRoles().get(mutedIndex)).queue();
        }
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(0, 255, 0));
        embed.setTitle("**" + victim.getUser().getAsTag() + "**'s mute has been revoked!");
        channel.sendMessage(embed.build()).queue();
        victim.getUser().openPrivateChannel().queue(privateChannel -> {
            embed.setTitle("Your mute in **" + guildInstance.getName() + "** has been revoked!");
            privateChannel.sendMessage(embed.build()).queue();
        });
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getInvoke() {
        return "unmute";
    }
}
