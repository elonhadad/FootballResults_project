package com.example.footballresults;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnViewTable = findViewById(R.id.btnViewTable);
        Button btnAddMatch = findViewById(R.id.btnAddMatch);
        Button btnViewMatches = findViewById(R.id.btnViewMatches);
        Button btnSearch;

        btnAddMatch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddMatchActivity.class);
            startActivity(intent);
        });
        btnViewTable.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TableActivity.class);
            startActivity(intent);
        });

        btnViewMatches.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewMatchesActivity.class);
            startActivity(intent);
        });

        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchMatchesActivity.class);
            startActivity(intent);
        });

    }
}
