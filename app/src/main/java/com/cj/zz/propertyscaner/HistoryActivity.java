package com.cj.zz.propertyscaner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cj.zz.propertyscaner.adapt.HistoryAdapter;
import com.cj.zz.propertyscaner.adapt.PropertyAdapt;
import com.cj.zz.propertyscaner.db.NewPropertyModel;
import com.cj.zz.propertyscaner.db.NewPropertyModel_Table;
import com.cj.zz.propertyscaner.db.PropertyStatus;
import com.cj.zz.propertyscaner.db.PropertyStatus_Table;
import com.cj.zz.propertyscaner.model.HistoryData;
import com.cj.zz.propertyscaner.model.NewPropertyData;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private List<HistoryData> historyData;
    private HistoryAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initViews();

        readHistoryData();
    }

    private void initViews() {

        historyData = new ArrayList<>();

        recyclerView = findViewById(R.id.propertyHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new HistoryAdapter(this, historyData);
        recyclerView.setAdapter(adapter);
    }

    private void readHistoryData() {
        List<PropertyStatus> status = SQLite.select().from(PropertyStatus.class).queryList();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        for (PropertyStatus statusData : status) {
            List<NewPropertyModel> data = SQLite.select().from(NewPropertyModel.class).where(NewPropertyModel_Table.beginTime.eq(statusData.beginTime)).queryList();
            HistoryData histData = new HistoryData();
            String time = format.format(new Date(Long.valueOf(statusData.beginTime)));
            histData.beginTime = time;
            if (data.size() > 0) {
                if (data.get(0).endTime == null) {
                    histData.endTime = "";
                    histData.endTimeLongValue = 0l;
                }else {
                    histData.endTime = format.format(new Date(Long.valueOf(data.get(0).endTime)));
                    histData.endTimeLongValue = Long.valueOf(data.get(0).endTime);
                }

            }
            histData.scanNumber = String.valueOf(data.size());
            histData.isFinished = statusData.isFinished;
            histData.beginTimeLongValue = Long.valueOf(statusData.beginTime);

            historyData.add(histData);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
