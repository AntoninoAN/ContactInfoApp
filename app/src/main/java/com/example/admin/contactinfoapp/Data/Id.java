
package com.example.admin.contactinfoapp.Data;

import java.util.HashMap;
import java.util.Map;

public class Id {

    public enum Titles{
        name,value
    }
    private String name;
    private Object value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }


}
