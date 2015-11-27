//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.letv.recorder.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkUtils {
    public static final String Type_WIFI = "wifi";
    public static final String Type_4G = "4g";
    public static final String Type_3G = "3g";
    public static final String Type_2G = "2g";

    public NetworkUtils() {
    }

    public static boolean isWifiNetType(Context context) {
        String type = getNetType(context);
        return "wifi".equals(type);
    }

    public static String getNetType(Context context) {
        if(context == null) {
            return null;
        } else {
            String type = null;
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService("connectivity");
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if(ni != null && ni.isConnectedOrConnecting()) {
                switch(ni.getType()) {
                    case 0:
                        switch(ni.getSubtype()) {
                            case 1:
                            case 2:
                            case 4:
                            case 7:
                            case 11:
                                type = "2g";
                                return type;
                            case 3:
                            case 5:
                            case 6:
                            case 8:
                            case 9:
                            case 10:
                            case 12:
                            case 14:
                            case 15:
                                type = "3g";
                                return type;
                            case 13:
                                type = "4g";
                                return type;
                            default:
                                type = null;
                                return type;
                        }
                    case 1:
                        type = "wifi";
                        break;
                    default:
                        type = null;
                }
            }

            return type;
        }
    }

    public static boolean getNetWorkStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService("connectivity");
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            if(activeNetwork.getType() == 1) {
                return true;
            }

            if(activeNetwork.getType() == 0) {
                return false;
            }
        }

        return false;
    }

    public static boolean isNetAvailable(Context paramContext) {
        boolean isAvilable = false;

        try {
            ConnectivityManager localException = (ConnectivityManager)paramContext.getSystemService("connectivity");
            NetworkInfo localNetworkInfo = localException.getActiveNetworkInfo();
            if(localNetworkInfo != null) {
                isAvilable = localNetworkInfo.isAvailable();
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return isAvilable;
    }

    public static String getMacaddress(Context context) {
        WifiManager wifi = (WifiManager)context.getSystemService("wifi");
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    public static String getTopDomainWithoutSubdomain(String url) {
        try {
            String e = (new URL(url)).toString().toLowerCase();
            Pattern pattern = Pattern.compile("(http://|)((\\w)+\\.)+\\w+");
            Matcher matcher = pattern.matcher(e);
            if(matcher.find()) {
                return matcher.group();
            }
        } catch (MalformedURLException var4) {
            var4.printStackTrace();
        }

        return null;
    }
}
