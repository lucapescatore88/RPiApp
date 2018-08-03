package com.pluca.rpiapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by pluca on 16/02/17.
 */

public class NetworkUtils {

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork;
    }

    public static boolean isConnected(Context context) {
        NetworkInfo activeNetwork = getNetworkInfo(context);
        return (activeNetwork!=null);
    }

    public static boolean isWifiConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info!=null) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) return true;
            return false;
        }
        return false;
    }

    public static boolean isMobileConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info!=null) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) return true;
            return false;
        }
        return false;
    }

    public static boolean checkNetwork(Context context){
        if(!NetworkUtils.isWifiConnected(context)) {
            Toast.makeText(context, "No Wifi available!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
