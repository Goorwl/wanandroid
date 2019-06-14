package com.goorwl.wandemo.mvp.fragment;

import androidx.fragment.app.Fragment;

public class FragmentOne extends Fragment {
    private static FragmentOne sSingleTest;

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

}
