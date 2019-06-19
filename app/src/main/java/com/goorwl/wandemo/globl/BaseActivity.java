package com.goorwl.wandemo.globl;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.goorwl.utils.CoreActivity;
import com.goorwl.wandemo.tool.SonicJavaScriptInterface;
import com.goorwl.wandemo.utils.Config;

public class BaseActivity extends CoreActivity implements Config {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //            getWindow().setStatusBarColor(Color.TRANSPARENT);
        //        } else {
        //            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //        }
        //        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    public void startBroswerActivity(int mode, String DEMO_URL) {
        Intent intent = new Intent(this, WebviewActivity.class);
        intent.putExtra(PARAM_URL, DEMO_URL);
        intent.putExtra(PARAM_MODE, mode);
        intent.putExtra(SonicJavaScriptInterface.PARAM_CLICK_TIME, System.currentTimeMillis());
        startActivityForResult(intent, -1);
    }
}
