package dev.littlebigowl.api.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import dev.littlebigowl.api.EssentialsAPI;

public class EssentialsPermission {
    
    private String node;
    private boolean value;

    private static EssentialsAPI api;
    private static HashSet<UUID> vanished = new HashSet<>();
    private static HashMap<UUID, PermissionAttachment> permissions = new HashMap<>();

    public EssentialsPermission(String node, boolean value) {
        this.node = node;
        this.value = value;
    }

    public static void init(EssentialsAPI plugin) {
        EssentialsPermission.api = plugin;
    }

    public static void resetPermissions(Player player) {
        PermissionAttachment attachment = permissions.get(player.getUniqueId());

        if(attachment != null) {
            for(String node : attachment.getPermissions().keySet()) {
                attachment.unsetPermission(node);
            }
        }
        permissions.remove(player.getUniqueId());
        player.updateCommands();
    }

    public static void givePermission(Player player, EssentialsPermission permission) {

        PermissionAttachment attachment = permissions.get(player.getUniqueId());

        if(attachment == null) {
            attachment = player.addAttachment(api);
        }

        attachment.setPermission(permission.getNode(), permission.getValue());
        permissions.put(player.getUniqueId(), attachment);
        player.updateCommands();
    }

    public static boolean isVanished(Player player) {
        return vanished.contains(player.getUniqueId());
    }

    public static boolean isVanished(UUID uuid) {
        return vanished.contains(uuid);
    }

    public static void addVanished(Player player) {
        vanished.add(player.getUniqueId());
    }

    public static void addVanished(UUID uuid) {
        vanished.add(uuid);
    }

    public static void removeVanished(Player player) {
        vanished.remove(player.getUniqueId());
    }

    public static void removeVanished(UUID uuid) {
        vanished.remove(uuid);
    }

    public static HashSet<Player> getVanishedPlayers() {
        HashSet<Player> vanishedPlayers = new HashSet<>();
        for(UUID vanishedUuid : vanished) {
            Player player = Bukkit.getPlayer(vanishedUuid);

            if(player != null) {
                vanishedPlayers.add(player);
            }
        }

        return vanishedPlayers;
    }

    public String getNode() {
        return this.node;
    }

    public boolean getValue() {
        return this.value;
    }

}
