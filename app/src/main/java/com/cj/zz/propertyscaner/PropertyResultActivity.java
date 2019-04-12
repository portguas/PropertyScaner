package com.cj.zz.propertyscaner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cj.zz.propertyscaner.adapt.PropertyAdapt;

public class PropertyResultActivity extends AppCompatActivity {

    private TextView resultDesc;
    private Button exportToExcel;

    private RecyclerView propertyResult;
    private PropertyAdapt adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_result);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initViews();
    }

    private void initViews() {
        resultDesc = findViewById(R.id.inventory_desc);
        exportToExcel = findViewById(R.id.exportExcelAgain);
        exportToExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        adapter = new PropertyAdapt(this);
        propertyResult.setAdapter(adapter);
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
