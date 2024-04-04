package com.example.footballresults;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewMatchesActivity extends AppCompatActivity implements MatchesAdapter.OnDataChangeListener {
    private MatchesAdapter adapter;
    private DatabaseHelper databaseHelper;
    @Override
    public void onDataChanged() {
        refreshMatchesList();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_matches);

        RecyclerView matchesRecyclerView = findViewById(R.id.matchesRecyclerView);
        matchesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = DatabaseHelper.getInstance(this);
        List<Match> matches = databaseHelper.getAllMatches();
        adapter = new MatchesAdapter(this, matches,databaseHelper,this);
        matchesRecyclerView.setAdapter(adapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        refreshMatchesList();
    }
    private void refreshMatchesList() {
        List<Match> matches = databaseHelper.getAllMatches();
        adapter.setMatches(matches);
    }
}
