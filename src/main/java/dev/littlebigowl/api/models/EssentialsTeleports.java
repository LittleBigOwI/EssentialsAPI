package dev.littlebigowl.api.models;

import dev.littlebigowl.api.EssentialsAPI;

public class EssentialsTeleports {
    
    private int maxTeleports;

    private EssentialsTeleports(int maxTeleports) {
        this.maxTeleports = maxTeleports;
    }

    public static EssentialsTeleports init(EssentialsAPI plugin) {
        int maxTeleports = plugin.getConfig().getInt("maxTeleports");

        return new EssentialsTeleports(maxTeleports);
    }

    public int getMaxTeleports() {
        return this.maxTeleports;
    }

}
