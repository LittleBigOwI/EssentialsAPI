package dev.littlebigowl.api.constants;

import java.awt.Color;

import net.md_5.bungee.api.ChatColor;

public enum Colors {
    
    SUCCESS(Color.decode("#6fed66")),
    FAILURE(Color.decode("#ed6666")),
    WARNING(Color.decode("#f2e055")),
    
    INFO(Color.decode("#66bced")),
    MAJOR(Color.decode("#463be3")),
    MINOR(Color.decode("#858585"));

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
