package com.cj.zz.propertyscaner;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
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

import com.cj.zz.propertyscaner.Util.ExcelUtil;
import com.cj.zz.propertyscaner.Util.PreferencesUtils;
import com.cj.zz.propertyscaner.adapt.PropertyAdapt;
import com.cj.zz.propertyscaner.db.NewPropertyModel;
import com.cj.zz.propertyscaner.db.NewPropertyModel_Table;
import com.cj.zz.propertyscaner.db.PropertyStatus;
import com.cj.zz.propertyscaner.db.PropertyStatus_Table;
import com.cj.zz.propertyscaner.model.ExcelBean;
import com.cj.zz.propertyscaner.model.NewPropertyData;
import com.cj.zz.propertyscaner.model.PropertyData;
import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class PropertyResultActivity extends AppCompatActivity {

    private TextView resultDesc;
    private Button exportToExcel;

    private RecyclerView propertyResult;
    private PropertyAdapt adapter;
    private List<NewPropertyData> propertyData;
    private Long currentInventoryTime;
    private Long endTime;
    private int dataCount;

    private AlertDialog alertDialog;
    private AlertDialog mDialog;
    private static final int NOT_NOTICE = 2;//如果勾选了不再询问

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_result);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        propertyData = new ArrayList<NewPropertyData>();
        initViews();

        readData();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
//            Toast.makeText(this, "您还没申请权限!", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);
        } else {
//            Toast.makeText(this, "您已经申请了权限!", Toast.LENGTH_SHORT).show();
            if (!getIntent().getBooleanExtra("fromHistory", false)) {
                saveToLocal();
            }
        }
    }

    private void readData() {
        Long beginTime = getIntent().getLongExtra("beginTime", 0);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String begin = format.format(new Date(beginTime));

        endTime = getIntent().getLongExtra("endTime", 0);
        String end = format.format(new Date(endTime));

        currentInventoryTime = beginTime;
        propertyData.clear();
        if (!getIntent().getBooleanExtra("fromHistory", false)) {
            List alist = (List<Object>)getIntent().getSerializableExtra("data");
            propertyData = alist;
            adapter.setData(alist);
            dataCount = alist.size();
            String string = getResources().getString(R.string.result_desc);
            resultDesc.setText(String.format(string, begin, end, dataCount));
        }else {
            dataCount = Integer.valueOf(getIntent().getStringExtra("count"));
            List<NewPropertyModel> data = SQLite.select()
                    .from(NewPropertyModel.class)
                    .where(NewPropertyModel_Table.beginTime
                            .eq(String .valueOf(beginTime))).queryList();
            for (NewPropertyModel model : data) {
                Gson gson = new Gson();
                NewPropertyData pdata = gson.fromJson(model.propertyJson, NewPropertyData.class);
                propertyData.add(pdata);
            }
            adapter.setData(propertyData);

            List<PropertyStatus> list  = SQLite.select()
                    .from(PropertyStatus.class)
                    .where(PropertyStatus_Table.beginTime
                            .eq(String .valueOf(beginTime))).queryList();
            if (list.size() == 1) {
                String string = getResources().getString(R.string.result_desc1);
                resultDesc.setText(String.format(string, begin, end, dataCount, list.get(0).savePath));
            }
        }
    }

    private void initViews() {
        resultDesc = findViewById(R.id.inventory_result);
        exportToExcel = findViewById(R.id.exportExcelAgain);
        propertyResult = findViewById(R.id.propertyResult);
        propertyResult.setLayoutManager(new LinearLayoutManager(this));
        exportToExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToLocal();
            }
        });

        adapter = new PropertyAdapt(this, propertyData);
        propertyResult.setAdapter(adapter);
    }

    private void saveToLocal() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-ddHHmmss");
        PreferencesUtils.putBoolean(this, "inventoring", false);
        String filePath =  Environment.getExternalStorageDirectory().getPath() + "/PropertyExcel";
        File file = new File(filePath);
        if (!file.exists()) {
            Boolean success = file.mkdir();
            if (success) {
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            }
        }
        String excelFileName = "/" + format1.format(new Date(currentInventoryTime)) + "~" + format1.format(endTime) + ".xls";
//        String excelFileName = "/" + currentInventoryTime + ".xls";
        String[] title = {"编码"};
        String sheetName = "PropertySheet";

        List<ExcelBean> list = new ArrayList<>();
        for (NewPropertyData data : propertyData) {
            ExcelBean bean = new ExcelBean(data.getKey());
            list.add(bean);
        }

        filePath = filePath + excelFileName;

        String savePath = "/sdcard/PropertyExcel" + excelFileName;
        String string = getResources().getString(R.string.result_desc1);
        String time = format.format(new Date(currentInventoryTime));
        resultDesc.setText(String.format(string, time, format.format(endTime), dataCount, savePath));

        ExcelUtil.initExcel(filePath, sheetName, title);
        ExcelUtil.writeObjListToExcel(list, filePath, this, time, format.format(endTime));

        PreferencesUtils.putBoolean(this, "inventoryBeing", false);

        SQLite.update(NewPropertyModel.class)
                .set(NewPropertyModel_Table.endTime.eq(String.valueOf(endTime)))
                .where(NewPropertyModel_Table.beginTime.eq(String.valueOf(currentInventoryTime)))
                .execute();

        SQLite.update(PropertyStatus.class)
                .set(PropertyStatus_Table.isFinished.eq(true))
                .where(PropertyStatus_Table.beginTime.eq(String.valueOf(currentInventoryTime)))
                .execute();


        SQLite.update(PropertyStatus.class)
                .set(PropertyStatus_Table.savePath.eq(savePath))
                .where(PropertyStatus_Table.beginTime.eq(String.valueOf(currentInventoryTime)))
                .execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.setResult(1002);
                this.finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        this.setResult(1002);
        this.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Boolean granted = true;
        if (requestCode == 1) {

            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PERMISSION_GRANTED) {//选择了“始终允许”
//                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请成功", Toast.LENGTH_SHORT).show();
                } else {
                    granted = false;
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {//用户选择了禁止不再询问

                        AlertDialog.Builder builder = new AlertDialog.Builder(PropertyResultActivity.this);
                        builder.setTitle("permission")
                                .setMessage("点击允许才可以使用我们的app哦")
                                .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (mDialog != null && mDialog.isShowing()) {
                                            mDialog.dismiss();
                                        }
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);//注意就是"package",不用改成自己的包名
                                        intent.setData(uri);
                                        startActivityForResult(intent, NOT_NOTICE);
                                    }
                                });
                        mDialog = builder.create();
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.show();


                    } else {//选择禁止
                        AlertDialog.Builder builder = new AlertDialog.Builder(PropertyResultActivity.this);
                        builder.setTitle("permission")
                                .setMessage("点击允许才可以使用我们的app哦")
                                .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (alertDialog != null && alertDialog.isShowing()) {
                                            alertDialog.dismiss();
                                        }
                                        ActivityCompat.requestPermissions(PropertyResultActivity.this,
                                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                    }
                                });
                        alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                    }

                }
            }
        }

        if (granted) {
            saveToLocal();
        }
    }
}
