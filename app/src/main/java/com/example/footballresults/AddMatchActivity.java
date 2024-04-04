package com.example.footballresults;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddMatchActivity extends AppCompatActivity {

    private EditText editTextCity, editTextDate, editTextTeamA, editTextTeamB, editTextResult;
    private DatabaseHelper databaseHelper;
    private boolean isEditMode = false;
    private int editMatchId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_match);
        initializeViews();
        databaseHelper = DatabaseHelper.getInstance(this);
        checkEditMode();
    }

    private void initializeViews() {
        editTextCity = findViewById(R.id.editTextCity);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTeamA = findViewById(R.id.editTextTeamA);
        editTextTeamB = findViewById(R.id.editTextTeamB);
        editTextResult = findViewById(R.id.editTextResult);
        Button btnSaveMatch = findViewById(R.id.btnSaveMatch);

        btnSaveMatch.setOnClickListener(v -> {
            if (isEditMode) {
                updateMatch();
            } else {
                saveMatch();
            }
        });
        editTextDate.setOnClickListener(v -> showDatePickerDialog());
    }

    private void checkEditMode() {
        if (getIntent().hasExtra("MATCH_ID")) {
            isEditMode = true;
            editMatchId = getIntent().getIntExtra("MATCH_ID", -1);
            Match match = databaseHelper.getMatch(editMatchId);
            if (match != null) {
                populateFields(match);
            }
        }
    }

    private void populateFields(Match match) {
        editTextCity.setText(match.getCity());
        editTextDate.setText(match.getDate());
        editTextTeamA.setText(match.getTeamA());
        editTextTeamB.setText(match.getTeamB());
        editTextResult.setText(match.getResult());
    }

    private void saveMatch() {
        String city = editTextCity.getText().toString();
        String date = editTextDate.getText().toString();
        String teamA = editTextTeamA.getText().toString();
        String teamB = editTextTeamB.getText().toString();
        String result = editTextResult.getText().toString();
        databaseHelper.addMatch(city, date, teamA, teamB, result);
        finish();
    }

    private void updateMatch() {
        String city = editTextCity.getText().toString();
        String date = editTextDate.getText().toString();
        String teamA = editTextTeamA.getText().toString();
        String teamB = editTextTeamB.getText().toString();
        String result = editTextResult.getText().toString();
        databaseHelper.updateMatch(editMatchId, city, date, teamA, teamB, result);
        finish();
    }

    public void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (datePicker, year1, month1, day) -> {
                    @SuppressLint("DefaultLocale") String selectedDate = String.format("%d/%02d/%02d", day, month1 + 1, year1);
                    editTextDate.setText(selectedDate);
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }
}
