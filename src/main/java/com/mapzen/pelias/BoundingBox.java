package com.mapzen.pelias;

public class BoundingBox {
    public String minLat;
    public String minLon;
    public String maxLat;
    public String maxLon;

    public BoundingBox(double minLat, double minLon, double maxLat, double maxLon) {
        if(((maxLat - minLat) > 0.0) && ((maxLon - minLon) > 0.0)) {
            this.minLat = Double.toString(minLat);
            this.minLon = Double.toString(minLon);
            this.maxLat = Double.toString(maxLat);
            this.maxLon = Double.toString(maxLon);
        } else {
            this.minLat = Double.toString(maxLat);
            this.minLon = Double.toString(maxLon);
            this.maxLat = Double.toString(minLat);
            this.maxLon = Double.toString(minLon);
        }
    }
}
