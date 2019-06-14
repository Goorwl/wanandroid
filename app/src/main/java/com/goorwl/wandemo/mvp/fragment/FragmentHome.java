package com.goorwl.wandemo.mvp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentHome extends Fragment {

    private static FragmentHome sSingleTest;

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
        return super.onCreateView(inflater, container, savedInstanceState);
    }


}
