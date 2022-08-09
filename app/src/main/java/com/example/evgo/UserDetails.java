package com.example.evgo;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import javax.annotation.Nullable;

public class UserDetails {
    public String name;
    public String shopName;
    public  String address;
    public  String phoneNumber;
    public  String renderID;

    @Nullable public LatLng latLng;

    public UserDetails(String renderId, String name, String shopName, String address, String phoneNumber, @androidx.annotation.Nullable LatLng latLng) {
        this.name = name;
        this.shopName = shopName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.latLng = latLng;
        this.renderID = renderId;

    }
}
