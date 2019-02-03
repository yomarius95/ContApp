package com.tma.android.contapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tma.android.contapp.AppExecutors;
import com.tma.android.contapp.EditorActivity;
import com.tma.android.contapp.R;
import com.tma.android.contapp.adapters.FurnizorAdapter;
import com.tma.android.contapp.data.AppDatabase;
import com.tma.android.contapp.data.Furnizor;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tma.android.contapp.EditorActivity.CLICKED_ITEM;
import static com.tma.android.contapp.EditorActivity.FRAGMENT_EDITOR_MODE;
import static com.tma.android.contapp.EditorActivity.FRAGMENT_TO_OPEN;

public class FurnizorFragment extends Fragment implements FurnizorAdapter.FurnizorItemClickListener {
    public static final String FURNIZOR_FRAGMENT = "furnizor_fragment";

    @BindView(R.id.fab_furnizor) FloatingActionButton fabFurnizor;

    private AppDatabase mDb;
    private FurnizorAdapter mAdapter;
    @BindView(R.id.rv_furnizor) RecyclerView mFurnizorList;

    public FurnizorFragment() {

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_furnizor, container, false);
        ButterKnife.bind(this, rootView);

        fabFurnizor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditorActivity.class);
                intent.putExtra(FRAGMENT_TO_OPEN, FURNIZOR_FRAGMENT);
                intent.putExtra(FRAGMENT_EDITOR_MODE, true);
                startActivity(intent);
            }
        });

        mDb = AppDatabase.getInstance(getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        mFurnizorList.setLayoutManager(layoutManager);
        mFurnizorList.setHasFixedSize(true);

        mAdapter = new FurnizorAdapter(this);

        mFurnizorList.setAdapter(mAdapter);

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
                        mAdapter.setFurnizorData(furnizori);
                    }
                });
            }
        });
    }

    @Override
    public void onFurnizorItemClick(Furnizor clickedFurnizor) {
        Intent intent = new Intent(getActivity(), EditorActivity.class);
        intent.putExtra(FRAGMENT_TO_OPEN, FURNIZOR_FRAGMENT);
        intent.putExtra(FRAGMENT_EDITOR_MODE, false);
        intent.putExtra(CLICKED_ITEM, clickedFurnizor);
        startActivity(intent);
    }
}
