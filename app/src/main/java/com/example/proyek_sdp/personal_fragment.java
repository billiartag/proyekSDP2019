package com.example.proyek_sdp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;

public class personal_fragment extends Fragment {
    ImageView profil_picture_user;
    ImageView gambar_ktp_profil;
    EditText edusername_profil,edname_profil,edemail_profil,edtanggal_lahir_profil,ednotelp_profil;
    Button btn_change_profil,btn_edit_profil,btn_verifikasi_ktp;
    Bitmap passing_gambar;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myview=inflater.inflate(R.layout.fragment_personal,container,false);
        setHasOptionsMenu(true);
        profil_picture_user=myview.findViewById(R.id.picture_profil_user);
        edusername_profil=myview.findViewById(R.id.edusername_profil);
        edname_profil=myview.findViewById(R.id.edname_profil);
        edemail_profil=myview.findViewById(R.id.edemail_profil);
        edtanggal_lahir_profil=myview.findViewById(R.id.ed_tanggal_lahir_profil);
        btn_change_profil=myview.findViewById(R.id.btn_change_profil);
        btn_edit_profil=myview.findViewById(R.id.btn_edit_profil);
        btn_verifikasi_ktp=myview.findViewById(R.id.btn_verifikasi_ktp);
        gambar_ktp_profil=myview.findViewById(R.id.gambar_ktp_profil);
        ednotelp_profil=myview.findViewById(R.id.edphone_profil);
        edemail_profil.setEnabled(false);
        edname_profil.setEnabled(false);
        ednotelp_profil.setEnabled(false);
        edtanggal_lahir_profil.setEnabled(false);

        btn_change_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "ganti username berhasil", Toast.LENGTH_SHORT).show();
            }
        });
        btn_edit_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btn_edit_profil.getText().toString().toUpperCase().equals("EDIT")){
                    edname_profil.setEnabled(true);
                    ednotelp_profil.setEnabled(true);
                    btn_edit_profil.setText("SAVE");
                }
                else if(btn_edit_profil.getText().toString().toUpperCase().equals("SAVE")){
                    edname_profil.setEnabled(false);
                    ednotelp_profil.setEnabled(false);
                    btn_edit_profil.setText("EDIT");
                }
            }
        });
        profil_picture_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent change=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(change, 1);
            }
        });
        btn_verifikasi_ktp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentCamera, 2);
            }
        });
        gambar_ktp_profil.setOnClickListener(new View.OnClickListener() {
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
        return myview;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.optionmenu_profil, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.reminder){
            //Toast.makeText(getContext(), "ini reminder", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getContext(),reminder.class);
            startActivity(i);
        }
        else if (item.getItemId()==R.id.top_up){
            Intent move=new Intent(getActivity(),topup_activity.class);
            startActivity(move);
        }
        else if(item.getItemId()==R.id.hsell){
            Intent i = new Intent(getActivity(),history_penjual.class);
            startActivity(i);
        }
        else if(item.getItemId()==R.id.hbuy){
            Intent i = new Intent(getActivity(),history_pembeli.class);
            startActivity(i);
        }
        else if(item.getItemId()==R.id.logout){
            FirebaseAuth.getInstance().signOut();
            getActivity().finish();
            Intent i = new Intent(getActivity(),Login.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && data!=null){
            Uri selected_image=data.getData();
            profil_picture_user.setImageURI(selected_image);
        }else if(requestCode == 2 && data!=null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            gambar_ktp_profil.setImageBitmap(bitmap);
            passing_gambar=bitmap;
        }
    }
}
