package com.mapzen.pelias.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Properties {

    @Expose
    private String country;
    @Expose
    private String region;
    @SerializedName("admin1_abbr")
    @Expose
    private String region_a;
    @Expose
    private String admin2;
    @Expose
    private String country_a;
    @SerializedName("local_admin")
    @Expose
    private String localAdmin;
    @Expose
    private String locality;
    @Expose
    private String name;
    @Expose
    private String neighbourhood;
    @Expose
    private String label;
    @Expose
    private String type;
    @Expose
    private String id;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion_a() {
        return region_a;
    }

    public void setRegion_a(String region_a) {
        this.region_a = region_a;
    }

    public String getAdmin2() {
        return admin2;
    }

    public void setAdmin2(String admin2) {
        this.admin2 = admin2;
    }

    public String getCountry_a() {
        return country_a;
    }

    public void setCountry_a(String country_a) {
        this.country_a = country_a;
    }

    public String getLocalAdmin() {
        return localAdmin;
    }

    public void setLocalAdmin(String localAdmin) {
        this.localAdmin = localAdmin;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
