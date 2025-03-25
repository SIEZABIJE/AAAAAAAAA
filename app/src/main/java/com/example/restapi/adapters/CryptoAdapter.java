package com.example.restapi.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restapi.R;
import com.example.restapi.models.Crypto;
import java.util.List;

public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder> {
    private List<Crypto> cryptos;

    public CryptoAdapter(List<Crypto> cryptos) {
        this.cryptos = cryptos;
    }

    @NonNull
    @Override
    public CryptoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.crypto_item, parent, false);
        return new CryptoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CryptoViewHolder holder, int position) {
        Crypto crypto = cryptos.get(position);

        holder.cryptoIcon.setImageResource(R.drawable.bitcoin);
        
        holder.cryptoCode.setText(crypto.getCode());
        holder.cryptoName.setText(crypto.getName());
        holder.cryptoPrice.setText(crypto.getPrice());
    }

    @Override
    public int getItemCount() {
        return cryptos.size();
    }

    public void updateData(List<Crypto> newCryptos) {
        this.cryptos = newCryptos;
        notifyDataSetChanged();
    }

    static class CryptoViewHolder extends RecyclerView.ViewHolder {
        ImageView cryptoIcon;
        TextView cryptoCode, cryptoName, cryptoPrice;

        CryptoViewHolder(View itemView) {
            super(itemView);
            cryptoIcon = itemView.findViewById(R.id.cryptoIcon);
            cryptoCode = itemView.findViewById(R.id.cryptoCode);
            cryptoName = itemView.findViewById(R.id.cryptoName);
            cryptoPrice = itemView.findViewById(R.id.cryptoPrice);
        }
    }
} 