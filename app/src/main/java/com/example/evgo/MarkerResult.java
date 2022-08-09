package com.example.evgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class MarkerResult extends AppCompatActivity {

    TextView name,shopName,address,phoneNumber,email;
    LinearLayout back;
    Button contact,payment;
    Button oneHr,twoHr,threeHr,fourHr,fiveHr,sixHr,clear;

    int PaymentAmout = 0;
    String slotHour;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_result);
        name = findViewById(R.id.name);
        shopName = findViewById(R.id.shopName);
        address = findViewById(R.id.address);
        phoneNumber = findViewById(R.id.phoneNumber);

        back = findViewById(R.id.back);

        contact = findViewById(R.id.contact);
        payment = findViewById(R.id.payment);

        oneHr = findViewById(R.id.oneHr);
        twoHr = findViewById(R.id.towHr);
        threeHr = findViewById(R.id.threeHr);
        fourHr = findViewById(R.id.fourHr);
        fiveHr = findViewById(R.id.fiveHr);
        sixHr = findViewById(R.id.sixHr);

        clear = findViewById(R.id.clear);



        payment.setVisibility(View.INVISIBLE);

        oneHr.setVisibility(View.INVISIBLE);
        twoHr.setVisibility(View.INVISIBLE);
        threeHr.setVisibility(View.INVISIBLE);
        fourHr.setVisibility(View.INVISIBLE);
        fiveHr.setVisibility(View.INVISIBLE);
        sixHr.setVisibility(View.INVISIBLE);
        clear.setVisibility(View.INVISIBLE);





        String nameR = getIntent().getStringExtra("nameIntent");
        String shopR = getIntent().getStringExtra("shopNameIntent");
        String addressR = getIntent().getStringExtra("addressIntent");
        String phoneNumberR = getIntent().getStringExtra("phoneNumberIntent");
        String renderIdR = getIntent().getStringExtra("renderIdIntent");




        name.setText(nameR);
        shopName.setText(shopR);
        address.setText(addressR);
        phoneNumber.setText(phoneNumberR);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = "+91"+phoneNumberR;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
                Toast.makeText(MarkerResult.this, "If slot is confirm then only pay for charge", Toast.LENGTH_SHORT).show();

                payment.setVisibility(View.VISIBLE);

                oneHr.setVisibility(View.VISIBLE);
                twoHr.setVisibility(View.VISIBLE);
                threeHr.setVisibility(View.VISIBLE);
                fourHr.setVisibility(View.VISIBLE);
                fiveHr.setVisibility(View.VISIBLE);
                sixHr.setVisibility(View.VISIBLE);
                clear.setVisibility(View.VISIBLE);

            }
        });


        oneHr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                twoHr.setVisibility(View.INVISIBLE);
                threeHr.setVisibility(View.INVISIBLE);
                fourHr.setVisibility(View.INVISIBLE);
                fiveHr.setVisibility(View.INVISIBLE);
                sixHr.setVisibility(View.INVISIBLE);
                slotHour="1/Hr";
                PaymentAmout = 0;
                PaymentAmout = PaymentAmout+10;
                Toast.makeText(MarkerResult.this, ""+PaymentAmout, Toast.LENGTH_SHORT).show();
            }
        });
        twoHr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneHr.setVisibility(View.INVISIBLE);
                threeHr.setVisibility(View.INVISIBLE);
                fourHr.setVisibility(View.INVISIBLE);
                fiveHr.setVisibility(View.INVISIBLE);
                sixHr.setVisibility(View.INVISIBLE);
                slotHour="2/Hr";
                PaymentAmout = 0;
                PaymentAmout = PaymentAmout+20;
                Toast.makeText(MarkerResult.this, ""+PaymentAmout, Toast.LENGTH_SHORT).show();
            }
        });
        threeHr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneHr.setVisibility(View.INVISIBLE);
                twoHr.setVisibility(View.INVISIBLE);
                fourHr.setVisibility(View.INVISIBLE);
                fiveHr.setVisibility(View.INVISIBLE);
                sixHr.setVisibility(View.INVISIBLE);
                slotHour="3/Hr";
                PaymentAmout = 0;
                PaymentAmout = PaymentAmout+30;
                Toast.makeText(MarkerResult.this, ""+PaymentAmout, Toast.LENGTH_SHORT).show();
            }
        });

        fourHr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneHr.setVisibility(View.INVISIBLE);
                twoHr.setVisibility(View.INVISIBLE);
                threeHr.setVisibility(View.INVISIBLE);
                fiveHr.setVisibility(View.INVISIBLE);
                sixHr.setVisibility(View.INVISIBLE);
                slotHour="4/Hr";
                PaymentAmout = 0;
                PaymentAmout = PaymentAmout+40;
                Toast.makeText(MarkerResult.this, ""+PaymentAmout, Toast.LENGTH_SHORT).show();
            }
        });

        fiveHr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneHr.setVisibility(View.INVISIBLE);
                twoHr.setVisibility(View.INVISIBLE);
                threeHr.setVisibility(View.INVISIBLE);
                fourHr.setVisibility(View.INVISIBLE);
                sixHr.setVisibility(View.INVISIBLE);
                slotHour="5/Hr";
                PaymentAmout = 0;
                PaymentAmout = PaymentAmout+50;
                Toast.makeText(MarkerResult.this, ""+PaymentAmout, Toast.LENGTH_SHORT).show();
            }
        });
        sixHr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneHr.setVisibility(View.INVISIBLE);
                twoHr.setVisibility(View.INVISIBLE);
                threeHr.setVisibility(View.INVISIBLE);
                fourHr.setVisibility(View.INVISIBLE);
                fiveHr.setVisibility(View.INVISIBLE);
                slotHour="6/Hr";
                PaymentAmout = 0;
                PaymentAmout = PaymentAmout+60;
                Toast.makeText(MarkerResult.this, ""+PaymentAmout, Toast.LENGTH_SHORT).show();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneHr.setVisibility(View.VISIBLE);
                twoHr.setVisibility(View.VISIBLE);
                threeHr.setVisibility(View.VISIBLE);
                fourHr.setVisibility(View.VISIBLE);
                fiveHr.setVisibility(View.VISIBLE);
                sixHr.setVisibility(View.VISIBLE);
                PaymentAmout = 0;
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PaymentAmout!=0){
                    Intent intentPayement = new Intent(getApplicationContext(),payment_activity.class);
                    String payAmtString = String.valueOf(PaymentAmout);
                    intentPayement.putExtra("slotRate",payAmtString);
                    intentPayement.putExtra("renderIdS",renderIdR);
                    intentPayement.putExtra("shopName",shopR);
                    intentPayement.putExtra("phoneNumber",phoneNumberR);
                    intentPayement.putExtra("slotHour",slotHour);
                    intentPayement.putExtra("addressOfRender",addressR);
                    startActivity(intentPayement);
                    finish();

                }else{
                    Toast.makeText(MarkerResult.this, "Please select slot", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
}