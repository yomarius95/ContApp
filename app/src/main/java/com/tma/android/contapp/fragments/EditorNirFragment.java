package com.tma.android.contapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tma.android.contapp.AppExecutors;
import com.tma.android.contapp.EditorActivity;
import com.tma.android.contapp.R;
import com.tma.android.contapp.adapters.NirAdapter;
import com.tma.android.contapp.adapters.ProdusNirAdapter;
import com.tma.android.contapp.data.AppDatabase;
import com.tma.android.contapp.data.DateConverter;
import com.tma.android.contapp.data.Furnizor;
import com.tma.android.contapp.data.Nir;
import com.tma.android.contapp.data.Produs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.tma.android.contapp.EditorActivity.CLICKED_ITEM;
import static com.tma.android.contapp.EditorActivity.FRAGMENT_EDITOR_MODE;
import static com.tma.android.contapp.EditorActivity.FRAGMENT_TO_OPEN;
import static com.tma.android.contapp.fragments.NirFragment.NUME_FURNIZOR;
import static com.tma.android.contapp.fragments.ProdusFragment.CUI_FURNIZOR;

public class EditorNirFragment extends Fragment implements ProdusNirAdapter.ProdusItemClickListener {
    public static final String PRODUS_NIR_FRAGMENT = "produs_nir_fragment";
    public static final String INDEX_PRODUS_NIR = "index_produs_nir";
    public static final String RESULT_PRODUS = "result_produs";
    public static final int ADD_PRODUS_REQUEST = 1;
    public static final int EDIT_PRODUS_REQUEST = 2;

    private boolean mEditorMode;

    private AppDatabase mDb;

    @BindView(R.id.add_produs_nir)
    FloatingActionButton fabProdusNir;

    @BindView(R.id.edit_nir_nume_furnizor)
    TextView mNumeFurnizor;
    @BindView(R.id.edit_nir_numar)
    EditText mNumarNir;
    @BindView(R.id.edit_nir_data)
    EditText mDataNir;
    @BindView(R.id.edit_nir_serie_act)
    EditText mSerieActNir;
    @BindView(R.id.edit_nir_numar_act)
    EditText mNumarActNir;
    @BindView(R.id.edit_nir_data_act)
    EditText mDataActNir;


    private ProdusNirAdapter mAdapter;
    @BindView(R.id.rv_produse_nir)
    RecyclerView mListaProduseNir;

    private Nir mNir;
    private String mCuiFurnizorNir;
    private String mNumeFurnizorNir;

    private ArrayList<Produs> mListaProduse = new ArrayList<>();;

