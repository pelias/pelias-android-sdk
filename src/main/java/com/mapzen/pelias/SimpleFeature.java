package com.mapzen.pelias;

import com.mapzen.pelias.gson.Feature;
import com.mapzen.pelias.gson.Geometry;
import com.mapzen.pelias.gson.Properties;

import android.os.Parcel;
import android.os.Parcelable;

import auto.parcel.AutoParcel;

@AutoParcel
public abstract class SimpleFeature implements Parcelable {
    public static SimpleFeature create(
            String id,
            String name,
            String country,
            String countryAbbr,
            String region,
            String regionAbbr,
            String county,
            String localAdmin,
            String locality,
            String neighborhood,
            String label,
            double lat,
            double lng) {

        return builder().id(id)
                .name(name)
                .country(country)
                .countryAbbr(countryAbbr)
                .region(region)
                .regionAbbr(regionAbbr)
                .county(county)
                .localAdmin(localAdmin)
                .locality(locality)
                .neighborhood(neighborhood)
                .label(label)
                .lat(lat)
                .lng(lng)
                .build();
    }

    public abstract String id();
    public abstract String name();
    public abstract String country();
    public abstract String countryAbbr();
    public abstract String region();
    public abstract String regionAbbr();
    public abstract String county();
    public abstract String localAdmin();
    public abstract String locality();
    public abstract String neighborhood();
    public abstract String label();
    public abstract double lat();
    public abstract double lng();

    @AutoParcel.Builder
    public abstract static class Builder {
        public abstract Builder id(String id);
        public abstract Builder name(String name);
        public abstract Builder country(String country);
        public abstract Builder countryAbbr(String countryAbbr);
        public abstract Builder region(String region);
        public abstract Builder regionAbbr(String regionAbbr);
        public abstract Builder county(String county);
        public abstract Builder localAdmin(String localAdmin);
        public abstract Builder locality(String locality);
        public abstract Builder neighborhood(String neighborhood);
        public abstract Builder label(String label);
        public abstract Builder lat(double lat);
        public abstract Builder lng(double lng);
        public abstract SimpleFeature build();
    }

    public static Builder builder() {
        return new AutoParcel_SimpleFeature.Builder();
    }

    public abstract Builder toBuilder();

    public static SimpleFeature fromFeature(Feature feature) {
        return SimpleFeature.builder()
                .id(feature.properties.id)
                .name(feature.properties.name)
                .country(feature.properties.country)
                .countryAbbr(feature.properties.country_a)
                .region(feature.properties.region)
                .regionAbbr(feature.properties.region_a)
                .county(feature.properties.county)
                .localAdmin(feature.properties.localadmin)
                .locality(feature.properties.locality)
                .neighborhood(feature.properties.neighbourhood)
                .label(feature.properties.label)
                .lat(feature.geometry.coordinates.get(1))
                .lng(feature.geometry.coordinates.get(0))
                .build();
    }

    public String address() {
        return label().substring(label().indexOf(", ") + 2);
    }

    public Feature toFeature() {
        final Feature feature = new Feature();
        final Properties properties = new Properties();
        final Geometry geometry = new Geometry();

        properties.id = id();
        properties.name = name();
        properties.country = country();
        properties.country_a = countryAbbr();
        properties.region = region();
        properties.region_a = regionAbbr();
        properties.county = county();
        properties.localadmin = localAdmin();
        properties.locality = locality();
        properties.neighbourhood = neighborhood();
        properties.label = label();

        geometry.coordinates.add(lng());
        geometry.coordinates.add(lat());

        feature.properties = properties;
        feature.geometry = geometry;

        return feature;
    }

    public Parcel toParcel() {
        Parcel parcel = Parcel.obtain();
        writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        return parcel;
    }

    public static SimpleFeature readFromParcel(Parcel in) {
        return AutoParcel_SimpleFeature.CREATOR.createFromParcel(in);
    }
}
