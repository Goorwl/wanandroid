package com.goorwl.wandemo.mvp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.tabs.TabLayout;
import com.goorwl.wandemo.R;
import com.goorwl.wandemo.adapter.HomeArticleAdapter;
import com.goorwl.wandemo.bean.HomeArticleResBean;
import com.goorwl.wandemo.bean.WechatTabResBean;
import com.goorwl.wandemo.globl.BaseActivity;
import com.goorwl.wandemo.mvp.imple.FragmentOneImple;
import com.goorwl.wandemo.mvp.presenter.FragmentOnePresent;
import com.goorwl.wandemo.utils.Config;
import com.goorwl.wandemo.utils.GsonUtils;

import java.util.List;

public class FragmentOne extends Fragment implements Config, FragmentOneImple {
    private static FragmentOne sSingleTest;

    private BaseActivity       mActivity;
    private View               mInflate;
    private FragmentOnePresent mPresent;
    private TabLayout          mTabLayout;
    private EditText           mEtSearch;
    private TextView           mTvSearch;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView       mRvItem;
    private int                mCurPage  = 0;
    private int                mPosition = 0;

    private List<WechatTabResBean.DataBean> mBeans;
    private int                             mPageCount;
    private HomeArticleAdapter              mArticleAdapter;

    private FragmentOne() {
    }

    public static FragmentOne getInstance() {
        if (sSingleTest == null) {
            synchronized (FragmentOne.class) {
                if (sSingleTest == null) {
                    sSingleTest = new FragmentOne();
                }
            }
        }
        return sSingleTest;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflate = LayoutInflater.from(mActivity).inflate(R.layout.fragment_one, container, false);
        return mInflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mTabLayout = mInflate.findViewById(R.id.wechat_tablayout);
        mEtSearch = mInflate.findViewById(R.id.wechat_et_search);
        mTvSearch = mInflate.findViewById(R.id.wechat_tv_search);
        mRefreshLayout = mInflate.findViewById(R.id.wechat_refresh);
        mRvItem = mInflate.findViewById(R.id.wechat_rv_item);

        mRvItem.setLayoutManager(new LinearLayoutManager(mActivity));
        mRefreshLayout.setRefreshing(true);

        mPresent = new FragmentOnePresent(mActivity, this);
        mPresent.getTab();

        mRefreshLayout.setOnRefreshListener(() -> {
            mCurPage = 0;
            mArticleAdapter = null;
            mPresent.getItem(mBeans.get(mPosition).getId(), mCurPage);
        });

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mRefreshLayout.setEnabled(true);
                mRefreshLayout.setRefreshing(true);
                mPosition = tab.getPosition();
                mCurPage = 0;
                mArticleAdapter = null;
                mPresent.getItem(mBeans.get(mPosition).getId(), mCurPage);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) getActivity();
    }

    @Override
    public void loadTab(String tabData) {
        WechatTabResBean tabResBean = GsonUtils.getInstance().fromJson(tabData, WechatTabResBean.class);
        if (tabResBean.getErrorCode() == 0) {
            mBeans = tabResBean.getData();
            for (int i = 0; i < mBeans.size(); i++) {
                mTabLayout.addTab(mTabLayout.newTab().setText(mBeans.get(i).getName()));
            }
            mPresent.getItem(mBeans.get(0).getId(), mCurPage);
        } else {
            Toast.makeText(mActivity, tabResBean.getErrorMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void loadItem(String itemData) {
        HomeArticleResBean articleResBean = GsonUtils.getInstance().fromJson(itemData, HomeArticleResBean.class);
        if (articleResBean.getErrorCode() == 0) {
            mCurPage = articleResBean.getData().getCurPage() + 1;
            mPageCount = articleResBean.getData().getPageCount();
            List<HomeArticleResBean.DataBean.DatasBean> datas = articleResBean.getData().getDatas();
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
                            mRefreshLayout.setRefreshing(true);
                            mPresent.getItem(mBeans.get(mPosition).getId(), mCurPage);
                        }
                    }
                });
            } else {
                mRefreshLayout.setEnabled(false);
            }
            if (mArticleAdapter == null) {
                mArticleAdapter = new HomeArticleAdapter(datas, mActivity);
                mRvItem.setAdapter(mArticleAdapter);
            } else {
                mArticleAdapter.loadMoreData(datas);
            }
            mArticleAdapter.setRvItemClick(url -> mActivity.startBroswerActivity(MODE_SONIC, (String) url));
        } else {
            Toast.makeText(mActivity, articleResBean.getErrorMsg(), Toast.LENGTH_SHORT).show();
        }
        mRefreshLayout.setRefreshing(false);
    }
}
