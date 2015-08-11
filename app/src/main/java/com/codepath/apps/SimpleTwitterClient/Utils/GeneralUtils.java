package com.codepath.apps.SimpleTwitterClient.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import org.json.JSONObject;

/**
 * Created by jordanhsu on 8/12/15.
 */
public class GeneralUtils {
    public static Boolean checkJSONObjectCol(String key, JSONObject obj){
        if (obj.has(key) && !obj.isNull(key)){
            return true;
        }
        else {
            return false;
        }
    }

    public static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean status = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        if(status == false){
            Toast.makeText(context, "Network Connection Error!", Toast.LENGTH_SHORT).show();
        }
        return status;
    }
}
