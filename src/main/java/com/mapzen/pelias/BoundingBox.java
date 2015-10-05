package com.mapzen.pelias;

public class BoundingBox {
    public String minLat;
    public String minLon;
    public String maxLat;
    public String maxLon;

    public BoundingBox(double minLat, double minLon, double maxLat, double maxLon) {
        this.minLat = Double.toString(minLat);
        this.minLon = Double.toString(minLon);
        this.maxLat = Double.toString(maxLat);
        this.maxLon = Double.toString(maxLon);
    }
}
