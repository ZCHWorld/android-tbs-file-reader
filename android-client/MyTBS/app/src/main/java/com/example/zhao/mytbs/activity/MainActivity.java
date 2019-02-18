package com.example.zhao.mytbs.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.zhao.mytbs.R;
import com.example.zhao.mytbs.callback.HttpCallback;
import com.example.zhao.mytbs.util.AlertUtil;
import com.example.zhao.mytbs.util.FileUtil;
import com.example.zhao.mytbs.util.HttpUtil;
import com.google.gson.Gson;
import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends Activity {
    private Button mGet;
    private ListView mList;

    private ProgressDialog progressDialog;

    // 这里换成你的开发机的ip
    private static final String HOST = "192.168.0.106";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
    }

    private void initView() {
        mGet = findViewById(R.id.get);
        mList = findViewById(R.id.list);
    }

    private void initListener() {
        mGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.get("http://" + HOST + ":8080/file/list", new HttpCallback() {
                    @Override
                    public void onSuccess(Object result) {
                        String data = result.toString();
                        List list = new ArrayList();
                        if (result != null || result != "") {
                            Gson gson = new Gson();
                            list = gson.fromJson(data, List.class);
                        }
                        ListAdapter adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, list);
                        mList.setAdapter(adapter);
                    }
                });
            }
        });

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String fileName = ((TextView) view).getText().toString();
                final String pathName = MainActivity.this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + fileName;
                final File file = new File(pathName);
                DialogInterface.OnClickListener ok = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String url = "http://" + HOST + ":8080/file/download?fileName=" + fileName;
                        progressDialog = new ProgressDialog(MainActivity.this);
                        progressDialog.setTitle("下载呢");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                FileUtil.downloadFile(MainActivity.this, url, fileName, pathName, progressDialog);
                            }
                        }).start();
                    }
                };
                if (file.exists()) {
                    DialogInterface.OnClickListener open = new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FileUtil.openFile(MainActivity.this, pathName);
                        }
                    };
                    AlertUtil.sure2(MainActivity.this, fileName + "已经存在" + file.getPath(), "重新下载", ok, open);
                } else {
                    AlertUtil.sure(MainActivity.this, "是否要下载" + fileName, "下载", ok);
                }
                ;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
