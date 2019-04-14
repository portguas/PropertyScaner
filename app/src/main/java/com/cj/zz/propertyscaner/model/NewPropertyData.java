package com.cj.zz.propertyscaner.model;

import java.io.Serializable;

public class NewPropertyData implements Serializable {
    private String k;
    private String[] v;

    public String getKey() {
        return k;
    }

    public void setKey(String key) {
        this.k = key;
    }

    public String[] getValue() {
        return v;
    }

    public void setValue(String[] value) {
        this.v = value;
    }

    public NewPropertyData(String key, String[] value) {
        this.k = key;
        this.v = value;
    }
}
