package dev.littlebigowl.api.models;

import java.awt.Color;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
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
import net.md_5.bungee.api.ChatColor;

public class EssentialsScoreboard {

    private String header;
    private String footer;

    private Scoreboard scoreboard;
    private EssentialsAPI plugin;

    private HashMap<String, EssentialsTeam> teams = new HashMap<>();

    private EssentialsScoreboard(EssentialsAPI plugin, String header, String footer) {
        this.header = header;
        this.footer = footer;
        
        this.plugin = plugin;
    }

    public static EssentialsScoreboard init(EssentialsAPI plugin) {
        String header = plugin.getConfig().getString("header");
        String footer = plugin.getConfig().getString("footer");

        return new EssentialsScoreboard(plugin, header, footer);
    }

    public static int getPlaytime(Player player) {
        return Math.round(player.getStatistic(Statistic.PLAY_ONE_MINUTE)/1200);
    }

    public static int getPlaytime(OfflinePlayer player) {
        return Math.round(player.getStatistic(Statistic.PLAY_ONE_MINUTE)/1200);
    }

    public static int getHourPlaytime(Player player) {
        return Math.round(EssentialsScoreboard.getPlaytime(player)/60);
    }

    private ArrayList<EssentialsTeam> getTeams() {
        return new ArrayList<EssentialsTeam>(this.teams.values());
    }

    private double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    private double getTPS() {
        Object server = null;
        Field tps = null;
        try {
            if (server == null) {
                Server mc = Bukkit.getServer();

                Field consoleField = mc.getClass().getDeclaredField("console");
                consoleField.setAccessible(true);
                server = consoleField.get(mc);
            }
            if (tps == null) {
                tps = server.getClass().getSuperclass().getDeclaredField("recentTps");
                tps.setAccessible(true);
            }
            
            double[] values = (double[]) tps.get(server);
            return values[0];

        } catch (IllegalAccessException | NoSuchFieldException ignored) {
            return 20;
        }
    }

    private float getMSPT() {
        BigDecimal bd = BigDecimal.valueOf(this.getTPS());
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.floatValue();
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
        
        Team spigotTeam = this.scoreboard.registerNewTeam(team.getId());
        spigotTeam.setPrefix(ChatColor.WHITE + "[" + ChatColor.of(team.getColor()) + team.getPrefix() + ChatColor.WHITE + "] " + ChatColor.of(team.getColor()));
        
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
        this.getTeams().forEach(team -> {
            playtimes.add(team.getPlaytime());
        });

        Collections.sort(playtimes);

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
                EssentialsTeam team = new EssentialsTeam(
                    this.plugin,
                    rank.getString("id"),
                    rank.getString("name"),
                    rank.getString("discordRole"),
                    Color.decode("#" + rank.getString("color")),
                    rank.getString("prefix"),
                    Integer.parseInt(rank.getString("playtime")),
                    Integer.parseInt(rank.getString("maxHomes")),
                    Integer.parseInt(rank.getString("claimBonus"))
                );

                for(String permission : rank.getStringList("permissions")) {
                    team.addPermission(new EssentialsPermission(permission, true));
                }

                this.plugin.scoreboard.addTeam(team);
            } catch (NumberFormatException e) {
                this.plugin.getLogger().severe("Couldn't register rank : " + teamName + ". " + e.getMessage().replace("\n", ". ").replace("\r", ". "));
            }
        }
    }

    public void setScoreboard(Player player) {
        player.setScoreboard(this.scoreboard);
    }

    public ArrayList<EssentialsTeam> getEssentialsTeams() {
        ArrayList<EssentialsTeam> teams = new ArrayList<>();
        this.teams.values().forEach(team -> teams.add(team));
        
        return teams;
    }

    public void setTitles(Player player) {
        player.setPlayerListHeader(ChatColor.translateAlternateColorCodes('&',
            this.header
            .replace("{playerCount}", "" + Bukkit.getOnlinePlayers().size())
            .replace("{maxPlayers}", "" + Bukkit.getMaxPlayers())
        ));
        
        player.setPlayerListFooter(ChatColor.translateAlternateColorCodes('&',
            this.footer
            .replace("{playerPing}", "" + Math.round(player.getPing()))
            .replace("{serverMSPT}", "" + this.round(this.getMSPT(), 1))
            .replace("{serverTPS}", "" + this.round(this.getTPS(), 1))
        ));
    }

}
