package com.example.proyek_sdp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class home_fragment extends Fragment {
    ArrayList<barang> kumpulanbarang = new ArrayList<barang>();
    ArrayList<barang> kumpulanpreorder = new ArrayList<barang>();
    ArrayList<barang> kumpulanlatestpreorder = new ArrayList<barang>();
    ArrayList<barang> kumpulanlatestflashsale= new ArrayList<barang>();
    ArrayList<user> kumpulanuser = new ArrayList<user>();
    ArrayList<voucher> kumpulanpromo = new ArrayList<voucher>();
    RecyclerView rv_topuser;
    RecyclerView rv_flashsale;
    RecyclerView rv_list_promo;
    RecyclerView rv_pre_order;
    RecyclerView rv_latest_pre_order;
    RecyclerView rv_latest_flashsale;
    DatabaseReference databaseReference;
    TextView seeLatestFS, seeLatestPO, seeFollowFS, seeFollowPO, seeSeller,seePromo;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View myview=inflater.inflate(R.layout.fragment_home,container,false);
        setHasOptionsMenu(true);
        rv_topuser=myview.findViewById(R.id.rv_topuser);
        rv_flashsale=myview.findViewById(R.id.rv_flashsale);
        rv_list_promo=myview.findViewById(R.id.rv_list_promo);
        rv_pre_order=myview.findViewById(R.id.rv_preorder_follow);
        rv_latest_flashsale=myview.findViewById(R.id.rv_latest_flashsale);
        rv_latest_pre_order=myview.findViewById(R.id.rv_latest_preorder);
        seeLatestFS=myview.findViewById(R.id.textViewLatestFlashsale);
        seeLatestPO=myview.findViewById(R.id.textViewLatestPreorder);
        seeFollowFS=myview.findViewById(R.id.textViewFlashsaleFollow);
        seeFollowPO=myview.findViewById(R.id.textViewAllPreorderFollow);
        seeSeller=myview.findViewById(R.id.textViewAllSeller);
        seePromo=myview.findViewById(R.id.textViewAllPromo);

        int item_depan = 5;

        //cetak promo
        FirebaseDatabase.getInstance().getReference().child("Voucher").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int ctr = 0;
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if(ds.child("status_promo").getValue().toString().equals("1")){
                        //check yang belum kedaluawarsa
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("y-M-d");
                        Boolean terlambat = true;
                        try {
                            Date tanggal_mulai = simpleDateFormat.parse(ds.child("mulai_promo").getValue().toString());
                            Date tanggal_selesai= simpleDateFormat.parse(ds.child("selesai_promo").getValue().toString());
                            Date dateSek= new Date();
                            Log.d("tanggal", simpleDateFormat.format(tanggal_selesai)+"---"+simpleDateFormat.format(dateSek));
                            if(tanggal_selesai.after(new Date())){
                                Log.d("tanggal bisa", simpleDateFormat.format(tanggal_selesai));
                                terlambat= false;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(!terlambat){
                            voucher x=new voucher();
                            x.setNama_promo(ds.child("nama_promo").getValue().toString());
                            x.setDeskripsi_promo(ds.child("deskripsi_promo").getValue().toString());
                            x.setDiskon_promo(ds.child("diskon_promo").getValue().toString());
                            x.setMulai_promo(ds.child("mulai_promo").getValue().toString());
                            x.setSelesai_promo(ds.child("selesai_promo").getValue().toString());
                            x.setStatus_promo(ds.child("status_promo").getValue().toString());
                            kumpulanpromo.add(x);
                            ctr++;
                            if(ctr>item_depan){break;}
                        }
                    }

                }
                rv_list_promo.setHasFixedSize(true);
                rv_list_promo.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
                ListPromoAdapter adapter = new ListPromoAdapter(getActivity(), kumpulanpromo);
                rv_list_promo.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //cetak icon top seller
        databaseReference= FirebaseDatabase.getInstance().getReference().child("UserDatabase");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count=dataSnapshot.getChildrenCount();
                final int[] ctr = {0};
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if(ds!=null){
                        if(!ds.child("email").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                            //cari rating
                            FirebaseDatabase.getInstance().getReference().child("RatingAndReviewDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                    long count=dataSnapshot2.getChildrenCount();
                                    int i=0;
                                    float hasil_rating=0;
                                    for (DataSnapshot ds2 :dataSnapshot2.getChildren()) {
                                        if(ds2.child("id_user").getValue().toString().equals(ds.child("email").getValue().toString())){
                                            i++;
                                            hasil_rating=hasil_rating+Float.parseFloat(ds2.child("rating").getValue().toString());
                                        }
                                    }
                                    //isi rating
                                    hasil_rating=hasil_rating/i;
                                    user baru=new user();
                                    baru.setId(ds.child("id").getValue().toString());
                                    baru.setVerifikasi_ktp(Integer.parseInt(ds.child("verifikasi_ktp").getValue().toString()));
                                    baru.setBirthdate(ds.child("birthdate").getValue().toString());
                                    baru.setEmail(ds.child("email").getValue().toString());
                                    baru.setPhone(ds.child("phone").getValue().toString());
                                    baru.setPassword(ds.child("password").getValue().toString());
                                    baru.setStatus(Integer.parseInt(ds.child("status").getValue().toString()));
                                    baru.setSaldo(Integer.parseInt(ds.child("saldo").getValue().toString()));
                                    if(String.valueOf(hasil_rating).equals("NaN")){
                                        baru.setRating(0);
                                    }
                                    else {
                                        baru.setRating(hasil_rating);
                                    }
                                    baru.setNama(ds.child("nama").getValue().toString());
                                    baru.setProfil_picture(Integer.parseInt(ds.child("profil_picture").getValue().toString()));
                                    baru.setAlamat(ds.child("alamat").getValue().toString());
                                    baru.setFirebase_user_id(ds.child("firebase_user_id").getValue().toString());
                                    databaseReference.child(ds.getKey()).setValue(baru);
                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                            long count=dataSnapshot.getChildrenCount();
                                            kumpulanuser.clear();
                                            for (DataSnapshot ds3 :dataSnapshot3.getChildren()) {
                                                if(ds3!=null){
                                                    if(!ds3.child("email").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                                                        user baru=new user();
                                                        baru.setId(ds3.child("id").getValue().toString());
                                                        baru.setVerifikasi_ktp(Integer.parseInt(ds3.child("verifikasi_ktp").getValue().toString()));
                                                        baru.setBirthdate(ds3.child("birthdate").getValue().toString());
                                                        baru.setEmail(ds3.child("email").getValue().toString());
                                                        baru.setPhone(ds3.child("phone").getValue().toString());
                                                        baru.setRating(Float.parseFloat(ds3.child("rating").getValue().toString()));
                                                        baru.setNama(ds3.child("nama").getValue().toString());
                                                        baru.setProfil_picture(Integer.parseInt(ds3.child("profil_picture").getValue().toString()));
                                                        baru.setAlamat(ds.child("alamat").getValue().toString());
                                                        baru.setFirebase_user_id(ds.child("firebase_user_id").getValue().toString());
                                                        kumpulanuser.add(baru);
                                                    }
                                                }
                                            }
                                            Collections.sort(kumpulanuser,user.sortdescrating);
                                            rv_topuser.setHasFixedSize(true);
                                            rv_topuser.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
                                            TopuserAdapter adaptertopuser = new TopuserAdapter(getActivity(), kumpulanuser);
                                            adaptertopuser.setOnItemClickListener(new OnItemClickListener() {
                                                @Override
                                                public void onClick(View view, int position) {
                                                    user x=kumpulanuser.get(position);
                                                    Bundle b = new Bundle();
                                                    b.putSerializable("user", x);
                                                    Intent intent = new Intent(getActivity(), detailprofil.class);
                                                    intent.putExtras(b);
                                                    startActivity(intent);
                                                }
                                            });
                                            rv_topuser.setAdapter(adaptertopuser);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            ctr[0]++;
                            if(ctr[0] >item_depan){
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //flash sale
        FirebaseDatabase.getInstance().getReference().child("FollowDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final int[] ctr = {0};
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if(ds.child("id_user").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        FirebaseDatabase.getInstance().getReference().child("BarangDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                for (DataSnapshot ds2 :dataSnapshot2.getChildren()) {
                                    if (!FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(ds2.child("idpenjual").getValue().toString())){
                                        if(ds2.child("jenis").getValue().toString().equals("Flash Sale") && ds2.child("idpenjual").getValue().toString().equals(ds.child("following").getValue().toString()) && ds2.child("status").getValue().toString().equals("1")){
                                            barang data=new barang();
                                            data.setId(ds2.child("id").getValue().toString());
                                            data.setDeskripsi(ds2.child("deskripsi").getValue().toString());
                                            data.setIdpenjual(ds2.child("idpenjual").getValue().toString());
                                            data.setJenis(ds2.child("jenis").getValue().toString());
                                            data.setNama(ds2.child("nama").getValue().toString());
                                            data.setLokasi(ds2.child("lokasi").getValue().toString());
                                            data.setVarian(ds2.child("varian").getValue().toString());
                                            data.setMaksimal(Integer.parseInt(ds2.child("maksimal").getValue().toString()));
                                            data.setWaktu_selesai(ds2.child("waktu_selesai").getValue().toString());
                                            data.setWaktu_mulai(ds2.child("waktu_mulai").getValue().toString());
                                            data.setWaktu_upload(ds2.child("waktu_upload").getValue().toString());
                                            data.setHarga(Integer.parseInt(ds2.child("harga").getValue().toString()));
                                            data.setKategori(ds2.child("kategori").getValue().toString());
                                            data.setBerat(Integer.parseInt(ds2.child("berat").getValue().toString()));
                                            data.setStatus(Integer.parseInt(ds2.child("status").getValue().toString()));
                                            kumpulanbarang.add(data);
                                            ctr[0]++;
                                            if(ctr[0] >item_depan){
                                                break;
                                            }
                                        }
                                    }
                                }
                                //cetak barang flashsale
                                rv_flashsale.setHasFixedSize(true);
                                rv_flashsale.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
                                TopFlashSaleHomeAdapter adapterflashsale = new TopFlashSaleHomeAdapter(getActivity(), kumpulanbarang);
                                adapterflashsale.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onClick(View view, int position) {
                                        barang x = kumpulanbarang.get(position);
                                        Bundle b = new Bundle();
                                        b.putSerializable("barang", x);
                                        Intent intent = new Intent(getActivity(), detail_feed.class);
                                        intent.putExtras(b);
                                        startActivity(intent);
                                    }
                                });
                                rv_flashsale.setAdapter(adapterflashsale);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //cetak pre order
        FirebaseDatabase.getInstance().getReference().child("FollowDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            int ctr=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if(ds.child("id_user").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        FirebaseDatabase.getInstance().getReference().child("BarangDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                for (DataSnapshot ds2 :dataSnapshot2.getChildren()) {
                                    if (!FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(ds2.child("idpenjual").getValue().toString())){
                                        if(ds2.child("jenis").getValue().toString().equals("Pre Order") && ds2.child("idpenjual").getValue().toString().equals(ds.child("following").getValue().toString()) && ds2.child("status").getValue().toString().equals("1")){
                                            barang data=new barang();
                                            data.setId(ds2.child("id").getValue().toString());
                                            data.setDeskripsi(ds2.child("deskripsi").getValue().toString());
                                            data.setIdpenjual(ds2.child("idpenjual").getValue().toString());
                                            data.setJenis(ds2.child("jenis").getValue().toString());
                                            data.setNama(ds2.child("nama").getValue().toString());
                                            data.setLokasi(ds2.child("lokasi").getValue().toString());
                                            data.setVarian(ds2.child("varian").getValue().toString());
                                            data.setMaksimal(Integer.parseInt(ds2.child("maksimal").getValue().toString()));
                                            data.setWaktu_mulai(ds2.child("waktu_mulai").getValue().toString());
                                            data.setWaktu_selesai(ds2.child("waktu_selesai").getValue().toString());
                                            data.setWaktu_upload(ds2.child("waktu_upload").getValue().toString());
                                            data.setHarga(Integer.parseInt(ds2.child("harga").getValue().toString()));
                                            data.setKategori(ds2.child("kategori").getValue().toString());
                                            data.setBerat(Integer.parseInt(ds2.child("berat").getValue().toString()));
                                            data.setStatus(Integer.parseInt(ds2.child("status").getValue().toString()));
                                            kumpulanpreorder.add(data);
                                            ctr++;
                                            if(ctr>item_depan){
                                                break;
                                            }
                                        }
                                    }
                                }
                                //cetak barang flashsale
                                rv_pre_order.setHasFixedSize(true);
                                rv_pre_order.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
                                TopFlashSaleHomeAdapter adapterflashsale = new TopFlashSaleHomeAdapter(getActivity(), kumpulanpreorder);
                                adapterflashsale.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onClick(View view, int position) {
                                        barang x = kumpulanpreorder.get(position);
                                        Bundle b = new Bundle();
                                        b.putSerializable("barang", x);
                                        Intent intent = new Intent(getActivity(), detail_feed.class);
                                        intent.putExtras(b);
                                        startActivity(intent);
                                    }
                                });
                                rv_pre_order.setAdapter(adapterflashsale);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //flashsale latest
        FirebaseDatabase.getInstance().getReference().child("BarangDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int ctr=0;
                for (DataSnapshot row:dataSnapshot.getChildren()) {
                    if(row.child("jenis").getValue().toString().equalsIgnoreCase("Flash Sale")&&row.child("status").getValue().toString().equals("1")&&!row.child("idpenjual").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        barang data=new barang();
                        data.setId(row.child("id").getValue().toString());
                        data.setDeskripsi(row.child("deskripsi").getValue().toString());
                        data.setIdpenjual(row.child("idpenjual").getValue().toString());
                        data.setJenis(row.child("jenis").getValue().toString());
                        data.setNama(row.child("nama").getValue().toString());
                        data.setLokasi(row.child("lokasi").getValue().toString());
                        data.setVarian(row.child("varian").getValue().toString());
                        data.setMaksimal(Integer.parseInt(row.child("maksimal").getValue().toString()));
                        data.setWaktu_selesai(row.child("waktu_selesai").getValue().toString());
                        data.setWaktu_mulai(row.child("waktu_mulai").getValue().toString());
                        data.setWaktu_upload(row.child("waktu_upload").getValue().toString());
                        data.setHarga(Integer.parseInt(row.child("harga").getValue().toString()));
                        data.setKategori(row.child("kategori").getValue().toString());
                        data.setBerat(Integer.parseInt(row.child("berat").getValue().toString()));
                        data.setStatus(Integer.parseInt(row.child("status").getValue().toString()));
                        kumpulanlatestflashsale.add(data);
                        ctr++;
                        if(ctr>item_depan){
                            break;
                        }
                        Log.d("ctr",ctr+"");
                    }
                }

                Collections.sort(kumpulanlatestflashsale,barang.sortdescwaktu);
                //cetak barang flashsale
                rv_latest_flashsale.setHasFixedSize(true);
                rv_latest_flashsale.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
                TopFlashSaleHomeAdapter adapterflashsale = new TopFlashSaleHomeAdapter(getActivity(), kumpulanlatestflashsale);
                adapterflashsale.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        barang x = kumpulanlatestflashsale.get(position);
                        Bundle b = new Bundle();
                        b.putSerializable("barang", x);
                        Intent intent = new Intent(getActivity(), detail_feed.class);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });
                rv_latest_flashsale.setAdapter(adapterflashsale);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //preorder latest
        FirebaseDatabase.getInstance().getReference().child("BarangDatabase").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int ctr=0;
                for (DataSnapshot row:dataSnapshot.getChildren()) {
                    if(row.child("jenis").getValue().toString().equalsIgnoreCase("Pre Order")&&row.child("status").getValue().toString().equals("1")&&!row.child("idpenjual").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        barang data=new barang();
                        data.setId(row.child("id").getValue().toString());
                        data.setDeskripsi(row.child("deskripsi").getValue().toString());
                        data.setIdpenjual(row.child("idpenjual").getValue().toString());
                        data.setJenis(row.child("jenis").getValue().toString());
                        data.setNama(row.child("nama").getValue().toString());
                        data.setLokasi(row.child("lokasi").getValue().toString());
                        data.setVarian(row.child("varian").getValue().toString());
                        data.setMaksimal(Integer.parseInt(row.child("maksimal").getValue().toString()));
                        data.setWaktu_selesai(row.child("waktu_selesai").getValue().toString());
                        data.setWaktu_mulai(row.child("waktu_mulai").getValue().toString());
                        data.setWaktu_upload(row.child("waktu_upload").getValue().toString());
                        data.setHarga(Integer.parseInt(row.child("harga").getValue().toString()));
                        data.setKategori(row.child("kategori").getValue().toString());
                        data.setBerat(Integer.parseInt(row.child("berat").getValue().toString()));
                        data.setStatus(Integer.parseInt(row.child("status").getValue().toString()));
                        kumpulanlatestpreorder.add(data);
                        ctr++;
                        if(ctr>item_depan){break;}
                    }
                }

                Collections.sort(kumpulanlatestflashsale,barang.sortdesctanggal);
                //cetak barang preorder
                rv_latest_pre_order.setHasFixedSize(true);
                rv_latest_pre_order.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
                TopFlashSaleHomeAdapter adapterflashsale = new TopFlashSaleHomeAdapter(getActivity(), kumpulanlatestpreorder);
                adapterflashsale.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        barang x = kumpulanlatestpreorder.get(position);
                        Bundle b = new Bundle();
                        b.putSerializable("barang", x);
                        Intent intent = new Intent(getActivity(), detail_feed.class);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });
                rv_latest_pre_order.setAdapter(adapterflashsale);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //link see all
        seeFollowFS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),SeeFollowFS.class);
                startActivity(i);
            }
        });
        seeFollowPO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),SeeFollowPO.class);
                startActivity(i);
            }
        });
        seeLatestFS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),SeeLatestFS.class);
                startActivity(i);
            }
        });
        seeLatestPO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),SeeLatestPO.class);
                startActivity(i);
            }
        });
        seeSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),SeeSeller.class);
                startActivity(i);
            }
        });
        seePromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),SeePromo.class);
                startActivity(i);
            }
        });
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
            Intent i = new Intent(getContext(), wishlist_activity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
