package co.uk.isxander.socialbot.commands.admin;

import co.uk.isxander.socialbot.commands.Embeds;
import co.uk.isxander.socialbot.objects.ICommand;
import co.uk.isxander.socialbot.Variables;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class MusicRoleCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member member = event.getMember();
        TextChannel channel = event.getChannel();

        if (!member.hasPermission(Permission.ADMINISTRATOR)) {
            channel.sendMessage(Embeds.error("You do not have permission to use this command!")).queue();
            return;
        }
        if (args.isEmpty()) {
            channel.sendMessage(Embeds.error("You need to specify a role!")).queue();
            return;
        }
        String subId = args.get(0);
        if (subId.startsWith("<")) {
            subId = args.get(0).substring(3, args.get(0).length() - 1);
        }
        Variables.setData(1, subId, event.getGuild().getId());
        channel.sendMessage(Embeds.success("Music Controller role has been set to " + args.get(0))).queue();
    }

    @Override
    public String getHelp() {
        return "Sets the role that allows people to control the music commands.";
    }

    @Override
    public String getInvoke() {
        return "setmusicrole";
    }
}
