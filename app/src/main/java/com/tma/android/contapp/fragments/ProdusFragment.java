package com.tma.android.contapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.tma.android.contapp.AppExecutors;
import com.tma.android.contapp.EditorActivity;
import com.tma.android.contapp.R;
import com.tma.android.contapp.adapters.FurnizorAdapter;
import com.tma.android.contapp.adapters.ProdusAdapter;
import com.tma.android.contapp.data.AppDatabase;
import com.tma.android.contapp.data.Furnizor;
import com.tma.android.contapp.data.Produs;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tma.android.contapp.EditorActivity.CLICKED_ITEM;
import static com.tma.android.contapp.EditorActivity.FRAGMENT_EDITOR_MODE;
import static com.tma.android.contapp.EditorActivity.FRAGMENT_TO_OPEN;

public class ProdusFragment extends Fragment implements ProdusAdapter.ProdusItemClickListener {
    public static final String PRODUS_FRAGMENT = "produs_fragment";
    public static final String CUI_FURNIZOR = "cui_furnizor";

    @BindView(R.id.fab_produs)
    FloatingActionButton fabProdus;

    private AppDatabase mDb;
    private ProdusAdapter mAdapter;
    @BindView(R.id.rv_produs)
    RecyclerView mProdusList;

    @BindView(R.id.spinner_produse_furnizor)
    Spinner mSpinnerFurnizor;
    private String[] spinnerCuiArray;
    private String[] spinnerNameArray;
    private int index = -1;

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
                if(index == -1) {
                    Toast.makeText(getActivity(), "Select Furnizor", Toast.LENGTH_SHORT).show();
                } else {
                    intent.putExtra(CUI_FURNIZOR, spinnerCuiArray[index]);

                    startActivity(intent);
                }
            }
        });

        mDb = AppDatabase.getInstance(getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        mProdusList.setLayoutManager(layoutManager);
        mProdusList.setHasFixedSize(true);

        mAdapter = new ProdusAdapter(this, getContext());

        mProdusList.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final ArrayList<Furnizor> furnizori = (ArrayList<Furnizor>) mDb.furnizorDao().loadAllFurnizori();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        spinnerNameArray = new String[furnizori.size()];
                        spinnerCuiArray = new String[furnizori.size()];

                        for (int i = 0; i < furnizori.size(); i++) {
                            spinnerNameArray[i] = furnizori.get(i).getNume();
                            spinnerCuiArray[i] = furnizori.get(i).getCui();
                        }

                        setupSpinner();
                    }
                });
            }
        });
    }

    @Override
    public void onProdusItemClick(Produs clickedProdus) {
        Intent intent = new Intent(getActivity(), EditorActivity.class);
        intent.putExtra(FRAGMENT_TO_OPEN, PRODUS_FRAGMENT);
        intent.putExtra(FRAGMENT_EDITOR_MODE, false);
        intent.putExtra(CLICKED_ITEM, clickedProdus);
        startActivity(intent);
    }

    private void setupSpinner() {
        // Apply the adapter to the spinner
        if (getActivity() != null) {
            // Create adapter for spinner. The list options are from the String array it will use
            // the spinner will use the default layout
            ArrayAdapter<String> furnizorSpinnerAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_item, spinnerNameArray);

            // Specify dropdown layout style - simple list view with 1 item per line
            furnizorSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            // Apply the adapter to the spinner
            mSpinnerFurnizor.setAdapter(furnizorSpinnerAdapter);
        }

        mSpinnerFurnizor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    index = position;
                    AppExecutors.getsInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            final ArrayList<Produs> produse = (ArrayList<Produs>) mDb.produsDao().loadAllProduseByCui(spinnerCuiArray[index]);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.setProdusData(produse);
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
}
