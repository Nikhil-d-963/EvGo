package com.example.evgo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class profileActivity extends AppCompatActivity {

    CardView feedbacks,issues,visitWebsite,helpLine,TC,appInfoA;

    TextView name,phoneNumber,email;

    ImageView back,logout;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;



    String nameD;
    String phoneNumD;
    String emailD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();

        name = findViewById(R.id.name);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.email);

        String userId = firebaseAuth.getCurrentUser().getUid();

        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userId);
        documentReference.addSnapshotListener(profileActivity.this, new EventListener<DocumentSnapshot>() {

            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                name.setText(documentSnapshot.getString("name"));
                email.setText(documentSnapshot.getString("email"));
                phoneNumber.setText(documentSnapshot.getString("phone_number"));
            }
        });




        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });




        logout = findViewById(R.id.signOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent intentNew = new Intent(getApplicationContext(),signIn.class);
                startActivity(intentNew);
                finish();
            }
        });













        issues = findViewById(R.id.issues);
        issues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentIssues = new Intent(getApplicationContext(),getHelp.class);
                startActivity(intentIssues);
                finish();
            }
        });

        feedbacks = findViewById(R.id.feedBack);
        feedbacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentF = new Intent(getApplicationContext(),feedBack.class);
                startActivity(intentF);
                finish();
            }
        });


        helpLine = findViewById(R.id.helpLine);
        helpLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobileNumber = "9741658576";
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL); // Action for what intent called for
                intent.setData(Uri.parse("tel: " + mobileNumber)); // Data with intent respective action on intent
                startActivity(intent);
            }
        });

        visitWebsite = findViewById(R.id.website);
        visitWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.evgo.tech/";
                Intent vw = new Intent(Intent.ACTION_VIEW);
                vw.setData(Uri.parse(url));
                startActivity(vw);
            }
        });

        TC = findViewById(R.id.terms);
        TC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.evgo.tech/p/terms-and-conditions.html";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });



        appInfoA = findViewById(R.id.appInfo);
        appInfoA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentA = new Intent(getApplicationContext(),appInfo.class);
                startActivity(intentA);
            }
        });



    }


}