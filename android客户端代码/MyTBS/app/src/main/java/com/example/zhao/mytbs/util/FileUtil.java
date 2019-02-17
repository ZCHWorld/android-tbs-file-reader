package com.example.zhao.mytbs.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.example.zhao.mytbs.activity.MainActivity;
import com.example.zhao.mytbs.activity.TbsFileReaderActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FileUtil {

    public static void downloadFile(final Context context, String url, String fileName, String pathName, ProgressDialog dialog) {
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection hcont = (HttpURLConnection) httpUrl.openConnection();
            //建立实际链接
            hcont.connect();
            int fileLen = hcont.getContentLength();

            dialog.setIndeterminate(false);
            dialog.setMax(fileLen);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

            //获取输入流内容
            InputStream is = hcont.getInputStream();
            //获取文件长度
            int ddddd = hcont.getContentLength();
            //为进度条赋值
            dialog.setMax(ddddd);
            //写入文件
            OutputStream os = new FileOutputStream(pathName);
            int length;
            int lengtsh = 0;
            byte[] bytes = new byte[1024];
            while ((length = is.read(bytes)) != -1) {
                os.write(bytes, 0, length);
                //获取当前进度值
                lengtsh += length;
                //把值传给handler
                dialog.setProgress(lengtsh);
            }
            //关闭流
            is.close();
            os.close();
            os.flush();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            dialog.dismiss();
            openFile(context, pathName);
        }
    }

    public static void openFile(Context context, String filePath) {
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        String fileExt = filePath.substring(filePath.lastIndexOf(".") + 1);

        Intent intent = new Intent();
        intent.putExtra("filePath", filePath);
        intent.putExtra("fileName", fileName);
        intent.putExtra("fileExt", fileExt);
        intent.setClass(context, TbsFileReaderActivity.class);
        context.startActivity(intent);
    }
}
