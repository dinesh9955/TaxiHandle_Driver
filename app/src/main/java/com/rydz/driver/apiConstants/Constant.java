package com.rydz.driver.apiConstants;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import com.rydz.driver.R;


public class Constant {

    public static ProgressDialog getProgressDialog(Context context, String msg) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        return progressDialog;
    }


    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            showInternaetMessage(context);
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        showInternaetMessage(context);
        return false;
    }

    private static void showInternaetMessage(Context context) {
        Toast.makeText(context, context.getResources().getString(R.string.network_error),Toast.LENGTH_SHORT).show();
    }

}
