package com.goorwl.wandemo.mvp.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.goorwl.wandemo.R;
import com.goorwl.wandemo.globl.BaseActivity;
import com.goorwl.wandemo.mvp.imple.SearchActivityImple;
import com.goorwl.wandemo.mvp.presenter.SearchActivityPresenter;

public class SearchActivity extends BaseActivity implements SearchActivityImple {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.activity_search);

        initView();
    }

    private void initView() {
        // GET INSTANCE OF PRESENTER
        new SearchActivityPresenter(this, this);
        FlexboxLayout flexboxLayout = findViewById(R.id.search_flexbox);

        findViewById(R.id.search_back).setOnClickListener(v -> finish());

        for (int i = 0; i < 10; i++) {
            // 通过代码向FlexboxLayout添加View
            TextView textView = new TextView(this);
            textView.setText("Test  Label" + i);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(30, 0, 30, 0);
            textView.setTextColor(getResources().getColor(R.color.red));
            flexboxLayout.addView(textView);
        }

    }
}
