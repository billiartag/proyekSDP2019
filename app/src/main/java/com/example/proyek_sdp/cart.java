package com.example.proyek_sdp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class cart extends AppCompatActivity {
    ListView lv;
    Button topup;
    Button bayar;
    TextView saldo;
    TextView total;
    ArrayList<barang> kumpulanbarang = new ArrayList<barang>();
    int totalharga=0;
    adapter adap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        kumpulanbarang.add(new barang("Treadmill",25000,"bagus untuk kesehatan","05:45:21","Flash Sale",R.drawable.treadmill,"Cosmas",12));
        kumpulanbarang.add(new barang("electronic Treadmill",55000,"bagus untuk kesehatan dan otot kaki","07:45:21","Pre Order",R.drawable.electrictreadmill,"Alfon",14));
        kumpulanbarang.add(new barang("Bike",75000,"bagus untuk kesehatan dan mudah di pakai tanpa keluar rumah","08:45:21","Flash Sale",R.drawable.bike,"Edwin",6));
        topup=findViewById(R.id.Topup);
        bayar=findViewById(R.id.bayar);
        saldo=findViewById(R.id.saldo);
        total=findViewById(R.id.total);
        lv=findViewById(R.id.lvcart);
        adap = new adapter(getApplicationContext(),kumpulanbarang);
        lv.setAdapter(adap);
        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move=new Intent(getApplicationContext(),topup_activity.class);
                startActivity(move);
            }
        });
        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Bayar Dengan E-wallet berhasil! Cek history :)", Toast.LENGTH_SHORT).show();
            }
        });
    }
    class adapter extends ArrayAdapter<barang> {
        Context context;
        ArrayList<barang>barang;

        public adapter(Context c,ArrayList<barang>barang){
            super(c,R.layout.list_barang_layout,barang);
            this.barang = barang;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.list_cart_layout,parent,false);
            TextView nama = row.findViewById(R.id.nama);
            TextView harga = row.findViewById(R.id.harga);
            final TextView jumlah = row.findViewById(R.id.jumlah);
            ImageView img = row.findViewById(R.id.gambar_barang);
            Button btnmin=row.findViewById(R.id.btnmin);
            Button btnplus=row.findViewById(R.id.btnplus);
            Button cancel=row.findViewById(R.id.cancel);
            nama.setText(barang.get(position).getNama());
            harga.setText("Rp. "+barang.get(position).getHarga());
            jumlah.setText("1");
            totalharga=totalharga+(barang.get(position).getHarga()*Integer.parseInt(jumlah.getText().toString()));
            total.setText("Total : Rp. 80000");//msh paten untuk sekarang
            jumlah.setTextColor(Color.BLACK);
            img.setImageResource(barang.get(position).getGambar());
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    barang.remove(position);
                    adap.notifyDataSetChanged();
                }
            });
            btnmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Integer.parseInt(jumlah.getText().toString())-1>=0){
                        jumlah.setText((Integer.parseInt(jumlah.getText().toString())-1)+"");
                    }
                }
            });
            btnplus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    jumlah.setText((Integer.parseInt(jumlah.getText().toString())+1)+"");
                }
            });
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
