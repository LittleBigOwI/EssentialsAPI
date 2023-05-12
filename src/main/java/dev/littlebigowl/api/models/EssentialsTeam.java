package dev.littlebigowl.api.models;

import java.awt.Color;
import java.util.ArrayList;

import org.bukkit.entity.Player;

import dev.littlebigowl.api.EssentialsAPI;
import net.md_5.bungee.api.ChatColor;

public class EssentialsTeam {
    
    private String id;
    private Color color;
    private String prefix;
    private String name;
    private String roleId;
    private int playtime;
    private int maxHomes;
    private int claimBonus;

    private EssentialsAPI plugin;

    public final ArrayList<Player> players = new ArrayList<>();

    public EssentialsTeam(EssentialsAPI plugin, String id, String name, String roleId, Color color, String prefix, int playtime, int maxHomes, int claimBonus) {        
        this.plugin = plugin;
        
        this.id = id;
        this.color = color;
        this.prefix = prefix;
        this.roleId = roleId;
        this.name = name;
        this.playtime = playtime;
        this.maxHomes = maxHomes;
        this.claimBonus = claimBonus;
    }

    public void addPlayer(Player player) {
        players.add(player);
        this.plugin.scoreboard.getTeam(this.getId()).addEntry(player.getName());

        player.setPlayerListName(
            "[" + ChatColor.of(this.color) + this.prefix + ChatColor.RESET +"] " + 
            ChatColor.of(this.color) + player.getName() + ChatColor.RESET
        );
    }

    public void removePlayer(Player player) {
        players.remove(player);
        this.plugin.scoreboard.getTeam(this.getId()).removeEntry(player.getName());
    }

    public boolean has(Player player) {
        return this.players.contains(player);
    }

    public String getId() {
        return this.id;
    }

    public Color getColor() {
        return this.color;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getName() {
        return this.name;
    }

    public String getRoleId() {
        return this.roleId;
    }

    public int getPlaytime() {
        return this.playtime;
    }

    public int getMaxHomes() {
        return this.maxHomes;
    }

    public int getClaimBonus() {
        return this.claimBonus;
    }

}
