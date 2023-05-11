package dev.littlebigowl.api.constants;

import java.awt.Color;

import net.md_5.bungee.api.ChatColor;

public enum Colors {
    
    SUCCESS(Color.decode("#54fb54")),
    FAILURE(Color.decode("#fb5454")),
    WARNING(Color.decode("#f2e055")),
    
    INFO(Color.decode("#66bced")),
    MAJOR(Color.decode("#6c63eb")),
    MINOR(Color.decode("#858585")),
    POPUP(Color.decode("#f2b755")),

    DISCORD(Color.decode("#5865f2"));

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
