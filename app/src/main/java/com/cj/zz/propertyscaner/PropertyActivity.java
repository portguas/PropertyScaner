package com.cj.zz.propertyscaner;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cj.zz.propertyscaner.Util.Util;
import com.cj.zz.propertyscaner.adapt.PropertyAdapt;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class PropertyActivity extends AppCompatActivity {

    private Button btnBeginScan;
    private Button btnExport;
    private RecyclerView propertyList;
    private PropertyAdapt adapter;
    private TextView inventoryDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initViews();
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

    private void initViews() {
        btnBeginScan = findViewById(R.id.beginScan);
        btnExport = findViewById(R.id.exportToExcel);
        btnBeginScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = null;
                Context context = v.getContext();
                while (context instanceof ContextWrapper) {
                    if (context instanceof Activity) {
                        activity = (Activity) context;
                        break;
                    }
                    context = ((ContextWrapper) context).getBaseContext();
                }

                new IntentIntegrator(activity).initiateScan();
            }
        });

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PropertyResultActivity.class);
                startActivity(intent);
            }
        });

        inventoryDesc = findViewById(R.id.inventory_desc);
        String string = getResources().getString(R.string.property_desc);
        inventoryDesc.setText(String.format(string, "2010", 3));

        propertyList = findViewById(R.id.propertyList);
        propertyList.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PropertyAdapt(this);
        propertyList.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if ( result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }else {
//                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                Toast.makeText(this, "scanned" + Util.decode(result.getContents()), Toast.LENGTH_LONG).show();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
