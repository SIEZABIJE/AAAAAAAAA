package com.example.restapi;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;

public class PersonDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);

        String imageUrl = getIntent().getStringExtra("image");
        String title = getIntent().getStringExtra("title");
        String name = getIntent().getStringExtra("name");
        String gender = getIntent().getStringExtra("gender");
        String location = getIntent().getStringExtra("location");
        String email = getIntent().getStringExtra("email");
        String phone = getIntent().getStringExtra("phone");

        ImageView imageView = findViewById(R.id.detailImage);
        TextView titleText = findViewById(R.id.detailTitle);
        TextView nameText = findViewById(R.id.detailName);
        TextView genderText = findViewById(R.id.detailGender);
        TextView locationText = findViewById(R.id.detailLocation);
        TextView emailText = findViewById(R.id.detailEmail);
        TextView phoneText = findViewById(R.id.detailPhone);

        Picasso.get().load(imageUrl).into(imageView);
        titleText.setText("Title: " + title);
        nameText.setText("Name: " + name);
        genderText.setText("Gender: " + gender);
        locationText.setText("Location: " + location);
        emailText.setText("Email: " + email);
        phoneText.setText("Phone: " + phone);
    }
} 