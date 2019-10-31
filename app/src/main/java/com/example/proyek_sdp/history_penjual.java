package com.example.proyek_sdp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class history_penjual extends AppCompatActivity {

    ArrayList<barangHistory>listBarang = new ArrayList<barangHistory>();
    ListView lvHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histoy_penjual);
        lvHistory = findViewById(R.id.lvHistoryPenjualFront);

        listBarang.add(new barangHistory("sepeda",0,"edwin","0123L01121D","31 Februari 2021"));
        listBarang.add(new barangHistory("sepeda",1,"edwin2","0123L41921D","31 Februari 2021"));
        listBarang.add(new barangHistory("sepeda",1,"edwin3","0123L01126D","31 Februari 2021"));
        listBarang.add(new barangHistory("sepeda",0,"edwin4","0123L41421D","31 Februari 2021"));






        adapter adap = new adapter(getApplicationContext(),listBarang);


        lvHistory.setAdapter(adap);
        lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(),detail_history_penjual.class);
                startActivity(intent);
            }
        });

        ActionBar ab=getSupportActionBar();
        ab.setTitle("TitipAku");
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
    class barangHistory{
        private String namabarang;
        private int status;
        private String penjual;
        private String id;
        private String tgl;

        public barangHistory(String namabarang, int status, String penjual, String id,String tgl) {
            this.namabarang = namabarang;
            this.status = status;
            this.penjual = penjual;
            this.id = id;
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

        public void setIdBarang(String id) {
            this.id = id;
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

        public String getIdBarang() {
            return id;
        }
    }

    class adapter extends ArrayAdapter<barangHistory> {
        ArrayList<barangHistory> brg = new ArrayList<barangHistory>();

        public adapter(Context context, ArrayList<barangHistory>a) {
            super(context, R.layout.list_history_front_pembeli_layout,a);
            this.brg = a;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.list_history_penjual_layout,parent,false);
            TextView tvNamaBarang = row.findViewById(R.id.tvNamaBarang);
            TextView tvStatus = row.findViewById(R.id.tvStatus);
            TextView tvId = row.findViewById(R.id.tvId);
            ImageView img = row.findViewById(R.id.ivImageBarang);

            if(brg.get(position).getStatus() == 0){
                tvStatus.setText("Belum Dikonfirmasi");
                tvStatus.setTextColor(Color.RED);
            }
            else if(brg.get(position).getStatus() == 1){
                tvStatus.setText("Dikonfirmasi");
                tvStatus.setTextColor(Color.BLUE);
            }
            img.setImageResource(R.drawable.bike);
            tvNamaBarang.setText(brg.get(position).getNamabarang());
            tvId.setText(brg.get(position).getIdBarang());
            tvId.setTextColor(Color.GREEN);
            tvId.setTypeface(null, Typeface.BOLD_ITALIC);
            tvNamaBarang.setTextColor(Color.BLACK);
            tvId.setTextColor(Color.BLACK);

            return row;

        }
    }

}
