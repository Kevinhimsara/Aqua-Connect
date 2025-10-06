package com.kevin.aquaconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerDashboardActivity extends AppCompatActivity {

    Button btnRequestService, btnRequestHistory, btnContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        btnRequestService = findViewById(R.id.btnRequestService);
        btnRequestHistory = findViewById(R.id.btnRequestHistory);
        btnContacts = findViewById(R.id.btnContacts);

        btnRequestService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CustomerDashboardActivity.this, CreateRequestActivity.class);
                    startActivity(intent);
                }
            });

        btnRequestHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerDashboardActivity.this, CreateRequestActivity.class);
                startActivity(intent);
            }
        });

        btnRequestService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerDashboardActivity.this, CreateRequestActivity.class);
                startActivity(intent);
            }
        });
        }


    }

