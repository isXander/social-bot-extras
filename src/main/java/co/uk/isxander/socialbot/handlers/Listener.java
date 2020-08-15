package co.uk.isxander.socialbot.handlers;

import co.uk.isxander.socialbot.commands.Embeds;
import co.uk.isxander.socialbot.Variables;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Listener extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger("Listener");
    private final CommandHandler manager;
    public static ReactionHandler reactionHandler = new ReactionHandler();

    public Listener(CommandHandler manager) {
        this.manager = manager;
    }

    @Override
    public void onReady(ReadyEvent event) {
        logger.info(String.format("Logged in as %#s", event.getJDA().getSelfUser()));
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        User author = event.getAuthor();
        Message message = event.getMessage();
        String content = message.getContentDisplay();

        if (event.isFromType(ChannelType.TEXT)) {

            Guild guild = event.getGuild();
            TextChannel textChannel = event.getTextChannel();

            logger.info(String.format("(%s)[%s]<%#s>: %s", guild.getName(), textChannel.getName(), author, content));
        } else if (event.isFromType(ChannelType.PRIVATE)) {
            logger.info(String.format("[PRIV]<%#s>: %s", author, content));
        }
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        String rw = event.getMessage().getContentRaw();
        String prefix = Variables.getData(0, event.getGuild().getId());
        if (prefix == null) {
            Variables.setData(0, "!", event.getGuild().getId());
            prefix = "!";
        }
        if (!event.getAuthor().isBot() && !event.getMessage().isWebhookMessage() && rw.startsWith(prefix)) {
            manager.handleCommand(event);
        }
        else if (!event.getAuthor().isBot() && !event.getMessage().isWebhookMessage() && rw.contains("<@729296402789957633>")) {
            event.getChannel().sendMessage(Embeds.info("My prefix is currently \"" + prefix + "\"")).queue();
        }
    }

    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent event) {
        String rw = event.getMessage().getContentRaw();
        String prefix = Variables.getData(0, event.getGuild().getId());
        if (prefix == null) {
            Variables.setData(0, "!", event.getGuild().getId());
            prefix = "!";
        }
        if (!event.getAuthor().isBot() && !event.getMessage().isWebhookMessage() && rw.startsWith(prefix)) {
            manager.handleCommand(event);
        }
        else if (!event.getAuthor().isBot() && !event.getMessage().isWebhookMessage() && rw.contains("<@729296402789957633>")) {
            event.getChannel().sendMessage(Embeds.info("My prefix is currently \"" + prefix + "\"")).queue();
        }
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        reactionHandler.onGuildReactionAdd(event);
    }

    private void shutdown(JDA jda) {
        jda.shutdown();
        System.exit(0);
    }
}
