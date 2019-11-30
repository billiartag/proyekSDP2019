package com.example.proyek_sdp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    }

    @Override
    public int getItemCount() {
        return list_hist_wallet.size();
    }

    public class HistoryWalletViewHolder extends RecyclerView.ViewHolder {
        public HistoryWalletViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
