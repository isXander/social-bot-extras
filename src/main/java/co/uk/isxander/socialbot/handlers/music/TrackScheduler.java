package co.uk.isxander.socialbot.handlers.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private boolean loop = false;
    private final AudioPlayer player;
    private BlockingQueue<AudioTrack> queue;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track) {
        try {
            if (!player.startTrack(track, true)) {
                queue.offer(track);
            }
        }
        catch (NullPointerException ignored) {
        }
    }

    public void nextTrack() {
        // if player gets null it will stop (valid arg)
        try {
            player.startTrack(queue.poll(), false);
        }
        catch (IllegalStateException ignored) {}
    }

    public List<AudioTrack> getTrackList() {
        return new ArrayList<>(queue);
    }

    public void shuffle() {
        List<AudioTrack> tracks = new ArrayList<>(getTrackList());
        Collections.shuffle(tracks);
        queue.clear();
        queue.addAll(tracks);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            if (loop) {
                player.stopTrack();
                player.startTrack(track.makeClone(), false);
            }
            else {
                nextTrack();
            }
        }
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public boolean getLoopState() {
        return loop;
    }

    public void clearQueue() {
        player.stopTrack();
        queue.clear();
    }
}
