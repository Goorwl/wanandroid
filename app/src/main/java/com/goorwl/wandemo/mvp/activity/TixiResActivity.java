package com.goorwl.wandemo.mvp.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.tabs.TabLayout;
import com.goorwl.wandemo.R;
import com.goorwl.wandemo.adapter.HomeArticleAdapter;
import com.goorwl.wandemo.bean.HomeArticleResBean;
import com.goorwl.wandemo.bean.TixiHomeResBean;
import com.goorwl.wandemo.globl.BaseActivity;
import com.goorwl.wandemo.mvp.imple.TixiResActivityImple;
import com.goorwl.wandemo.mvp.presenter.TixiResActivityPresenter;
import com.goorwl.wandemo.utils.Config;
import com.goorwl.wandemo.utils.GsonUtils;

import java.util.List;

public class TixiResActivity extends BaseActivity implements TixiResActivityImple, Config {
    private static final String TAG = "TixiResActivity";

    private TixiHomeResBean.DataBean mDataBean;

    private Activity                 mActivity;
    private TixiResActivityPresenter mPresenter;
    private RecyclerView             mRvItem;
    private HomeArticleAdapter       mHomeArticleAdapter;
    private int                      mCurPage   = 0;
    private int                      mPageCount = 1;
    private int                      mPosition;
    private SwipeRefreshLayout       mRefreshLayout;
    private boolean                  isLoadmore = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        setContentView(R.layout.activity_tixi_res);
        mActivity = this;
        initView();
    }

    private void initView() {
        // GET INSTANCE OF PRESENTER
        mPresenter = new TixiResActivityPresenter(this, this);

        Bundle extras = getIntent().getExtras();
        int    anInt  = extras.getInt(CONSTANT_JUMP_DATA);
        String string = extras.getString(CONSTANT_JUMP_DATA_STR);

        TixiHomeResBean tixiHomeResBean = GsonUtils.getInstance().fromJson(string, TixiHomeResBean.class);
        mDataBean = tixiHomeResBean.getData().get(anInt);

        Toolbar   toolbar   = findViewById(R.id.tixires_toolbar);
        TabLayout tabLayout = findViewById(R.id.tixires_tablayout);
        mRefreshLayout = findViewById(R.id.tixires_fresh);
        mRvItem = findViewById(R.id.tixires_rv);

        mRvItem.setLayoutManager(new LinearLayoutManager(mActivity));

        toolbar.setLogo(R.drawable.back_black);
        toolbar.setTitle(mDataBean.getName());

        getToolbarLogoIcon(toolbar).setOnClickListener(v -> finish());
        List<TixiHomeResBean.DataBean.ChildrenBean> children = mDataBean.getChildren();
        for (int i = 0; i < children.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(children.get(i).getName()));
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPosition = tab.getPosition();
                mCurPage = 0;
                mPresenter.getData(children.get(mPosition).getId(), mCurPage);
                isLoadmore = false;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mPresenter.getData(children.get(0).getId(), mCurPage);
        mRefreshLayout.setRefreshing(true);

        mRvItem.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 获取最后一个完全显示的itemPosition
                LinearLayoutManager manager          = (LinearLayoutManager) recyclerView.getLayoutManager();
                int                 lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
                int                 itemCount        = manager.getItemCount();
                if (lastItemPosition == itemCount - 1 &&
                        newState == RecyclerView.SCROLL_STATE_IDLE              //当列表滚动停止时
                        && !mRvItem.canScrollHorizontally(1)) {        // 可以向下滚动
                    if (mCurPage >= mPageCount) {
                        return;
                    }
                    if (mRefreshLayout.isRefreshing()) {
                        return;
                    }
                    mRefreshLayout.setRefreshing(true);
                    mPresenter.getData(children.get(mPosition).getId(), mCurPage);
                    isLoadmore = true;
                }
            }
        });

        mRefreshLayout.setOnRefreshListener(() -> mRefreshLayout.setRefreshing(false));
    }

    @Override
    public void loadData(String data) {
        HomeArticleResBean homeArticleResBean = GsonUtils.getInstance().fromJson(data, HomeArticleResBean.class);
        if (homeArticleResBean.getErrorCode() == 0) {
            mCurPage = homeArticleResBean.getData().getCurPage();
            mPageCount = homeArticleResBean.getData().getPageCount();
            if (mHomeArticleAdapter == null) {
                mHomeArticleAdapter = new HomeArticleAdapter(homeArticleResBean.getData().getDatas(), mActivity);
                mRvItem.setAdapter(mHomeArticleAdapter);
            } else {
                if (isLoadmore) {
                    mHomeArticleAdapter.loadMoreData(homeArticleResBean.getData().getDatas());
                } else
                    mHomeArticleAdapter.setDataBean(homeArticleResBean.getData().getDatas());
            }
            mHomeArticleAdapter.setRvItemClick(url -> startBroswerActivity(MODE_SONIC, (String) url));
        }

        mRefreshLayout.setRefreshing(false);
    }
}
