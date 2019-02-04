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

import static com.tma.android.contapp.data.Produs.*;

public class ProdusAdapter extends RecyclerView.Adapter<ProdusAdapter.ProdusAdapterViewHolder> {

    private ArrayList<Produs> mProdus;

    private final ProdusItemClickListener mClickHandler;
    private Context mContext;

    public interface ProdusItemClickListener {
        void onProdusItemClick(Produs clickedProdus);
    }

    public ProdusAdapter(ProdusItemClickListener listener, Context context) {
        mClickHandler = listener;
        mContext = context;
    }

    @NonNull
    @Override
    public ProdusAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.produs_list_item, parent, false);
        return new ProdusAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdusAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mProdus == null) return 0;
        return mProdus.size();
    }

    class ProdusAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.nume_produs)
        TextView numeProdus;
        @BindView(R.id.pret_intrare_produs)
        TextView pretIntrareProdus;
        @BindView(R.id.pret_iesire_produs)
        TextView pretIesireProdus;
        @BindView(R.id.stoc_produs)
        TextView stocProdus;
        @BindView(R.id.unitate_masura_produs)
        TextView unitateMasuraProdus;
        @BindView(R.id.categorie_tva_produs)
        TextView categorieTvaProdus;

        public ProdusAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int listItemIndex) {
            numeProdus.setText(mProdus.get(listItemIndex).getNume());
            pretIntrareProdus.setText(String.valueOf(mProdus.get(listItemIndex).getPretIntrare()));
            stocProdus.setText(String.valueOf(mProdus.get(listItemIndex).getCantitate()));
            pretIesireProdus.setText(String.valueOf(mProdus.get(listItemIndex).getPretIesire()));
            switch (mProdus.get(listItemIndex).getUnitateMasura()) {
                case UNITATE_MASURA_BUC:
                    unitateMasuraProdus.setText(mContext.getString(R.string.unitate_masura_buc));
                    break;
                case UNITATE_MASURA_KG:
                    unitateMasuraProdus.setText(mContext.getString(R.string.unitate_masura_kg));
                    break;
                case UNITTE_MASURA_M:
                    unitateMasuraProdus.setText(mContext.getString(R.string.unitate_masura_metru));
                    break;
            }

            switch (mProdus.get(listItemIndex).getCategorieTVA()) {
                case COTA_TVA_0:
                    categorieTvaProdus.setText(mContext.getString(R.string.categorie_tva_0));
                    break;
                case COTA_TVA_5:
                    categorieTvaProdus.setText(mContext.getString(R.string.categorie_tva_5));
                    break;
                case COTA_TVA_9:
                    categorieTvaProdus.setText(mContext.getString(R.string.categorie_tva_9));
                    break;
                case COTA_TVA_19:
                    categorieTvaProdus.setText(mContext.getString(R.string.categorie_tva_19));
                    break;
                case COTA_TVA_20:
                    categorieTvaProdus.setText(mContext.getString(R.string.categorie_tva_20));
                    break;
                case COTA_TVA_24:
                    categorieTvaProdus.setText(mContext.getString(R.string.categorie_tva_24));
                    break;
            }

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
