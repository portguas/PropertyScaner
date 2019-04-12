package com.cj.zz.propertyscaner.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = AppDataBase.class)
public class PropertyModel extends BaseModel {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @PrimaryKey(autoincrement = true)
    private int id;

    @Column
    private String propertyCode;

    @Column
    private String propertyName;

    @Column
    private String propertyBrand;

    @Column
    private String propertyKeeper;

    @Column
    private String securityLevel;

    @Column
    private String manufNumber;

    @Column
    private String beginTime;

    @Column
    private String endTime;

    @Column
    private long timeStamp;
}
