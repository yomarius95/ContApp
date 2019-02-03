package com.tma.android.contapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tma.android.contapp.R;
import com.tma.android.contapp.data.Produs;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProdusNirAdapter extends RecyclerView.Adapter<ProdusNirAdapter.ProdusAdapterNirViewHolder> {

    private ArrayList<Produs> mProdus;

    private final ProdusItemClickListener mClickHandler;

    public interface ProdusItemClickListener {
        void onProdusItemClick(Produs clickedProdus);
    }

    public ProdusNirAdapter(ProdusItemClickListener listener) {
        mClickHandler = listener;
    }

    @NonNull
    @Override
    public ProdusAdapterNirViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.produs_nir_list_item, parent, false);
        return new ProdusAdapterNirViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdusAdapterNirViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mProdus == null) return 0;
        return mProdus.size();
    }

    class ProdusAdapterNirViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.nume_produs_nir)
        TextView numeProdus;
        @BindView(R.id.cantitate_produs)
        TextView cantitateProdus;
        @BindView(R.id.total_aprovizionare_produs)
        TextView totalAprovizionareProdus;
        @BindView(R.id.total_vanzare_produs)
        TextView totalVanzareProdus;

        public ProdusAdapterNirViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int listItemIndex) {
            numeProdus.setText(mProdus.get(listItemIndex).getNume());
            cantitateProdus.setText(String.valueOf(mProdus.get(listItemIndex).getCantitate()));
            totalAprovizionareProdus.setText(String.valueOf(mProdus.get(listItemIndex).getValoareTotalIntrare()));
            totalVanzareProdus.setText(String.valueOf(mProdus.get(listItemIndex).getValoareTotalIesire()));

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mClickHandler.onProdusItemClick(mProdus.get(clickedPosition));
        }
    }

    public void setProdusData(ArrayList<Produs> produseData) {
        mProdus = produseData;
        notifyDataSetChanged();
    }

    public void resetProdusData() {
        mProdus = null;
    }
}
