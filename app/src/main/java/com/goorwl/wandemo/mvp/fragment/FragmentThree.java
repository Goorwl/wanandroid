package com.goorwl.wandemo.mvp.fragment;

import androidx.fragment.app.Fragment;

public class FragmentThree extends Fragment {
    private static FragmentThree sSingleTest;

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
}
