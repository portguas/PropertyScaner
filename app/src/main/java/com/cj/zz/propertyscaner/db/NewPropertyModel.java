package com.cj.zz.propertyscaner.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

@Table(database = AppDataBase.class)
public class NewPropertyModel extends BaseModel implements Serializable {

    @PrimaryKey(autoincrement = true)
    public int id;

    // 保存的是整个二维码扫描出来的json
    @Column
    public String propertyJson;

    // 未处理的时间戳格式
    @Column
    public String beginTime;

    @Column
    public String endTime;

}
