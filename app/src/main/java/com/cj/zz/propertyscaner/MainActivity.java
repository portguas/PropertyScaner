package com.cj.zz.propertyscaner;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

public class MainActivity extends AppCompatActivity {

    private Button btnBegin;
    private Button btnHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlowManager.init(this);
        initView();
    }

    private void initView() {
        btnBegin = findViewById(R.id.begin);
        btnHistory = findViewById(R.id.history);
        btnBegin.setBackgroundColor(Color.parseColor("#1E90FF"));
        btnHistory.setBackgroundColor(Color.parseColor("#FFFFFF"));
        btnBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PropertyActivity.class);
                startActivity(intent);
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HistoryActivity.class);
                startActivity(intent);
            }
        });
    }
}
