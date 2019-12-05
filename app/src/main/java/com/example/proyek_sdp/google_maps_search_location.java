package com.example.proyek_sdp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class google_maps_search_location extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;
    SupportMapFragment mapFragment;
    SearchView searchView;
    EditText edverifikasi_search;
    Button verifikasi_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_maps_search_layout);
        searchView = findViewById(R.id.sv_location);
        edverifikasi_search = findViewById(R.id.edverifikasi_search);
        verifikasi_search = findViewById(R.id.btn_verifikasi_search);
        mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myMap_search);
        edverifikasi_search.setFocusable(false);
        verifikasi_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move=new Intent(getApplicationContext(), home.class);
                move.putExtra("judul",getIntent().getExtras().getString("judul"));
                move.putExtra("jenis",getIntent().getExtras().getString("jenis"));
                move.putExtra("varian",getIntent().getExtras().getString("varian"));
                move.putExtra("max",getIntent().getExtras().getString("max"));
                move.putExtra("lokasi",edverifikasi_search.getText().toString());
                move.putExtra("time_dari",getIntent().getExtras().getString("time_dari"));
                move.putExtra("time_ke",getIntent().getExtras().getString("time_ke"));
                move.putExtra("kategori",getIntent().getExtras().getString("kategori"));
                move.putExtra("deskripsi",getIntent().getExtras().getString("deskripsi"));
                move.putExtra("harga",getIntent().getExtras().getString("harga"));
                move.putExtra("berat",getIntent().getExtras().getString("berat"));
                startActivity(move);
                finish();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location=searchView.getQuery().toString();
                List<Address> addressList=null;
                if (location!=null || !location.equals("")){
                    Geocoder geocoder=new Geocoder(google_maps_search_location.this);
                    try {
                        addressList=geocoder.getFromLocationName(location,1);
                        if(addressList.size()>0){
                            Address address=addressList.get(0);
                            LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());
                            map.addMarker(new MarkerOptions().position(latLng).title(location));
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                            edverifikasi_search.setText(getAddress(google_maps_search_location.this,address.getLatitude(),address.getLongitude()));
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        mapFragment.getMapAsync(this);
    }
    public String getAddress(Context context, double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);

            String add = obj.getAddressLine(0);

            return add;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
    }
}