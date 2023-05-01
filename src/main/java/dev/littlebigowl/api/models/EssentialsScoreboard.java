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
        Scoreboard scoreboard = player.getScoreboard();

        Objective playtime;
        Objective health;

        if(scoreboard == null) {
            scoreboard = this.plugin.getServer().getScoreboardManager().getNewScoreboard();

            playtime = scoreboard.registerNewObjective("playtime", Criteria.create("PLAYTIME"), "playtime");
            health = scoreboard.registerNewObjective("health", Criteria.HEALTH, Colors.FAILURE.getChatColor() + "❤");

            playtime.setDisplaySlot(DisplaySlot.PLAYER_LIST);
            health.setDisplaySlot(DisplaySlot.BELOW_NAME);
        } else {
            playtime = scoreboard.getObjective("playtime");
            health = scoreboard.getObjective("health");
        }

        playtime.getScore(player.getName()).setScore(EssentialsScoreboard.getHourPlaytime(player));
    }

    public void addTeam(EssentialsTeam team) {
        boolean existing = (scoreboard.getTeam(team.getId()) != null);
        if(existing) {
            scoreboard.getTeam(team.getId()).unregister();
        }
        scoreboard.registerNewTeam(team.getId());
        
        this.teams.put(team.getId(), team);
        plugin.getLogger().info("Registered new rank : " + team.getName());
    }

    public EssentialsTeam getEssentialsTeam(String teamName) {
        return this.teams.get(teamName);
    }

    public EssentialsTeam getEssentialsTeam(Player player) {
        
        for(Team team : this.scoreboard.getTeams()) {
            EssentialsTeam essentialsTeam = this.teams.get(team.getName());

            if(essentialsTeam.players.contains(player)) {
                return essentialsTeam;
            }
        }
        
        return null;
    }

    public Team getTeam(String teamName) {
        return this.scoreboard.getTeam(teamName);
    }

    public Team getTeam(Player player) {
        String teamId = this.getEssentialsTeam(player).getId();
        for(Team team : this.scoreboard.getTeams()) {
            if(team.getName().equals(teamId)) {
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
            if(team.getPlaytime() == teamPlaytime) {
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
                    Color.decode("#" + rank.getString("color")),
                    rank.getString("prefix"),
                    teamName,
                    Integer.parseInt(rank.getString("playtime")),
                    Integer.parseInt(rank.getString("maxHomes"))
                ));
            } catch (NumberFormatException e) {
                this.plugin.getLogger().severe("Couldn't register rank : " + teamName + ". " + e.getMessage().replace("\n", ". ").replace("\r", ". "));
            }
        }
    }

}
