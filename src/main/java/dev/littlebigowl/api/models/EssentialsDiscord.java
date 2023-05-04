package dev.littlebigowl.api.models;

import dev.littlebigowl.api.EssentialsAPI;

public class EssentialsDiscord {
    
    private String token;
    private String webhookURL;
    private String webhookAvatarURL;
    private String channel;
    private String status;

    private EssentialsDiscord(String token, String webhookURL, String webhookAvatarURL, String channel, String status) {
        this.token = token;
        this.webhookURL = webhookURL;
        this.webhookAvatarURL = webhookAvatarURL;
        this.channel = channel;
        this.status = status;
    }

    public static EssentialsDiscord init(EssentialsAPI plugin) {
        return new EssentialsDiscord(
            plugin.getConfig().getString("token"),
            plugin.getConfig().getString("webhookURL"),
            plugin.getConfig().getString("webhookAvatarURL"),
            plugin.getConfig().getString("channel"),
            plugin.getConfig().getString("status")
        );
    }

    public String getToken() {
        return this.token;
    }

    public String getWebhookURL() {
        return this.webhookURL;
    }

    public String getWebhookAvatarURL() {
        return this.webhookAvatarURL;
    }

    public String getChannel() {
        return this.channel;
    }

    public String getStatus() {
        return this.status;
    }

}
