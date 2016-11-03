package com.example.konka.workbench.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @file com.example.konka.workbench.util.IsOnline (本文件的文件名)
 * @breif 检测是否可以连接网络 (本文件实现的功能的简述)
 *
 * 检测是否可以连接网络. (本文件的功能详述)
 *
 * @author zhuhua (作者)
 * @version V1.0.00 (版本声明)
 * @date 2016-10-28 10:18
 */
public class IsOnline {

    public static boolean isOnline(Context context) {
        boolean isOnline;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            isOnline = true;
        } else {
            isOnline = false;
        }
        return isOnline;
    }
}
