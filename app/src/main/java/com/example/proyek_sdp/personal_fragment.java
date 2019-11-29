package com.example.proyek_sdp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class personal_fragment extends Fragment {
    ImageView profil_picture_user;
    ImageView gambar_ktp_profil;
    EditText edname_profil,edemail_profil,edtanggal_lahir_profil,ednotelp_profil;
    Button btn_edit_profil,btn_verifikasi_ktp;
    Bitmap passing_gambar;
    DatabaseReference databaseReference;
    TextView status_verifikasi_ktp;
    int saldo=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myview=inflater.inflate(R.layout.fragment_personal,container,false);
        setHasOptionsMenu(true);
        profil_picture_user=myview.findViewById(R.id.picture_profil_user);
        edname_profil=myview.findViewById(R.id.edname_profil);
        edemail_profil=myview.findViewById(R.id.edemail_profil);
        edtanggal_lahir_profil=myview.findViewById(R.id.ed_tanggal_lahir_profil);
        btn_edit_profil=myview.findViewById(R.id.btn_edit_profil);
        btn_verifikasi_ktp=myview.findViewById(R.id.btn_verifikasi_ktp);
        gambar_ktp_profil=myview.findViewById(R.id.gambar_ktp_profil);
        ednotelp_profil=myview.findViewById(R.id.edphone_profil);
        status_verifikasi_ktp=myview.findViewById(R.id.status_verifikasi_ktp);

        edemail_profil.setEnabled(false);
        edname_profil.setEnabled(false);
        ednotelp_profil.setEnabled(false);
        edtanggal_lahir_profil.setEnabled(false);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("UserDatabase");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count=dataSnapshot.getChildrenCount();
                boolean berhasil_register=true;
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(ds.child("email").getValue().toString())){
                        FirebaseStorage.getInstance().getReference().child("profil_picture").child(FirebaseAuth.getInstance().getCurrentUser().getEmail()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                if (getActivity()!=null){
                                    Glide.with(getActivity()).load(uri).into(profil_picture_user);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                profil_picture_user.setBackgroundResource(Integer.parseInt(ds.child("profil_picture").getValue().toString()));
                            }
                        });
                        FirebaseStorage.getInstance().getReference().child("foto_ktp").child(FirebaseAuth.getInstance().getCurrentUser().getEmail()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                if (getActivity()!=null){
                                    Glide.with(getActivity()).load(uri).into(gambar_ktp_profil);
                                }
                            }
                        });
                        edname_profil.setText(ds.child("nama").getValue().toString());
                        edemail_profil.setText(ds.child("email").getValue().toString());
                        ednotelp_profil.setText(ds.child("phone").getValue().toString());
                        saldo=Integer.parseInt(ds.child("saldo").getValue().toString());
                        edtanggal_lahir_profil.setText(ds.child("birthdate").getValue().toString());
                        if (ds.child("verifikasi_ktp").getValue().toString().equals("0")){
                            status_verifikasi_ktp.setText("Belum Diverifikasi");
                        }
                        else if (ds.child("verifikasi_ktp").getValue().toString().equals("1")){
                            status_verifikasi_ktp.setText("Sudah Diverifikasi");
                            btn_verifikasi_ktp.setEnabled(false);
                        }
                        else if (ds.child("verifikasi_ktp").getValue().toString().equals("2")){
                            status_verifikasi_ktp.setText("Gagal diverifikasi");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            long count=dataSnapshot.getChildrenCount();
                            boolean berhasil_register=true;
                            for (DataSnapshot ds :dataSnapshot.getChildren()) {
                                if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(ds.child("email").getValue().toString())){
                                    user baru=new user();
                                    baru.setId(ds.child("id").getValue().toString());
                                    baru.setNama(edname_profil.getText().toString());
                                    baru.setProfil_picture(Integer.parseInt(ds.child("profil_picture").getValue().toString()));
                                    baru.setSaldo(Integer.parseInt(ds.child("saldo").getValue().toString()));
                                    baru.setRating(Float.parseFloat(ds.child("rating").getValue().toString()));
                                    baru.setPhone(ednotelp_profil.getText().toString());
                                    baru.setEmail(ds.child("email").getValue().toString());
                                    baru.setBirthdate(ds.child("birthdate").getValue().toString());
                                    baru.setStatus(Integer.parseInt(ds.child("status").getValue().toString()));
                                    baru.setPassword(ds.child("password").getValue().toString());
                                    baru.setVerifikasi_ktp(Integer.parseInt(ds.child("verifikasi_ktp").getValue().toString()));
                                    databaseReference.child(ds.getKey()).setValue(baru);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                passing_gambar.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                byte[] byteArray = stream.toByteArray();
//
//                Intent in1 = new Intent(getContext(), image_viewer.class);
//                in1.putExtra("image",byteArray);
//                startActivity(in1);
            }
        });
        return myview;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.optionmenu_profil, menu);
        super.onCreateOptionsMenu(menu, inflater);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("UserDatabase");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count=dataSnapshot.getChildrenCount();
                boolean berhasil_register=true;
                for (DataSnapshot ds :dataSnapshot.getChildren()) {
                    if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(ds.child("email").getValue().toString())){
                        menu.getItem(0).setTitle("Saldo : "+Integer.parseInt(ds.child("saldo").getValue().toString()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
            final ProgressDialog progressDialog=new ProgressDialog(getActivity());
            progressDialog.setTitle("Mengupload....");
            progressDialog.show();
            StorageReference reference= FirebaseStorage.getInstance().getReference().child("profil_picture/"+ FirebaseAuth.getInstance().getCurrentUser().getEmail());
            reference.putFile(selected_image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    profil_picture_user.setImageURI(selected_image);
                    Toast.makeText(getActivity(), "berhasil upload profil picture", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "gagal upload file profile picture", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Upload "+(int)progress+"%");
                    if(progress==100){
                        progressDialog.dismiss();
                    }
                }
            });
        }else if(requestCode == 2 && data!=null){
            Uri selected_ktp=data.getData();

            final ProgressDialog progressDialog=new ProgressDialog(getActivity());
            progressDialog.setTitle("Mengupload....");
            progressDialog.show();
            StorageReference reference= FirebaseStorage.getInstance().getReference().child("foto_ktp/"+ FirebaseAuth.getInstance().getCurrentUser().getEmail());
            reference.putFile(selected_ktp).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    gambar_ktp_profil.setImageURI(selected_ktp);
                    Toast.makeText(getActivity(), "berhasil upload profil picture", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "gagal upload file profile picture", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Upload "+(int)progress+"%");
                    if(progress==100){
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }
}
