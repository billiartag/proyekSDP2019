package com.example.proyek_sdp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class image_viewer extends AppCompatActivity {
    ImageView img;
    Bitmap chg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        img=findViewById(R.id.change_img);
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        chg = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        img.setImageBitmap(chg);
        ActionBar ab=getSupportActionBar();
        ab.setTitle("TitipAku");
    }
}
