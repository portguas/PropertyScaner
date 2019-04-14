package com.cj.zz.propertyscaner.Util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.security.Permission;

public class PermissionUtil {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void RequestPermission(final Context context) {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (PermissionUtil.lacksPermissions(context, permissions)) {
            //已经获取相关权限
        } else {
            ActivityCompat.requestPermissions((Activity) context, permissions, 0);
        }
    }

    public static boolean lacksPermissions(Context mContexts, String[] permissionsREAD) {
        for (String permission : permissionsREAD) {
            if (lacksPermission(mContexts, permission)) {
                return true;
            }
        }
        return false;
    }

    private static boolean lacksPermission(Context mContexts, String permission) {
        return ContextCompat.checkSelfPermission(mContexts, permission) ==
                PackageManager.PERMISSION_DENIED;
    }
}
