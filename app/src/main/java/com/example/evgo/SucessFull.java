package com.example.evgo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;

import java.util.Locale;

public class SucessFull extends AppCompatActivity {

    Button backToHome,wayToCharge;
//    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore;
    GeoPoint geoPointS;

    String currentAmtInDataBase;

    String PaidAmt;


    TextView transactionNumberDisplay;
    String renderIdR;
    String transaction_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucess_full);


        transactionNumberDisplay = findViewById(R.id.transactionNo);
        backToHome = findViewById(R.id.backToHome);
        wayToCharge = findViewById(R.id.wayTocharge);



        firebaseFirestore = FirebaseFirestore.getInstance();

        renderIdR = getIntent().getStringExtra("renderId");
        transaction_number = getIntent().getStringExtra("transaction_Number");
//        PaidAmt = getIntent().getStringExtra("PaidAmount");
        transactionNumberDisplay.setText(transaction_number);



        DocumentReference documentReference = firebaseFirestore.collection("Renders").document(renderIdR);
        documentReference.addSnapshotListener(SucessFull.this, new EventListener<DocumentSnapshot>() {

            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                geoPointS = documentSnapshot.getGeoPoint("geo");
//                currentAmtInDataBase = documentSnapshot.getString("wallet");


            }


        });
//
//        UpdateBalence();
































        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentH = new Intent(getApplicationContext(),Home.class);
                startActivity(intentH);
                finish();
            }
        });



        wayToCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", geoPointS.getLatitude(), geoPointS.getLongitude(), "Way to charge");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);

            }
        });




    }

//    public void UpdateBalence(){
//        int currentAmountInFireStore = Integer.parseInt(currentAmtInDataBase);
//        int PaidAmount = Integer.parseInt(PaidAmt);
//        int finalTotalAmount = currentAmountInFireStore+PaidAmount;
//        String TotalAmount = String.valueOf(finalTotalAmount);
//        if (TotalAmount!=null){
//            DocumentReference documentReferenceR = firebaseFirestore.collection("Renders").document(renderIdR);
//            documentReferenceR.update("wallet",TotalAmount);
//        }else{
//            Toast.makeText(this, "Problum", Toast.LENGTH_SHORT).show();
//        }
//
//
//    }
}