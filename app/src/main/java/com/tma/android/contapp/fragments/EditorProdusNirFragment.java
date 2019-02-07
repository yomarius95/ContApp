package com.tma.android.contapp.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.TextView;

import com.tma.android.contapp.AppExecutors;
import com.tma.android.contapp.EditorActivity;
import com.tma.android.contapp.R;
import com.tma.android.contapp.data.AppDatabase;
import com.tma.android.contapp.data.Furnizor;
import com.tma.android.contapp.data.Nir;
import com.tma.android.contapp.data.Produs;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tma.android.contapp.EditorActivity.CLICKED_ITEM;
import static com.tma.android.contapp.EditorActivity.FRAGMENT_EDITOR_MODE;
import static com.tma.android.contapp.fragments.EditorNirFragment.INDEX_PRODUS_NIR;
import static com.tma.android.contapp.fragments.EditorNirFragment.RESULT_PRODUS;
import static com.tma.android.contapp.fragments.ProdusFragment.CUI_FURNIZOR;

public class EditorProdusNirFragment extends Fragment {

    private boolean mEditorMode;

    private AppDatabase mDb;

    private Produs mProdus;

    @BindView(R.id.edit_unitate_masura_produs_nir)
    TextView mUnitateMasura;
    @BindView(R.id.edit_produs_nir_pret_intrare)
    EditText mPretIntrare;
    @BindView(R.id.edit_produs_nir_pret_iesire)
    EditText mPretIesire;
    @BindView(R.id.edit_produs_nir_cantitate)
    EditText mStoc;
    @BindView(R.id.categorie_tva_produs_nir)
    TextView mCategorieTVA;

    @BindView(R.id.spinner_nir_produse)
    Spinner mSpinnerProdus;
    private String[] spinnerNameArray;
    private int index = -1;

    private String mCuiFurnizor;
    private int indexProdusNir;

