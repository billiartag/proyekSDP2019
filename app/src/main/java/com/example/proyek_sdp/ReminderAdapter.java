package com.example.proyek_sdp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    private Context context;
    private ArrayList<ReminderClass> list_reminder;

    public ReminderAdapter(Context context, ArrayList<ReminderClass> list_reminder) {
        this.context = context;
        this.list_reminder = list_reminder;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_reminder,parent,false);
        ReminderViewHolder holder=new ReminderViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        holder.tvHargaReminder.setText("Jumlah: "+list_reminder.get(position).getJumlah_barang());
        holder.cbReminder.setText(list_reminder.get(position).getNama_barang());
        holder.btnDeleteReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((reminder)context).removeEntry(list_reminder.get(position));
            }
        });
        //checkbox reminder
        holder.cbReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((reminder)context).removeEntryCheckBox(list_reminder.get(position), holder.cbReminder.isChecked());
            }
        });
        if(list_reminder.get(position).getIs_checked()){
            holder.cbReminder.setChecked(true);
        }
        else {
            holder.cbReminder.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return list_reminder.size();
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbReminder;
        TextView tvHargaReminder;
        Button btnDeleteReminder;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            cbReminder=itemView.findViewById(R.id.checkBoxReminder);
            tvHargaReminder=itemView.findViewById(R.id.textViewReminderJumlah);
            btnDeleteReminder=itemView.findViewById(R.id.buttonReminderDelete);
        }
    }
}
