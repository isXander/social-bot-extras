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

public class BanCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Guild guildInstance = event.getGuild();
        Member member = event.getMember();
        TextChannel channel = event.getChannel();
        Message message = event.getMessage();

        if (!member.hasPermission(Permission.BAN_MEMBERS)) {
            channel.sendMessage(":x: You do not have permission!").queue();
            return;
        }

        if(args.size() < 3) {
            channel.sendMessage(getHelp()).queue();
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 3; i < args.size(); i++) {
            sb.append(args.get(i)).append(" ");
        }
        String reason = sb.toString();

        List<Member> mentionedMembers = message.getMentionedMembers();
        Member victim = mentionedMembers.get(0);

        if (victim.getId().equals(member.getId())) {
            channel.sendMessage(Embeds.error("You cannot ban yourself!")).queue();
            return;
        }
        if (victim.getRoles().get(0).getPosition() > member.getRoles().get(0).getPosition()) {
            channel.sendMessage(Embeds.error("You cannot punish this user!")).queue();
            return;
        }
        if (guildInstance.getMemberById(SocialBot.id).getRoles().get(0).getPosition() < victim.getRoles().get(0).getPosition()) {
            channel.sendMessage(Embeds.error("I cannot punish this user! Maybe try moving up my role?")).queue();
            return;
        }
        if (victim.isOwner()) {
            channel.sendMessage(Embeds.error("You cannot punish the owner! I have notified them that you have attempted this.")).queue();
            guildInstance.getOwner().getUser().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage("**" + event.getAuthor().getAsTag() + "** Tried to punish you!").queue();
            });
            return;
        }

        guildInstance.ban(victim, Integer.parseInt(args.get(1).substring(0, args.get(1).length() - 1)), reason).queue();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(0, 255, 0));
        embed.setTitle("**" + victim.getUser().getAsTag() + "** was banned!");
        embed.addField("Reason: ", reason, true);
        embed.addField("Expiry: ", args.get(1), true);
        channel.sendMessage(embed.build()).queue();
        victim.getUser().openPrivateChannel().queue(privateChannel -> {
            embed.setColor(new Color(0, 0, 0));
            embed.setTitle("You were banned from **" + guildInstance.getName() + "**");
            privateChannel.sendMessage(embed.build()).queue();
        });
    }

    @Override
    public String getHelp() {
        return "Usage: /" + getInvoke() + " <player> <time>d <reason>";
    }

    @Override
    public String getInvoke() {
        return "ban";
    }
}
