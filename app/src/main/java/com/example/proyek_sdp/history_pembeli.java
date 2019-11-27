package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class history_pembeli extends AppCompatActivity {
    ArrayList<barangHistory> listBarang = new ArrayList<barangHistory>();
    ListView lvHistory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_pembeli_layout);
        lvHistory = findViewById(R.id.listHistoryPembeliFront);
        listBarang.add(new barangHistory("sepeda1",0,"edwin","Jakartah","31 Februari 2021"));
        listBarang.add(new barangHistory("sepeda2",1,"edwin2","Jakartah","31 Februari 2021"));
        listBarang.add(new barangHistory("sepeda3",1,"edwin3","Jakartah","31 Februari 2021"));
        listBarang.add(new barangHistory("sepeda4",2,"edwin4","Jakartah","31 Februari 2021"));

        adapter adap = new adapter(getApplicationContext(),listBarang);
        lvHistory.setAdapter(adap);
        //lvHistory.setDivider(null);
        lvHistory.setDividerHeight(10);

        ActionBar ab=getSupportActionBar();
        ab.setTitle("Aku titip");
    }


    class barangHistory{
        private String namabarang;
        private int status;
        private String penjual;
        private String lokasi;
        private String tgl;

        public barangHistory(String namabarang, int status, String penjual, String lokasi,String tgl) {
            this.namabarang = namabarang;
            this.status = status;
            this.penjual = penjual;
            this.lokasi = lokasi;
            this.tgl = tgl;
        }

        public String getTgl() {
            return tgl;
        }

        public void setTgl(String tgl) {
            this.tgl = tgl;
        }

        public void setNamabarang(String namabarang) {
            this.namabarang = namabarang;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setPenjual(String penjual) {
            this.penjual = penjual;
        }

        public void setLokasi(String lokasi) {
            this.lokasi = lokasi;
        }

        public String getNamabarang() {
            return namabarang;
        }

        public int getStatus() {
            return status;
        }

        public String getPenjual() {
            return penjual;
        }

        public String getLokasi() {
            return lokasi;
        }
    }
    class adapter extends ArrayAdapter<barangHistory>{
        ArrayList<barangHistory> brg = new ArrayList<barangHistory>();

        public adapter(Context context, ArrayList<barangHistory>a) {
            super(context, R.layout.list_history_front_pembeli_layout,a);
            this.brg = a;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.list_history_front_pembeli_layout,parent,false);
            TextView tvnamabarang = row.findViewById(R.id.tvNamaBarang);
            TextView tvstatus = row.findViewById(R.id.tvStatusBarang);
            TextView tvPenjual = row.findViewById(R.id.tvPenjual);
            TextView tvLokasi = row.findViewById(R.id.tvLokasi);
            TextView tvTglBeli = row.findViewById(R.id.tvTglBeli);
            ImageView img = row.findViewById(R.id.ivImageBarang);

            if(brg.get(position).getStatus() == 0){
                tvstatus.setText("Dikonfirmasi");
                tvstatus.setTextColor(Color.BLACK);
                tvstatus.setBackgroundColor(Color.GRAY);
            }
            else if(brg.get(position).getStatus() == 1){
                tvstatus.setText("Dikirim");
                tvstatus.setTextColor(Color.BLACK);
                tvstatus.setBackgroundColor(Color.YELLOW);
            }
            else if(brg.get(position).getStatus() == 2){
                tvstatus.setText("Sudah sampai");
                tvstatus.setTextColor(Color.BLACK);
                tvstatus.setBackgroundColor(Color.GREEN);
            }
            img.setImageResource(R.drawable.bike);
            tvnamabarang.setText("Barang: "+brg.get(position).getNamabarang());
            tvPenjual.setText("Penjual: "+brg.get(position).getPenjual());
            tvLokasi.setText("Lokasi: "+brg.get(position).getLokasi());
            tvTglBeli.setText(brg.get(position).getTgl()+"");

            tvnamabarang.setTextColor(Color.BLACK);
            tvPenjual.setTextColor(Color.BLACK);
            tvLokasi.setTextColor(Color.BLACK);
            tvTglBeli.setTextColor(Color.BLACK);

            return row;

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu_topup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.close){
            finish();
        }
        return true;
    }
}
