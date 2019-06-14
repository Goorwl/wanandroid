package com.goorwl.wandemo.mvp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.goorwl.wandemo.R;
import com.goorwl.wandemo.mvp.imple.WebActivityImple;
import com.goorwl.wandemo.mvp.presenter.WebActivityPresenter;

public class WebActivity extends AppCompatActivity implements WebActivityImple {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        initView();
    }

    private void initView() {
        // GET INSTANCE OF PRESENTER
        new WebActivityPresenter(this, this);
    }
}
