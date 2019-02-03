package com.tma.android.contapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.tma.android.contapp.AppExecutors;
import com.tma.android.contapp.R;
import com.tma.android.contapp.data.AppDatabase;
import com.tma.android.contapp.data.Produs;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tma.android.contapp.EditorActivity.CLICKED_ITEM;
import static com.tma.android.contapp.EditorActivity.FRAGMENT_EDITOR_MODE;
import static com.tma.android.contapp.fragments.ProdusFragment.CUI_FURNIZOR;

public class EditorProdusFragment extends Fragment {

    private boolean mEditorMode;

    private AppDatabase mDb;

    @BindView(R.id.edit_produs_nume)
    EditText mNumeProdus;
    @BindView(R.id.spinner_produs_unitate_masura)
    Spinner mUnitateMasura;
    @BindView(R.id.spinner_produs_categorie_tva)
    Spinner mCategorieTVA;
    @BindView(R.id.edit_produs_pret_intrare)
    EditText mPretIntrare;
    @BindView(R.id.edit_produs_pret_iesire)
    EditText mPretIesire;
    @BindView(R.id.edit_produs_stoc)
    EditText mStoc;

    private Produs mProdus;
    private String mCuiFurnizorProdus;

    public EditorProdusFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_editor_produs, container, false);
        ButterKnife.bind(this, rootView);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mEditorMode = bundle.getBoolean(FRAGMENT_EDITOR_MODE);
            if (bundle.containsKey(CUI_FURNIZOR)) {
                mCuiFurnizorProdus = bundle.getString(CUI_FURNIZOR);
            }

            if (!mEditorMode) {
                mProdus = bundle.getParcelable(CLICKED_ITEM);
                mCuiFurnizorProdus = mProdus.getCuiFurnizor();
                mNumeProdus.setText(mProdus.getNume());
                mNumeProdus.setSelection(mNumeProdus.getText().length());
                mUnitateMasura.setSelection(mProdus.getUnitateMasura());
                mCategorieTVA.setSelection(mProdus.getCategorieTVA());
                mPretIntrare.setText(String.valueOf(mProdus.getPretIntrare()));
                mPretIntrare.setSelection(mPretIntrare.getText().length());
                mPretIesire.setText(String.valueOf(mProdus.getPretIesire()));
                mPretIesire.setSelection(mPretIesire.getText().length());
                mStoc.setText(String.valueOf(mProdus.getCantitate()));
                mStoc.setSelection(mStoc.getText().length());
            }
        }

        mDb = AppDatabase.getInstance(getContext());

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_editor_produs, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mEditorMode) {
            MenuItem menuItem = menu.findItem(R.id.delete_produs);
            menuItem.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_produs:
                saveProdus();
                getActivity().finish();
                return true;
            case R.id.delete_produs:
                deleteProdus();
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveProdus() {
        String nume = mNumeProdus.getText().toString().trim();
        Double pretIntrare = Double.valueOf(mPretIntrare.getText().toString().trim());
        Double pretIesire = Double.valueOf(mPretIesire.getText().toString().trim());
        Double stoc = Double.valueOf(mStoc.getText().toString().trim());

        // Get unitateMasura and categorieTVA from spinners

        final Produs produs;

        if (mProdus != null) {
            produs = new Produs(mProdus.getId(), nume, 1, stoc, pretIntrare, pretIesire, 1, mCuiFurnizorProdus);
        } else {
            produs = new Produs(nume, 1, stoc, pretIntrare, pretIesire, 1, mCuiFurnizorProdus);
        }

        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mProdus == null) {
                    mDb.produsDao().insertProdus(produs);
                } else {
                    mDb.produsDao().updateProdus(produs);
                }
            }
        });
    }

    private void deleteProdus() {
        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.produsDao().deleteProdus(mProdus);
            }
        });
    }
}
