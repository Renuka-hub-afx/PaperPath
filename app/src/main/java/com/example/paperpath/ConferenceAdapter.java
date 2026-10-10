package com.example.paperpath;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ConferenceAdapter extends RecyclerView.Adapter<ConferenceAdapter.ConferenceViewHolder> {

    public interface OnConferenceClickListener {
        void onConferenceClick(Conference conference);
    }

    private List<Conference> conferenceList;
    private final OnConferenceClickListener listener;

    public ConferenceAdapter(List<Conference> conferenceList, OnConferenceClickListener listener) {
        this.conferenceList = conferenceList;
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setConferences(List<Conference> list) {
        this.conferenceList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ConferenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_conference_summary, parent, false);
        return new ConferenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConferenceViewHolder holder, int position) {
        Conference conf = conferenceList.get(position);
        holder.bind(conf, listener);
    }

    @Override
    public int getItemCount() {
        return conferenceList != null ? conferenceList.size() : 0;
    }

    public static class ConferenceViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSummaryAcronym;
        private final TextView tvSummaryFullName;
        private final TextView tvSummaryRank;
        private final TextView tvSummaryDomain;
        private final TextView tvSummaryDeadline;

        public ConferenceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSummaryAcronym = itemView.findViewById(R.id.tvSummaryAcronym);
            tvSummaryFullName = itemView.findViewById(R.id.tvSummaryFullName);
            tvSummaryRank = itemView.findViewById(R.id.tvSummaryRank);
            tvSummaryDomain = itemView.findViewById(R.id.tvSummaryDomain);
            tvSummaryDeadline = itemView.findViewById(R.id.tvSummaryDeadline);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Conference conf, OnConferenceClickListener listener) {
            tvSummaryAcronym.setText(conf.getAcronym());
            tvSummaryFullName.setText(conf.getFullName());
            tvSummaryRank.setText(conf.getCoreRank());
            tvSummaryDomain.setText(conf.getDomainTag());
            tvSummaryDeadline.setText("⏰ " + conf.getSubmissionDeadline() + " (" + conf.getDaysLeft() + "d left)");

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onConferenceClick(conf);
                }
            });
        }
    }
}
