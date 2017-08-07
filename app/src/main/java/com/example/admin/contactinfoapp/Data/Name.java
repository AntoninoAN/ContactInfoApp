
package com.example.admin.contactinfoapp.Data;

import java.util.HashMap;
import java.util.Map;

public class Name {
    public enum Titles{
        //title("title"),first("first"),last("last");
        title,first,last
        /*private final String nameOf;
        Titles(String nameOf){
            this.nameOf=nameOf;
        }
        String getTitles(String enumTitle){
            return this.nameOf;
        }*/
    }


    private String title;
    private String first;
    private String last;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
