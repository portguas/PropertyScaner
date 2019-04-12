package com.cj.zz.propertyscaner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class HistoryActivity extends AppCompatActivity {

    private TextView beginTime;
    private TextView endTime;
    private TextView scanNumber;
    private TextView scanStatus;
    private TextView operation;

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
    }

    private void initViews() {
        beginTime = findViewById(R.id.history_beginTime);
        endTime = findViewById(R.id.history_endTime);
        scanNumber = findViewById(R.id.scanNumber);
        scanStatus = findViewById(R.id.history_status);
        operation = findViewById(R.id.history_Operation);

        operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
