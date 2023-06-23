package dev.littlebigowl.api.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

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

    public static Iterator<UUID> getVanished() {
        return vanished.iterator();
    }

    public static boolean isVanished(Player player) {
        return vanished.contains(player.getUniqueId());
    }

    public String getNode() {
        return this.node;
    }

    public boolean getValue() {
        return this.value;
    }

}
