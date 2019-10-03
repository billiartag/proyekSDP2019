package com.example.proyek_sdp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class register extends AppCompatActivity {

    Button register;
    Button login;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    EditText name;
    EditText username;
    EditText email;
    EditText phone;
    EditText postal;
    EditText birthday;
    EditText password;
    EditText confirmpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register=findViewById(R.id.register);
        login=findViewById(R.id.login);
        name=findViewById(R.id.name);
        username=findViewById(R.id.username);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        birthday=findViewById(R.id.birthdate);
        postal=findViewById(R.id.postal);
        password=findViewById(R.id.password);
        confirmpassword=findViewById(R.id.confirm_password);
        //start game
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fp=new Intent(getApplicationContext(),Login.class);
                startActivity(fp);
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String validation="";
                boolean berhasil=true;
                int ctr=0;
                if (name.getText().toString().trim().equals("")){
                    ctr++;
                    validation=validation+ctr+". Nama Tidak Boleh Kosong\n";
                    berhasil=false;
                }
                if (username.getText().toString().trim().equals("") || username.getText().toString().length()>8){
                    ctr++;
                    validation=validation+ctr+". username Tidak Boleh Kosong dan username tidak boleh lebih dari 8 huruf\n";
                    berhasil=false;
                }
                if (!isEmailValid(email.getText().toString())){
                    ctr++;
                    validation=validation+ctr+". email harus sesuai dengan format email\n";
                    berhasil=false;
                }
                if (phone.getText().toString().trim().equals("")||phone.getText().toString().length()<11 && phone.getText().toString().length()>14){
                    ctr++;
                    validation=validation+ctr+". phone Tidak Boleh Kosong dan phone tidak kurang 11 atau lebih dari 14 huruf\n";
                    berhasil=false;
                }
                if (birthday.getText().toString().equals("")){
                    ctr++;
                    validation=validation+ctr+". birthday Tidak Boleh Kosong \n";
                    berhasil=false;
                }
                if (postal.getText().toString().equals("")){
                    ctr++;
                    validation=validation+ctr+". Postal Tidak Boleh Kosong \n";
                    berhasil=false;
                }
                if (password.getText().toString().trim().equals("")||password.getText().toString().length()<8||!password.getText().toString().equals(confirmpassword.getText().toString())){
                    ctr++;
                    validation=validation+ctr+". password Tidak Boleh Kosong atau kurang dari 8 huruf atau Password tidak sama dengan confirm password\n";
                    berhasil=false;
                }

                if (berhasil){
                    Toast.makeText(register.this, "Register Berhasil", Toast.LENGTH_SHORT).show();
                    /*
                    user baru=new user();
                    baru.nama=name.getText().toString();
                    baru.username=username.getText().toString();
                    baru.email=email.getText().toString();
                    baru.phone=phone.getText().toString();
                    baru.postal=postal.getText().toString();
                    baru.birthdate=birthday.getText().toString();
                    baru.passsword=password.getText().toString();git remote add origin https://github.com/billiartag/proyekSDP2019.git

                    fp.putExtra("data",name.getText().toString()+"-"+username.getText().toString()+"-"+email.getText().toString()+"-"+phone.getText().toString()+"-"+postal.getText().toString()+"-"+birthday.getText().toString()+"-"+password.getText().toString());

                     */
                    Intent fp=new Intent(getApplicationContext(),home.class);
                    startActivity(fp);
                    finish();
                }
                else {
                    Toast.makeText(register.this, validation, Toast.LENGTH_SHORT).show();
                }

            }
        });
        ActionBar ab=getSupportActionBar();
        ab.setTitle("TitipAku");
    }
    public boolean isEmailValid(CharSequence Email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches();
    }
}
