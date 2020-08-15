package co.uk.isxander.socialbot.commands.general;

import co.uk.isxander.socialbot.objects.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class VerifyCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member member = event.getMember();
        TextChannel channel = event.getChannel();
        Message msg = event.getMessage();
        String rawMsg = event.getMessage().getContentRaw();

        msg.addReaction("U+1F44C").queue();
        new Thread(() -> {

        });
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getInvoke() {
        return "verify";
    }
}
