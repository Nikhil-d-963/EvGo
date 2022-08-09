package com.example.evgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class signIn extends AppCompatActivity {

    TextView email, password;
    Button signInBtn, forgotPassword;
    ProgressBar progressBar;


    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);





        ImageView backPress = findViewById(R.id.backPree);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        email = findViewById(R.id.emailInput);
        password = findViewById(R.id.passWordInput);
        signInBtn = findViewById(R.id.loginBtn);
        forgotPassword = findViewById(R.id.forgotPassword);
        progressBar = findViewById(R.id.progress);

        progressBar.setVisibility(View.INVISIBLE);


        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sEmailId = email.getText().toString().trim();
                String sPasswordInput = password.getText().toString().trim();


                final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

                //validating the null input and others
                if(TextUtils.isEmpty(sEmailId)) {
                    email.setError("Enter email");
                    return;
                }if (!sEmailId.matches(EMAIL_PATTERN)) {
                    email.setError("Invalid Email Address");
                }if(sPasswordInput.isEmpty()){
                    password.setError("Enter password");
                    return;
                }if(sPasswordInput.length()<6){
                    password.setError("Password is short");
                    return;
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                }


                //Authenticate the user
                firebaseAuth.signInWithEmailAndPassword(sEmailId,sPasswordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //DashBoard
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(signIn.this, "Welcome", Toast.LENGTH_SHORT).show();
                            Intent home2 = new Intent(signIn.this, Home.class);
                            home2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(home2);
                        }else {
                            Toast.makeText(getApplicationContext(), "Error! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });


            }
        });


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(signIn.this);
                // Add customization options here
                materialAlertDialogBuilder.setTitle("Forgot password");
                materialAlertDialogBuilder.setMessage("Don't worry! we will recover your password");
                final EditText input = new EditText(signIn.this);

                input.setHint("Enter registered email");
                materialAlertDialogBuilder.setView(input);

                materialAlertDialogBuilder.setPositiveButton("reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String inputMail = input.getText().toString().trim();
                        firebaseAuth.sendPasswordResetEmail(inputMail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(signIn.this, "Mail sent", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Reset email not sent. "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                materialAlertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                materialAlertDialogBuilder.show();
            }
        });





    }
}