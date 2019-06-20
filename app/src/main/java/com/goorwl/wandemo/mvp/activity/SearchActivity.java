package com.goorwl.wandemo.mvp.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.goorwl.utils.SPUtils;
import com.goorwl.wandemo.R;
import com.goorwl.wandemo.bean.HotWordResBean;
import com.goorwl.wandemo.globl.BaseActivity;
import com.goorwl.wandemo.mvp.imple.SearchActivityImple;
import com.goorwl.wandemo.mvp.presenter.SearchActivityPresenter;
import com.goorwl.wandemo.utils.GsonUtils;

import java.util.List;

public class SearchActivity extends BaseActivity implements SearchActivityImple {

    private SearchActivityPresenter mPresenter;
    private EditText                mEt;
    private FlexboxLayout           mFlexboxLayout;
    private TextView                mTvEmpty;
    private FlexboxLayout           mFlexboxHistory;

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
        initData();
    }

    private void initData() {
        mPresenter.getHotword();
        mPresenter.getHistory();
    }

    private void initView() {
        // GET INSTANCE OF PRESENTER
        mPresenter = new SearchActivityPresenter(this, this);

        mFlexboxLayout = findViewById(R.id.search_flexbox);
        mEt = findViewById(R.id.search_et);
        mFlexboxHistory = findViewById(R.id.search_flexbox_history);
        mTvEmpty = findViewById(R.id.search_history_empty);
        findViewById(R.id.search_search).setOnClickListener(v -> {
            // TODO: 2019/6/20 开始搜索
        });
        findViewById(R.id.search_clear).setOnClickListener(v -> {
            SPUtils.putString(SP_WORD_SEARCH_HISTORY, "");
            mFlexboxHistory.setVisibility(View.INVISIBLE);
            mTvEmpty.setVisibility(View.VISIBLE);
        });
        findViewById(R.id.search_back).setOnClickListener(v -> finish());
    }

    @Override
    public void loadHotWord(String string) {
        HotWordResBean hotWordResBean = GsonUtils.getInstance().fromJson(string, HotWordResBean.class);
        if (hotWordResBean.getErrorCode() == 0) {
            List<HotWordResBean.DataBean> data = hotWordResBean.getData();
            for (int i = 0; i < data.size(); i++) {
                // 通过代码向FlexboxLayout添加View
                String   res      = data.get(i).getName();
                TextView textView = new TextView(this);
                textView.setText(res);
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(30, 30, 30, 30);
                textView.setTextColor(getResources().getColor(R.color.red));
                textView.setOnClickListener(v -> {
                    mEt.setText(res);
                    mEt.setSelection(res.length());
                });
                mFlexboxLayout.addView(textView);
            }
        } else {

        }
    }

    @Override
    public void loadHistory(String string) {
        
    }
}
