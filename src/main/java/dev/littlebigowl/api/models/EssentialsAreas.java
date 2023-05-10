package dev.littlebigowl.api.models;

import dev.littlebigowl.api.EssentialsAPI;

public class EssentialsAreas {
    
    private int minAreaSurface;
    private int minAreaWidthX;
    private int minAreaWidthY;

    private EssentialsAreas(int minAreaSurface, int minAreaWidthX, int minAreaWidthY) {
        this.minAreaSurface = minAreaSurface;
        this.minAreaWidthX = minAreaWidthX;
        this.minAreaWidthY = minAreaWidthY;
    }

    public static EssentialsAreas init(EssentialsAPI plugin) {
        int minAreaSurface = plugin.getConfig().getInt("minAreaSurface");
        int minAreaWidthX = plugin.getConfig().getInt("minAreaWidthX");
        int minAreaWidthY = plugin.getConfig().getInt("minAreaWidthY");

        return new EssentialsAreas(minAreaSurface, minAreaWidthX, minAreaWidthY);
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
}
