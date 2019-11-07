package com.example.proyek_sdp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText email,password;
    Button register;
    Button login;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register=findViewById(R.id.btnregister_login);
        login=findViewById(R.id.btnlogin_login);
        email=findViewById(R.id.edemail_login);
        password=findViewById(R.id.edpassword_login);
        firebaseAuth=FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser =firebaseAuth.getCurrentUser();
                if (firebaseUser!=null){
                    Toast.makeText(Login.this, "Welcome, "+FirebaseAuth.getInstance().getCurrentUser().getEmail()+" !", Toast.LENGTH_SHORT).show();
                    Intent i =new Intent(getApplicationContext(),home.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(Login.this, "Tolong Login!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fp=new Intent(getApplicationContext(),register.class);
                startActivity(fp);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().equals("") && password.getText().toString().equals("")){
                    email.setError("Please Enter Your Email");
                    password.setError("Please Enter Your Password");
                }
                else if (email.getText().toString().equals("")){
                    email.setError("Please Enter Your Email");
                }
                else if (password.getText().toString().equals("")){
                    password.setError("Please Enter Your Email");
                }
                else {
                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(Login.this, "login gagal", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(Login.this, "login berhasil", Toast.LENGTH_SHORT).show();
                                Intent i =new Intent(getApplicationContext(),home.class);
                                startActivity(i);
                                finish();
                            }
                        }
                    });
                }
            }
        });
        ActionBar ab=getSupportActionBar();
        ab.setTitle("TitipAku");
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
