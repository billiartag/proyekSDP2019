package com.example.proyek_sdp;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.Serializable;

public class personal_fragment extends Fragment {
    ImageView profil;
    Button btnktp;
    ImageView fotoktp;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myview=inflater.inflate(R.layout.fragment_personal,container,false);
        profil=myview.findViewById(R.id.profil_user);
        btnktp=myview.findViewById(R.id.btnktp);
        fotoktp=myview.findViewById(R.id.ktp);
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
        }
    }
}
