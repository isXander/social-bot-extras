package co.uk.isxander.socialbot.handlers;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class ReactionHandler {
    private final List<Message> messageInvokes = new ArrayList<>();
    private final List<String> emoteInvokes = new ArrayList<>();
    private final List<Runnable> runnables = new ArrayList<>();

    public void addReactionListener(Message message, String reaction, Runnable action) {

        messageInvokes.add(message);
        emoteInvokes.add(reaction);
        runnables.add(action);

        message.addReaction(reaction).queue();

    }

    public void removeReactionListener(Message message, String reaction, Runnable action) {
        messageInvokes.remove(message);
        emoteInvokes.remove(reaction);
        runnables.remove(action);
    }

    public void onGuildReactionAdd(GuildMessageReactionAddEvent event) {
        MessageChannel channel = event.getChannel();
        MessageReaction reaction = event.getReaction();
        String emote = event.getReactionEmote().getName();
        User user = event.getUser();
        Message message = channel.retrieveMessageById(reaction.getMessageId()).complete();


        for (int i = 0; i < messageInvokes.size(); i++) {
            if (messageInvokes.get(i).equals(message) && emoteInvokes.get(i).equals(emote)) {
                if (user.isBot()) return;
                message.removeReaction(emote, user).queue();
                runnables.get(i).run();
                break;
            }
        }

    }
}