    private boolean mProdusHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProdusHasChanged = true;
            return false;
        }
    };

    public EditorProdusNirFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_editor_produs_nir, container, false);
        ButterKnife.bind(this, rootView);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mEditorMode = bundle.getBoolean(FRAGMENT_EDITOR_MODE);
            if (bundle.containsKey(CUI_FURNIZOR)) {
                mCuiFurnizor = bundle.getString(CUI_FURNIZOR);
            }

            if (bundle.containsKey(INDEX_PRODUS_NIR)) {
                indexProdusNir = bundle.getInt(INDEX_PRODUS_NIR);
            }

            mDb = AppDatabase.getInstance(getContext());

            if (!mEditorMode) {
                getActivity().setTitle(R.string.editor_produs_nir_title_edit_produs);
                mProdus = bundle.getParcelable(CLICKED_ITEM);
                mUnitateMasura.setText(String.valueOf(mProdus.getUnitateMasura()));
                mCategorieTVA.setText(String.valueOf(mProdus.getCategorieTVA()));
                mPretIntrare.setText(String.valueOf(mProdus.getPretIntrare()));
                mPretIntrare.setSelection(mPretIntrare.getText().length());
                mPretIesire.setText(String.valueOf(mProdus.getPretIesire()));
                mPretIesire.setSelection(mPretIesire.getText().length());
                mStoc.setText(String.valueOf(mProdus.getCantitate()));
                mStoc.setEnabled(false);

                spinnerNameArray = new String[] {mProdus.getNume()};
                setupSpinnerEdit();
                mSpinnerProdus.setEnabled(false);
            } else {
                getActivity().setTitle(R.string.editor_produs_nir_title_add_produs);
                AppExecutors.getsInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        final ArrayList<Produs> produs = (ArrayList<Produs>) mDb.produsDao().loadAllProduseByCui(mCuiFurnizor);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                spinnerNameArray = new String[produs.size()];

                                for (int i = 0; i < produs.size(); i++) {
                                    spinnerNameArray[i] = produs.get(i).getNume();
                                }

                                setupSpinner();
                            }
                        });
                    }
                });
            }
        }

        mPretIntrare.setOnTouchListener(mTouchListener);
        mPretIesire.setOnTouchListener(mTouchListener);
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
        inflater.inflate(R.menu.menu_editor_produs_nir, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mEditorMode) {
            MenuItem menuItem = menu.findItem(R.id.delete_produs_nir);
            menuItem.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_produs_nir:
                saveProdusNir();
                getActivity().finish();
                return true;

            case R.id.delete_produs_nir:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveProdusNir() {

        String nume = spinnerNameArray[index];
        Double pretIntrare = Double.valueOf(mPretIntrare.getText().toString().trim());
        Double pretIesire = Double.valueOf(mPretIesire.getText().toString().trim());
        Double stoc = Double.valueOf(mStoc.getText().toString().trim());
        int unitateMasura = Integer.valueOf(mUnitateMasura.getText().toString().trim());
        int categorieTva = Integer.valueOf(mCategorieTVA.getText().toString().trim());

        Produs produs = new Produs(nume, unitateMasura, stoc, pretIntrare, pretIesire, categorieTva, mCuiFurnizor);

        Intent returnIntent = new Intent();

        if (mProdus != null) {
            produs = new Produs(mProdus.getNume(), mProdus.getUnitateMasura(), stoc, pretIntrare, pretIesire, mProdus.getCategorieTVA(), mCuiFurnizor);
            updateStoc(true, produs);
            returnIntent.putExtra(INDEX_PRODUS_NIR, indexProdusNir);
            returnIntent.putExtra(RESULT_PRODUS, produs);
            getActivity().setResult(Activity.RESULT_OK, returnIntent);
        } else {
            updateStoc(true, produs);
            returnIntent.putExtra(RESULT_PRODUS, produs);
            getActivity().setResult(Activity.RESULT_OK, returnIntent);
        }

        getActivity().finish();
    }

    private void deleteProdusNir() {
        updateStoc(false, mProdus);
        Intent returnIntent = new Intent();
        returnIntent.putExtra(INDEX_PRODUS_NIR, indexProdusNir);
        getActivity().setResult(2, returnIntent);
        getActivity().finish();
    }

    private void updateStoc(final boolean choice, final Produs p) {
        final double stoc = p.getCantitate();
        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Produs produs = mDb.produsDao().loadProdusByNume(p.getNume());
                if (choice) {
                    produs.setCantitate(produs.getCantitate() + stoc);
                } else {
                    produs.setCantitate(produs.getCantitate() - stoc);
                }
                mDb.produsDao().updateProdus(produs);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter<String> produsSpinnerAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, spinnerNameArray);

        // Specify dropdown layout style - simple list view with 1 item per line
        produsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mSpinnerProdus.setAdapter(produsSpinnerAdapter);

        mSpinnerProdus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    index = position;
                    AppExecutors.getsInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            final Produs produs = mDb.produsDao().loadProdusByNume(spinnerNameArray[index]);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mUnitateMasura.setText(String.valueOf(produs.getUnitateMasura()));
                                    mCategorieTVA.setText(String.valueOf(produs.getCategorieTVA()));
                                    mPretIntrare.setText(String.valueOf(produs.getPretIntrare()));
                                    mPretIntrare.setSelection(mPretIntrare.getText().length());
                                    mPretIesire.setText(String.valueOf(produs.getPretIesire()));
                                    mPretIesire.setSelection(mPretIesire.getText().length());
                                }
                            });
                        }
                    });
                } else {
                    index = -1;
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                index = -1;
            }
        });
    }

    private void setupSpinnerEdit() {
        ArrayAdapter<String> produsSpinnerAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, spinnerNameArray);

        // Specify dropdown layout style - simple list view with 1 item per line
        produsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mSpinnerProdus.setAdapter(produsSpinnerAdapter);

        index = 0;
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.delete_produs_nir_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProdusNir();
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