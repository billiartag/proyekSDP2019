package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;

import java.util.Calendar;

public class register extends AppCompatActivity {

    Button register;
    Button login;
    Button btnchoosedate;
    EditText name;
    EditText username;
    EditText email;
    EditText phone;
    TextView birthday;
    EditText password;
    EditText confirmpassword;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register=findViewById(R.id.btnregister_register);
        login=findViewById(R.id.btnlogin_register);
        name=findViewById(R.id.edname_register);
        username=findViewById(R.id.edusername_register);
        email=findViewById(R.id.edemail_register);
        phone=findViewById(R.id.edphone_register);
        birthday=findViewById(R.id.hasil_tanggal_register);
        password=findViewById(R.id.edpassword_register);
        confirmpassword=findViewById(R.id.edconfirm_register);
        btnchoosedate=findViewById(R.id.btnbirth_register);

        firebaseAuth=FirebaseAuth.getInstance();
        btnchoosedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        register.this::onDateSet,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
                dpd.show(getSupportFragmentManager(), "Datepickerdialog");
            }
        });

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
                boolean berhasil=true;
                int ctr=0;
                if (name.getText().toString().trim().equals("")){
                    name.setError("Nama Tidak Boleh Kosong");
                    berhasil=false;
                }
                if (username.getText().toString().trim().equals("") || username.getText().toString().length()>8){
                    username.setError("username Tidak Boleh Kosong dan username tidak boleh lebih dari 8 huruf");
                    berhasil=false;
                }
                if (!isEmailValid(email.getText().toString())){
                    email.setError("email harus sesuai dengan format email");
                    berhasil=false;
                }
                if (phone.getText().toString().trim().equals("")||phone.getText().toString().length()<11 && phone.getText().toString().length()>14){
                    phone.setError("phone Tidak Boleh Kosong dan phone tidak kurang 11 atau lebih dari 14 huruf");
                    berhasil=false;
                }
                if (birthday.getText().toString().equals("")){
                    birthday.setError("birthday Tidak Boleh Kosong");
                    berhasil=false;
                }
                if (password.getText().toString().trim().equals("")||password.getText().toString().length()<8||!password.getText().toString().equals(confirmpassword.getText().toString())){
                    password.setError("password Tidak Boleh Kosong atau kurang dari 8 huruf atau Password tidak sama dengan confirm password");
                    confirmpassword.setError("password Tidak Boleh Kosong atau kurang dari 8 huruf atau Password tidak sama dengan confirm password");
                    berhasil=false;
                }

                if (berhasil){
                    Toast.makeText(register.this, "Register Berhasil", Toast.LENGTH_SHORT).show();
                    user baru=new user();
                    baru.nama=name.getText().toString();
                    baru.username=username.getText().toString();
                    baru.email=email.getText().toString();
                    baru.phone=phone.getText().toString();
                    baru.birthdate=birthday.getText().toString();
                    baru.password=password.getText().toString();

                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(register.this, "register gagal, coba ulang", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(register.this, "register berhasil", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Login.class));
                            }
                        }
                    });

                    Intent fp=new Intent(getApplicationContext(),home.class);
                    startActivity(fp);
                    finish();
                }
                else {
                    Toast.makeText(register.this, "Register Gagal!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ActionBar ab=getSupportActionBar();
        ab.setTitle("TitipAku");
    }
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String time = ""+hourOfDay+"h"+minute+"m"+second;
        birthday.setText(time);
    }

    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = ""+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        birthday.setText(date);
    }
    public boolean isEmailValid(CharSequence Email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches();
    }
}
