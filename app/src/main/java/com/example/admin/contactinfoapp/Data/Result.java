
package com.example.admin.contactinfoapp.Data;

import java.util.HashMap;
import java.util.Map;

public final class Result {

    private String gender;
    private Name name;
    private Location location;
    private String email;
    private Login login;
    private String dob;
    private String registered;
    private String phone;
    private String cell;
    private Id id;
    private Picture picture;
    private String nat;
public enum Titles{
    name,location,email,picture,gender
}
    public Result(){}

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name=name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }


}
