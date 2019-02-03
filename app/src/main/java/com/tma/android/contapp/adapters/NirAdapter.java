package com.tma.android.contapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tma.android.contapp.R;
import com.tma.android.contapp.data.Nir;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NirAdapter extends RecyclerView.Adapter<NirAdapter.NirAdapterViewHolder> {

    private ArrayList<Nir> mNir;

    private final NirItemClickListener mClickHandler;

    public interface NirItemClickListener {
        void onNirItemClick(Nir clickedNir);
    }

    public NirAdapter(NirItemClickListener listener) {
        mClickHandler = listener;
    }

    @NonNull
    @Override
    public NirAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.nir_list_item, parent, false);
        return new NirAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NirAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mNir == null) return 0;
        return mNir.size();
    }

    class NirAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.numar_nir) TextView numarNir;
        @BindView(R.id.nume_furnizor_nir) TextView numeFurnizorNir;
        @BindView(R.id.serie_act_nir) TextView serieActNir;
        @BindView(R.id.numar_act_nir) TextView numarActNir;
        @BindView(R.id.data_nir) TextView dataNir;

        public NirAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int listItemIndex) {
            numarNir.setText(String.valueOf(mNir.get(listItemIndex).getNumar()));
            numeFurnizorNir.setText(mNir.get(listItemIndex).getNumeFurnizor());
            serieActNir.setText(mNir.get(listItemIndex).getSerieAct());
            numarActNir.setText(String.valueOf(mNir.get(listItemIndex).getNumarAct()));
            dataNir.setText(mNir.get(listItemIndex).getData().toString());

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mClickHandler.onNirItemClick(mNir.get(clickedPosition));
        }
    }

    public void setNirData(ArrayList<Nir> nirData) {
        mNir = nirData;
        notifyDataSetChanged();
    }

    public void resetNirData() {
        mNir = null;
    }
}
