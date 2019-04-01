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
import com.tma.android.contapp.adapters.NirAdapter;
import com.tma.android.contapp.data.AppDatabase;
import com.tma.android.contapp.data.Furnizor;
import com.tma.android.contapp.data.Nir;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tma.android.contapp.EditorActivity.CLICKED_ITEM;
import static com.tma.android.contapp.EditorActivity.FRAGMENT_EDITOR_MODE;
import static com.tma.android.contapp.EditorActivity.FRAGMENT_TO_OPEN;
import static com.tma.android.contapp.fragments.ProdusFragment.CUI_FURNIZOR;

public class NirFragment extends Fragment implements NirAdapter.NirItemClickListener {
    public static final String NIR_FRAGMENT = "nir_fragment";
    public static final String NUME_FURNIZOR = "nume_furnizor";
    public static final String LOCALITATE_FURNIZOR = "localitate_furnizor";

    @BindView(R.id.fab_nir)
    FloatingActionButton fabNir;

    private AppDatabase mDb;
    private NirAdapter mAdapter;
    @BindView(R.id.rv_nir)
    RecyclerView mNirList;

    @BindView(R.id.spinner_nir_furnizor)
    Spinner mSpinnerFurnizor;
    private String[] spinnerCuiArray;
    private String[] spinnerNameArray;
    private String[] spinnerLocalitateArray;
    private int index = -1;

    public NirFragment() {

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nir, container, false);
        ButterKnife.bind(this, rootView);

        fabNir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditorActivity.class);
                intent.putExtra(FRAGMENT_TO_OPEN, NIR_FRAGMENT);
                intent.putExtra(FRAGMENT_EDITOR_MODE, true);
                if(index == -1) {
                    Toast.makeText(getActivity(), "Select Furnizor", Toast.LENGTH_SHORT).show();
                } else {
                    intent.putExtra(CUI_FURNIZOR, spinnerCuiArray[index]);
                    intent.putExtra(NUME_FURNIZOR, spinnerNameArray[index]);
                    intent.putExtra(LOCALITATE_FURNIZOR, spinnerLocalitateArray[index]);

                    startActivity(intent);
                }
            }
        });

        mDb = AppDatabase.getInstance(getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        mNirList.setLayoutManager(layoutManager);
        mNirList.setHasFixedSize(true);

        mAdapter = new NirAdapter(this);

        mNirList.setAdapter(mAdapter);

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
                        spinnerLocalitateArray = new String[furnizori.size()];

                        for (int i = 0; i < furnizori.size(); i++) {
                            spinnerNameArray[i] = furnizori.get(i).getNume();
                            spinnerCuiArray[i] = furnizori.get(i).getCui();
                            spinnerLocalitateArray[i] = furnizori.get(i).getLocalitate();
                        }

                        setupSpinner();
                    }
                });
            }
        });
    }

    @Override
    public void onNirItemClick(Nir clickedNir) {
        Intent intent = new Intent(getActivity(), EditorActivity.class);
        intent.putExtra(FRAGMENT_TO_OPEN, NIR_FRAGMENT);
        intent.putExtra(FRAGMENT_EDITOR_MODE, false);
        intent.putExtra(CLICKED_ITEM, clickedNir);
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
                            final ArrayList<Nir> nir = (ArrayList<Nir>) mDb.nirDao().loadAllNirByCui(spinnerCuiArray[index]);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.setNirData(nir);
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
