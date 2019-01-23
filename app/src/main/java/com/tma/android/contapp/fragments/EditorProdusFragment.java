package com.tma.android.contapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tma.android.contapp.R;

import static com.tma.android.contapp.EditorActivity.FRAGMENT_EDITOR_MODE;

public class EditorProdusFragment extends Fragment {

    private boolean mEditorMode;

    public EditorProdusFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_editor_produs, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mEditorMode = bundle.getBoolean(FRAGMENT_EDITOR_MODE);
        }

        return rootView;
    }
}
