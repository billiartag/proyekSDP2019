package com.example.proyek_sdp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryWalletAdapter extends RecyclerView.Adapter<HistoryWalletAdapter.HistoryWalletViewHolder> {
    Context context;
    ArrayList<history_wallet>list_hist_wallet=new ArrayList<history_wallet>();

    public HistoryWalletAdapter(Context context, ArrayList<history_wallet> list_hist_wallet) {
        this.context = context;
        this.list_hist_wallet = list_hist_wallet;
    }

    @NonNull
    @Override
    public HistoryWalletViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.history_wallet_layout,parent,false);
        HistoryWalletViewHolder holder=new HistoryWalletViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryWalletViewHolder holder, int position) {
        holder.tanggal_history_wallet.setText(list_hist_wallet.get(position).getWaktu_history());
        holder.detail_transfer_history_wallet.setText(list_hist_wallet.get(position).getStatus_history());
        if(list_hist_wallet.get(position).getStatus_history().equals("Transaksi Keluar")){
            holder.jumlah_transfer_history_wallet.setTextColor(Color.RED);
        }
        holder.jumlah_transfer_history_wallet.setText(list_hist_wallet.get(position).getNominal_berubah());
    }

    @Override
    public int getItemCount() {
        return list_hist_wallet.size();
    }

    public class HistoryWalletViewHolder extends RecyclerView.ViewHolder {
        TextView tanggal_history_wallet,detail_transfer_history_wallet,jumlah_transfer_history_wallet;
        public HistoryWalletViewHolder(@NonNull View itemView) {
            super(itemView);
            tanggal_history_wallet=itemView.findViewById(R.id.tanggal_history_wallet);
            detail_transfer_history_wallet=itemView.findViewById(R.id.detail_transfer_history_wallet);
            jumlah_transfer_history_wallet=itemView.findViewById(R.id.jumlah_transfer_history_wallet);
        }
    }
}
