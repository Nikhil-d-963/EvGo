package com.example.evgo;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MapsFragment extends Fragment {

    private GoogleMap mMap;
    GeoPoint geoPoint;

    String lalak;
    GeoPoint currentGeo;
    View view;
    ImageView changeView, myLoc,ProfilePic;
    FusedLocationProviderClient client;
    androidx.appcompat.widget.SearchView searchView;


    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    public ArrayList<UserDetails> userDetailsList = new ArrayList<>();



    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {



            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);





            ProfilePic = view.findViewById(R.id.profilePic);
            ProfilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentProfile = new Intent(getActivity(), profileActivity.class);
                    startActivity(intentProfile);
                }
            });




            changeView = view.findViewById(R.id.changeView);
            changeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showOptionsDialog();
                }
            });






            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Renders").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult())
                                {
                                    geoPoint = document.getGeoPoint("geo");
                                    String renderId = document.getId();
                                    String name_ =
                                            document.getString("name");
                                    String shop_Name = document.getString("shop_name");
                                    String address_ = document.getString("address");
                                    String phone_num = document.getString("phone_number");
                                    LatLng location = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
                                    mMap.addMarker(new MarkerOptions().position(location).title(name_).icon(BitmapFromVector(getActivity(),R.drawable.ic_point)));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

                                    userDetailsList.add(new UserDetails(renderId,name_,shop_Name,address_,phone_num,location));

                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {

                    Intent MarkerClickIntent = new Intent(getActivity(), MarkerResult.class);

                    for(UserDetails userDetails : userDetailsList){

                        if (Objects.equals(userDetails.latLng, marker.getPosition())){
                            MarkerClickIntent.putExtra("nameIntent",userDetails.name);
                            MarkerClickIntent.putExtra("shopNameIntent",userDetails.shopName);
                            MarkerClickIntent.putExtra("addressIntent",userDetails.address);
                            MarkerClickIntent.putExtra("phoneNumberIntent",userDetails.phoneNumber);
                            MarkerClickIntent.putExtra("renderIdIntent",userDetails.renderID);
                            break;
                        }
                    }
                    startActivity(MarkerClickIntent);
                    return true;
                }
            });


        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_maps, container, false);





         client = LocationServices.getFusedLocationProviderClient(getActivity());
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getActivity(), "Thank-you", Toast.LENGTH_SHORT).show();
            getLocation();
        }else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);

        }








        searchView = view.findViewById(R.id.serchTxt);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String locationSearch = searchView.getQuery().toString();
                List<Address> addressList = null;
                if(locationSearch != null || locationSearch.equals("")){
                    Geocoder geocoder1 = new Geocoder(getActivity());
                    try {
                        addressList = geocoder1.getFromLocationName(locationSearch,1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address1 = addressList.get(0);
                    LatLng latLngS = new LatLng(address1.getLatitude(),address1.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLngS).title(locationSearch)
                            .icon(BitmapFromVector(getActivity(),R.drawable.ic_location_svgrepo_com__1_)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngS,10));

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        myLoc = view.findViewById(R.id.myLoc);
        myLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();

            }
        });


        return view;
    }


    ////method of the location getting
    @SuppressLint("MissingPermission")
    public void getLocation() {
        client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if(location!=null){

                    try {
                        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                        double Latitude = addresses.get(0).getLatitude();
                        double Longitude = addresses.get(0).getLongitude();
                        currentGeo = new GeoPoint(Latitude,Longitude);
                        LatLng latLng = new LatLng(currentGeo.getLatitude(), currentGeo.getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,100));
                        mMap.addMarker(new MarkerOptions().position(latLng).title("You are here ")
                                .icon(BitmapFromVector(getActivity(),R.drawable.ic_ami)));
                    }catch (IOException e){
                        e.printStackTrace();
                    }


                }
            }
        });
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }














    }


    private void showOptionsDialog() {
        String[] mapType = {"roadMap", "hybrid", "satellite", "terrain"};
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setTitle("Select MapType");
        builder.setItems(mapType, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 1:
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    case 2:
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case 3:
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                    default:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                dialog.dismiss();
            }
        });

        builder.show();
    }





}