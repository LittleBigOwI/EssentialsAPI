package dev.littlebigowl.api.constants;

import java.awt.Color;

import net.md_5.bungee.api.ChatColor;

public enum Colors {
    
    SUCCESS(Color.decode("#54fb54")),
    SUCCESS_DARK(Color.decode("#349b34")),

    INFO(Color.decode("#66bced")),
    INFO_DARK(Color.decode("#6c63eb")),

    WARNING(Color.decode("#f2e055")),
    WARNING_DARK(Color.decode("#f2b755")),

    FAILURE(Color.decode("#fb5454")),
    FAILURE_DARK(Color.decode("#ab3939")),
    
    MINOR(Color.decode("#858585")),
    MINOR_DARK(Color.decode("#5d5d5d")),

    DISCORD(Color.decode("#5865f2")),
    DISCORD_OLD(Color.decode("#7289da"));

    private Color color;

    private Colors(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public ChatColor getChatColor() {
        return ChatColor.of(this.color);
    }
}
