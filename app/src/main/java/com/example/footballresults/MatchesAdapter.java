package com.example.footballresults;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.ViewHolder> {
    private final List<Match> matchesList;
    private final Context context;
    private final DatabaseHelper databaseHelper;
    private final OnDataChangeListener onDataChangeListener;

    public MatchesAdapter(Context context, List<Match> matchesList, DatabaseHelper databaseHelper, OnDataChangeListener onDataChangeListener) {
        this.context = context;
        this.matchesList = matchesList;
        this.databaseHelper = databaseHelper;
        this.onDataChangeListener = onDataChangeListener;
    }
    public interface OnDataChangeListener {
        void onDataChanged();
    }

    @NonNull
    @Override
    public MatchesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Match match = matchesList.get(position);
        holder.dateTextView.setText(match.getDate());
        holder.cityTextView.setText(match.getCity());
        holder.teamATextView.setText(match.getTeamA());
        holder.teamBTextView.setText(match.getTeamB());
        String[] goals = match.getResult().split("-");
        holder.goalsATextView.setText(goals[0]);
        holder.goalsBTextView.setText(goals[1]);

        holder.btnDelete.setOnClickListener(v -> {

            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                int matchId = matchesList.get(currentPosition).getId();
                databaseHelper.deleteMatch(matchId);
                matchesList.remove(currentPosition);
                notifyItemRemoved(currentPosition);
                notifyItemRangeChanged(currentPosition, matchesList.size());
                if (onDataChangeListener != null) {
                    onDataChangeListener.onDataChanged();
                }
            }
        });


        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddMatchActivity.class);
            intent.putExtra("MATCH_ID", match.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return matchesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, cityTextView, teamATextView, goalsATextView, teamBTextView, goalsBTextView;
        Button btnDelete, btnEdit;

        public ViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            teamATextView = itemView.findViewById(R.id.teamATextView);
            goalsATextView = itemView.findViewById(R.id.goalsATextView);
            teamBTextView = itemView.findViewById(R.id.teamBTextView);
            goalsBTextView = itemView.findViewById(R.id.goalsBTextView);
            btnDelete = itemView.findViewById(R.id.btnDeleteMatch);
            btnEdit = itemView.findViewById(R.id.btnEditMatch);
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setMatches(List<Match> matches) {
        this.matchesList.clear();
        this.matchesList.addAll(matches);
        notifyDataSetChanged();
    }
}
