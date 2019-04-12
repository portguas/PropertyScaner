package com.cj.zz.propertyscaner.model;

import java.io.Serializable;

public class PropertyData implements Serializable {

    private String propertyCode;
    private String propertyName;
    private String propertyBrand;
    private String propertyKeeper;
    private String securityLevel;
    private String manufNumber;

    public PropertyData(String propertyCode, String propertyName, String propertyBrand, String propertyKeeper, String securityLevel, String manufNumber) {
        this.propertyCode = propertyCode;
        this.propertyName = propertyName;
        this.propertyBrand = propertyBrand;
        this.propertyKeeper = propertyKeeper;
        this.securityLevel = securityLevel;
        this.manufNumber = manufNumber;
    }

    public String getPropertyCode() {
        return propertyCode;
    }

    public void setPropertyCode(String propertyCode) {
        this.propertyCode = propertyCode;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyBrand() {
        return propertyBrand;
    }

    public void setPropertyBrand(String propertyBrand) {
        this.propertyBrand = propertyBrand;
    }

    public String getPropertyKeeper() {
        return propertyKeeper;
    }

    public void setPropertyKeeper(String propertyKeeper) {
        this.propertyKeeper = propertyKeeper;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }

    public String getManufNumber() {
        return manufNumber;
    }

    public void setManufNumber(String manufNumber) {
        this.manufNumber = manufNumber;
    }
}
