package com.tma.android.contapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tma.android.contapp.AppExecutors;
import com.tma.android.contapp.EditorActivity;
import com.tma.android.contapp.R;
import com.tma.android.contapp.data.AppDatabase;
import com.tma.android.contapp.data.Produs;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tma.android.contapp.EditorActivity.CLICKED_ITEM;
import static com.tma.android.contapp.EditorActivity.FRAGMENT_EDITOR_MODE;
import static com.tma.android.contapp.fragments.ProdusFragment.CUI_FURNIZOR;

public class EditorStocFragment extends Fragment {

    private boolean mEditorMode;

    private AppDatabase mDb;

    @BindView(R.id.produs_nume_stoc)
    TextView mNumeProdus;
    @BindView(R.id.stoc_edit_produs_stoc)
    EditText mStoc;

    private Produs mProdus;
    private String mCuiFurnizorStoc;

    private boolean mStocHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mStocHasChanged = true;
            return false;
        }
    };

    public EditorStocFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_editor_stoc, container, false);
        ButterKnife.bind(this, rootView);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mEditorMode = bundle.getBoolean(FRAGMENT_EDITOR_MODE);

            if (bundle.containsKey(CUI_FURNIZOR)) {
                mCuiFurnizorStoc = bundle.getString(CUI_FURNIZOR);
            }

            if (!mEditorMode) {
                getActivity().setTitle(R.string.editor_stoc_title_edit_stoc);
                mProdus = bundle.getParcelable(CLICKED_ITEM);
                mCuiFurnizorStoc = mProdus.getCuiFurnizor();
                mNumeProdus.setText(mProdus.getNume());
                mStoc.setText(String.valueOf(mProdus.getCantitate()));
                mStoc.setSelection(mStoc.getText().length());
            }
        }

        mStoc.setOnTouchListener(mTouchListener);

        ((EditorActivity) getActivity()).setOnBackClickListener(new EditorActivity.OnBackClickListener() {
            @Override
            public boolean onBackClick() {
                if (!mStocHasChanged) {
                    return false;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getActivity().finish();
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
            }
        });

        mDb = AppDatabase.getInstance(getContext());

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_editor_stoc, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_stoc:
                saveStoc();
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveStoc() {
        Double stoc = Double.valueOf(mStoc.getText().toString().trim());

        final Produs produs = new Produs(mProdus.getId(), mProdus.getNume(), 1, stoc, mProdus.getPretIntrare(), mProdus.getPretIesire(), 1, mCuiFurnizorStoc);

        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.produsDao().updateProdus(produs);
            }
        });

    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
