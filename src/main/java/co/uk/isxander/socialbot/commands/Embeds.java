package co.uk.isxander.socialbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class Embeds {
    public static MessageEmbed success(String message) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setDescription(message);
        embed.setColor(new Color(0, 255, 0));
        return embed.build();
    }
    public static MessageEmbed warning(String message) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setDescription(message);
        embed.setColor(new Color(255, 255, 0));
        return embed.build();
    }
    public static MessageEmbed error(String message) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setDescription(message);
        embed.setColor(new Color(255, 0, 0));
        return embed.build();
    }
    public static MessageEmbed info(String message) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setDescription(message);
        embed.setColor(new Color(250, 250, 250));
        return embed.build();
    }
}
