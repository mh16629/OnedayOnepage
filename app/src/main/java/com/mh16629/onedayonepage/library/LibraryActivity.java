package com.mh16629.onedayonepage.library;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mh16629.onedayonepage.R;

public class LibraryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        setTitle(R.string.activity_library);

    }
}
