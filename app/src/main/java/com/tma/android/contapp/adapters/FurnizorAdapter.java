package com.tma.android.contapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tma.android.contapp.R;
import com.tma.android.contapp.data.Furnizor;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FurnizorAdapter extends RecyclerView.Adapter<FurnizorAdapter.FurnizorAdapterViewHolder> {

    private ArrayList<Furnizor> mFurnizor;

    private final FurnizorItemClickListener mClickHandler;

    public interface FurnizorItemClickListener {
        void onFurnizorItemClick(Furnizor clickedFurnizor);
    }

    public FurnizorAdapter(FurnizorItemClickListener listener) {
        mClickHandler = listener;
    }

    @NonNull
    @Override
    public FurnizorAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.furnizor_list_item, parent, false);
        return new FurnizorAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FurnizorAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mFurnizor == null) return 0;
        return mFurnizor.size();
    }

    class FurnizorAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.nume_furnizor) TextView numeFurnizor;
        @BindView(R.id.localitate_furnizor) TextView localitateFurnizor;
        @BindView(R.id.cui_furnizor) TextView cuiFurnizor;

        public FurnizorAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int listItemIndex) {
            numeFurnizor.setText(mFurnizor.get(listItemIndex).getNume());
            localitateFurnizor.setText(mFurnizor.get(listItemIndex).getLocalitate());
            cuiFurnizor.setText(mFurnizor.get(listItemIndex).getCui());

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mClickHandler.onFurnizorItemClick(mFurnizor.get(clickedPosition));
        }
    }

    public void setFurnizorData(ArrayList<Furnizor> furnizoriData) {
        mFurnizor = furnizoriData;
        notifyDataSetChanged();
    }

    public void resetFurnizorData() {
        mFurnizor = null;
    }
}
