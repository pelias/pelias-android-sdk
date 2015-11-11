package com.mapzen.pelias;

public class BoundingBox {
    public double minLat;
    public double minLon;
    public double maxLat;
    public double maxLon;

    public BoundingBox(double minLat, double minLon, double maxLat, double maxLon) {
        if (((maxLat - minLat) > 0.0) && ((maxLon - minLon) > 0.0)) {
            this.minLat = minLat;
            this.minLon = minLon;
            this.maxLat = maxLat;
            this.maxLon = maxLon;
        } else {
            this.minLat = maxLat;
            this.minLon = maxLon;
            this.maxLat = minLat;
            this.maxLon = minLon;
        }
    }
}
