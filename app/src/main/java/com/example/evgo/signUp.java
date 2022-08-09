package com.example.evgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signUp extends AppCompatActivity {


    TextView name, email, phoneNumber, passWord, cPassWord;
    Button signUpBtn;
    ImageView backPress;
    ProgressBar progressBar;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        name = findViewById(R.id.nameInput);
        email = findViewById(R.id.emailInput);
        phoneNumber = findViewById(R.id.phoneNumberInput);
        passWord = findViewById(R.id.passWordInput);
        cPassWord = findViewById(R.id.confirmPasswordInput);

        progressBar = findViewById(R.id.progress);

        signUpBtn = findViewById(R.id.signUpBtn);

       backPress = findViewById(R.id.backPree);


        progressBar.setVisibility(View.INVISIBLE);



        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sEmailId = email.getText().toString().trim();
                String sPasswordInput = passWord.getText().toString().trim();
                String sConfirmPasswordInput = cPassWord.getText().toString().trim();
                String sName = name.getText().toString().trim();
                String sPhoneNumber = phoneNumber.getText().toString().trim();

                final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

                //validating the null input and others
                if(TextUtils.isEmpty(sEmailId)) {
                    email.setError("Enter email");
                    return;
                }if (!sEmailId.matches(EMAIL_PATTERN)) {
                    email.setError("Invalid Email Address");
                    return;
                }if(passWord.toString().trim().isEmpty()){
                    passWord.setError("Enter password");
                    return;
                }if(cPassWord.toString().trim().isEmpty()){
                    cPassWord.setError("Enter password");
                    return;
                }if(sPasswordInput.length()<6){
                    passWord.setError("Password is short");
                    return;
                }if(!sPasswordInput.equals(sConfirmPasswordInput)) {
                    cPassWord.setError("Password not match");
                    passWord.setError("Password not match");
                    return;
                }if(TextUtils.isEmpty(sName)){
                    name.setError("Enter name");
                    return;
                }if(TextUtils.isEmpty(sPhoneNumber)){
                    phoneNumber.setError("Enter phone number");
                    return;
                }if(sPhoneNumber.length()<10){
                    phoneNumber.setError("invalid phone number");
                    return;
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                }


                firebaseAuth.createUserWithEmailAndPassword(sEmailId, sPasswordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            String emailID = firebaseAuth.getCurrentUser().getEmail();
                            if(firebaseAuth.getCurrentUser()!=null){
                                String userId = firebaseAuth.getCurrentUser().getUid();
                                //declaring the object of documentation reference
                                DocumentReference documentReference = firebaseFirestore.collection("Users").document(userId);
                                Map<String, Object> user = new HashMap<>();
                                user.put("name", sName);
                                user.put("email", emailID);
                                user.put("phone_number", sPhoneNumber);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show();
                                        Intent home2 = new Intent(signUp.this, Home.class);
                                        home2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(home2);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error! " + e, Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }


                        }else {
                            Toast.makeText(getApplicationContext(), "Error! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });




    }
}