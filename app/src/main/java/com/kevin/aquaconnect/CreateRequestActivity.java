package com.kevin.aquaconnect;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateRequestActivity extends AppCompatActivity {

    private RadioGroup rgServiceType;
    private RadioGroup rgUrgency;
    private EditText etDescription;
    private EditText etAddress;
    private Button btnAddPhoto;
    private Button btnSubmitRequest;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        initViews();

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("service_requests");

        setupClickListeners();
    }

    private void initViews() {
        rgServiceType = findViewById(R.id.rgServiceType);
        rgUrgency = findViewById(R.id.rgUrgency);
        etDescription = findViewById(R.id.etDescription);
        etAddress = findViewById(R.id.etAddress);
        btnAddPhoto = findViewById(R.id.btnAddPhoto);
        btnSubmitRequest = findViewById(R.id.btnSubmitRequest);
    }

    private void setupClickListeners() {

        btnAddPhoto.setOnClickListener(v -> onAddPhotoClicked(v));

        btnSubmitRequest.setOnClickListener(v -> submitServiceRequest());
    }

    private void submitServiceRequest() {
        String description = etDescription.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (TextUtils.isEmpty(description)) {
            etDescription.setError("Description is required.");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            etAddress.setError("Address is required.");
            return;
        }

        int selectedServiceTypeId = rgServiceType.getCheckedRadioButtonId();
        if (selectedServiceTypeId == -1) {
            Toast.makeText(this, "Please select a service type.", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton serviceTypeRadioButton = findViewById(selectedServiceTypeId);
        String serviceType = serviceTypeRadioButton.getText().toString();

        int selectedUrgencyId = rgUrgency.getCheckedRadioButtonId();
        if (selectedUrgencyId == -1) {
            Toast.makeText(this, "Please select an urgency level.", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton urgencyRadioButton = findViewById(selectedUrgencyId);
        String urgency = urgencyRadioButton.getText().toString();

        String requestId = databaseReference.push().getKey();

        if (requestId == null) {
            Toast.makeText(this, "Error generating request ID.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ensure you have a 'ServiceRequest' class created with a matching constructor
        ServiceRequest request = new ServiceRequest(requestId, serviceType, description, address, urgency);

        databaseReference.child(requestId).setValue(request)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(CreateRequestActivity.this, "Request submitted successfully!", Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(CreateRequestActivity.this, "Failed to submit: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }

    private void onAddPhotoClicked(View v) {
        // TODO: Add logic for camera/gallery selection
        Toast.makeText(CreateRequestActivity.this, "Add Photo clicked!", Toast.LENGTH_SHORT).show();
    }
}