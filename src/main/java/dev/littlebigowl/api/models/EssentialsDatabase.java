package dev.littlebigowl.api.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;

import dev.littlebigowl.api.EssentialsAPI;

public class EssentialsDatabase {
    
    private String host;
    private String database;
    private String username;
    private String password;

    private Connection connection;
    private Logger logger;

    private EssentialsDatabase(String host, String database, String username, String password, EssentialsAPI plugin) throws ClassNotFoundException, SQLException {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        this.logger = plugin.getLogger();

        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database, username, password);
    }

    public static EssentialsDatabase init(EssentialsAPI plugin) {
        plugin.saveDefaultConfig();

        ConfigurationSection credentials = plugin.getConfig().getConfigurationSection("database");
        
        EssentialsDatabase database = null;
        try {
            database = new EssentialsDatabase(
                credentials.getString("host"), 
                credentials.getString("database"), 
                credentials.getString("user"), 
                credentials.getString("password"),
                plugin
            );
        } catch (ClassNotFoundException e) {
            plugin.getLogger().severe("Couldn't find sql driver : " + e.getMessage().replace("\n", ". ").replace("\r", ". "));
        } catch (SQLException e) {
            plugin.getLogger().severe("Invalid database credentials : " + e.getMessage().replace("\n", ". ").replace("\r", ". "));
        }

        return database;
    }

    private void resetConnection() throws SQLException {
        this.connection.close();
        this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + "/" + this.database, this.username, this.password);
    }

    public boolean create(String sql) throws SQLException {
        this.resetConnection();
        logger.warning("Executed create statement [" + sql + "].");
        return this.connection.createStatement().execute(sql);
    }

    public int update(String sql) throws SQLException {
        this.resetConnection();
        logger.warning("Executed update statement [" + sql + "].");
        return this.connection.createStatement().executeUpdate(sql);
    }

    public ResultSet fetch(String sql) throws SQLException {
        this.resetConnection();
        logger.warning("Executed fetch statement [" + sql + "].");
        return this.connection.prepareStatement(sql).executeQuery();
    }

}
