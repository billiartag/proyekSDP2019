package com.example.proyek_sdp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class home_fragment extends Fragment {
    ArrayList<barang> kumpulanbarang = new ArrayList<barang>();
    ArrayList<user> kumpulanuser = new ArrayList<user>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View myview=inflater.inflate(R.layout.fragment_home,container,false);
        setHasOptionsMenu(true);
        //cetak icon top seller
        kumpulanuser.add(new user("Chrysman Yul",5,R.drawable.img1));
        kumpulanuser.add(new user("Cosmoz",4,R.drawable.img2));
        kumpulanuser.add(new user("Alfonz",3,R.drawable.img3));

        for (int i=0;i<kumpulanuser.size();i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            Button btn=new Button(myview.getContext());
            //untuk border dan radius
            /*
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setStroke(5, Color.YELLOW);
            drawable.setCornerRadius(100);
            btn.setBackground(drawable);
            */
            btn.setId(i);
            btn.setBackgroundResource(kumpulanuser.get(i).getProfil_picture());
            final int finalI = i;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user x=kumpulanuser.get(finalI);
                    Bundle b = new Bundle();
                    b.putSerializable("usr", x);
                    Intent intent = new Intent(getContext(), detailprofil.class);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
            LinearLayout tampung=myview.findViewById(R.id.containertopseller);
            tampung.addView(btn,params);
        }

        kumpulanbarang.add(new barang("Treadmill",25000,"bagus untuk kesehatan","05:45:21","Flash Sale",R.drawable.treadmill,"Cosmas",12));
        kumpulanbarang.add(new barang("electronic Treadmill",55000,"bagus untuk kesehatan dan otot kaki","07:45:21","Pre Order",R.drawable.electrictreadmill,"Alfon",14));
        kumpulanbarang.add(new barang("Bike",75000,"bagus untuk kesehatan dan mudah di pakai tanpa keluar rumah","08:45:21","Flash Sale",R.drawable.bike,"Edwin",6));
        //cetak barang flashsale
        for (int i=0;i<kumpulanbarang.size();i++){
            //buat linear layout vertikal untuk container gambar dan tulisan
            LinearLayout objek=new LinearLayout(myview.getContext());
            objek.setGravity(Gravity.CENTER);
            objek.setOrientation(LinearLayout.VERTICAL);
            //ambil linear layout flash sale dari fragment home
            LinearLayout tampung=myview.findViewById(R.id.containerflashsale);

            //masukkan gambar
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            Button btn=new Button(myview.getContext());
            //untuk border dan radius
            /*
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setStroke(5, Color.YELLOW);
            drawable.setCornerRadius(100);
            btn.setBackground(drawable);
            */
            btn.setId(i);
            btn.setBackgroundResource(kumpulanbarang.get(i).getGambar());
            final int finalI = i;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    barang x=kumpulanbarang.get(finalI);
                    Bundle b = new Bundle();
                    b.putSerializable("brg", x);
                    Intent intent = new Intent(getContext(), detail_feed.class);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
            objek.addView(btn,params);

            //tambah tulisan
            TextView tulis=new TextView(myview.getContext());
            tulis.setText(kumpulanbarang.get(i).toString());
            tulis.setTypeface(null,Typeface.BOLD);
            tulis.setGravity(Gravity.CENTER);
            objek.addView(tulis);

            //masukkan ke container yang di home
            tampung.addView(objek,params);
        }
        // Set title bar
        ((home) getActivity()).setActionBarTitle("TitipAku");
        return myview;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.optionmenu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()== R.id.chat){
            //Toast.makeText(getActivity(),"chat",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getContext(),chat_front.class);
            startActivity(i);
        }
        else if (item.getItemId()== R.id.cart){
            //Toast.makeText(getActivity(),"chat",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getContext(),cart.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
