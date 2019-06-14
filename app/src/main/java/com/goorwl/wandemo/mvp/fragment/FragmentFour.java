package com.goorwl.wandemo.mvp.fragment;

import androidx.fragment.app.Fragment;

public class FragmentFour extends Fragment {
    private static FragmentFour sSingleTest;

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
}
