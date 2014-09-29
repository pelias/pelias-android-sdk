
package com.mapzen.android.gson;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Properties {

    @Expose
    private String address_name;
    @Expose
    private String admin0_abbr;
    @Expose
    private String admin0_name;
    @Expose
    private String admin1_abbr;
    @Expose
    private String admin1_name;
    @Expose
    private String admin2_name;
    @Expose
    private String hint;
    @Expose
    private String local_admin_name;
    @Expose
    private String locality_name;
    @Expose
    private String text;
    @Expose
    private String neighborhood_name;
    @Expose
    private String poi_name;
    @Expose
    private String street_name;
    @Expose
    private String type;

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public Properties withAddress_name(String address_name) {
        this.address_name = address_name;
        return this;
    }

    public String getAdmin0_abbr() {
        return admin0_abbr;
    }

    public void setAdmin0_abbr(String admin0_abbr) {
        this.admin0_abbr = admin0_abbr;
    }

    public Properties withAdmin0_abbr(String admin0_abbr) {
        this.admin0_abbr = admin0_abbr;
        return this;
    }

    public String getAdmin0_name() {
        return admin0_name;
    }

    public void setAdmin0_name(String admin0_name) {
        this.admin0_name = admin0_name;
    }

    public Properties withAdmin0_name(String admin0_name) {
        this.admin0_name = admin0_name;
        return this;
    }

    public String getAdmin1_abbr() {
        return admin1_abbr;
    }

    public void setAdmin1_abbr(String admin1_abbr) {
        this.admin1_abbr = admin1_abbr;
    }

    public Properties withAdmin1_abbr(String admin1_abbr) {
        this.admin1_abbr = admin1_abbr;
        return this;
    }

    public String getAdmin1_name() {
        return admin1_name;
    }

    public void setAdmin1_name(String admin1_name) {
        this.admin1_name = admin1_name;
    }

    public Properties withAdmin1_name(String admin1_name) {
        this.admin1_name = admin1_name;
        return this;
    }

    public String getAdmin2_name() {
        return admin2_name;
    }

    public void setAdmin2_name(String admin2_name) {
        this.admin2_name = admin2_name;
    }

    public Properties withAdmin2_name(String admin2_name) {
        this.admin2_name = admin2_name;
        return this;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public Properties withHint(String hint) {
        this.hint = hint;
        return this;
    }

    public String getLocal_admin_name() {
        return local_admin_name;
    }

    public void setLocal_admin_name(String local_admin_name) {
        this.local_admin_name = local_admin_name;
    }

    public Properties withLocal_admin_name(String local_admin_name) {
        this.local_admin_name = local_admin_name;
        return this;
    }

    public String getLocality_name() {
        return locality_name;
    }

    public void setLocality_name(String locality_name) {
        this.locality_name = locality_name;
    }

    public Properties withLocality_name(String locality_name) {
        this.locality_name = locality_name;
        return this;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Properties withText(String text) {
        this.text = text;
        return this;
    }

    public String getNeighborhood_name() {
        return neighborhood_name;
    }

    public void setNeighborhood_name(String neighborhood_name) {
        this.neighborhood_name = neighborhood_name;
    }

    public Properties withNeighborhood_name(String neighborhood_name) {
        this.neighborhood_name = neighborhood_name;
        return this;
    }

    public String getPoi_name() {
        return poi_name;
    }

    public void setPoi_name(String poi_name) {
        this.poi_name = poi_name;
    }

    public Properties withPoi_name(String poi_name) {
        this.poi_name = poi_name;
        return this;
    }

    public String getStreet_name() {
        return street_name;
    }

    public void setStreet_name(String street_name) {
        this.street_name = street_name;
    }

    public Properties withStreet_name(String street_name) {
        this.street_name = street_name;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Properties withType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

}
