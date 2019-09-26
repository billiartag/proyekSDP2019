package com.example.proyek_sdp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

class user implements Parcelable {
    public String nama;
    public String username;
    public String email;
    public String birthdate;
    public String phone;
    public String postal;
    public String passsword;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
