package dev.littlebigowl.api.models;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.Statistic;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import dev.littlebigowl.api.EssentialsAPI;
import dev.littlebigowl.api.constants.Colors;

public class EssentialsScoreboard {

    private Scoreboard scoreboard;
    private EssentialsAPI plugin;

    private HashMap<String, EssentialsTeam> teams = new HashMap<>();

    private EssentialsScoreboard(EssentialsAPI plugin) {
        this.plugin = plugin;
    }

    public static EssentialsScoreboard init(EssentialsAPI plugin) {
        return new EssentialsScoreboard(plugin);
    }

    public static int getPlaytime(Player player) {
        return Math.round(player.getStatistic(Statistic.PLAY_ONE_MINUTE)/1200);
    }

    public static int getHourPlaytime(Player player) {
        return Math.round(EssentialsScoreboard.getPlaytime(player)/60);
    }

    private ArrayList<EssentialsTeam> getTeams() {
        return new ArrayList<EssentialsTeam>(this.teams.values());
    }

    public void createNewScoreboard() {
        this.scoreboard = this.plugin.getServer().getScoreboardManager().getNewScoreboard();
    }

    public void setScores(Player player) {

        Objective playtime = this.scoreboard.getObjective("playtime");
        Objective health = this.scoreboard.getObjective("health");

        if(playtime == null) {
            playtime = this.scoreboard.registerNewObjective("playtime", Criteria.create("PLAYTIME"), "playtime");
            playtime.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        }

        if(health == null) {
            health = this.scoreboard.registerNewObjective("health", Criteria.HEALTH, Colors.FAILURE.getChatColor() + "❤");
            health.setDisplaySlot(DisplaySlot.BELOW_NAME);
        }

        playtime.getScore(player.getName()).setScore(EssentialsScoreboard.getPlaytime(player));
    }

    public void addTeam(EssentialsTeam team) {
        boolean existing = (this.scoreboard.getTeam(team.getId()) != null);
        if(existing) {
            this.scoreboard.getTeam(team.getId()).unregister();
        }
        this.scoreboard.registerNewTeam(team.getId());
        
        this.teams.put(team.getId(), team);
        plugin.getLogger().info("Registered new rank : " + team.getName());
    }

    public EssentialsTeam getEssentialsTeam(String teamName) {
        return this.teams.get(teamName);
    }

    public EssentialsTeam getEssentialsTeam(Player player) {

        for(EssentialsTeam team : this.getTeams()) {

            if(team.has(player)) {
                return team;
            }
        }
        
        return null;
    }

    public Team getTeam(String teamName) {
        return this.scoreboard.getTeam(teamName);
    }

    public Team getTeam(Player player) {
        for(Team team : this.scoreboard.getTeams()) {
            if(team.hasEntry(player.getName())) {
                return team;
            }
        }

        return null;
    }

    public EssentialsTeam setTeam(Player player) {
        ArrayList<Integer> playtimes = new ArrayList<>();
        this.getTeams().forEach(team -> playtimes.add(team.getPlaytime()));

        int playerPlaytime = EssentialsScoreboard.getPlaytime(player);
        int distance = Math.abs(playtimes.get(0) - playerPlaytime);
        int idx = 0;
        for(int i = 1; i < playtimes.size(); i++){
            int cdistance = Math.abs(playtimes.get(i) - playerPlaytime);
            if(cdistance < distance && playerPlaytime >= playtimes.get(i)){
                idx = i;
                distance = cdistance;
            }
        }

        int teamPlaytime = playtimes.get(idx);

        for(EssentialsTeam team : this.getTeams()) {
            if(team.getPlaytime() == teamPlaytime && !team.has(player) && !player.isOp()) {
                EssentialsTeam playerTeam = this.getEssentialsTeam(player);
                
                if(playerTeam != null) playerTeam.removePlayer(player);
            }
            
            if(team.getPlaytime() == teamPlaytime && !player.isOp()) {
                team.addPlayer(player);
                return team;

            } else if(player.isOp() && team.getPlaytime() == -1){
                team.addPlayer(player);
                return team;
            }
        }
        
        return null;
    }

    public void registerTeams() {
        Set<String> teams = this.plugin.getConfig().getConfigurationSection("ranks").getKeys(false);
        for(String teamName : teams) {
            ConfigurationSection rank = this.plugin.getConfig().getConfigurationSection("ranks." + teamName);
            
            try {
                this.plugin.scoreboard.addTeam(new EssentialsTeam(
                    this.plugin,
                    rank.getString("id"),
                    rank.getString("name"),
                    Color.decode("#" + rank.getString("color")),
                    rank.getString("prefix"),
                    Integer.parseInt(rank.getString("playtime")),
                    Integer.parseInt(rank.getString("maxHomes"))
                ));
            } catch (NumberFormatException e) {
                this.plugin.getLogger().severe("Couldn't register rank : " + teamName + ". " + e.getMessage().replace("\n", ". ").replace("\r", ". "));
            }
        }
    }

    public void setScoreboard(Player player) {
        player.setScoreboard(this.scoreboard);
    }

}
