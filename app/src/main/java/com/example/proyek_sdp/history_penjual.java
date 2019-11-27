package com.example.proyek_sdp;
import androidx.annotation.Nullable;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
public class history_penjual extends AppCompatActivity {

    ArrayList<barangHistory>listBarang = new ArrayList<barangHistory>();
    ListView lvHistory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histoy_penjual_layout);
        lvHistory = findViewById(R.id.lvHistoryPenjualFront);


        //add barang dari list firebase
        listBarang.add(new barangHistory("sepeda1",0,"edwin1","0123L41921E","09 Februari 2021"));
        listBarang.add(new barangHistory("sepeda2",1,"edwin2","0123L41921D","31 Februari 2021"));
        listBarang.add(new barangHistory("sepeda3",1,"edwin3","0123L01126D","31 Februari 2021"));
        listBarang.add(new barangHistory("sepeda4",2,"edwin4","0123L41422D","31 Februari 2021"));

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
        ab.setTitle("Dititipin");
    }
    public void cancelOrder(String orderID){
        Toast.makeText(this, orderID+"", Toast.LENGTH_SHORT).show();
        //kasi kode cancel ke firebase disini
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
        Context ctx;
        public adapter(Context context, ArrayList<barangHistory>a) {
            super(context, R.layout.list_history_front_pembeli_layout,a);
            this.brg = a;
            ctx=context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.list_history_penjual_layout,parent,false);
            TextView tvNamaBarang = row.findViewById(R.id.tvNamaBarang);
            TextView tvStatus = row.findViewById(R.id.tvStatus);
            TextView tvId = row.findViewById(R.id.tvId);
            ImageView img = row.findViewById(R.id.ivImageBarang);
            Button btnCancel = row.findViewById(R.id.buttonPenjualCancelOrder);

            if(brg.get(position).getStatus() == 0){
                tvStatus.setText("Diterima");
                tvStatus.setTextColor(Color.BLACK);
            }
            else if(brg.get(position).getStatus() == 1){
                tvStatus.setText("Sedang dikirim");
                tvStatus.setTextColor(Color.BLUE);
            }
            else if(brg.get(position).getStatus() == 2){
                tvStatus.setText("Telah dikirim");
                tvStatus.setTextColor(Color.GREEN);
            }
            img.setImageResource(R.drawable.bike);
            tvNamaBarang.setText(brg.get(position).getNamabarang());
            tvId.setText(brg.get(position).getIdBarang());
            tvId.setTextColor(Color.GREEN);
            tvId.setTypeface(null, Typeface.BOLD_ITALIC);
            tvNamaBarang.setTextColor(Color.BLACK);
            tvId.setTextColor(Color.BLACK);
            //kalau sudah selesai / sudah selesai dikirim
            if(brg.get(position).getStatus()!=0){
                btnCancel.setVisibility(View.GONE);
            }
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id_barang = brg.get(position).getIdBarang();
//                    Toast.makeText(history_penjual.this, "ID barang yang diambil: "+id_barang, Toast.LENGTH_SHORT).show();
                    cancelOrder(id_barang);
                }
            });
            return row;

        }
    }

}
