package com.tma.android.contapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tma.android.contapp.AppExecutors;
import com.tma.android.contapp.EditorActivity;
import com.tma.android.contapp.R;
import com.tma.android.contapp.data.AppDatabase;
import com.tma.android.contapp.data.Produs;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tma.android.contapp.EditorActivity.CLICKED_ITEM;
import static com.tma.android.contapp.EditorActivity.FRAGMENT_EDITOR_MODE;
import static com.tma.android.contapp.data.Produs.*;
import static com.tma.android.contapp.fragments.ProdusFragment.CUI_FURNIZOR;

public class EditorProdusFragment extends Fragment {

    private boolean mEditorMode;

    private AppDatabase mDb;

    @BindView(R.id.edit_produs_nume)
    EditText mNumeProdus;
    @BindView(R.id.spinner_produs_unitate_masura)
    Spinner mUnitateMasuraSpinner;
    @BindView(R.id.spinner_produs_categorie_tva)
    Spinner mCategorieTVASpinner;
    @BindView(R.id.edit_produs_pret_intrare)
    EditText mPretIntrare;
    @BindView(R.id.edit_produs_pret_iesire)
    EditText mPretIesire;
    @BindView(R.id.edit_produs_stoc)
    EditText mStoc;

    private int mUnitateMasura;
    private int mCategorieTVA;

    private Produs mProdus;
    private String mCuiFurnizorProdus;

    private boolean mProdusHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProdusHasChanged = true;
            return false;
        }
    };

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

        mDb = AppDatabase.getInstance(getContext());

        setupSpinner();

        Bundle bundle = getArguments();
        if (bundle != null) {
            mEditorMode = bundle.getBoolean(FRAGMENT_EDITOR_MODE);
            if (bundle.containsKey(CUI_FURNIZOR)) {
                mCuiFurnizorProdus = bundle.getString(CUI_FURNIZOR);
            }

            if (!mEditorMode) {
                getActivity().setTitle(R.string.editor_produs_title_edit_produs);
                mProdus = bundle.getParcelable(CLICKED_ITEM);
                mCuiFurnizorProdus = mProdus.getCuiFurnizor();
                mNumeProdus.setText(mProdus.getNume());
                mNumeProdus.setSelection(mNumeProdus.getText().length());
                mUnitateMasuraSpinner.setSelection(mProdus.getUnitateMasura());
                mCategorieTVASpinner.setSelection(mProdus.getCategorieTVA());
                mPretIntrare.setText(String.valueOf(mProdus.getPretIntrare()));
                mPretIntrare.setSelection(mPretIntrare.getText().length());
                mPretIesire.setText(String.valueOf(mProdus.getPretIesire()));
                mPretIesire.setSelection(mPretIesire.getText().length());
                mStoc.setText(String.valueOf(mProdus.getCantitate()));
                mStoc.setSelection(mStoc.getText().length());
                mStoc.setEnabled(false);
            } else {
                getActivity().setTitle(R.string.editor_produs_title_add_produs);
            }
        }

        mNumeProdus.setOnTouchListener(mTouchListener);
        mUnitateMasuraSpinner.setOnTouchListener(mTouchListener);
        mPretIntrare.setOnTouchListener(mTouchListener);
        mPretIesire.setOnTouchListener(mTouchListener);
        mCategorieTVASpinner.setOnTouchListener(mTouchListener);
        mStoc.setOnTouchListener(mTouchListener);

        ((EditorActivity) getActivity()).setOnBackClickListener(new EditorActivity.OnBackClickListener() {
            @Override
            public boolean onBackClick() {
                if (!mProdusHasChanged) {
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
                return true;
            case R.id.delete_produs:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveProdus() {
        String nume = mNumeProdus.getText().toString().trim();
        String pretIntrareString = mPretIntrare.getText().toString().trim();
        String pretIesireString = mPretIesire.getText().toString().trim();
        String stocString = mStoc.getText().toString().trim();

        if (TextUtils.isEmpty(nume) || TextUtils.isEmpty(pretIntrareString) || TextUtils.isEmpty(pretIesireString) || TextUtils.isEmpty(stocString) || mUnitateMasura == -1 || mCategorieTVA == -1){
            Toast.makeText(getContext(), "Completati toate campurile", Toast.LENGTH_SHORT).show();
        } else {
            Double pretIntrare = Double.valueOf(pretIntrareString);
            Double pretIesire = Double.valueOf(pretIesireString);
            Double stoc = Double.valueOf(stocString);

            final Produs produs;

            if (mProdus != null) {
                produs = new Produs(mProdus.getId(), nume, mUnitateMasura, stoc, pretIntrare, pretIesire, mCategorieTVA, mCuiFurnizorProdus);
            } else {
                produs = new Produs(nume, mUnitateMasura, stoc, pretIntrare, pretIesire, mCategorieTVA, mCuiFurnizorProdus);
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

            getActivity().finish();
        }
    }

    private void deleteProdus() {
        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.produsDao().deleteProdus(mProdus);
            }
        });

        getActivity().finish();
    }

    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter categorieTVASpinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.array_categorie_tva, android.R.layout.simple_spinner_item);

        ArrayAdapter unitateMasuraSpinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.array_unitate_masura, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        categorieTVASpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        unitateMasuraSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mCategorieTVASpinner.setAdapter(categorieTVASpinnerAdapter);
        mUnitateMasuraSpinner.setAdapter(unitateMasuraSpinnerAdapter);

        mUnitateMasuraSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (position > 0) {
                    if (selection.equals(getString(R.string.unitate_masura_buc))) {
                        mUnitateMasura = UNITATE_MASURA_BUC;
                    } else if (selection.equals(getString(R.string.unitate_masura_kg))) {
                        mUnitateMasura = UNITATE_MASURA_KG;
                    } else if (selection.equals(getString(R.string.unitate_masura_metru))) {
                        mUnitateMasura = UNITTE_MASURA_M;
                    }
                } else if (selection.equals(getString(R.string.unitate_masura_default))) {
                    Toast.makeText(getContext(), "Selectati Unitatea de masura", Toast.LENGTH_SHORT).show();
                    mUnitateMasura = -1;
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mUnitateMasura = -1;
            }
        });

        // Set the integer mSelected to the constant values
        mCategorieTVASpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (position > 0) {
                    if (selection.equals(getString(R.string.categorie_tva_0))) {
                        mCategorieTVA = COTA_TVA_0;
                    } else if (selection.equals(getString(R.string.categorie_tva_5))) {
                        mCategorieTVA = COTA_TVA_5;
                    } else if (selection.equals(getString(R.string.categorie_tva_9))) {
                        mCategorieTVA = COTA_TVA_9;
                    } else if (selection.equals(getString(R.string.categorie_tva_19))) {
                        mCategorieTVA = COTA_TVA_19;
                    } else if (selection.equals(getString(R.string.categorie_tva_20))) {
                        mCategorieTVA = COTA_TVA_20;
                    } else if (selection.equals(getString(R.string.categorie_tva_24))) {
                        mCategorieTVA = COTA_TVA_24;
                    }
                } else if (selection.equals(getString(R.string.categorie_tva_default))) {
                    Toast.makeText(getContext(), "Selectati Categorie TVA", Toast.LENGTH_SHORT).show();
                    mCategorieTVA = -1;
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mCategorieTVA = -1;
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.delete_produs_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProdus();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
