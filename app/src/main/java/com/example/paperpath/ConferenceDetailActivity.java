package com.example.paperpath;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

@SuppressWarnings("SpellCheckingInspection")
public class ConferenceDetailActivity extends AppCompatActivity {

    public static final String EXTRA_CONFERENCE = "EXTRA_CONFERENCE";

    private ImageButton btnBackFromDetail;
    private ImageButton btnBookmarkDetail;
    private TextView tvDetailDomainTag;
    private TextView tvDetailCoreRank;
    private TextView tvDetailAcronym;
    private TextView tvDetailFullName;
    private TextView tvDetailTopics;
    private TextView tvDetailVenue;
    private TextView tvDetailDates;
    private TextView tvDetailSubmissionDeadline;
    private TextView tvDetailNotificationDate;
    private MaterialButton btnSyncToCalendar;
    private MaterialButton btnOfficialPortal;

    private Conference conference;
    private boolean isBookmarked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_conference_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayoutDetail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            conference = getIntent().getSerializableExtra(EXTRA_CONFERENCE, Conference.class);
        } else {
            conference = (Conference) getIntent().getSerializableExtra(EXTRA_CONFERENCE);
        }

        initViews();
        populateData();
        setupListeners();
    }

    private void initViews() {
        btnBackFromDetail = findViewById(R.id.btnBackFromDetail);
        btnBookmarkDetail = findViewById(R.id.btnBookmarkDetail);
        tvDetailDomainTag = findViewById(R.id.tvDetailDomainTag);
        tvDetailCoreRank = findViewById(R.id.tvDetailCoreRank);
        tvDetailAcronym = findViewById(R.id.tvDetailAcronym);
        tvDetailFullName = findViewById(R.id.tvDetailFullName);
        tvDetailTopics = findViewById(R.id.tvDetailTopics);
        tvDetailVenue = findViewById(R.id.tvDetailVenue);
        tvDetailDates = findViewById(R.id.tvDetailDates);
        tvDetailSubmissionDeadline = findViewById(R.id.tvDetailSubmissionDeadline);
        tvDetailNotificationDate = findViewById(R.id.tvDetailNotificationDate);
        btnSyncToCalendar = findViewById(R.id.btnSyncToCalendar);
        btnOfficialPortal = findViewById(R.id.btnOfficialPortal);
    }

    @SuppressLint("SetTextI18n")
    private void populateData() {
        if (conference == null) return;

        tvDetailDomainTag.setText(conference.getDomainTag());
        tvDetailCoreRank.setText(conference.getCoreRank());
        tvDetailAcronym.setText(conference.getAcronym());
        tvDetailFullName.setText(conference.getFullName());
        tvDetailTopics.setText("Topic: " + conference.getTopics());
        tvDetailVenue.setText("📍 Venue: " + conference.getVenue());
        tvDetailDates.setText("🗓 Conference Dates: " + conference.getConfDates());
        tvDetailSubmissionDeadline.setText("⏰ SUBMISSION DEADLINE: " + conference.getSubmissionDeadline() + " (" + conference.getDaysLeft() + " Days Left)");
        tvDetailNotificationDate.setText("📢 Notification: " + conference.getNotificationDate());
        isBookmarked = conference.isBookmarked();
    }

    private void setupListeners() {
        btnBackFromDetail.setOnClickListener(v -> finish());

        btnBookmarkDetail.setOnClickListener(v -> {
            isBookmarked = !isBookmarked;
            if (isBookmarked) {
                Toast.makeText(this, "Added " + conference.getAcronym() + " to Watchlist", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Removed from Watchlist", Toast.LENGTH_SHORT).show();
            }
        });

        btnSyncToCalendar.setOnClickListener(v -> syncToDeviceCalendar());

        btnOfficialPortal.setOnClickListener(v -> {
            if (conference != null && conference.getOfficialUrl() != null) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(conference.getOfficialUrl()));
                startActivity(browserIntent);
            }
        });
    }

    private void syncToDeviceCalendar() {
        if (conference == null) return;

        long startTime = System.currentTimeMillis() + (long) conference.getDaysLeft() * 24 * 60 * 60 * 1000;
        long endTime = startTime + 60 * 60 * 1000;

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
                .putExtra(CalendarContract.Events.TITLE, "[Paper Deadline] " + conference.getAcronym())
                .putExtra(CalendarContract.Events.DESCRIPTION, conference.getFullName() + "\nTopics: " + conference.getTopics() + "\nOfficial Link: " + conference.getOfficialUrl())
                .putExtra(CalendarContract.Events.EVENT_LOCATION, conference.getVenue())
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open calendar application", Toast.LENGTH_SHORT).show();
        }
    }
}
