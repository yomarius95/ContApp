package com.tma.android.contapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tma.android.contapp.EditorActivity;
import com.tma.android.contapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tma.android.contapp.EditorActivity.FRAGMENT_EDITOR_MODE;
import static com.tma.android.contapp.EditorActivity.FRAGMENT_TO_OPEN;

public class FurnizorFragment extends Fragment {
    public static final String FURNIZOR_FRAGMENT = "furnizor_fragment";

    @BindView(R.id.fab_furnizor) FloatingActionButton fabFurnizor;

    public FurnizorFragment() {

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_furnizor, container, false);
        ButterKnife.bind(this, rootView);

        fabFurnizor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditorActivity.class);
                intent.putExtra(FRAGMENT_TO_OPEN, FURNIZOR_FRAGMENT);
                intent.putExtra(FRAGMENT_EDITOR_MODE, true);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
