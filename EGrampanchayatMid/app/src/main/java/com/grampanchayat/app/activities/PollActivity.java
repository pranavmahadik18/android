package com.grampanchayat.app.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.grampanchayat.app.R;
import com.grampanchayat.app.database.DatabaseHelper;
import com.grampanchayat.app.database.SessionManager;
import com.grampanchayat.app.models.Poll;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PollActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private SessionManager session;
    private LinearLayout adminPanel, pollSection, noDataSection;
    private EditText etQuestion, etOpt1, etOpt2, etOpt3;
    private Button btnCreatePoll;
    private TextView tvQuestion, tvOpt1Votes, tvOpt2Votes, tvOpt3Votes, tvTotalVotes;
    private Button btnVote1, btnVote2, btnVote3;
    private ProgressBar pb1, pb2, pb3;
    private SharedPreferences votePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);

        db = DatabaseHelper.getInstance(this);
        session = new SessionManager(this);
        votePref = getSharedPreferences("VotePref", MODE_PRIVATE);

        adminPanel = findViewById(R.id.adminPanel);
        pollSection = findViewById(R.id.pollSection);
        noDataSection = findViewById(R.id.noDataSection);
        etQuestion = findViewById(R.id.etQuestion);
        etOpt1 = findViewById(R.id.etOpt1);
        etOpt2 = findViewById(R.id.etOpt2);
        etOpt3 = findViewById(R.id.etOpt3);
        btnCreatePoll = findViewById(R.id.btnCreatePoll);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvOpt1Votes = findViewById(R.id.tvOpt1Votes);
        tvOpt2Votes = findViewById(R.id.tvOpt2Votes);
        tvOpt3Votes = findViewById(R.id.tvOpt3Votes);
        tvTotalVotes = findViewById(R.id.tvTotalVotes);
        btnVote1 = findViewById(R.id.btnVote1);
        btnVote2 = findViewById(R.id.btnVote2);
        btnVote3 = findViewById(R.id.btnVote3);
        pb1 = findViewById(R.id.pb1);
        pb2 = findViewById(R.id.pb2);
        pb3 = findViewById(R.id.pb3);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        if (session.isAdmin()) {
            adminPanel.setVisibility(View.VISIBLE);
        } else {
            adminPanel.setVisibility(View.GONE);
        }

        btnCreatePoll.setOnClickListener(v -> {
            String q = etQuestion.getText().toString().trim();
            String o1 = etOpt1.getText().toString().trim();
            String o2 = etOpt2.getText().toString().trim();
            String o3 = etOpt3.getText().toString().trim();
            if (q.isEmpty() || o1.isEmpty() || o2.isEmpty()) {
                Toast.makeText(this, "Fill question and at least 2 options / प्रश्न आणि किमान 2 पर्याय भरा", Toast.LENGTH_SHORT).show();
                return;
            }
            String date = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
            if (db.addPoll(q, o1, o2, o3.isEmpty() ? "" : o3, date)) {
                Toast.makeText(this, "Poll created! / मतदान तयार!", Toast.LENGTH_SHORT).show();
                etQuestion.setText(""); etOpt1.setText(""); etOpt2.setText(""); etOpt3.setText("");
                // Reset vote tracking for new poll
                votePref.edit().clear().apply();
                loadPoll();
            }
        });

        loadPoll();
    }

    private void loadPoll() {
        Poll poll = db.getActivePoll();
        if (poll == null) {
            pollSection.setVisibility(View.GONE);
            noDataSection.setVisibility(View.VISIBLE);
            return;
        }
        pollSection.setVisibility(View.VISIBLE);
        noDataSection.setVisibility(View.GONE);

        tvQuestion.setText(poll.question);

        int total = poll.votes1 + poll.votes2 + poll.votes3;
        tvTotalVotes.setText("Total Votes / एकूण मते: " + total);

        // Buttons text
        btnVote1.setText(poll.option1);
        btnVote2.setText(poll.option2);
        btnVote3.setText(poll.option3.isEmpty() ? "(N/A)" : poll.option3);
        btnVote3.setVisibility(poll.option3.isEmpty() ? View.GONE : View.VISIBLE);

        // Vote counts
        tvOpt1Votes.setText(poll.option1 + ": " + poll.votes1 + " votes");
        tvOpt2Votes.setText(poll.option2 + ": " + poll.votes2 + " votes");
        tvOpt3Votes.setText((poll.option3.isEmpty() ? "" : poll.option3 + ": " + poll.votes3 + " votes"));

        // Progress bars
        if (total > 0) {
            pb1.setProgress((int)(poll.votes1 * 100.0 / total));
            pb2.setProgress((int)(poll.votes2 * 100.0 / total));
            pb3.setProgress((int)(poll.votes3 * 100.0 / total));
        } else {
            pb1.setProgress(0); pb2.setProgress(0); pb3.setProgress(0);
        }

        boolean hasVoted = votePref.getBoolean("voted_" + poll.id, false);
        if (hasVoted || session.isAdmin()) {
            btnVote1.setEnabled(false);
            btnVote2.setEnabled(false);
            btnVote3.setEnabled(false);
            if (hasVoted) {
                btnVote1.setText(btnVote1.getText() + " ✓");
            }
        } else {
            btnVote1.setEnabled(true);
            btnVote2.setEnabled(true);
            btnVote3.setEnabled(true);
        }

        final int pollId = poll.id;
        btnVote1.setOnClickListener(v -> castVote(pollId, 1));
        btnVote2.setOnClickListener(v -> castVote(pollId, 2));
        btnVote3.setOnClickListener(v -> castVote(pollId, 3));
    }

    private void castVote(int pollId, int option) {
        db.voteOnPoll(pollId, option);
        votePref.edit().putBoolean("voted_" + pollId, true).apply();
        Toast.makeText(this, "Vote cast! / मत दिले!", Toast.LENGTH_SHORT).show();
        loadPoll();
    }
}
