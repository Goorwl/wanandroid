package com.goorwl.wandemo.mvp.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.goorwl.utils.LogUtils;
import com.goorwl.wandemo.R;
import com.goorwl.wandemo.adapter.HomeArticleAdapter;
import com.goorwl.wandemo.bean.HomeArticleResBean;
import com.goorwl.wandemo.globl.BaseActivity;
import com.goorwl.wandemo.mvp.imple.SearchResActivityImple;
import com.goorwl.wandemo.mvp.presenter.SearchResActivityPresenter;
import com.goorwl.wandemo.utils.GsonUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchResActivity extends BaseActivity implements SearchResActivityImple {

    private SearchResActivityPresenter mPresenter;

    private static final String TAG = "SearchResActivity";

    private int                mCurPage = 0;
    private String             mKey;
    private RecyclerView       mRvItem;
    private BaseActivity       mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int                mPageCount;
    private HomeArticleAdapter mArticleAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(R.layout.activity_search_res);
        mContext = this;
        initView();
        initData();
    }

    private void initData() {
        LogUtils.e(TAG, "initView: " + mKey);
        mPresenter.getSearch(mKey, mCurPage);
    }

    private void initView() {
        // GET INSTANCE OF PRESENTER
        mPresenter = new SearchResActivityPresenter(this, this);
        Bundle extras = getIntent().getExtras();
        mKey = extras.getString(CONSTANT_JUMP_DATA);

        Toolbar toolbar = findViewById(R.id.search_res_toolbar);

        toolbar.setTitle(String.format("\" %s \" 的搜索结果", mKey));
        toolbar.setLogo(R.drawable.back_black);
        toolbar.setBackgroundColor(getResources().getColor(R.color.orange));
        getToolbarLogoIcon(toolbar).setOnClickListener(v -> finish());
        mRvItem = findViewById(R.id.search_res_rv);
        mSwipeRefreshLayout = findViewById(R.id.search_res_fresh);
        mSwipeRefreshLayout.setRefreshing(true);
        mRvItem.setLayoutManager(new LinearLayoutManager(mContext));
        mRvItem.setAdapter(mArticleAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mCurPage = 0;
            mPresenter.getSearch(mKey, mCurPage);
        });
    }

    @Override
    public void loadSearch(String data) {
        mSwipeRefreshLayout.setRefreshing(false);
        HomeArticleResBean searchResBean = GsonUtils.getInstance().fromJson(data, HomeArticleResBean.class);
        if (searchResBean.getErrorCode() == 0) {
            mCurPage = searchResBean.getData().getCurPage();
            mPageCount = searchResBean.getData().getPageCount();
            List<HomeArticleResBean.DataBean.DatasBean> datas = searchResBean.getData().getDatas();
            if (mCurPage != mPageCount) {
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
                            mSwipeRefreshLayout.setRefreshing(true);
                            mPresenter.getSearch(mKey, mCurPage);
                        }
                    }
                });
            } else {
                mSwipeRefreshLayout.setEnabled(false);
            }
            if (mArticleAdapter == null) {
                mArticleAdapter = new HomeArticleAdapter(datas, mContext);
                mRvItem.setAdapter(mArticleAdapter);
            } else {
                mArticleAdapter.loadMoreData(datas);
                mArticleAdapter.notifyDataSetChanged();
            }
            mArticleAdapter.setRvItemClick(url -> mContext.startBroswerActivity(MODE_SONIC, url));
        } else {
            Toast.makeText(this, searchResBean.getErrorMsg(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public View getToolbarLogoIcon(Toolbar toolbar) {
        //check if contentDescription previously was set
        boolean hadContentDescription = android.text.TextUtils.isEmpty(toolbar.getLogoDescription());
        String  contentDescription    = String.valueOf(!hadContentDescription ? toolbar.getLogoDescription() : "logoContentDescription");
        toolbar.setLogoDescription(contentDescription);
        ArrayList<View> potentialViews = new ArrayList<>();
        //find the view based on it's content description, set programatically or with android:contentDescription
        toolbar.findViewsWithText(potentialViews, contentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        //Nav icon is always instantiated at this point because calling setLogoDescription ensures its existence
        View logoIcon = null;
        if (potentialViews.size() > 0) {
            logoIcon = potentialViews.get(0);
        }
        //Clear content description if not previously present
        if (hadContentDescription)
            toolbar.setLogoDescription(null);
        return logoIcon;
    }
}
