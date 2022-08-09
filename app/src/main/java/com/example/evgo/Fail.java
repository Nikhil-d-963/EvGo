package com.example.evgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Fail extends AppCompatActivity {

    Button retry,back_to_home,getHelp;
    TextView transactionNumberDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fail);

//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
//
//        String currentUserID = firebaseAuth.getCurrentUser().getUid();
        String renderId = getIntent().getStringExtra("renderId");
        String transactionNumber = getIntent().getStringExtra("transaction_Number");




        back_to_home = findViewById(R.id.backToHome);
        getHelp = findViewById(R.id.getHelp);
        transactionNumberDisplay = findViewById(R.id.transactionNo);


        transactionNumberDisplay.setText(transactionNumber);

        retry = findViewById(R.id.retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentR = new Intent(getApplicationContext(),Home.class);
                startActivity(intentR);
                finish();
            }
        });


        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentR = new Intent(getApplicationContext(),Home.class);
                startActivity(intentR);
                finish();
            }
        });


        getHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentR = new Intent(getApplicationContext(),getHelp.class);
                intentR.putExtra("renderId",renderId);
                startActivity(intentR);

            }
        });
    }
}