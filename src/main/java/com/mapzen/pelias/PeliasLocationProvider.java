package com.mapzen.pelias;

public interface PeliasLocationProvider {
    public double getLat();
    public double getLon();
    public BoundingBox getBoundingBox();
}
