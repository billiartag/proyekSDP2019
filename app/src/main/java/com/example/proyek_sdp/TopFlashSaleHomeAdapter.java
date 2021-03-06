package com.example.proyek_sdp;

import android.content.Context;
import android.net.Uri;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TopFlashSaleHomeAdapter extends RecyclerView.Adapter<TopFlashSaleHomeAdapter.TopFlashSaleHomeViewHolder> {
    private Context context;
    private ArrayList<barang> list_barang;
    private OnItemClickListener onItemClickListener;
    private int total_waktu;

    public TopFlashSaleHomeAdapter(Context context, ArrayList<barang> list_barang) {
        this.context = context;
        this.list_barang = list_barang;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public TopFlashSaleHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_flashsale_home,parent,false);
        TopFlashSaleHomeViewHolder holder=new TopFlashSaleHomeViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TopFlashSaleHomeViewHolder holder, int position) {
        FirebaseStorage.getInstance().getReference().child("img_barang").child(list_barang.get(position).getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (context!=null){
                    Glide.with(context).load(uri).into(holder.gambar);
                }
            }
        });
        Locale locale = new Locale("in","ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        String harga  = format.format(list_barang.get(position).getHarga());
        holder.harga.setText(harga);
        if (list_barang.get(position).getNama().length()>10){
            holder.nama.setText(list_barang.get(position).getNama().substring(0,10)+"...");
        }
        else {
            holder.nama.setText(list_barang.get(position).getNama());
        }
        //agar timernya ga jalan bareng2, supaya ga nge flick, jadi pakai sistem, jalan trus stop, begitu seterusnya
        if (holder.timer != null) {
            holder.timer.cancel();
        }
        holder.timer = new CountDownTimer(999999999, 1000) {
            @Override
            public void onTick(long l) {
                String waktu=list_barang.get(position).getWaktu_selesai();
                String[] waktu_split=waktu.split(":");
                if(waktu_split.length>1){
                    int jam_selesai=Integer.parseInt(waktu_split[0]);
                    int menit_selesai=Integer.parseInt(waktu_split[1]);
                    int detik_selesai=Integer.parseInt(waktu_split[2]);
                    int total_waktu_selesai=(jam_selesai*3600) + (menit_selesai*60) + detik_selesai;
                    Calendar now = Calendar.getInstance();
                    int jam_mulai=now.get(Calendar.HOUR_OF_DAY);
                    int menit_mulai=now.get(Calendar.MINUTE);
                    int detik_mulai=now.get(Calendar.SECOND);
                    int total_waktu_mulai=(jam_mulai*3600) + (menit_mulai*60) + detik_mulai;
                    if(total_waktu_selesai-total_waktu_mulai>0){
                        //tanggal sekarang
                        Date dt = new Date();
                        SimpleDateFormat sdf2 = new SimpleDateFormat("d/MM/yyyy");
                        String time2 = sdf2.format(dt);
                        String[] temptgl=time2.split("/");
                        long hari_sekarang=Long.parseLong(temptgl[0]);
                        long bulan_sekarang=Long.parseLong(temptgl[1]);
                        long tahun_sekarang=Long.parseLong(temptgl[2]);
                        long totalhari_sekarang=hari_sekarang+(bulan_sekarang*30)+(tahun_sekarang*365);
                        //tanggal upload
                        String[] temptgl_upload=list_barang.get(position).getWaktu_upload().split("/");
                        long hari_sekarang_upload=Long.parseLong(temptgl_upload[0]);
                        long bulan_sekarang_upload=Long.parseLong(temptgl_upload[1]);
                        long tahun_sekarang_upload=Long.parseLong(temptgl_upload[2]);
                        long totalhari_sekarang_upload=hari_sekarang_upload+(bulan_sekarang_upload*30)+(tahun_sekarang_upload*365);
                        if(totalhari_sekarang_upload<totalhari_sekarang){
                            holder.tvtimer_barang.setText("expired");
                            FirebaseDatabase.getInstance().getReference().child("BarangDatabase").child(list_barang.get(position).getId()).child("status").setValue("0");
                        }
                        else {
                            holder.tvtimer_barang.setText("Sisa Waktu : \n"+formatSeconds(total_waktu_selesai-total_waktu_mulai));
                        }
                    }
                    else {
                        holder.tvtimer_barang.setText("expired");
                        FirebaseDatabase.getInstance().getReference().child("BarangDatabase").child(list_barang.get(position).getId()).child("status").setValue("0");
                    }
                }
                else {
                    //tanggal sekarang
                    Date dt = new Date();
                    SimpleDateFormat sdf2 = new SimpleDateFormat("d/MM/yyyy");
                    String time2 = sdf2.format(dt);
                    String[] temptgl=time2.split("/");
                    long hari_sekarang=Long.parseLong(temptgl[0]);
                    long bulan_sekarang=Long.parseLong(temptgl[1]);
                    long tahun_sekarang=Long.parseLong(temptgl[2]);
                    long totalhari_sekarang=hari_sekarang+(bulan_sekarang*30)+(tahun_sekarang*365);
                    //tanggal upload
                    String[] temptgl_selesai=list_barang.get(position).getWaktu_selesai().split("/");
                    long hari_sekarang_selesai=Long.parseLong(temptgl_selesai[0]);
                    long bulan_sekarang_selesai=Long.parseLong(temptgl_selesai[1]);
                    long tahun_sekarang_selesai=Long.parseLong(temptgl_selesai[2]);
                    long totalhari_sekarang_selesai=hari_sekarang_selesai+(bulan_sekarang_selesai*30)+(tahun_sekarang_selesai*365);
                    if(totalhari_sekarang_selesai<totalhari_sekarang){
                        holder.tvtimer_barang.setText("expired");
                        FirebaseDatabase.getInstance().getReference().child("BarangDatabase").child(list_barang.get(position).getId()).child("status").setValue("0");
                    }
                    else {
                        holder.tvtimer_barang.setText("Mulai : \n"+list_barang.get(position).getWaktu_mulai()+"\n Sampai \n"+list_barang.get(position).getWaktu_selesai());
                    }
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();

    }

    @Override
    public int getItemCount() {
        return list_barang.size();
    }

    public class TopFlashSaleHomeViewHolder extends RecyclerView.ViewHolder {
        public TextView nama,harga,tvtimer_barang;
        public ImageView gambar;
        public CountDownTimer timer;
        public TopFlashSaleHomeViewHolder(@NonNull final View itemView) {
            super(itemView);
            nama=itemView.findViewById(R.id.nama);
            harga=itemView.findViewById(R.id.harga);
            gambar=itemView.findViewById(R.id.gambar);
            tvtimer_barang=itemView.findViewById(R.id.tvtimer_barang);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onClick(itemView, getAdapterPosition());
                    }
                }
            });
        }
    }
    public static String formatSeconds(int timeInSeconds)
    {
        int hours = timeInSeconds / 3600;
        int secondsLeft = timeInSeconds - hours * 3600;
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String formattedTime = "";
        if (hours < 10)
            formattedTime += "0";
        formattedTime += hours + ":";

        if (minutes < 10)
            formattedTime += "0";
        formattedTime += minutes + ":";

        if (seconds < 10)
            formattedTime += "0";
        formattedTime += seconds ;

        return formattedTime;
    }
}
