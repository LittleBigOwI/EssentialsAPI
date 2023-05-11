package dev.littlebigowl.api.models;

import dev.littlebigowl.api.EssentialsAPI;

public class EssentialsAreas {
    
    private int minAreaSurface;
    private int minAreaWidthX;
    private int minAreaWidthY;

    private int claimAmount;
    private int claimInterval;

    private EssentialsAreas(int minAreaSurface, int minAreaWidthX, int minAreaWidthY, int claimAmount, int claimInterval) {
        this.minAreaSurface = minAreaSurface;
        this.minAreaWidthX = minAreaWidthX;
        this.minAreaWidthY = minAreaWidthY;

        this.claimAmount = claimAmount;
        this.claimInterval = claimInterval;
    }

    public static EssentialsAreas init(EssentialsAPI plugin) {
        int minAreaSurface = plugin.getConfig().getInt("minAreaSurface");
        int minAreaWidthX = plugin.getConfig().getInt("minAreaWidthX");
        int minAreaWidthY = plugin.getConfig().getInt("minAreaWidthY");

        int claimAmount = plugin.getConfig().getInt("claimAmount");
        int claimInterval = plugin.getConfig().getInt("claimInterval");

        return new EssentialsAreas(minAreaSurface, minAreaWidthX, minAreaWidthY, claimAmount, claimInterval);
    }

    public int getMinAreaSurface() {
        return this.minAreaSurface;
    }

    public int getMinAreaWidthX() {
        return this.minAreaWidthX;
    }

    public int getMinAreaWidthY() {
        return this.minAreaWidthY;
    }

    public int getPassiveClaimAmount() {
        return this.claimAmount;
    }

    public int getPassiveClaimInterval() {
        return this.claimInterval;
    }
}
