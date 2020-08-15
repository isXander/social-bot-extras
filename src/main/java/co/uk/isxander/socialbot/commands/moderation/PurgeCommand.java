package co.uk.isxander.socialbot.commands.moderation;

import co.uk.isxander.socialbot.objects.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class PurgeCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        MessageHistory history = channel.getHistory();
        Member member = event.getMember();

        if (!member.hasPermission(Permission.MESSAGE_MANAGE)) {
            channel.sendMessage(":x: You do not have permission!").queue();
            return;
        }

        if (args.isEmpty()) {
            channel.sendMessage(":x: Please specify how many messages you want to delete!").queue();
            return;
        }
        int historySize = channel.getHistory().getRetrievedHistory().size();
        for (int i = historySize; i > historySize - Integer.parseInt(args.get(0)); i--) {
            channel.getHistory().getRetrievedHistory().get(i).delete().queue();
        }
        channel.sendMessage(":white_check_mark: Deleted " + args.get(0) + " messages!").queue(message -> {
            try {
                Thread.sleep(5000);
            }
            catch (InterruptedException ignored) {
            }
            message.delete().queue();
        });
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getInvoke() {
        return "delete";
    }
}
