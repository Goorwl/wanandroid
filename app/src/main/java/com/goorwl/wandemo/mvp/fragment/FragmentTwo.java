package com.goorwl.wandemo.mvp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.goorwl.utils.LogUtils;
import com.goorwl.wandemo.R;
import com.goorwl.wandemo.adapter.TixiAdapter;
import com.goorwl.wandemo.bean.TixiHomeResBean;
import com.goorwl.wandemo.globl.BaseActivity;
import com.goorwl.wandemo.mvp.activity.TixiResActivity;
import com.goorwl.wandemo.mvp.imple.FragmentTwoImple;
import com.goorwl.wandemo.mvp.presenter.FragmentTwoPresent;
import com.goorwl.wandemo.utils.Config;
import com.goorwl.wandemo.utils.GsonUtils;

import java.util.List;

public class FragmentTwo extends Fragment implements FragmentTwoImple, Config {
    private static final String TAG = "FragmentTwo";

    private static FragmentTwo        sSingleTest;
    private        BaseActivity       mActivity;
    private        View               mInflate;
    private        FragmentTwoPresent mPresent;
    private        RecyclerView       mRvItem;
    private        SwipeRefreshLayout mRefreshLayout;

    private FragmentTwo() {
    }

    public static FragmentTwo getInstance() {
        if (sSingleTest == null) {
            synchronized (FragmentTwo.class) {
                if (sSingleTest == null) {
                    sSingleTest = new FragmentTwo();
                }
            }
        }
        return sSingleTest;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflate = LayoutInflater.from(mActivity).inflate(R.layout.fragment_two, container, false);
        return mInflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresent = new FragmentTwoPresent(mActivity, this);
        initView();
        initData();
    }

    private void initData() {
        mPresent.getData();
        mRefreshLayout.setRefreshing(true);
    }

    private void initView() {
        mRvItem = mInflate.findViewById(R.id.tixi_rv);
        mRefreshLayout = mInflate.findViewById(R.id.tixi_fresh);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) getActivity();
    }

    @Override
    public void loadData(String data) {
        TixiHomeResBean tixiHomeResBean = GsonUtils.getInstance().fromJson(data, TixiHomeResBean.class);
        if (tixiHomeResBean.getErrorCode() == 0) {
            List<TixiHomeResBean.DataBean> data1 = tixiHomeResBean.getData();

            TixiAdapter tixiAdapter = new TixiAdapter(mActivity, data1);
            tixiAdapter.setItemOnClick(url -> {
                Bundle bundle = new Bundle();
                bundle.putInt(CONSTANT_JUMP_DATA, (Integer) url);
                bundle.putString(CONSTANT_JUMP_DATA_STR, data);
                mActivity.jumpActivity(TixiResActivity.class, bundle);
                LogUtils.e(TAG, "onItemClick: " + url);
            });
            mRvItem.setLayoutManager(new LinearLayoutManager(mActivity));
            mRvItem.setAdapter(tixiAdapter);
        }
        mRefreshLayout.setRefreshing(false);
        mRefreshLayout.setEnabled(false);
    }
}
