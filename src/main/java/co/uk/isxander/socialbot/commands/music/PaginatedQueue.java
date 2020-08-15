package co.uk.isxander.socialbot.commands.music;

import co.uk.isxander.socialbot.handlers.CommandHandler;
import co.uk.isxander.socialbot.handlers.Listener;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

public class PaginatedQueue {

    public static PaginatedQueue queue;

    public static void sendTo(MessageChannel channel, String... content) {
        channel.sendMessage(content[0]).queue(message -> {
            queue = new PaginatedQueue(message, content);
        });
    }

    private final Message message;
    private final String[] pages;
    private int current = 0;

    protected PaginatedQueue(Message message, String[] pages) {
        this.message = message;
        this.pages = pages;
//        Listener.reactionHandler.addReactionListener(message, "\u2B06\uFE0F", CommandHandler.queueCommand::upArrowEvent);
//        Listener.reactionHandler.addReactionListener(message, "\u2B07\uFE0F", CommandHandler.queueCommand::downArrowEvent);
    }

    public synchronized void set(int page) {
        if (page == current || page < 0 || page >= pages.length - 1) return;
        current = page;
        message.editMessage(pages[page]).queue();
    }

    public synchronized void first() {
        set(0);
    }

    public synchronized void last() {
        set(pages.length - 1);
    }

    public synchronized void next() {
        set(current + 1);
    }

    public synchronized void previous() {
        set(current - 1);
    }
}
