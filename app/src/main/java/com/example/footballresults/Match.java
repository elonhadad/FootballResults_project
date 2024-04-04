package com.example.footballresults;

public class Match {
    private int id;
    private String city;
    private String date;
    private String teamA;
    private String teamB;
    private String result;

    public Match(int id, String city, String date, String teamA, String teamB, String result) {
        this.id = id;
        this.city = city;
        this.date = date;
        this.teamA = teamA;
        this.teamB = teamB;
        this.result = result;
    }

    public Match() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getCity() {
        return city;
    }
    public String getDate() {
        return date;
    }
    public String getTeamA() {
        return teamA;
    }
    public String getTeamB() {
        return teamB;
    }
    public String getResult() {
        return result;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }
    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }
    public void setResult(String result) {
        this.result = result;
    }
}
