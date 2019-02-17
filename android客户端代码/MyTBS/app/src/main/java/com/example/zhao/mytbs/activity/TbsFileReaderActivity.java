package com.example.zhao.mytbs.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.zhao.mytbs.R;
import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;

public class TbsFileReaderActivity extends Activity implements TbsReaderView.ReaderCallback {
    private static final String TAG = "TbsFileReaderActivity";

    private TbsReaderView mTbsReaderView;
    private TextView title;
    private Button back;
    private FrameLayout frameLayout;

    private String tbsReaderTemp = Environment.getExternalStorageDirectory() + "/TbsReaderTemp";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_reader);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        mTbsReaderView = new TbsReaderView(this, this);
        mTbsReaderView.gReaderPackName = "com.example.zhao.mytbs.activity";
        frameLayout = findViewById(R.id.reader);
        frameLayout.addView(mTbsReaderView);
    }

    private void initListener() {
        back.setOnClickListener(v->{
            finish();
        });
    }

    private void initData() {
        Intent intent =getIntent();
        String filePath = intent.getStringExtra("filePath");
        String fileName = intent.getStringExtra("fileName");
        String fileExt = intent.getStringExtra("fileExt");

        title.setText(fileName);

        String bsReaderTemp = tbsReaderTemp;
        File bsReaderTempFile =new File(bsReaderTemp);
        if (!bsReaderTempFile.exists()) {
            boolean mkdir = bsReaderTempFile.mkdir();
            if(!mkdir){
                Log.d("print","创建/TbsReaderTemp失败！！！！！");
            }
        }

        //加载文件
        Bundle localBundle = new Bundle();
        localBundle.putString("filePath", filePath);
        localBundle.putString("tempPath", tbsReaderTemp);

        boolean result = mTbsReaderView.preOpen(fileExt, false);
        if (result) {
            mTbsReaderView.openFile(localBundle);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTbsReaderView.onStop();
    }


    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {
        Log.d(TAG, String.valueOf(integer));
    }
}
