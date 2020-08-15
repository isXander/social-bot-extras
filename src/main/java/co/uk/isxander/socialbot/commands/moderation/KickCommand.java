package co.uk.isxander.socialbot.commands.moderation;

import co.uk.isxander.socialbot.SocialBot;
import co.uk.isxander.socialbot.commands.Embeds;
import co.uk.isxander.socialbot.objects.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class KickCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Guild guildInstance = event.getGuild();
        Member member = event.getMember();
        TextChannel channel = event.getChannel();
        Message message = event.getMessage();

        if (!member.hasPermission(Permission.KICK_MEMBERS)) {
            channel.sendMessage(Embeds.error("You do not have permission to use this command!")).queue();
            return;
        }

        if(args.size() < 2) {
            channel.sendMessage(getHelp()).queue();
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < args.size(); i++) {
            sb.append(args.get(i)).append(" ");
        }
        String reason = sb.toString();

        List<Member> mentionedMembers = message.getMentionedMembers();
        Member victim = mentionedMembers.get(0);

        if (victim.getId().equals(member.getId())) {
            channel.sendMessage(Embeds.error("You cannot punish yourself!")).queue();
            return;
        }
        if (victim.getRoles().get(0).getPosition() > member.getRoles().get(0).getPosition()) {
            channel.sendMessage(Embeds.error("You cannot punish this user!")).queue();
            return;
        }
        if (guildInstance.getMemberById(SocialBot.id).getRoles().get(0).getPosition() < victim.getRoles().get(0).getPosition()) {
            channel.sendMessage("I cannot punish this user! Maybe try moving up my role?").queue();
            return;
        }

        guildInstance.kick(victim, reason).queue();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(0, 255, 0));
        embed.setTitle("**" + victim.getUser().getAsTag() + "** has been kicked!");
        embed.addField("Reason: ", reason, true);
        embed.addField("Expiry: ", args.get(1), true);
        channel.sendMessage(embed.build()).queue();
        victim.getUser().openPrivateChannel().queue(privateChannel -> {
            embed.setColor(new Color(0, 0, 0));
            embed.setTitle("You have been kicked from **" + guildInstance.getName() + "**");
            privateChannel.sendMessage(embed.build()).queue();
        });
    }

    @Override
    public String getHelp() {
        return "Usage: /" + getInvoke() + " <player> <reason>";
    }

    @Override
    public String getInvoke() {
        return "kick";
    }
}