    private boolean mNirHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mNirHasChanged = true;
            return false;
        }
    };

    public EditorNirFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_editor_nir, container, false);
        ButterKnife.bind(this, rootView);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mEditorMode = bundle.getBoolean(FRAGMENT_EDITOR_MODE);
            if (bundle.containsKey(CUI_FURNIZOR) && bundle.containsKey(NUME_FURNIZOR)) {
                mCuiFurnizorNir = bundle.getString(CUI_FURNIZOR);
                mNumeFurnizorNir = bundle.getString(NUME_FURNIZOR);
                mNumeFurnizor.setText(mNumeFurnizorNir);
            }

            if (!mEditorMode) {
                getActivity().setTitle(R.string.editor_nir_title_edit_nir);
                mNir = bundle.getParcelable(CLICKED_ITEM);
                mCuiFurnizorNir = mNir.getCuiFurnizor();
                mNumeFurnizor.setText(mNir.getNumeFurnizor());
                mNumeFurnizorNir = mNir.getNumeFurnizor();
                mNumarNir.setText(String.valueOf(mNir.getNumar()));
                mNumarNir.setSelection(mNumarNir.getText().length());
                mDataNir.setText(mNir.getData());
                mDataNir.setSelection(mDataNir.getText().length());
                mSerieActNir.setText(mNir.getSerieAct());
                mSerieActNir.setSelection(mSerieActNir.getText().length());
                mNumarActNir.setText(String.valueOf(mNir.getNumarAct()));
                mNumarActNir.setSelection(mNumarActNir.getText().length());
                mDataActNir.setText(mNir.getDataAct());
                mDataActNir.setSelection(mDataActNir.getText().length());
                mListaProduse = mNir.getListaProduse();
            } else {
                getActivity().setTitle(R.string.editor_nir_title_add_nir);
            }
        }

        mNumarNir.setOnTouchListener(mTouchListener);
        mDataNir.setOnTouchListener(mTouchListener);
        mSerieActNir.setOnTouchListener(mTouchListener);
        mNumarActNir.setOnTouchListener(mTouchListener);
        mDataActNir.setOnTouchListener(mTouchListener);

        ((EditorActivity) getActivity()).setOnBackClickListener(new EditorActivity.OnBackClickListener() {
            @Override
            public boolean onBackClick() {
                if (!mNirHasChanged) {
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

        fabProdusNir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditorActivity.class);
                intent.putExtra(FRAGMENT_TO_OPEN, PRODUS_NIR_FRAGMENT);
                intent.putExtra(FRAGMENT_EDITOR_MODE, true);
                intent.putExtra(CUI_FURNIZOR, mCuiFurnizorNir);
                startActivityForResult(intent, ADD_PRODUS_REQUEST);
            }
        });

        mDb = AppDatabase.getInstance(getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        mListaProduseNir.setLayoutManager(layoutManager);
        mListaProduseNir.setHasFixedSize(true);

        mAdapter = new ProdusNirAdapter(this);

        mListaProduseNir.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_editor_nir, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mEditorMode) {
            MenuItem menuItem = menu.findItem(R.id.delete_nir);
            menuItem.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_nir:
                saveNir();
                getActivity().finish();
                return true;

            case R.id.delete_nir:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNir() {

        int numar = Integer.parseInt(mNumarNir.getText().toString().trim());
        String data = mDataNir.getText().toString().trim();
        String dataAct = mDataActNir.getText().toString().trim();
        String serieAct = mSerieActNir.getText().toString().trim();
        int numarAct = Integer.parseInt(mNumarActNir.getText().toString().trim());

        final Nir nir;

        if (mNir != null) {
            nir = new Nir(mNir.getId(), numar, data, serieAct, numarAct, dataAct, mNumeFurnizorNir, mCuiFurnizorNir, mListaProduse);
        } else {
            nir = new Nir(numar, data, serieAct, numarAct, dataAct, mNumeFurnizorNir, mCuiFurnizorNir, mListaProduse);
        }

        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mNir == null) {
                    mDb.nirDao().insertNir(nir);
                } else {
                    mDb.nirDao().updateNir(nir);
                }
            }
        });
    }

    private void deleteNir() {
        updateStoc(false);
        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.nirDao().deleteNir(mNir);
            }
        });

        getActivity().finish();
    }



    private void updateStoc(final boolean choice) {
        for (final Produs p : mListaProduse) {
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
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.setProdusData(mListaProduse);
    }

    @Override
    public void onProdusItemClick(Produs clickedProdus) {
        Intent intent = new Intent(getActivity(), EditorActivity.class);
        intent.putExtra(FRAGMENT_TO_OPEN, PRODUS_NIR_FRAGMENT);
        intent.putExtra(FRAGMENT_EDITOR_MODE, false);
        intent.putExtra(CLICKED_ITEM, clickedProdus);
        intent.putExtra(INDEX_PRODUS_NIR, mListaProduse.indexOf(clickedProdus));
        startActivityForResult(intent, EDIT_PRODUS_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_PRODUS_REQUEST) {
            if (resultCode == RESULT_OK) {
                Produs produs = data.getParcelableExtra(RESULT_PRODUS);
                mListaProduse.add(produs);
                mAdapter.setProdusData(mListaProduse);
                mNirHasChanged = true;
            }
        }

        if (requestCode == EDIT_PRODUS_REQUEST) {
            if (resultCode == RESULT_OK) {
                int position = data.getIntExtra(INDEX_PRODUS_NIR, -1);
                Produs produs = data.getParcelableExtra(RESULT_PRODUS);

                if (position != -1) {
                    mListaProduse.remove(position);
                    mListaProduse.add(position, produs);
                    mAdapter.setProdusData(mListaProduse);
                    mNirHasChanged = true;
                }

            } else if (resultCode == 2) {
                int position = data.getIntExtra(INDEX_PRODUS_NIR, -1);
                if (position != -1) {
                    mListaProduse.remove(position);
                    mAdapter.setProdusData(mListaProduse);
                    mNirHasChanged = true;
                }
            }
        }
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.delete_nir_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteNir();
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
