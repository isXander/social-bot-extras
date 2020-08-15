package co.uk.isxander.socialbot.commands.admin;

import co.uk.isxander.socialbot.commands.Embeds;
import co.uk.isxander.socialbot.objects.ICommand;
import co.uk.isxander.socialbot.Variables;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class PrefixCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member member = event.getMember();
        TextChannel channel = event.getChannel();

        if (!member.hasPermission(Permission.ADMINISTRATOR)) {
            channel.sendMessage(Embeds.error("You do not have permission to use this command!")).queue();
            return;
        }
        if (args.isEmpty()) {
            channel.sendMessage(Embeds.error("You must specify a prefix!")).queue();
            return;
        }
        Variables.setData(0, args.get(0), event.getGuild().getId());
        channel.sendMessage(Embeds.success("Prefix has been set to `" + args.get(0) + "`")).queue();
    }

    @Override
    public String getHelp() {
        return "Sets the prefix of the bot.";
    }

    @Override
    public String getInvoke() {
        return "setprefix";
    }
}
