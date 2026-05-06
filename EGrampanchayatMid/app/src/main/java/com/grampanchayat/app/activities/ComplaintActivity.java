package com.grampanchayat.app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.grampanchayat.app.R;
import com.grampanchayat.app.database.DatabaseHelper;
import com.grampanchayat.app.database.SessionManager;
import com.grampanchayat.app.models.Complaint;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ComplaintActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private SessionManager session;
    private LinearLayout userPanel, listContainer;
    private EditText etName, etPhone, etIssue;
    private Button btnSubmit;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        db = DatabaseHelper.getInstance(this);
        session = new SessionManager(this);

        userPanel = findViewById(R.id.userPanel);
        listContainer = findViewById(R.id.listContainer);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etIssue = findViewById(R.id.etIssue);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvEmpty = findViewById(R.id.tvEmpty);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        if (session.isAdmin()) {
            userPanel.setVisibility(View.GONE);
        } else {
            userPanel.setVisibility(View.VISIBLE);
        }

        btnSubmit.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String issue = etIssue.getText().toString().trim();
            if (name.isEmpty() || issue.isEmpty()) {
                Toast.makeText(this, "Fill required fields / आवश्यक माहिती भरा", Toast.LENGTH_SHORT).show();
                return;
            }
            String date = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
            if (db.addComplaint(name, phone, issue, date)) {
                Toast.makeText(this, "Complaint submitted! / तक्रार नोंदवली!", Toast.LENGTH_SHORT).show();
                etName.setText(""); etPhone.setText(""); etIssue.setText("");
                loadComplaints();
            }
        });

        loadComplaints();
    }

    private void loadComplaints() {
        listContainer.removeAllViews();
        List<Complaint> list = db.getAllComplaints();
        if (list.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            for (Complaint c : list) {
                View card = getLayoutInflater().inflate(R.layout.item_complaint, listContainer, false);
                TextView tvName = card.findViewById(R.id.tvName);
                TextView tvIssue = card.findViewById(R.id.tvIssue);
                TextView tvStatus = card.findViewById(R.id.tvStatus);
                TextView tvDate = card.findViewById(R.id.tvDate);
                Button btnResolve = card.findViewById(R.id.btnResolve);

                tvName.setText(c.name + (c.phone.isEmpty() ? "" : " | " + c.phone));
                tvIssue.setText(c.issue);
                tvDate.setText(c.date);

                if ("Resolved".equals(c.status)) {
                    tvStatus.setText("✅ Resolved / निराकरण");
                    tvStatus.setTextColor(getResources().getColor(R.color.green));
                } else {
                    tvStatus.setText("⏳ Pending / प्रलंबित");
                    tvStatus.setTextColor(getResources().getColor(R.color.orange));
                }

                if (session.isAdmin() && !"Resolved".equals(c.status)) {
                    btnResolve.setVisibility(View.VISIBLE);
                    btnResolve.setOnClickListener(v -> {
                        db.updateComplaintStatus(c.id, "Resolved");
                        Toast.makeText(this, "Marked Resolved / निराकरण केले", Toast.LENGTH_SHORT).show();
                        loadComplaints();
                    });
                } else {
                    btnResolve.setVisibility(View.GONE);
                }

                listContainer.addView(card);
            }
        }
    }
}
