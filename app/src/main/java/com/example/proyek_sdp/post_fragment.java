package com.example.proyek_sdp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class post_fragment extends Fragment {
    ImageView gambarpost;
    Spinner jenis;
    String[] isidata={
            "Flash Sale",
            "Pre Order"
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myview=inflater.inflate(R.layout.fragment_post,container,false);;
        gambarpost=myview.findViewById(R.id.fotopost);
        jenis=myview.findViewById(R.id.spinner2);
        ArrayAdapter<String> adap=new ArrayAdapter<String>(getContext(),R.layout.custom_spinner,isidata);
        jenis.setAdapter(adap);
        gambarpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent change=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(change, 1);
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
