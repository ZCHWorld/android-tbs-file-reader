package com.example.zhao.mytbs.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertUtil {

    public static void info(Context context,String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("提示");
        alertDialog.setMessage(message);
        alertDialog.setNegativeButton("知道了", null);
        alertDialog.show();
    }

    public static void sure(Context context, String message, String okButtonName, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("提示");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton(okButtonName, onClickListener);
        alertDialog.setNegativeButton("取消", null);
        alertDialog.show();
    }

    public static void sure2(Context context, String message, String okButtonName, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("提示");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton(okButtonName, onClickListener);
        alertDialog.setNegativeButton("直接打开", onClickListener2);
        alertDialog.show();
    }
}
