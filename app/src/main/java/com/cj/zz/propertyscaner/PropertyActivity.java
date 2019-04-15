package com.cj.zz.propertyscaner;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.cj.zz.propertyscaner.db.NewPropertyModel;
import com.cj.zz.propertyscaner.db.NewPropertyModel_Table;
import com.cj.zz.propertyscaner.db.PropertyStatus;
import com.cj.zz.propertyscaner.db.PropertyStatus_Table;
import com.cj.zz.propertyscaner.model.NewPropertyData;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Condition;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class PropertyActivity extends AppCompatActivity {

    private Button btnBeginScan;
    private Button btnExport;
    private RecyclerView propertyList;
    private PropertyAdapt adapter;
    private TextView inventoryDesc;
    private List<NewPropertyData> propertyData;
    static boolean isFirstClick = true;
    private String currentTime;
    private long beginTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        propertyData = new ArrayList<NewPropertyData>();

        initViews();

        readDataFromDB();
    }

    private void readDataFromDB() {
        List<PropertyStatus> data = SQLite.select().from(PropertyStatus.class).where(PropertyStatus_Table.isFinished.eq(false)).queryList();
        if (data.size() == 1) {
            String lastInventoryTime = data.get(0).beginTime;
            List<NewPropertyModel> list = SQLite.select().from(NewPropertyModel.class).where(NewPropertyModel_Table.beginTime.eq(lastInventoryTime)).queryList();
            if (list.size() > 0) {
                for (NewPropertyModel model : list) {
                    Gson gson = new Gson();
                    NewPropertyData pdata = gson.fromJson(model.propertyJson, NewPropertyData.class);
                    propertyData.add(pdata);
                }
                beginTime = Long.valueOf(lastInventoryTime);
                isFirstClick = false;
                refreshDescription();
            }
        }
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
                if (isFirstClick) {
                    beginTime = System.currentTimeMillis();
                    currentTime = String.valueOf(beginTime);
                }
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
                if (propertyData.size() <= 0) {
                    Toast.makeText(v.getContext(), "沒有盘点数据", Toast.LENGTH_SHORT).show();
                    return;
                }
                goToExcel();
            }
        });

        inventoryDesc = findViewById(R.id.inventory_desc);
        String string = getResources().getString(R.string.property_desc);
        if (propertyData.size() > 0) {
            inventoryDesc.setVisibility(View.VISIBLE);
        }else {
            inventoryDesc.setVisibility(View.INVISIBLE);
        }


        propertyList = findViewById(R.id.propertyList);
        propertyList.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PropertyAdapt(this, propertyData);
        propertyList.setAdapter(adapter);

    }

    private void goToExcel() {
        Intent intent = new Intent(this, PropertyResultActivity.class);
        intent.putExtra("data", (Serializable) propertyData);
        intent.putExtra("beginTime", beginTime);
        intent.putExtra("endTime", System.currentTimeMillis());
        intent.putExtra("fromHistory", false);
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if ( result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }else {
                isFirstClick = false;
                String jsonString = Util.decode(result.getContents());
                Gson gson = new Gson();
                NewPropertyData pdata = gson.fromJson(jsonString, NewPropertyData.class);
                adapter.addData(pdata);
                // 存储数据
                NewPropertyModel model = new NewPropertyModel();
                model.propertyJson = jsonString;
                model.beginTime = currentTime;
                model.save();
                // 存储状态
                PropertyStatus status = new PropertyStatus();
                status.beginTime = currentTime;
                status.isFinished = MainActivity.isinventoryFinished;
                status.save();
                propertyList.smoothScrollToPosition(0);
                refreshDescription();
//                Toast.makeText(this, "scanned" + pdata.getKey(), Toast.LENGTH_LONG).show();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void refreshDescription() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        String time = format.format(new Date(beginTime));
        String string = getResources().getString(R.string.property_desc);
        inventoryDesc.setVisibility(View.VISIBLE);
        inventoryDesc.setText(String.format(string, time, propertyData.size()));
    }
}
