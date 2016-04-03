package uneg.software.sebu.utils;

import android.support.v4.app.ActivityCompat;

import permissions.dispatcher.PermissionUtils;
import uneg.software.sebu.activities.LoginActivity;

/**
 * Created by Jhonny on 21/3/2016.
 */
public class PermissionsDispatcher {
    private static final int REQUEST = 1;

    private static final String[] PERMISSIONS = new String[] {"android.permission.WRITE_EXTERNAL_STORAGE","android.permission.ACCESS_COARSE_LOCATION","android.permission.ACCESS_FINE_LOCATION","android.permission.CAMERA","android.permission.SYSTEM_ALERT_WINDOW"};

    private PermissionsDispatcher() {
    }

    public static void showDialogPermissions(LoginActivity target) {
        if (PermissionUtils.hasSelfPermissions(target, PERMISSIONS)) {
            //target.openAlertar();
        } else {
            ActivityCompat.requestPermissions(target, PERMISSIONS, REQUEST);
        }
    }

    public static void onRequestPermissionsResult(LoginActivity target, int requestCode, int[] grantResults) {
        switch (requestCode) {
            case REQUEST:
                if (!PermissionUtils.hasSelfPermissions(target, PERMISSIONS)) {
                    target.finish();
                }else
                {
                    //target.openAlertar();
                }
                break;
            default:
                break;
        }
    }
}