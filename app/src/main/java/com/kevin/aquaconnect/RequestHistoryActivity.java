package com.kevin.aquaconnect;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class RequestHistoryActivity extends AppCompatActivity {

    private RequestAdapter adapter;
    private List<ServiceRequest> requestList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_history);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewHistory);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        requestList = new ArrayList<>();
        adapter = new RequestAdapter(requestList);

        if (recyclerView != null) {
            recyclerView.setAdapter(adapter);
        }

        String dbUrl = "https://aqua-connect-e3324-default-rtdb.firebaseio.com/";
        databaseReference = FirebaseDatabase.getInstance(dbUrl).getReference("service_requests");

        loadHistoryFromFirebase();
    }

    private void loadHistoryFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        ServiceRequest request = data.getValue(ServiceRequest.class);
                        if (request != null) {
                            requestList.add(request);
                        }
                    }
                } else {
                    Toast.makeText(RequestHistoryActivity.this, "No data found in database", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RequestHistoryActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}