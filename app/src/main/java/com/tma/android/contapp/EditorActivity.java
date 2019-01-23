package com.tma.android.contapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.tma.android.contapp.fragments.EditorFurnizorFragment;
import com.tma.android.contapp.fragments.EditorNirFragment;
import com.tma.android.contapp.fragments.EditorProdusFragment;
import com.tma.android.contapp.fragments.EditorProdusNir;
import com.tma.android.contapp.fragments.EditorStocFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tma.android.contapp.fragments.EditorNirFragment.PRODUS_NIR_FRAGMENT;
import static com.tma.android.contapp.fragments.FurnizorFragment.FURNIZOR_FRAGMENT;
import static com.tma.android.contapp.fragments.NirFragment.NIR_FRAGMENT;
import static com.tma.android.contapp.fragments.ProdusFragment.PRODUS_FRAGMENT;
import static com.tma.android.contapp.fragments.StocFragment.STOC_FRAGMENT;

public class EditorActivity extends AppCompatActivity {
    public static final String FRAGMENT_TO_OPEN = "fragment_to_open";
    public static final String FRAGMENT_EDITOR_MODE = "fragment_editor_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(FRAGMENT_TO_OPEN) && intent.hasExtra(FRAGMENT_EDITOR_MODE)) {
            String fragmentToOpen = intent.getStringExtra(FRAGMENT_TO_OPEN);
            Boolean fragmentEditorMode = intent.getBooleanExtra(FRAGMENT_EDITOR_MODE, true);

            FragmentManager fragmentManager = getSupportFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putBoolean(FRAGMENT_EDITOR_MODE, fragmentEditorMode);

            /*
            * Add coresponding fragment depending on the intent extras
            */
            switch (fragmentToOpen) {
                case FURNIZOR_FRAGMENT:
                    EditorFurnizorFragment editorFurnizorFragment = new EditorFurnizorFragment();
                    editorFurnizorFragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .add(R.id.editor_container, editorFurnizorFragment)
                            .commit();
                    break;
                case PRODUS_FRAGMENT:
                    EditorProdusFragment editorProdusFragment = new EditorProdusFragment();
                    editorProdusFragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .add(R.id.editor_container, editorProdusFragment)
                            .commit();
                    break;
                case NIR_FRAGMENT:
                    EditorNirFragment editorNirFragment = new EditorNirFragment();
                    editorNirFragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .add(R.id.editor_container, editorNirFragment)
                            .commit();
                    break;
                case STOC_FRAGMENT:
                    EditorStocFragment editorStocFragment = new EditorStocFragment();
                    editorStocFragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .add(R.id.editor_container, editorStocFragment)
                            .commit();
                    break;
                case PRODUS_NIR_FRAGMENT:
                    EditorProdusNir editorProdusNir = new EditorProdusNir();
                    editorProdusNir.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .add(R.id.editor_container, editorProdusNir)
                            .commit();
            }
        }

    }
}
