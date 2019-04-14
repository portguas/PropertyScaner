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
import com.cj.zz.propertyscaner.adapt.PropertyAdapt;
import com.cj.zz.propertyscaner.model.ExcelBean;
import com.cj.zz.propertyscaner.model.NewPropertyData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class PropertyResultActivity extends AppCompatActivity {

    private TextView resultDesc;
    private Button exportToExcel;

    private RecyclerView propertyResult;
    private PropertyAdapt adapter;
    private List<NewPropertyData> propertyData;

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
            Toast.makeText(this, "您还没申请权限!", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);
        } else {
            Toast.makeText(this, "您已经申请了权限!", Toast.LENGTH_SHORT).show();
//            saveToLocal();
        }

    }

    private void readData() {
        List alist = (List<Object>)getIntent().getSerializableExtra("data");
        propertyData = alist;
        adapter.setData(alist);
        String begin = getIntent().getStringExtra("beginTime");
        String endTiem = getIntent().getStringExtra("endTime");
        String string = getResources().getString(R.string.result_desc);
        resultDesc.setText(String.format(string, begin, endTiem, alist.size()));
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
        String filePath =  Environment.getDataDirectory().getPath() + "/AndroidExcel";
        File file = new File(filePath);
        if (!file.exists()) {
            Boolean success = file.mkdir();
            if (success) {
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            }
        }
        String excelFileName = "/demo.xls";

        String[] title = {"编码"};
        String sheetName = "PropertySheet";

        List<ExcelBean> list = new ArrayList<>();
        for (NewPropertyData data : propertyData) {
            ExcelBean bean = new ExcelBean(data.getKey());
            list.add(bean);
        }

        filePath = filePath + excelFileName;
        ExcelUtil.initExcel(filePath, sheetName, title);
        ExcelUtil.writeObjListToExcel(list, filePath, this);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PERMISSION_GRANTED) {//选择了“始终允许”
                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请成功", Toast.LENGTH_SHORT).show();
                } else {
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
    }
}
