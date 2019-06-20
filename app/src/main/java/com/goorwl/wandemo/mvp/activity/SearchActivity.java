package com.goorwl.wandemo.mvp.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.goorwl.utils.LogUtils;
import com.goorwl.utils.SPUtils;
import com.goorwl.wandemo.R;
import com.goorwl.wandemo.bean.HotWordResBean;
import com.goorwl.wandemo.globl.BaseActivity;
import com.goorwl.wandemo.mvp.imple.SearchActivityImple;
import com.goorwl.wandemo.mvp.presenter.SearchActivityPresenter;
import com.goorwl.wandemo.utils.GsonUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

public class SearchActivity extends BaseActivity implements SearchActivityImple {

    private static final String TAG = "SearchActivity";

    private SearchActivityPresenter mPresenter;
    private EditText                mEt;
    private FlexboxLayout           mFlexboxLayout;
    private TextView                mTvEmpty;
    private FlexboxLayout           mFlexboxHistory;
    private HashSet<String>         mHashSet;

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
        mHashSet = new HashSet<>();

        mFlexboxLayout = findViewById(R.id.search_flexbox);
        mEt = findViewById(R.id.search_et);
        mFlexboxHistory = findViewById(R.id.search_flexbox_history);
        mTvEmpty = findViewById(R.id.search_history_empty);
        findViewById(R.id.search_search).setOnClickListener(v -> {
            String data = mEt.getText().toString();
            if (TextUtils.isEmpty(data)) {
                return;
            }
            loadMoreSearch(data);
            Bundle bundle = new Bundle();
            bundle.putString(CONSTANT_JUMP_DATA, data);
            jumpActivity(SearchResActivity.class, bundle);
        });
        findViewById(R.id.search_clear).setOnClickListener(v -> {
            SPUtils.putString(SP_WORD_SEARCH_HISTORY, "");
            mHashSet.clear();
            loadHistory(null);
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
            // TODO: 2019/6/20  搜索热词获取失败
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtils.putString(SP_WORD_SEARCH_HISTORY, "");
        StringBuilder stringBuffer = new StringBuilder();
        for (String data : mHashSet) {
            stringBuffer.append(data).append(CONSTANT_SPEARTOR);
        }
        SPUtils.putString(SP_WORD_SEARCH_HISTORY, stringBuffer.toString());
        LogUtils.e(TAG, "onDestroy: 搜索历史存储成功。。。。" + stringBuffer.toString());
    }

    public void loadMoreSearch(String data) {
        if (mHashSet.contains(data)){
            return;
        }
        // 通过代码向FlexboxLayout添加View
        TextView textView = new TextView(this);
        textView.setText(data);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(30, 30, 30, 30);
        textView.setTextColor(getResources().getColor(R.color.red));
        textView.setOnClickListener(v -> {
            mEt.setText(data);
            mEt.setSelection(data.length());
        });
        mFlexboxHistory.addView(textView);
        mHashSet.add(data);
    }

    @Override
    public void loadHistory(String string) {
        LogUtils.e(TAG, "loadHistory: " + string);
        if (TextUtils.isEmpty(string)) {
            mFlexboxHistory.setVisibility(View.GONE);
            mTvEmpty.setVisibility(View.VISIBLE);
        } else {
            mTvEmpty.setVisibility(View.GONE);
            mFlexboxHistory.setVisibility(View.VISIBLE);

            String[] split = string.split(Pattern.quote(CONSTANT_SPEARTOR));
            mHashSet.addAll(Arrays.asList(split));

            for (String data : mHashSet) {
                // 通过代码向FlexboxLayout添加View
                TextView textView = new TextView(this);
                textView.setText(data);
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(30, 30, 30, 30);
                textView.setTextColor(getResources().getColor(R.color.red));
                textView.setOnClickListener(v -> {
                    mEt.setText(data);
                    mEt.setSelection(data.length());
                });
                mFlexboxHistory.addView(textView);
            }
        }
    }
}
