package com.kevin.aquaconnect;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateRequestActivity extends AppCompatActivity {

    // View Variables
    private RadioGroup rgServiceType, rgUrgency;
    private EditText etDescription, etAddress;
    private Button btnAddPhoto, btnSubmitRequest;
    private ImageView mapsButton;

    // Firebase & Location Variables
    private DatabaseReference databaseReference;
    private FusedLocationProviderClient fusedLocationClient;

    // Background Threading Tools
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        initViews();

        // Initialize Firebase and Location Client
        databaseReference = FirebaseDatabase.getInstance().getReference("service_requests");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setupClickListeners();
    }

    private void initViews() {
        rgServiceType = findViewById(R.id.rgServiceType);
        rgUrgency = findViewById(R.id.rgUrgency);
        etDescription = findViewById(R.id.etDescription);
        etAddress = findViewById(R.id.etAddress);
        btnAddPhoto = findViewById(R.id.btnAddPhoto);
        btnSubmitRequest = findViewById(R.id.btnSubmitRequest);
        mapsButton = findViewById(R.id.btnOpenMaps);
    }

    private void setupClickListeners() {
        // Option 1: User clicks the icon to AUTO-FILL location via GPS
        mapsButton.setOnClickListener(v -> checkPermissionAndGetLocation());

        // Option 2: Regular button clicks
        btnAddPhoto.setOnClickListener(this::onAddPhotoClicked);
        btnSubmitRequest.setOnClickListener(v -> submitServiceRequest());
    }

    // --- LOCATION LOGIC ---

    private void checkPermissionAndGetLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission (100 is just a request code)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        } else {
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        Toast.makeText(this, "Detecting your location...", Toast.LENGTH_SHORT).show();

        try {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    // Convert GPS coordinates to Address String in background
                    fetchAddressFromCoords(location.getLatitude(), location.getLongitude());
                } else {
                    Toast.makeText(this, "Please ensure GPS is turned on.", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void fetchAddressFromCoords(double lat, double lng) {
        executorService.execute(() -> {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    String finalAddress = addresses.get(0).getAddressLine(0);

                    // Update UI on Main Thread
                    mainThreadHandler.post(() -> {
                        etAddress.setText(finalAddress);
                        Toast.makeText(this, "Address updated!", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (IOException e) {
                mainThreadHandler.post(() -> Toast.makeText(this, "Network error fetching address.", Toast.LENGTH_SHORT).show());
            }
        });
    }

    // --- SUBMISSION LOGIC ---

    private void submitServiceRequest() {
        String description = etDescription.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(description)) {
            etDescription.setError("Description is required.");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            etAddress.setError("Address is required.");
            return;
        }

        // Get Service Type String
        int selectedServiceTypeId = rgServiceType.getCheckedRadioButtonId();
        if (selectedServiceTypeId == -1) {
            Toast.makeText(this, "Select a service type.", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton serviceTypeRadioButton = findViewById(selectedServiceTypeId);
        String serviceType = serviceTypeRadioButton.getText().toString();

        // Get Urgency String
        int selectedUrgencyId = rgUrgency.getCheckedRadioButtonId();
        if (selectedUrgencyId == -1) {
            Toast.makeText(this, "Select urgency level.", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton urgencyRadioButton = findViewById(selectedUrgencyId);
        String urgency = urgencyRadioButton.getText().toString();

        // Push to Firebase
        String requestId = databaseReference.push().getKey();
        if (requestId == null) return;

        ServiceRequest request = new ServiceRequest(requestId, serviceType, description, address, urgency);

        databaseReference.child(requestId).setValue(request)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Request submitted successfully!", Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            Toast.makeText(this, "Location permission is required to auto-select.", Toast.LENGTH_SHORT).show();
        }
    }

    private void onAddPhotoClicked(View v) {
        Toast.makeText(this, "Photo upload feature coming soon.", Toast.LENGTH_SHORT).show();
    }
}