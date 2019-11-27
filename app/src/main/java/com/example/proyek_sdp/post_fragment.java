package com.example.proyek_sdp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class post_fragment extends Fragment {
    ImageView gambarpost;
    Button post,lokasi_post;
    EditText judul;
    EditText lokasi;
    EditText Varian;
    EditText max;
    EditText deskripsi;
    EditText harga;
    TextView time_dari,time_ke,tvharga;
    Spinner jenis;
    String[] isidata={
            "Flash Sale",
            "Pre Order"
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myview=inflater.inflate(R.layout.fragment_post,container,false);
        setHasOptionsMenu(true);
        gambarpost=myview.findViewById(R.id.fotopost);
        judul=myview.findViewById(R.id.judul_post);
        lokasi=myview.findViewById(R.id.lokasi_post);
        Varian=myview.findViewById(R.id.varian_post);
        max=myview.findViewById(R.id.max_post);
        time_dari=myview.findViewById(R.id.time_dari_post);
        time_ke=myview.findViewById(R.id.time_ke_post);
        post=myview.findViewById(R.id.post);
        jenis=myview.findViewById(R.id.jenis_post);
        deskripsi=myview.findViewById(R.id.eddeskripsi_post);
        harga=myview.findViewById(R.id.ed_harga_post);
        tvharga=myview.findViewById(R.id.tv_harga_post);

        lokasi_post=myview.findViewById(R.id.btn_lokasi_post);
        lokasi.setFocusable(false);
        //program
        ArrayAdapter<String> adap=new ArrayAdapter<String>(getContext(),R.layout.custom_spinner,isidata);
        jenis.setAdapter(adap);
        if(!((home)getActivity()).lokasi.equals("")){
            judul.setText(((home)getActivity()).judul);
            lokasi.setText(((home)getActivity()).lokasi);
            Varian.setText(((home)getActivity()).varian);
            max.setText(((home)getActivity()).max);
            time_dari.setText(((home)getActivity()).time_dari);
            time_ke.setText(((home)getActivity()).time_ke);
            if(((home)getActivity()).jenis.equals("Pre Order")){
                jenis.setSelection(1);
            }
            else {
                jenis.setSelection(0);
            }
        }
        lokasi_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(jenis.getSelectedItem().toString().equals("Pre Order")){
                    Intent move=new Intent(getContext(), google_maps_search_location.class);
                    move.putExtra("judul",judul.getText().toString());
                    move.putExtra("jenis",jenis.getSelectedItem().toString());
                    move.putExtra("varian",Varian.getText().toString());
                    move.putExtra("max",max.getText().toString());
                    move.putExtra("time_dari",time_dari.getText().toString());
                    move.putExtra("time_ke",time_ke.getText().toString());
                    startActivity(move);
                }
                else {
                    Intent move=new Intent(getContext(), google_maps_current_location.class);
                    move.putExtra("judul",judul.getText().toString());
                    move.putExtra("jenis",jenis.getSelectedItem().toString());
                    move.putExtra("varian",Varian.getText().toString());
                    move.putExtra("max",max.getText().toString());
                    move.putExtra("time_dari",time_dari.getText().toString());
                    move.putExtra("time_ke",time_ke.getText().toString());
                    startActivity(move);
                }
            }
        });
        jenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(jenis.getSelectedItem().toString().equals("Pre Order")){
                    if(((home)getActivity()).lokasi.equals("")){
                        judul.setText("");
                        lokasi.setText("");
                        Varian.setText("");
                        max.setText("");
                        time_dari.setText("");;
                        time_ke.setText("");
                    }
                    if(((home)getActivity()).time_ke.equals("")){
                        time_dari.setText("");
                    }
                    time_dari.setHint("Pilih Awal Tanggal");
                    time_dari.setEnabled(true);
                    if(((home)getActivity()).time_ke.equals("")){
                        time_ke.setText("");
                    }
                    harga.setVisibility(View.INVISIBLE);
                    tvharga.setVisibility(View.INVISIBLE);

                    time_ke.setHint("Pilih Akhir Tanggal");
                    judul.setError(null);
                    lokasi.setError(null);
                    Varian.setError(null);
                    max.setError(null);
                    time_ke.setError(null);
                    time_dari.setError(null);
                    ((home)getActivity()).judul="";
                    ((home)getActivity()).lokasi="";
                    ((home)getActivity()).varian="";
                    ((home)getActivity()).max="";
                    ((home)getActivity()).time_dari="";
                    ((home)getActivity()).time_ke="";
                }
                else {

                    if(((home)getActivity()).lokasi.equals("")){
                        judul.setText("");
                        lokasi.setText("");
                        Varian.setText("");
                        max.setText("");
                        time_dari.setText("");;
                        time_ke.setText("");
                    }
                    Calendar now = Calendar.getInstance();
                    time_dari.setText(now.get(Calendar.HOUR)+":"+now.get(Calendar.MINUTE));
                    time_dari.setEnabled(false);
                    if(((home)getActivity()).time_ke.equals("")){
                        time_ke.setText("");
                    }
                    harga.setVisibility(View.VISIBLE);
                    tvharga.setVisibility(View.VISIBLE);
                    time_ke.setHint("Pilih Akhir Jam");
                    judul.setError(null);
                    lokasi.setError(null);
                    Varian.setError(null);
                    max.setError(null);
                    time_ke.setError(null);
                    time_dari.setError(null);
                    ((home)getActivity()).judul="";
                    ((home)getActivity()).lokasi="";
                    ((home)getActivity()).varian="";
                    ((home)getActivity()).max="";
                    ((home)getActivity()).time_dari="";
                    ((home)getActivity()).time_ke="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        gambarpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent change=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(change, 1);
            }
        });
        time_dari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        post_fragment.this::onDateSet,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
                time_dari.setError(null);
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        time_ke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(jenis.getSelectedItem().toString().equals("Flash Sale")){
                    Calendar now = Calendar.getInstance();
                    TimePickerDialog dpd = TimePickerDialog.newInstance(
                            post_fragment.this::onTimeSet,
                            now.get(Calendar.HOUR),
                            now.get(Calendar.MINUTE),
                            true
                    );
                    time_ke.setError(null);
                    dpd.show(getFragmentManager(), "TimePickerDialog");
                }
                else {
                    Calendar now = Calendar.getInstance();
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            post_fragment.this::onDateSet1,
                            now.get(Calendar.YEAR), // Initial year selection
                            now.get(Calendar.MONTH), // Initial month selection
                            now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                    );
                    dpd.show(getFragmentManager(), "Datepickerdialog");
                }
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean berhasil=true;
                if (judul.getText().toString().trim().equals("")){
                    judul.setError("judul kosong !");
                    berhasil=false;
                }
                if (lokasi.getText().toString().trim().equals("")){
                    lokasi.setError("lokasi kosong !");
                    berhasil=false;
                }
                if (Varian.getText().toString().trim().equals("")){
                    Varian.setError("Varian kosong !");
                    berhasil=false;
                }
                if (max.getText().toString().trim().equals("")){
                    max.setError("max kosong !");
                    berhasil=false;
                }
                if (time_dari.getText().toString().trim().equals("")){
                    time_dari.setError("time_dari kosong !");
                    berhasil=false;
                }
                if (time_ke.getText().toString().trim().equals("")){
                    time_ke.setError("time_ke kosong !");
                    berhasil=false;
                }
                if(berhasil){
                    Toast.makeText(getContext(), "post berhasil di buat", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return myview;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater=getActivity().getMenuInflater();
        inflater.inflate(R.menu.optionmenu_profil, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.reminder){
            //Toast.makeText(getContext(), "ini reminder", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getContext(),reminder.class);
            startActivity(i);
        }
        else if (item.getItemId()==R.id.top_up){
            Intent move=new Intent(getActivity(),topup_activity.class);
            startActivity(move);
        }
        else if(item.getItemId()==R.id.hsell){
            Intent i = new Intent(getActivity(),history_penjual.class);
            startActivity(i);
        }
        else if(item.getItemId()==R.id.hbuy){
            Intent i = new Intent(getActivity(),history_pembeli.class);
            startActivity(i);
        }
        else if(item.getItemId()==R.id.logout){
            FirebaseAuth.getInstance().signOut();
            getActivity().finish();
            Intent i = new Intent(getActivity(),Login.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && data!=null){
            Uri selected_image=data.getData();
            gambarpost.setImageURI(selected_image);
        }
    }

    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = ""+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        time_dari.setText(date);
    }
    public void onDateSet1(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = ""+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        time_ke.setText(date);
    }

    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String time = hourOfDay+":"+minute;
        time_ke.setText(time);
    }
}
