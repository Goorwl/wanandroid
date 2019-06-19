package com.goorwl.wandemo.mvp.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.goorwl.wandemo.R;
import com.goorwl.wandemo.adapter.HomeArticleAdapter;
import com.goorwl.wandemo.bean.HomeArticleResBean;
import com.goorwl.wandemo.bean.HomeBannerResBean;
import com.goorwl.wandemo.globl.BaseActivity;
import com.goorwl.wandemo.mvp.imple.FragmentHomeImple;
import com.goorwl.wandemo.mvp.presenter.FragmentHomePresent;
import com.goorwl.wandemo.utils.Config;
import com.goorwl.wandemo.utils.GsonUtils;

import java.util.ArrayList;

public class FragmentHome extends Fragment implements FragmentHomeImple, OnItemClickListener, Config {

    private static final String TAG = "FragmentHome";

    private BaseActivity mActivity;

    private static FragmentHome        sSingleTest;
    private        View                mInflate;
    private        FragmentHomePresent sFragmentHomePresent;
    private        ConvenientBanner    mConvenientBanner;
    private        SwipeRefreshLayout  mRefreshLayout;
    private        RecyclerView        mRvItem;

    private HomeBannerResBean  mHomeBannerResBean;
    private int                mCurPage = 0;
    private HomeArticleAdapter mHomeArticleAdapter;
    private NestedScrollView   mNestedScrollView;

    private FragmentHome() {
    }

    public static FragmentHome getInstance() {
        if (sSingleTest == null) {
            synchronized (FragmentHome.class) {
                if (sSingleTest == null) {
                    sSingleTest = new FragmentHome();
                }
            }
        }
        return sSingleTest;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflate = LayoutInflater.from(mActivity).inflate(R.layout.fragment_home, container, false);
        sFragmentHomePresent = new FragmentHomePresent(sSingleTest, sSingleTest);
        return mInflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sFragmentHomePresent.getBannerInfo();
        sFragmentHomePresent.getArticle(mCurPage);
        initView();
    }

    @Override
    public void onItemClick(int position) {
        if (mHomeBannerResBean.getErrorCode() == 0) {
            String url = mHomeBannerResBean.getData().get(position).getUrl();
            if (!TextUtils.isEmpty(url)) {
                mActivity.startBroswerActivity(MODE_SONIC, url);
            }
        }
    }

    // banner加载网络图片
    public class LocalImageHolderView extends Holder<String> {
        private ImageView imageView;

        public LocalImageHolderView(View itemView) {
            super(itemView);
        }

        @Override
        protected void initView(View itemView) {
            imageView = itemView.findViewById(R.id.banner_iv);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        @Override
        public void updateUI(String data) {
            Glide.with(mActivity).load(data).into(imageView);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        mConvenientBanner = mInflate.findViewById(R.id.convenientBanner);
        mRefreshLayout = mInflate.findViewById(R.id.home_swipe);
        mRvItem = mInflate.findViewById(R.id.home_rv);
        mNestedScrollView = mInflate.findViewById(R.id.home_content);
        mRvItem.setLayoutManager(new LinearLayoutManager(mActivity));
        mRvItem.setNestedScrollingEnabled(true);

        mRefreshLayout.setRefreshing(true);

        mRefreshLayout.setOnRefreshListener(() -> {
            mHomeArticleAdapter = null;
            mCurPage = 0;
            sFragmentHomePresent.getArticle(mCurPage);
        });

        mNestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                mRefreshLayout.setRefreshing(true);
                sFragmentHomePresent.getArticle(mCurPage);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) getActivity();
    }

    @Override
    public void loadBanner(String data) {
        mHomeBannerResBean = GsonUtils.getInstance().fromJson(data, HomeBannerResBean.class);
        ArrayList<String> bannerList = new ArrayList<>();
        for (int i = 0; i < mHomeBannerResBean.getData().size(); i++) {
            bannerList.add(mHomeBannerResBean.getData().get(i).getImagePath());
        }
        mConvenientBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Holder createHolder(View itemView) {
                return new LocalImageHolderView(itemView);
            }

            @Override
            public int getLayoutId() {
                return R.layout.layout_banner;
            }
        }, bannerList).setOnItemClickListener(this)
                .setPageIndicator(new int[]{R.drawable.shape_white_indicator, R.drawable.shape_red_indicator});

        mConvenientBanner.startTurning(3000);
    }

    @Override
    public void loadAitcle(String data) {
        HomeArticleResBean homeArticleResBean = GsonUtils.getInstance().fromJson(data, HomeArticleResBean.class);
        if (homeArticleResBean.getErrorCode() == 0) {
            HomeArticleResBean.DataBean data1 = homeArticleResBean.getData();
            mCurPage = data1.getCurPage();
            if (mHomeArticleAdapter == null) {
                mHomeArticleAdapter = new HomeArticleAdapter(data1.getDatas(), mActivity);
                mRvItem.setAdapter(mHomeArticleAdapter);
            } else {
                mHomeArticleAdapter.loadMoreData(data1.getDatas());
                mHomeArticleAdapter.notifyDataSetChanged();
            }
            mHomeArticleAdapter.setRvItemClick(url -> {
                mActivity.startBroswerActivity(MODE_SONIC, url);
            });
        } else {
            Toast.makeText(mActivity, "获取首页文章失败。。。", Toast.LENGTH_SHORT).show();
        }
        mRefreshLayout.setRefreshing(false);
    }
}
