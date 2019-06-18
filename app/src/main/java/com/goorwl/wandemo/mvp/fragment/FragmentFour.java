package com.goorwl.wandemo.mvp.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.goorwl.wandemo.R;

public class FragmentFour extends Fragment {
    private static FragmentFour sSingleTest;
    private Activity mActivity;
    private View mInflate;

    private FragmentFour() {
    }

    public static FragmentFour getInstance() {
        if (sSingleTest == null) {
            synchronized (FragmentFour.class) {
                if (sSingleTest == null) {
                    sSingleTest = new FragmentFour();
                }
            }
        }
        return sSingleTest;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflate = LayoutInflater.from(mActivity).inflate(R.layout.fragment_four, container, false);
        return mInflate;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }
}
