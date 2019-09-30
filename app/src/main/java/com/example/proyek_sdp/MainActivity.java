package com.example.proyek_sdp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ImageButton btncontinue;
    ProgressBar load;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "Test kosmos", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_main);
        btncontinue=findViewById(R.id.imageButton6);
        load=findViewById(R.id.progressBar4);
        btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btncontinue.setVisibility(View.INVISIBLE);
                load.setVisibility(View.VISIBLE);
                new CountDownTimer(3000, 1000) {

                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        Intent fp=new Intent(getApplicationContext(),Login.class);
                        startActivity(fp);
                        finish();
                    }
                }.start();
            }
        });

    }
}
