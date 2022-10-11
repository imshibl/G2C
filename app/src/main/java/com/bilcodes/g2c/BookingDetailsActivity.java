package com.bilcodes.g2c;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.Objects;

public class BookingDetailsActivity extends AppCompatActivity {

    ShapeableImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        Objects.requireNonNull(getSupportActionBar()).hide();


    }
}