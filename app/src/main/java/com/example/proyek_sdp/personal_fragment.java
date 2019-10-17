package com.example.proyek_sdp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class personal_fragment extends Fragment {
    ImageView profil;
    Button btnktp;
    ImageView fotoktp;
    TextView cekktp;
    Button topup;
    Bitmap passing_gambar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View myview=inflater.inflate(R.layout.fragment_personal,container,false);
        profil=myview.findViewById(R.id.profil_user);
        btnktp=myview.findViewById(R.id.btnktp);
        fotoktp=myview.findViewById(R.id.ktp);
        cekktp=myview.findViewById(R.id.cekktp);
        topup=myview.findViewById(R.id.topup);
        Button btnBuyer = myview.findViewById(R.id.buyer);
        Button btnSeller = myview.findViewById(R.id.seller);
        /*
        if (getArguments().getString("data").toString().equals(null)){
            String[]user=getArguments().getString("data").toString().split("-");
            Toast.makeText(getActivity(), user[0], Toast.LENGTH_SHORT).show();
        }
         */
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent change=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(change, 1);
            }
        });
        btnktp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentCamera, 2);
            }
        });
        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move=new Intent(getActivity(),topup_activity.class);
                startActivity(move);
            }
        });
        fotoktp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Convert to byte array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                passing_gambar.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Intent in1 = new Intent(getContext(), image_viewer.class);
                in1.putExtra("image",byteArray);
                startActivity(in1);
            }
        });
        btnBuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),history_pembeli.class);
                startActivity(i);
            }
        });
        btnSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),history_penjual.class);
                startActivity(i);
            }
        });
        return myview;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && data!=null){
            Uri selected_image=data.getData();
            profil.setImageURI(selected_image);
        }else if(requestCode == 2 && data!=null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            fotoktp.setImageBitmap(bitmap);
            passing_gambar=bitmap;
        }
    }
}
