package com.example.evgo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class onBoard extends AppCompatActivity {


    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this Thread is to mention the duration of splash screen
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this); //Installed the splash screen api that as in dependency
        // and the configurations in res/values/styles.xml;



        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboard_activity);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser!=null){
            Intent homeIntent = new Intent(getApplicationContext(),Home.class);
            startActivity(homeIntent);
            finish();
        }else {
            Toast.makeText(this, "Hii", Toast.LENGTH_SHORT).show();
        }

        Button logIn,signUp;
        logIn = findViewById(R.id.login);
        signUp = findViewById(R.id.signUp);


        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),signIn.class);
                startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(),signUp.class);
                startActivity(intent2);
            }
        });


    }
}