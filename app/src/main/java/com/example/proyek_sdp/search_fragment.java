package com.example.proyek_sdp;

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

public class search_fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View myview=inflater.inflate(R.layout.fragment_search,container,false);
        for (int i=0;i<3;i++){
            //ambil linear layout flash sale dari fragment home
            LinearLayout tampung=myview.findViewById(R.id.containerfeed);
            //tambah tulisan
            TextView tulis=new TextView(myview.getContext());
            if (i==0){
                tulis.setText("Flash Sale : \nPemberi Jasa : Alfonsus\nDurasi : 05:59:00\nHarga:Rp 20.000");
            }else if (i==1){
                tulis.setText("Pre Order : \nPemberi Jasa : Edwin");
            }else if (i==2){
                tulis.setText("Flash Sale : \nPemberi Jasa : Cosmas\nDurasi : 09:59:00\nHarga:Rp 100.000");
            }
            tulis.setTypeface(null, Typeface.BOLD);
            tulis.setGravity(Gravity.CENTER);
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
        inflater.inflate(R.menu.optionmenu_search, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
