package com.mo.kumengpush;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * @ author：mo
 * @ data：2019/5/22:13:24
 * @ 功能：
 */
public class TestActivity extends AppCompatActivity {
    private TextView tv_main;
    public static boolean tag=true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_main=findViewById(R.id.tv_main);
        tv_main.setText("推送");
        tag=false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        tag=true;
    }
}
