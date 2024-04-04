package com.example.footballresults;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        RecyclerView standingsRecyclerView = findViewById(R.id.standingsRecyclerView);
        standingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
        List<Standing> standings = databaseHelper.getStandings();

        StandingsAdapter standingsAdapter = new StandingsAdapter(standings);
        standingsRecyclerView.setAdapter(standingsAdapter);
    }
}
