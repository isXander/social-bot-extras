package co.uk.isxander.socialbot.handlers;

import co.uk.isxander.socialbot.commands.admin.MusicRoleCommand;
import co.uk.isxander.socialbot.commands.admin.PingCommand;
import co.uk.isxander.socialbot.commands.admin.PrefixCommand;
import co.uk.isxander.socialbot.commands.general.HelpCommand;
import co.uk.isxander.socialbot.commands.moderation.*;
import co.uk.isxander.socialbot.commands.music.*;
import co.uk.isxander.socialbot.objects.ICommand;
import co.uk.isxander.socialbot.Variables;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;

public class CommandHandler {
    public static QueueCommand queueCommand = new QueueCommand();
    private final Map<String, ICommand> commands = new HashMap<>();

    public CommandHandler() {
        //Music\\
        addCommand(new PlayCommand());
        addCommand(new PauseCommand());
        addCommand(new SkipCommand());
        addCommand(new LeaveCommand());
        addCommand(new VolumeCommand());
        addCommand(new PlayingCommand());
        addCommand(new LoopCommand());
        addCommand(new ClearCommand());
        addCommand(new ShuffleCommand());
        addCommand(queueCommand);
        //Admin\\
        addCommand(new PingCommand());
        addCommand(new PrefixCommand());
        addCommand(new MusicRoleCommand());
//        addCommand(new VerifyCommand());
        //Moderation\\
        addCommand(new PurgeCommand());
        addCommand(new BanCommand());
        addCommand(new KickCommand());
        addCommand(new MuteCommand());
        addCommand(new UnmuteCommand());
        addCommand(new UnbanCommand());
        //General\\
        addCommand(new HelpCommand());
    }

    private void addCommand(ICommand command) {
        if (!commands.containsKey(command.getInvoke())) {
            commands.put(command.getInvoke(), command);
        }
    }

    public Collection<ICommand> getCommands() {
        return commands.values();
    }

    public ICommand getCommand(@NotNull String name) {
        return commands.get(name);
    }

    public void handleCommand(GuildMessageReceivedEvent event) {
        final String prefix = Variables.getData(0, event.getGuild().getId());

        final String[] split = event.getMessage().getContentRaw().replaceFirst(
                "(?i)" + Pattern.quote(prefix), "").split("\\s+");
        final String invoke = split[0].toLowerCase();

        if (commands.containsKey(invoke)) {
            final List<String> args = Arrays.asList(split).subList(1, split.length);

            commands.get(invoke).handle(args, event);
        }
    }

//    public void handleCommand(GuildMessageUpdateEvent event) {
//        final String prefix = Variables.getData(0, event.getGuild().getId());
//
//        final String[] split = event.getMessage().getContentRaw().replaceFirst(
//                "(?i)" + Pattern.quote(prefix), "").split("\\s+");
//        final String invoke = split[0].toLowerCase();
//
//        if (commands.containsKey(invoke)) {
//            final List<String> args = Arrays.asList(split).subList(1, split.length);
//
//            GuildMessageReceivedEvent fakeEvent = new GuildMessageReceivedEvent(event.getJDA(), event.getResponseNumber(), event.getMessage());
//            commands.get(invoke).handle(args, fakeEvent);
//        }
//    }
}
