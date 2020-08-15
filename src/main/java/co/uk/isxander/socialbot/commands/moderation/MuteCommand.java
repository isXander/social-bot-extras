package co.uk.isxander.socialbot.commands.moderation;

import co.uk.isxander.socialbot.SocialBot;
import co.uk.isxander.socialbot.objects.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MuteCommand implements ICommand {
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

        if (args.size() < 3) {
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
            channel.sendMessage(":x: You can't punish yourself!").queue();
            return;
        }
        if (victim.getRoles().get(0).getPosition() > member.getRoles().get(0).getPosition()) {
            channel.sendMessage(":x: You cannot punish this player!").queue();
            return;
        }
        if (guildInstance.getMemberById(SocialBot.id).getRoles().get(0).getPosition() < victim.getRoles().get(0).getPosition()) {
            channel.sendMessage(":x: I cannot punish this player!").queue();
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
            guildInstance.createRole()
                    .setName("Muted")
                    .setColor(new Color(0, 0, 0, 255))
                    .setPermissions(Permission.EMPTY_PERMISSIONS)
                    .setPermissions(Permission.CREATE_INSTANT_INVITE, Permission.MESSAGE_HISTORY, Permission.MESSAGE_ADD_REACTION)
                    .queue(role -> {
                        guildInstance.addRoleToMember(victim, role).queue();
                    });
        }
        else {
            guildInstance.addRoleToMember(victim, guildInstance.getRoles().get(mutedIndex)).queue();
        }
        channel.sendMessage("**" + victim.getUser().getAsTag() + "** was muted for " + reason).queue();
        victim.getUser().openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage("You were muted in `" + guildInstance.getName()
                    + "`\nReason: `" + reason
                    + "`\nThis mute will expire in " + args.get(1)).queue();
        });
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getInvoke() {
        return "mute";
    }
}
