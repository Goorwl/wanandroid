package com.goorwl.wandemo.mvp.activity;

import android.os.Bundle;

import com.goorwl.utils.LogUtils;
import com.goorwl.wandemo.R;
import com.goorwl.wandemo.globl.BaseActivity;
import com.goorwl.wandemo.mvp.imple.SearchResActivityImple;
import com.goorwl.wandemo.mvp.presenter.SearchResActivityPresenter;

public class SearchResActivity extends BaseActivity implements SearchResActivityImple {

    private SearchResActivityPresenter mPresenter;

    private static final String TAG = "SearchResActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_res);

        initView();
    }

    private void initView() {
        // GET INSTANCE OF PRESENTER
        mPresenter = new SearchResActivityPresenter(this, this);

        Bundle extras = getIntent().getExtras();
        String string = extras.getString(CONSTANT_JUMP_DATA);
        LogUtils.e(TAG, "initView: " + string);
    }
}
