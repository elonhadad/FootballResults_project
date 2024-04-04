package com.example.footballresults;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchMatchesActivity extends AppCompatActivity {
    private RecyclerView searchResultsRecyclerView;
    private DatabaseHelper databaseHelper;
    private Spinner teamSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_matches);

        teamSpinner = findViewById(R.id.teamSpinner);

        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);

        databaseHelper = DatabaseHelper.getInstance(this);

        loadTeamsList();
        teamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTeam = (String) parent.getItemAtPosition(position);
                if (!selectedTeam.equals("בחר")) {
                    performSearch(selectedTeam);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void performSearch(String teamName) {
        List<Match> matches = databaseHelper.getMatchesByTeam(teamName);
        MatchesAdapter adapter = new MatchesAdapter(this, matches, databaseHelper, null); // Pass null or implement OnDataChangeListener interface
        searchResultsRecyclerView.setAdapter(adapter);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private void loadTeamsList() {
        List<String> teams = new ArrayList<>();
        teams.add("בחר");
        teams.addAll(databaseHelper.getAllTeams());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teams);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamSpinner.setAdapter(adapter);
    }
}
