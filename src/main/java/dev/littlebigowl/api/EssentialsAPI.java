package dev.littlebigowl.api;

import org.bukkit.plugin.java.JavaPlugin;

import dev.littlebigowl.api.models.EssentialsDatabase;
import dev.littlebigowl.api.models.EssentialsDiscord;
import dev.littlebigowl.api.models.EssentialsScoreboard;
import dev.littlebigowl.api.models.EssentialsTeleports;
import dev.littlebigowl.api.models.EssentialsAreas;

public class EssentialsAPI extends JavaPlugin {

    public final EssentialsDatabase database = EssentialsDatabase.init(this);
    public final EssentialsScoreboard scoreboard = EssentialsScoreboard.init(this);
    public final EssentialsTeleports teleports = EssentialsTeleports.init(this);
    public final EssentialsDiscord discord = EssentialsDiscord.init(this);
    public final EssentialsAreas areas = EssentialsAreas.init(this);

    @Override
    public void onEnable() {
        
        scoreboard.createNewScoreboard();
        scoreboard.registerTeams();

        if(database == null) {
            this.getLogger().severe("Couldn't find database.");
        } else {
            this.getLogger().info("API enabled.");
        }

    }

    @Override
    public void onDisable() {
        this.getLogger().info("API disabled.");
    }
}
