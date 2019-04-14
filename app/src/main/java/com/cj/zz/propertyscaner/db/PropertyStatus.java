package com.cj.zz.propertyscaner.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = AppDataBase.class)
public class PropertyStatus extends BaseModel {

    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public String beginTime;

    @Column
    public boolean isFinished;

    @Column
    public String savePath;
}
