package com.goorwl.wandemo.mvp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.goorwl.wandemo.bean.XiangmuTabResBean;
import com.goorwl.wandemo.globl.BaseActivity;
import com.goorwl.wandemo.globl.WebviewActivity;
import com.goorwl.wandemo.mvp.imple.FragmentThreeImple;
import com.goorwl.wandemo.mvp.presenter.FragmentThreePresenter;
import com.goorwl.wandemo.utils.Config;
import com.goorwl.wandemo.utils.GsonUtils;

import java.util.List;

public class FragmentThree extends Fragment implements Config, FragmentThreeImple {
    private static FragmentThree                    sSingleTest;
    private        BaseActivity                     mActivity;
    private        View                             mInflate;
    private        TabLayout                        mTabLayout;
    private        SwipeRefreshLayout               mRefreshLayout;
    private        RecyclerView                     mRvItem;
    private        FragmentThreePresenter           mPresenter;
    private        int                              mPosition;
    private        int                              mCurPage;
    private        HomeArticleAdapter               mAdapter;
    private        List<XiangmuTabResBean.DataBean> mTabData;
    private        int                              mPageCount;

    private FragmentThree() {
    }

    public static FragmentThree getInstance() {
        if (sSingleTest == null) {
            synchronized (FragmentThree.class) {
                if (sSingleTest == null) {
                    sSingleTest = new FragmentThree();
                }
            }
        }
        return sSingleTest;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflate = LayoutInflater.from(mActivity).inflate(R.layout.fragment_three, container, false);
        return mInflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mTabLayout = mInflate.findViewById(R.id.xiangmu_tab);
        mRefreshLayout = mInflate.findViewById(R.id.xiangmu_refresh);
        mRvItem = mInflate.findViewById(R.id.xiangmu_rv);

        mRvItem.setLayoutManager(new LinearLayoutManager(mActivity));
        mPresenter = new FragmentThreePresenter(mActivity, this);
        mPresenter.getTab();

        mRefreshLayout.setOnRefreshListener(() -> {
            mAdapter = null;
            mCurPage = 1;
            mPresenter.getItem(mTabData.get(mPosition).getId(), mCurPage);
        });

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
                    mPresenter.getItem(mTabData.get(mPosition).getId(), mCurPage);
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) getActivity();
    }

    @Override
    public void loadTab(String data) {
        XiangmuTabResBean resBean = GsonUtils.getInstance().fromJson(data, XiangmuTabResBean.class);
        if (resBean.getErrorCode() == 0) {
            mTabData = resBean.getData();
            for (int i = 0; i < mTabData.size(); i++) {
                mTabLayout.addTab(mTabLayout.newTab().setText(mTabData.get(i).getName()));
            }

            mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    mRefreshLayout.setRefreshing(true);
                    mPosition = tab.getPosition();
                    mAdapter = null;
                    mCurPage = 1;
                    mPresenter.getItem(mTabData.get(mPosition).getId(), mCurPage);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        } else {
            Toast.makeText(mActivity, resBean.getErrorMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void loadItem(String itemData) {
        HomeArticleResBean articleResBean = GsonUtils.getInstance().fromJson(itemData, HomeArticleResBean.class);
        if (articleResBean.getErrorCode() == 0) {
            mCurPage = articleResBean.getData().getCurPage() + 1;
            mPageCount = articleResBean.getData().getPageCount();
            if (mAdapter == null) {
                mAdapter = new HomeArticleAdapter(articleResBean.getData().getDatas(), mActivity);
                mRvItem.setAdapter(mAdapter);
            } else {
                mAdapter.loadMoreData(articleResBean.getData().getDatas());
            }

            mAdapter.setRvItemClick(url -> {
                mActivity.startBroswerActivity(MODE_SONIC, (String) url);
            });
        } else {
            Toast.makeText(mActivity, articleResBean.getErrorMsg(), Toast.LENGTH_SHORT).show();
        }
        mRefreshLayout.setRefreshing(false);
    }
}
