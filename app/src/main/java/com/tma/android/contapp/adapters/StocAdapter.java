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

public class StocAdapter extends RecyclerView.Adapter<StocAdapter.StocAdapterViewHolder> {

    private ArrayList<Produs> mProdus;

    private final StocItemClickListener mClickHandler;

    public interface StocItemClickListener {
        void onStocItemClick(Produs clickedProdus);
    }

    public StocAdapter(StocItemClickListener listener) {
        mClickHandler = listener;
    }

    @NonNull
    @Override
    public StocAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.stoc_list_item, parent, false);
        return new StocAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StocAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mProdus == null) return 0;
        return mProdus.size();
    }

    class StocAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.stoc_list_nume_produs)
        TextView stocNumeProdus;
        @BindView(R.id.stoc_list_produs_stoc) TextView produsStoc;

        public StocAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int listItemIndex) {
            stocNumeProdus.setText(mProdus.get(listItemIndex).getNume());
            produsStoc.setText(String.valueOf(mProdus.get(listItemIndex).getCantitate()));

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mClickHandler.onStocItemClick(mProdus.get(clickedPosition));
        }
    }

    public void setProdusData(ArrayList<Produs> produsData) {
        mProdus = produsData;
        notifyDataSetChanged();
    }

    public void resetProdusData() {
        mProdus = null;
    }
}
