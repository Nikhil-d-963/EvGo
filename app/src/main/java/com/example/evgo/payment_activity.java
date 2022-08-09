package com.example.evgo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class payment_activity extends AppCompatActivity {

    TextView ToShopName,slotHoldTime,slotAmt,RenderPhoneNumber,totalAmt,addressRender;
    EditText otherQuery;
    Button paymentBtn;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    String currentUserName;
    String currentUserPhoneNumber;
    String currentUserEmail;

    String Query;
    String slotRateR;
    String shopRR;
    String slotHrR;

    String renderIdRR;
    String dateF;
    String uuid;


    String LatLongP;
    String finalAmount;

    String currentAmtInDataBase;
    DocumentReference documentReference1;

    final int UPI_PAYMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);



        ToShopName = findViewById(R.id.toShopName);
        slotHoldTime = findViewById(R.id.slotTime);
        slotAmt= findViewById(R.id.slotAmt);
        RenderPhoneNumber = findViewById(R.id.shopPhoneNumber);
        totalAmt = findViewById(R.id.totalAmt);
        addressRender = findViewById(R.id.addressRender);


        otherQuery = findViewById(R.id.OtherQueryInput);
        paymentBtn = findViewById(R.id.paymentBtn);


        slotRateR = getIntent().getStringExtra("slotRate");
        shopRR = getIntent().getStringExtra("shopName");
        String addressR = getIntent().getStringExtra("addressOfRender");
        String phoneNumberRR = getIntent().getStringExtra("phoneNumber");
        renderIdRR = getIntent().getStringExtra("renderIdS");
        slotHrR = getIntent().getStringExtra("slotHour");
        LatLongP = getIntent().getStringExtra("LatLongP");


        ToShopName.setText(shopRR);
        slotHoldTime.setText(slotHrR);
        slotAmt.setText(slotRateR);
        RenderPhoneNumber.setText(phoneNumberRR);
        addressRender.setText(addressR);
        totalAmt.setText(slotRateR);




        String userID = firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userID);
        documentReference.addSnapshotListener(payment_activity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
               currentUserName = documentSnapshot.getString("name");
               currentUserPhoneNumber = documentSnapshot.getString("phone_number");
               currentUserEmail = documentSnapshot.getString("email");
                Toast.makeText(payment_activity.this, ""+currentUserEmail, Toast.LENGTH_SHORT).show();
            }
        });



        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBalence();
                Date currentTime = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    currentTime = Calendar.getInstance().getTime();
                }
                dateF = String.valueOf(currentTime);
                payUsingUpi();

            }
        });







    }

    private void payUsingUpi() {
        Query = otherQuery.getText().toString();
        if(Query.isEmpty()){
            Query = "No Query";
        }
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa","q515134334@ybl") //here enter upi id
                .appendQueryParameter("pn","Nikhil D") //here enter name
                .appendQueryParameter("tn","From: "+currentUserName+"\n"
                        +"Customer email: "+currentUserEmail+"\n"
                        +"Customer phone number: "+currentUserPhoneNumber+"\n"
                        +"Amount: "+slotRateR+"\n"
                        +"Slot hour: "+slotHrR+"\n"
                        +"Your name: "+shopRR+"\n"
                        +"Other query: "+Query+"\n")//here enter note
                .appendQueryParameter("am","1") //here enter amount
                .appendQueryParameter("cu","INR") //currency type
                .build();
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(payment_activity.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }
    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(payment_activity.this)) {
            String str = data.get(0);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(payment_activity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();


                String userId = renderIdRR;
                uuid = UUID.randomUUID().toString();
                //declaring the object of documentation reference
                DocumentReference documentReference = firebaseFirestore.collection("Transactions-"+userId).document(uuid);
                Map<String, Object> user = new HashMap<>();
                user.put("customer_name", currentUserName);
                user.put("email", currentUserEmail );
                user.put("Amount", slotRateR);
                user.put("Slot_hour", slotHrR);
                user.put("Date_Time",dateF);
                user.put("TransactionID",uuid);
                user.put("phoneNumber",currentUserPhoneNumber);
                user.put("Customer_Query",Query);
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        documentReference1.update("wallet",finalAmount);

                        Intent intentS = new Intent(getApplicationContext(),SucessFull.class);
                        intentS.putExtra("renderId",userId);
                        intentS.putExtra("transaction_Number",uuid);
                        startActivity(intentS);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error! " + e, Toast.LENGTH_SHORT).show();
                    }
                });



            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                String userId = renderIdRR;
                uuid = UUID.randomUUID().toString();
                //declaring the object of documentation reference
                DocumentReference documentReference = firebaseFirestore.collection("Failed-"+userId).document(uuid);
                Map<String, Object> user = new HashMap<>();
                user.put("customer name", currentUserName);
                user.put("email", currentUserEmail );
                user.put("Amount", slotRateR);
                user.put("Slot hour", slotHrR);
                user.put("shop name",shopRR);
                user.put("Date & Time",dateF);
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Intent intentS = new Intent(getApplicationContext(),Fail.class);
                        intentS.putExtra("renderId",userId);
                        intentS.putExtra("transaction_Number",uuid);
                        startActivity(intentS);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error! " + e, Toast.LENGTH_SHORT).show();
                    }
                });




            }
            else {
                Toast.makeText(payment_activity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();

            }
        } else {

            Toast.makeText(payment_activity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }
    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable())
            {
                return true;
            }
        }
        return false;
    }


    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), Home.class));
        finish();

    }



    private void updateBalence(){
        DocumentReference documentReference = firebaseFirestore.collection("Renders").document(renderIdRR);
        documentReference.addSnapshotListener(payment_activity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                currentAmtInDataBase = documentSnapshot.getString("wallet");
                int currentAmtInDataBaseInt = Integer.parseInt(currentAmtInDataBase);
                int PaidAmount = Integer.parseInt(slotRateR);
                int totalAmount = currentAmtInDataBaseInt+PaidAmount;
                finalAmount = String.valueOf(totalAmount);
                documentReference1 = firebaseFirestore.collection("Renders").document(renderIdRR);
            }
        });
    }


}