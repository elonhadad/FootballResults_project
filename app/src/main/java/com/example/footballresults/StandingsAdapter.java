package com.example.footballresults;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StandingsAdapter extends RecyclerView.Adapter<StandingsAdapter.ViewHolder> {

    private final List<Standing> standingsList;

    public StandingsAdapter(List<Standing> standingsList) {
        this.standingsList = standingsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_standing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Standing standing = standingsList.get(position);
        holder.rankTextView.setText(String.valueOf(position + 1));
        holder.teamNameTextView.setText(standing.getTeamName());
        holder.gamesTextView.setText(String.valueOf(standing.getGames()));
        holder.winTextView.setText(String.valueOf(standing.getWins()));
        holder.lossTextView.setText(String.valueOf(standing.getLosses()));
        holder.drawTextView.setText(String.valueOf(standing.getDraws()));
        holder.goalsForTextView.setText(String.valueOf(standing.getGoalsFor()));
        holder.goalsAgainstTextView.setText(String.valueOf(standing.getGoalsAgainst()));
        holder.goalDifferenceTextView.setText(String.valueOf(standing.getGoalDifference()));
        holder.pointsTextView.setText(String.valueOf(standing.getPoints()));
    }

    @Override
    public int getItemCount() {
        return standingsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView teamNameTextView, gamesTextView, winTextView, lossTextView, drawTextView, goalsForTextView, goalsAgainstTextView, goalDifferenceTextView, pointsTextView,rankTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            rankTextView = itemView.findViewById(R.id.rankTextView);
            teamNameTextView = itemView.findViewById(R.id.teamNameTextView);
            gamesTextView = itemView.findViewById(R.id.gamesTextView);
            winTextView = itemView.findViewById(R.id.winTextView);
            lossTextView = itemView.findViewById(R.id.lossTextView);
            drawTextView = itemView.findViewById(R.id.drawTextView);
            goalsForTextView = itemView.findViewById(R.id.goalFTextView);
            goalsAgainstTextView = itemView.findViewById(R.id.goalATextView);
            goalDifferenceTextView = itemView.findViewById(R.id.goalDTextView);
            pointsTextView = itemView.findViewById(R.id.pointsTextView);
        }
    }
}
