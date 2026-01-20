package com.kevin.aquaconnect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        // 1. Initialize the Cards
        CardView phoneCard = findViewById(R.id.phoneCard);
        CardView emailCard = findViewById(R.id.emailCard);
        CardView addressCard = findViewById(R.id.addressCard);
        ImageButton ivLogout = findViewById(R.id.ivLogout);

        // 2. Click to Dial
        phoneCard.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+94112861295"));
            startActivity(intent);
        });

        // 3. Click to Email
        emailCard.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:info@hydroaqua.lk"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Inquiry from AquaConnect App");
            startActivity(intent);
        });

        // 4. Click to Open Maps
        addressCard.setOnClickListener(v -> {
            String uri = "geo:6.9038,79.9035?q=523, Kotte Road, Pitakotte, Sri Lanka";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        });

        // 5. Logout Button
        ivLogout.setOnClickListener(v -> {
            finish();
        });
    }
}