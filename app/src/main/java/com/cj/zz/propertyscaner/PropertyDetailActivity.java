package com.cj.zz.propertyscaner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cj.zz.propertyscaner.model.NewPropertyData;
import com.cj.zz.propertyscaner.model.PropertyData;

import java.util.ArrayList;
import java.util.List;

public class PropertyDetailActivity extends AppCompatActivity {

    private PropertyData data;
    private TextView propertyCode;
    private TextView propertyName;
    private Button continueInverntory;
    private NewPropertyData newPropertyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_detail);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        newPropertyData = (NewPropertyData) getIntent().getSerializableExtra("detail");

        initViews();

    }

    public void  initViews() {
        propertyCode = findViewById(R.id.propertyCode);
        propertyName = findViewById(R.id.propertyName);

        continueInverntory = findViewById(R.id.continueInventory);
        continueInverntory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        propertyCode.setText(newPropertyData.getKey());
        String[] info = newPropertyData.getValue();
        String allInfo = "";
        for (String i : info) {
            allInfo += i + "; \n";
        }
        propertyName.setText(allInfo);

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
