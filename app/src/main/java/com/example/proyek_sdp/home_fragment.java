package com.example.proyek_sdp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class home_fragment extends Fragment {
    ArrayList<barang> kumpulanbarang = new ArrayList<barang>();
    ArrayList<user> kumpulanuser = new ArrayList<user>();
    RecyclerView rv_topuser;
    RecyclerView rv_flashsale;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View myview=inflater.inflate(R.layout.fragment_home,container,false);
        setHasOptionsMenu(true);
        rv_topuser=myview.findViewById(R.id.rv_topuser);
        rv_flashsale=myview.findViewById(R.id.rv_flashsale);

        //cetak icon top seller
        kumpulanuser.add(new user("edwin sidharta","12345678","etern1ty430","edwin0sidharta@gmail.com","081331322570","11/12/2000","ADMIN",3,R.drawable.img1));
        kumpulanuser.add(new user("edwin sidharta","12345678","etern1ty430","edwin0sidharta@gmail.com","081331322570","11/12/2000","ADMIN",3,R.drawable.img1));
        kumpulanuser.add(new user("edwin sidharta","12345678","etern1ty430","edwin0sidharta@gmail.com","081331322570","11/12/2000","ADMIN",3,R.drawable.img1));
        kumpulanuser.add(new user("edwin sidharta","12345678","etern1ty430","edwin0sidharta@gmail.com","081331322570","11/12/2000","ADMIN",3,R.drawable.img1));
        kumpulanuser.add(new user("edwin sidharta","12345678","etern1ty430","edwin0sidharta@gmail.com","081331322570","11/12/2000","ADMIN",3,R.drawable.img1));
        kumpulanuser.add(new user("edwin sidharta","12345678","etern1ty430","edwin0sidharta@gmail.com","081331322570","11/12/2000","ADMIN",3,R.drawable.img1));
        rv_topuser.setHasFixedSize(true);
        rv_topuser.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        TopuserAdapter adaptertopuser = new TopuserAdapter(getActivity(), kumpulanuser);
        adaptertopuser.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                user x=kumpulanuser.get(position);
                Bundle b = new Bundle();
                //b.putSerializable("user", x);
                Intent intent = new Intent(getActivity(), detailprofil.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        rv_topuser.setAdapter(adaptertopuser);

        kumpulanbarang.add(new barang("Treadmill",25000,"bagus untuk kesehatan","05:45:21","Flash Sale",R.drawable.treadmill,"Cosmas",12));
        kumpulanbarang.add(new barang("electronic Treadmill",55000,"bagus untuk kesehatan dan otot kaki","07:45:21","Pre Order",R.drawable.electrictreadmill,"Alfon",14));
        kumpulanbarang.add(new barang("Bike",75000,"bagus untuk kesehatan dan mudah di pakai tanpa keluar rumah","08:45:21","Flash Sale",R.drawable.bike,"Edwin",6));
        //cetak barang flashsale
        rv_flashsale.setHasFixedSize(true);
        rv_flashsale.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        TopFlashSaleHome adapterflashsale = new TopFlashSaleHome(getActivity(), kumpulanbarang);
        adapterflashsale.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                barang x = kumpulanbarang.get(position);
                Toast.makeText(getActivity(), x.getGambar()+"", Toast.LENGTH_SHORT).show();
                Bundle b = new Bundle();
                b.putSerializable("barang", x);
                Intent intent = new Intent(getActivity(), detail_feed.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        rv_flashsale.setAdapter(adapterflashsale);
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
        else if(item.getItemId()==R.id.wishlist){
            Intent i = new Intent(getContext(),wishlist.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
