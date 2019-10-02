package com.example.proyek_sdp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

public class home_fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View myview=inflater.inflate(R.layout.fragment_home,container,false);
        //cetak icon top seller
        for (int i=0;i<7;i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            Button btn=new Button(myview.getContext());
            setHasOptionsMenu(true);
            //untuk border dan radius
            /*
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setStroke(5, Color.YELLOW);
            drawable.setCornerRadius(100);
            btn.setBackground(drawable);
            */
            btn.setId(i);
            if (i==0){
                btn.setBackgroundResource(R.drawable.img1);
            }else if (i==1){
                btn.setBackgroundResource(R.drawable.img2);
            }else if (i==2){
                btn.setBackgroundResource(R.drawable.img3);
            }else if (i==3){
                btn.setBackgroundResource(R.drawable.img4);
            }else if (i==4){
                btn.setBackgroundResource(R.drawable.img5);
            }else if (i==5){
                btn.setBackgroundResource(R.drawable.img6);
            }else if (i==6){
                btn.setBackgroundResource(R.drawable.img7);
            }
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button x=(Button) view;
                    Toast.makeText(getActivity(), "User Ke "+ (Integer.parseInt(x.getId()+"")+1), Toast.LENGTH_SHORT).show();
                }
            });
            LinearLayout tampung=myview.findViewById(R.id.containertopseller);
            tampung.addView(btn,params);
        }

        //cetak barang flashsale
        for (int i=0;i<3;i++){
            //ambil linear layout flash sale dari fragment home
            LinearLayout tampung=myview.findViewById(R.id.containerflashsale);
            //tambah tulisan
            TextView tulis=new TextView(myview.getContext());
            if (i==0){
                tulis.setText("Pemberi Jasa : Alfonsus\nDurasi : 05:59:00\nHarga:Rp 20.000");
            }else if (i==1){
                tulis.setText("Pemberi Jasa : Edwin\nDurasi : 07:59:00\nHarga:Rp 50.000");
            }else if (i==2){
                tulis.setText("Pemberi Jasa : Cosmas\nDurasi : 09:59:00\nHarga:Rp 100.000");
            }
            tulis.setTypeface(null,Typeface.BOLD);
            tampung.addView(tulis);

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
            if (i==0){
                btn.setBackgroundResource(R.drawable.bike);
            }else if (i==1){
                btn.setBackgroundResource(R.drawable.treadmill);
            }else if (i==2){
                btn.setBackgroundResource(R.drawable.electrictreadmill);
            }
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button x=(Button) view;
                    Toast.makeText(getActivity(), "barang flash sale Ke "+ (Integer.parseInt(x.getId()+"")+1), Toast.LENGTH_SHORT).show();
                }
            });
            tampung.addView(btn,params);
        }
        return myview;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.optionmenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()== R.id.chat){
            Toast.makeText(getActivity(),"chat",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
