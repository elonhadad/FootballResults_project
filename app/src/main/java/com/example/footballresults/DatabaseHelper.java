package com.example.footballresults;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "footballResultsDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_MATCHES = "matches";
    private static final String KEY_MATCH_ID = "id";
    private static final String KEY_CITY = "city";
    private static final String KEY_DATE = "date";
    private static final String KEY_TEAM_A = "teamA";
    private static final String KEY_TEAM_B = "teamB";
    private static final String KEY_RESULT = "result";
    private static DatabaseHelper instance;


    private DatabaseHelper(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MATCHES_TABLE = "CREATE TABLE " + TABLE_MATCHES + "(" +
                KEY_MATCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_CITY + " TEXT," +
                KEY_DATE + " TEXT," +
                KEY_TEAM_A + " TEXT," +
                KEY_TEAM_B + " TEXT," +
                KEY_RESULT + " TEXT" + ")";
        db.execSQL(CREATE_MATCHES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCHES);
            onCreate(db);
        }
    }

    public void addMatch(String city, String date, String teamA, String teamB, String result) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_CITY, city);
            values.put(KEY_DATE, date);
            values.put(KEY_TEAM_A, teamA);
            values.put(KEY_TEAM_B, teamB);
            values.put(KEY_RESULT, result);

            db.insertOrThrow(TABLE_MATCHES, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error while trying to add match to database", e);
        } finally {
            db.endTransaction();
        }
    }
    public List<Match> getAllMatches() {
        List<Match> matches = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MATCHES, null);

        try {
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(KEY_MATCH_ID);
                int cityIndex = cursor.getColumnIndex(KEY_CITY);
                int dateIndex = cursor.getColumnIndex(KEY_DATE);
                int teamAIndex = cursor.getColumnIndex(KEY_TEAM_A);
                int teamBIndex = cursor.getColumnIndex(KEY_TEAM_B);
                int resultIndex = cursor.getColumnIndex(KEY_RESULT);

                do {
                    if (idIndex != -1 && cityIndex != -1 && dateIndex != -1 && teamAIndex != -1 && teamBIndex != -1 && resultIndex != -1) {
                        int id = cursor.getInt(idIndex);
                        String city = cursor.getString(cityIndex);
                        String date = cursor.getString(dateIndex);
                        String teamA = cursor.getString(teamAIndex);
                        String teamB = cursor.getString(teamBIndex);
                        String result = cursor.getString(resultIndex);

                        Match match = new Match(id, city, date, teamA, teamB, result);
                        matches.add(match);
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return matches;
    }

    public List<Standing> getStandings() {
        Map<String, Standing> standingsMap = new HashMap<>();
        String selectQuery = "SELECT * FROM matches";
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery(selectQuery, null)) {

            while (cursor.moveToNext()) {
                String teamA = cursor.getString(cursor.getColumnIndexOrThrow("teamA"));
                String teamB = cursor.getString(cursor.getColumnIndexOrThrow("teamB"));
                String result = cursor.getString(cursor.getColumnIndexOrThrow("result"));
                String[] goals = result.split("-");
                int goalsForA = Integer.parseInt(goals[0]);
                int goalsForB = Integer.parseInt(goals[1]);

                updateStandings(standingsMap, teamA, goalsForA, goalsForB);
                updateStandings(standingsMap, teamB, goalsForB, goalsForA);
            }
        }

        List<Standing> standingsList = new ArrayList<>(standingsMap.values());
        standingsList.sort(Comparator.comparingInt(Standing::getPoints).thenComparingInt(s -> s.getGoalsFor() - s.getGoalsAgainst()).reversed());
        return standingsList;
    }

    private void updateStandings(Map<String, Standing> standingsMap, String team, int goalsFor, int goalsAgainst) {
        Standing standing = standingsMap.getOrDefault(team, new Standing(team, 0,0, 0, 0, 0, 0, 0, 0));

        assert standing != null;
        standing.setGames(standing.getGames() +1);
        standing.setGoalsFor(standing.getGoalsFor() + goalsFor);
        standing.setGoalsAgainst(standing.getGoalsAgainst() + goalsAgainst);
        standing.setGoalDifference(standing.getGoalDifference() + (goalsFor - goalsAgainst));

        if (goalsFor > goalsAgainst) {
            // Win
            standing.setWins(standing.getWins() + 1);
            standing.setPoints(standing.getPoints() + 3);
        } else if (goalsFor == goalsAgainst) {
            // Draw
            standing.setDraws(standing.getDraws() + 1);
            standing.setPoints(standing.getPoints() + 1);
        } else {
            // Loss
            standing.setLosses(standing.getLosses() + 1);
        }
        standingsMap.put(team, standing);
    }
    public void deleteMatch(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deleteCount = db.delete(TABLE_MATCHES, "id = ?", new String[]{String.valueOf(id)});
        Log.d("DatabaseHelper", "Deleted rows: " + deleteCount);
        db.close();
    }

    public Match getMatch(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MATCHES, new String[] { "id", "city", "date", "teamA", "teamB", "result" }, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Match match = new Match(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
            cursor.close();
            return match;
        }
        return null;
    }
    public void updateMatch(int id, String city, String date, String teamA, String teamB, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("city", city);
        values.put("date", date);
        values.put("teamA", teamA);
        values.put("teamB", teamB);
        values.put("result", result);
        db.update(TABLE_MATCHES, values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Match> getMatchesByTeam(String teamName) {
        List<Match> matches = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MATCHES, null, "teamA=? OR teamB=?", new String[]{teamName, teamName}, null, null, null);

        int idIndex = cursor.getColumnIndex(KEY_MATCH_ID);
        int cityIndex = cursor.getColumnIndex(KEY_CITY);
        int dateIndex = cursor.getColumnIndex(KEY_DATE);
        int teamAIndex = cursor.getColumnIndex(KEY_TEAM_A);
        int teamBIndex = cursor.getColumnIndex(KEY_TEAM_B);
        int resultIndex = cursor.getColumnIndex(KEY_RESULT);

        if (cursor.moveToFirst()) {
            do {
                if (idIndex != -1 && cityIndex != -1 && dateIndex != -1 && teamAIndex != -1 && teamBIndex != -1 && resultIndex != -1) {
                    Match match = new Match(
                            cursor.getInt(idIndex),
                            cursor.getString(cityIndex),
                            cursor.getString(dateIndex),
                            cursor.getString(teamAIndex),
                            cursor.getString(teamBIndex),
                            cursor.getString(resultIndex)
                    );
                    matches.add(match);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return matches;
    }
    public List<String> getAllTeams() {
        List<String> teams = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT DISTINCT teamA FROM " + TABLE_MATCHES + " UNION SELECT DISTINCT teamB FROM " + TABLE_MATCHES;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            String team = cursor.getString(0);
            teams.add(team);
        }
        cursor.close();
        return teams;
    }
}
