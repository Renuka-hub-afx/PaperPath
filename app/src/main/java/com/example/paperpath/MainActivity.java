package com.example.paperpath;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("SpellCheckingInspection")
public class MainActivity extends AppCompatActivity implements ConferenceAdapter.OnConferenceClickListener {

    private TextView tvFeedSubtitle;
    private RecyclerView rvConferences;
    private ConferenceAdapter adapter;

    private String userEmail;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        userEmail = intent.getStringExtra("USER_EMAIL");
        userName = intent.getStringExtra("USER_NAME");

        initViews();
        setupRecyclerView();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh feed list based on any updated domain tracks from Settings
        loadConferences();
    }

    private void initViews() {
        tvFeedSubtitle = findViewById(R.id.tvFeedSubtitle);
        rvConferences = findViewById(R.id.rvConferences);
    }

    private void setupRecyclerView() {
        rvConferences.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ConferenceAdapter(new ArrayList<>(), this);
        rvConferences.setAdapter(adapter);
    }

    private void setupListeners() {
        ImageButton btnOpenSettings = findViewById(R.id.btnOpenSettings);
        btnOpenSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.putExtra("USER_EMAIL", userEmail);
            intent.putExtra("USER_NAME", userName);
            startActivity(intent);
        });
    }

    @SuppressLint("SetTextI18n")
    private void loadConferences() {
        SharedPreferences prefs = getSharedPreferences(TopicSelectionActivity.PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> userTracks = prefs.getStringSet(TopicSelectionActivity.KEY_USER_TRACKS, null);

        List<Conference> allConferences = getMockConferenceData();
        List<Conference> filteredConferences = new ArrayList<>();

        if (userTracks != null && !userTracks.isEmpty()) {
            for (Conference conf : allConferences) {
                // If any user track matches the conference domain, include it
                for (String track : userTracks) {
                    if (track.toLowerCase().contains(conf.getDomainTag().toLowerCase()) ||
                            conf.getDomainTag().toLowerCase().contains(track.toLowerCase()) ||
                            conf.getTopics().toLowerCase().contains(track.toLowerCase())) {
                        filteredConferences.add(conf);
                        break;
                    }
                }
            }
        }

        // If no filter match or no tracks selected, display all top conferences
        if (filteredConferences.isEmpty()) {
            filteredConferences = allConferences;
            tvFeedSubtitle.setText("Showing all top upcoming conferences (" + filteredConferences.size() + ")");
        } else {
            tvFeedSubtitle.setText("Matched to your research tracks (" + filteredConferences.size() + " conferences)");
        }

        adapter.setConferences(filteredConferences);
    }

    @Override
    public void onConferenceClick(Conference conference) {
        // Tapping on conference name opens the complete card in ConferenceDetailActivity
        Intent intent = new Intent(MainActivity.this, ConferenceDetailActivity.class);
        intent.putExtra(ConferenceDetailActivity.EXTRA_CONFERENCE, conference);
        startActivity(intent);
    }

    private List<Conference> getMockConferenceData() {
        List<Conference> list = new ArrayList<>();

        list.add(new Conference(
                "1",
                "CVPR 2026",
                "IEEE/CVF Conference on Computer Vision and Pattern Recognition",
                "Computer Vision (CV)",
                "CORE A*",
                "Seattle, WA, USA (Hybrid)",
                "June 17 – June 21, 2026",
                "Nov 14, 2026",
                35,
                "Feb 28, 2027",
                "Object Detection, Generative Models, 3D Vision, Visual Transformer",
                "https://cvpr.thecvf.com"
        ));

        list.add(new Conference(
                "2",
                "ICML 2026",
                "International Conference on Machine Learning",
                "Artificial Intelligence (AI)",
                "CORE A*",
                "Vienna, Austria & Online",
                "July 12 – July 18, 2026",
                "Oct 28, 2026",
                18,
                "Dec 05, 2026",
                "Deep Learning, Reinforcement Learning, Foundation Models, Optimization",
                "https://icml.cc"
        ));

        list.add(new Conference(
                "3",
                "ACL 2026",
                "Annual Meeting of the Association for Computational Linguistics",
                "Natural Language Processing (NLP)",
                "CORE A*",
                "Toronto, Canada (Hybrid)",
                "August 02 – August 07, 2026",
                "Dec 15, 2026",
                66,
                "March 20, 2027",
                "Large Language Models, Multilingual NLP, Dialogue Systems, Text Summarization",
                "https://www.aclweb.org"
        ));

        list.add(new Conference(
                "4",
                "NeurIPS 2026",
                "Conference on Neural Information Processing Systems",
                "Machine Learning & Deep Learning",
                "CORE A*",
                "New Orleans, LA & Virtual",
                "Dec 08 – Dec 14, 2026",
                "May 18, 2026",
                220,
                "Sept 25, 2026",
                "Neural Computing, Deep Learning Theory, Statistical Learning, Generative AI",
                "https://nips.cc"
        ));

        list.add(new Conference(
                "5",
                "OSDI 2026",
                "USENIX Symposium on Operating Systems Design and Implementation",
                "Distributed & Operating Systems",
                "CORE A*",
                "Boston, MA, USA",
                "July 08 – July 10, 2026",
                "Nov 30, 2026",
                51,
                "Feb 15, 2027",
                "Distributed Systems, Cloud Infrastructure, Kernel Architecture, Storage",
                "https://www.usenix.org"
        ));

        list.add(new Conference(
                "6",
                "IEEE S&P 2026",
                "IEEE Symposium on Security and Privacy (Oakland)",
                "Cybersecurity & Cryptography",
                "CORE A*",
                "San Francisco, CA, USA",
                "May 18 – May 21, 2026",
                "Nov 05, 2026",
                26,
                "Jan 10, 2027",
                "Zero Trust, Cryptography, Privacy Preserving Machine Learning, Web Security",
                "https://sp2026.ieee-security.org"
        ));

        list.add(new Conference(
                "7",
                "ACM SenSys 2026",
                "ACM Conference on Embedded Networked Sensor Systems",
                "Internet of Things (IoT)",
                "CORE A",
                "Yokohama, Japan",
                "Nov 04 – Nov 07, 2026",
                "April 20, 2026",
                192,
                "July 15, 2026",
                "Embedded AI, Wearable Sensing, Edge Computing, Smart Cities",
                "https://sensys.acm.org"
        ));

        list.add(new Conference(
                "8",
                "SIGMOD 2026",
                "ACM SIGMOD International Conference on Management of Data",
                "Data Science & Big Data",
                "CORE A*",
                "Berlin, Germany",
                "June 21 – June 26, 2026",
                "Oct 15, 2026",
                5,
                "Dec 20, 2026",
                "Distributed Query Engines, Vector Databases, Graph Analytics, Real-time Processing",
                "https://sigmod.org"
        ));

        return list;
    }
}