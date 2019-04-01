package com.tma.android.contapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tma.android.contapp.fragments.EditorFurnizorFragment;
import com.tma.android.contapp.fragments.EditorNirFragment;
import com.tma.android.contapp.fragments.EditorProdusFragment;
import com.tma.android.contapp.fragments.EditorProdusNirFragment;
import com.tma.android.contapp.fragments.EditorStocFragment;

import butterknife.ButterKnife;

import static com.tma.android.contapp.fragments.EditorNirFragment.INDEX_PRODUS_NIR;
import static com.tma.android.contapp.fragments.EditorNirFragment.PRODUS_NIR_FRAGMENT;
import static com.tma.android.contapp.fragments.FurnizorFragment.FURNIZOR_FRAGMENT;
import static com.tma.android.contapp.fragments.NirFragment.LOCALITATE_FURNIZOR;
import static com.tma.android.contapp.fragments.NirFragment.NIR_FRAGMENT;
import static com.tma.android.contapp.fragments.NirFragment.NUME_FURNIZOR;
import static com.tma.android.contapp.fragments.ProdusFragment.CUI_FURNIZOR;
import static com.tma.android.contapp.fragments.ProdusFragment.PRODUS_FRAGMENT;
import static com.tma.android.contapp.fragments.StocFragment.STOC_FRAGMENT;

public class EditorActivity extends AppCompatActivity {
    public static final String FRAGMENT_TO_OPEN = "fragment_to_open";
    public static final String FRAGMENT_EDITOR_MODE = "fragment_editor_mode";
    public static final String CLICKED_ITEM = "clicked_item";

    public interface OnBackClickListener {
        boolean onBackClick();
    }

    private OnBackClickListener onBackClickListener;

    public void setOnBackClickListener(OnBackClickListener onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
    }

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

            if (intent.hasExtra(CUI_FURNIZOR)) {
                String cuiFurnizorProdus = intent.getStringExtra(CUI_FURNIZOR);
                bundle.putString(CUI_FURNIZOR, cuiFurnizorProdus);
            }

            if (intent.hasExtra(NUME_FURNIZOR)) {
                String numeFurnizorProdus = intent.getStringExtra(NUME_FURNIZOR);
                bundle.putString(NUME_FURNIZOR, numeFurnizorProdus);
            }

            if (intent.hasExtra(LOCALITATE_FURNIZOR)) {
                String localitateFurnizor = intent.getStringExtra(LOCALITATE_FURNIZOR);
                bundle.putString(LOCALITATE_FURNIZOR, localitateFurnizor);
            }

            if (intent.hasExtra(CLICKED_ITEM)) {
                bundle.putParcelable(CLICKED_ITEM, intent.getParcelableExtra(CLICKED_ITEM));
            }

            if (intent.hasExtra(INDEX_PRODUS_NIR)) {
                bundle.putInt(INDEX_PRODUS_NIR, intent.getIntExtra(INDEX_PRODUS_NIR, -1));
            }

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
                    EditorProdusNirFragment editorProdusNirFragment = new EditorProdusNirFragment();
                    editorProdusNirFragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .add(R.id.editor_container, editorProdusNirFragment)
                            .commit();
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (onBackClickListener != null && onBackClickListener.onBackClick()) {
            return;
        }
        super.onBackPressed();
    }
}
