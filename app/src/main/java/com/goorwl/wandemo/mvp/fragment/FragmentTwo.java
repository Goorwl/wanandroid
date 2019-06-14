package com.goorwl.wandemo.mvp.fragment;

import androidx.fragment.app.Fragment;

public class FragmentTwo extends Fragment {
    private static FragmentTwo sSingleTest;

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
}
