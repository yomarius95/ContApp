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

public class ProdusFragment extends Fragment {
    public static final String PRODUS_FRAGMENT = "produs_fragment";

    @BindView(R.id.fab_produs)
    FloatingActionButton fabProdus;

    public ProdusFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_produs, container, false);
        ButterKnife.bind(this, rootView);

        fabProdus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditorActivity.class);
                intent.putExtra(FRAGMENT_TO_OPEN, PRODUS_FRAGMENT);
                intent.putExtra(FRAGMENT_EDITOR_MODE, true);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
