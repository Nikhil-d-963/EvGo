package com.example.evgo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class getHelp extends AppCompatActivity {

    EditText issueInput;
    Button issueBtn;
    String uuid;

    String issueText;
    String dateOfIssue;


    String getRenderID;
    LinearLayout back;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    String currentUserId = firebaseAuth.getCurrentUser().getUid();
    String currentUserEmail = firebaseAuth.getCurrentUser().getEmail();
    String currentUserName;
    String currentUserPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_help);



        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });




        getRenderID = getIntent().getStringExtra("renderId");

        issueInput = findViewById(R.id.issueInput);
        issueBtn = findViewById(R.id.issueBtn);




        String userIdXXXX = firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userIdXXXX);
        documentReference.addSnapshotListener(getHelp.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                currentUserName = documentSnapshot.getString("name");
                currentUserPhoneNumber = documentSnapshot.getString("phone_number");
                currentUserEmail = documentSnapshot.getString("email");
            }
        });




        issueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                issueText = issueInput.getText().toString();
                Date currentTime = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    currentTime = Calendar.getInstance().getTime();
                }
                dateOfIssue = String.valueOf(currentTime);
                String userId = currentUserId;
                uuid = UUID.randomUUID().toString();
                //declaring the object of documentation reference
                DocumentReference documentReference = firebaseFirestore.collection("Issues-"+userId).document(uuid);
                Map<String, Object> user = new HashMap<>();
                user.put("customer_id", currentUserId);
                user.put("customer name", currentUserName);
                user.put("email", currentUserEmail );
                user.put("phoneNumber", currentUserPhoneNumber );
                user.put("issue",issueText);
                user.put("date_of_issue",dateOfIssue);
                user.put("renderId",getRenderID);

                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Intent intentI = new Intent(getApplicationContext(),Home.class);
                        startActivity(intentI);
                        finish();
                        Toast.makeText(getHelp.this, "Issue Submitted! We will get back you soon", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error! " + e, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}