package com.mapzen.android.gson;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Properties {

    @Expose
    private String admin0;
    @Expose
    private String admin1;
    @SerializedName("admin1_abbr")
    @Expose
    private String admin1Abbr;
    @Expose
    private String admin2;
    @Expose
    private String alpha3;
    @SerializedName("local_admin")
    @Expose
    private String localAdmin;
    @Expose
    private String locality;
    @Expose
    private String name;
    @Expose
    private String neighborhood;
    @Expose
    private String text;
    @Expose
    private String type;
    @Expose
    private String id;

    public String getAdmin0() {
        return admin0;
    }

    public void setAdmin0(String admin0) {
        this.admin0 = admin0;
    }

    public String getAdmin1() {
        return admin1;
    }

    public void setAdmin1(String admin1) {
        this.admin1 = admin1;
    }

    public String getAdmin1Abbr() {
        return admin1Abbr;
    }

    public void setAdmin1Abbr(String admin1Abbr) {
        this.admin1Abbr = admin1Abbr;
    }

    public String getAdmin2() {
        return admin2;
    }

    public void setAdmin2(String admin2) {
        this.admin2 = admin2;
    }

    public String getAlpha3() {
        return alpha3;
    }

    public void setAlpha3(String alpha3) {
        this.alpha3 = alpha3;
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

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
