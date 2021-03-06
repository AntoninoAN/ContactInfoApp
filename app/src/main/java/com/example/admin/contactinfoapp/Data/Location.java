
package com.example.admin.contactinfoapp.Data;

import java.util.HashMap;
import java.util.Map;

public class Location {

    private String street;
    private String city;
    private String state;
    private Integer postcode;

    public enum Titles{
        street,city,state
    }
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
