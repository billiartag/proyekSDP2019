package com.example.proyek_sdp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class post_fragment extends Fragment {
    ImageView gambarpost;
    Button post;
    EditText judul;
    EditText city;
    EditText region;
    EditText max;
    EditText time;
    Spinner jenis;
    String[] isidata={
            "Flash Sale",
            "Pre Order"
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myview=inflater.inflate(R.layout.fragment_post,container,false);
        gambarpost=myview.findViewById(R.id.fotopost);
        judul=myview.findViewById(R.id.judul);
        city=myview.findViewById(R.id.city);
        region=myview.findViewById(R.id.region);
        max=myview.findViewById(R.id.max);
        time=myview.findViewById(R.id.time);
        post=myview.findViewById(R.id.post);
        jenis=myview.findViewById(R.id.jenis);
        //program
        ArrayAdapter<String> adap=new ArrayAdapter<String>(getContext(),R.layout.custom_spinner,isidata);
        jenis.setAdapter(adap);
        gambarpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent change=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(change, 1);
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String validation="";
                if (judul.getText().toString().trim().equals("")){
                    validation=validation+"-judul yang dimasukkan kosong\n";
                }
                if (city.getText().toString().trim().equals("")){
                    validation=validation+"-city yang dimasukkan kosong\n";
                }
                if (region.getText().toString().trim().equals("")){
                    validation=validation+"-region yang dimasukkan kosong\n";
                }
                if (max.getText().toString().trim().equals("")){
                    validation=validation+"-max barang yang dimasukkan kosong\n";
                }
                if (time.getText().toString().trim().equals("")){
                    validation=validation+"-Waktu yang dimasukkan kosong\n";
                }
                if (!validation.equals("")){
                    Toast.makeText(getContext(), validation, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Post Berhasil", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return myview;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && data!=null){
            Uri selected_image=data.getData();
            gambarpost.setImageURI(selected_image);
        }
    }
}
