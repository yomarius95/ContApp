package com.tma.android.contapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tma.android.contapp.R;

import butterknife.BindView;

public class StocFragment extends Fragment {
    public static final String STOC_FRAGMENT = "stoc_fragment";

    public StocFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stoc, container, false);

        return rootView;
    }
}
