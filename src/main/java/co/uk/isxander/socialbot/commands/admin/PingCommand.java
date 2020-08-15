package co.uk.isxander.socialbot.commands.admin;

import co.uk.isxander.socialbot.commands.Embeds;
import co.uk.isxander.socialbot.objects.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class PingCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        Member member = event.getMember();

        if (!member.hasPermission(Permission.ADMINISTRATOR)) {
            channel.sendMessage(Embeds.error("You do not have permission to use this command!")).queue();
            return;
        }
        long time = System.currentTimeMillis();
        channel.sendMessage(Embeds.info("Pong! Getting ping...")).queue(response -> {
            long ping = System.currentTimeMillis() - time;
            response.editMessage(Embeds.info("Pong! " + ping)).queue();
        });

    }

    @Override
    public String getHelp() {
        return "Checks ping of bot.";
    }

    @Override
    public String getInvoke() {
        return "ping";
    }
}
