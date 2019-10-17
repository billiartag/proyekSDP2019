package com.example.proyek_sdp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu_topup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.close){
            finish();
        }
        return true;
    }
}
