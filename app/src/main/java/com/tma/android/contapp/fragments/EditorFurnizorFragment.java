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

import com.tma.android.contapp.AppExecutors;
import com.tma.android.contapp.R;
import com.tma.android.contapp.data.AppDatabase;
import com.tma.android.contapp.data.Furnizor;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tma.android.contapp.EditorActivity.CLICKED_ITEM;
import static com.tma.android.contapp.EditorActivity.FRAGMENT_EDITOR_MODE;

public class EditorFurnizorFragment extends Fragment {

    private boolean mEditorMode;

    private AppDatabase mDb;

    @BindView(R.id.edit_furnizor_nume)
    EditText mNumeFurnizor;
    @BindView(R.id.edit_furnizor_cui)
    EditText mCuiFurnizor;
    @BindView(R.id.edit_furnizor_localitate)
    EditText mLocalitateFurnizor;

    private Furnizor mFurnizor;


    public EditorFurnizorFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_editor_furnizor, container, false);
        ButterKnife.bind(this, rootView);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mEditorMode = bundle.getBoolean(FRAGMENT_EDITOR_MODE);

            if (!mEditorMode) {
                mFurnizor = bundle.getParcelable(CLICKED_ITEM);
                mNumeFurnizor.setText(mFurnizor.getNume());
                mNumeFurnizor.setSelection(mNumeFurnizor.getText().length());
                mCuiFurnizor.setText(mFurnizor.getCui());
                mCuiFurnizor.setEnabled(false);
                mLocalitateFurnizor.setText(mFurnizor.getLocalitate());
                mLocalitateFurnizor.setSelection(mLocalitateFurnizor.getText().length());
            }
        }

        mDb = AppDatabase.getInstance(getContext());

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_editor_furnizor, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mEditorMode) {
            MenuItem menuItem = menu.findItem(R.id.delete_furnizor);
            menuItem.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_furnizor:
                saveFurnizor();
                getActivity().finish();
                return true;
            case R.id.delete_furnizor:
                deleteFurnizor();
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveFurnizor() {
        String nume = mNumeFurnizor.getText().toString().trim();
        String localitate = mLocalitateFurnizor.getText().toString().trim();
        String cui = mCuiFurnizor.getText().toString().trim();

        final Furnizor furnizor = new Furnizor(cui, nume, localitate);

        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mEditorMode) {
                    mDb.furnizorDao().insertFurnizor(furnizor);
                } else {
                    mDb.furnizorDao().updateFurnizor(furnizor);
                }
            }
        });
    }

    private void deleteFurnizor() {
        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.furnizorDao().deleteFurnizor(mFurnizor);
            }
        });
    }
}
