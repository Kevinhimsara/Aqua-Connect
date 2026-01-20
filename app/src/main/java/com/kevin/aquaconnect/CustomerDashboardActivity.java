package com.kevin.aquaconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CustomerDashboardActivity extends AppCompatActivity {

    private Button btnRequestService, btnRequestHistory, btnContacts;
    private TextView tvActiveCount, tvCompletedCount;
    private String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);


        // Initialize Views
        btnRequestService = findViewById(R.id.btnRequestService);
        btnRequestHistory = findViewById(R.id.btnRequestHistory);
        btnContacts = findViewById(R.id.btnContacts);
        tvActiveCount = findViewById(R.id.tvActiveRequests);
        tvCompletedCount = findViewById(R.id.tvCompletedRequests);

        // TEMPORARY: Hardcode a name that exists in your Firebase to test
        // Once the login part is done, change this back to:
        // loggedInUsername = getIntent().getStringExtra("USER_NAME");
        loggedInUsername = "kevin123";


        // 3. Set Button Listeners
        btnRequestService.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerDashboardActivity.this, CreateRequestActivity.class);
            // Pass username forward so new requests can be tagged with it
            intent.putExtra("USER_NAME", loggedInUsername);
            startActivity(intent);
        });

        btnRequestHistory.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerDashboardActivity.this, RequestHistoryActivity.class);
            intent.putExtra("USER_NAME", loggedInUsername);
            startActivity(intent);
        });

        // 4. Start counting if username exists
        if (loggedInUsername != null) {
            updateDashboardStats(loggedInUsername);
        }
    }

    // This method MUST be outside of onCreate
    private void updateDashboardStats(String username) {
        // Use your specific Asia regional URL
        String dbUrl = "https://aqua-connect-e3324-default-rtdb.firebaseio.com/";
        DatabaseReference ref = FirebaseDatabase.getInstance(dbUrl).getReference("service_requests");

        // Query to find only requests belonging to this username
        Query userRequestsQuery = ref.orderByChild("userName").equalTo(username);

        userRequestsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int active = 0;
                int completed = 0;

                for (DataSnapshot data : snapshot.getChildren()) {
                    ServiceRequest request = data.getValue(ServiceRequest.class);
                    if (request != null) {
                        String status = request.getStatus(); // This uses the getter in ServiceRequest.java

                        if ("Completed".equalsIgnoreCase(status)) {
                            completed++;
                        } else {
                            active++;
                        }
                    }
                }

                // Update the UI
                tvActiveCount.setText(String.valueOf(active));
                tvCompletedCount.setText(String.valueOf(completed));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CustomerDashboardActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}