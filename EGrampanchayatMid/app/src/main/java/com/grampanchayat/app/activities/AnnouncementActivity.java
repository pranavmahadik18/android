package com.grampanchayat.app.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.grampanchayat.app.R;
import com.grampanchayat.app.database.DatabaseHelper;
import com.grampanchayat.app.database.SessionManager;
import com.grampanchayat.app.models.Announcement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AnnouncementActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private SessionManager session;
    private LinearLayout adminPanel;
    private EditText etTitle, etContent;
    private Button btnAdd;
    private LinearLayout listContainer;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        db = DatabaseHelper.getInstance(this);
        session = new SessionManager(this);

        adminPanel = findViewById(R.id.adminPanel);
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        btnAdd = findViewById(R.id.btnAdd);
        listContainer = findViewById(R.id.listContainer);
        tvEmpty = findViewById(R.id.tvEmpty);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        if (session.isAdmin()) {
            adminPanel.setVisibility(View.VISIBLE);
        } else {
            adminPanel.setVisibility(View.GONE);
        }

        btnAdd.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String content = etContent.getText().toString().trim();
            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Fill all fields / सर्व माहिती भरा", Toast.LENGTH_SHORT).show();
                return;
            }
            String date = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
            if (db.addAnnouncement(title, content, date)) {
                Toast.makeText(this, "Announcement added! / जाहीर केले!", Toast.LENGTH_SHORT).show();
                etTitle.setText("");
                etContent.setText("");
                loadAnnouncements();
            }
        });

        loadAnnouncements();
    }

    private void loadAnnouncements() {
        listContainer.removeAllViews();
        List<Announcement> list = db.getAllAnnouncements();
        if (list.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            for (Announcement a : list) {
                View card = getLayoutInflater().inflate(R.layout.item_announcement, listContainer, false);
                TextView tvTitle = card.findViewById(R.id.tvItemTitle);
                TextView tvContent = card.findViewById(R.id.tvItemContent);
                TextView tvDate = card.findViewById(R.id.tvItemDate);
                ImageButton btnDelete = card.findViewById(R.id.btnDelete);

                tvTitle.setText(a.title);
                tvContent.setText(a.content);
                tvDate.setText(a.date);

                if (session.isAdmin()) {
                    btnDelete.setVisibility(View.VISIBLE);
                    btnDelete.setOnClickListener(v -> {
                        new AlertDialog.Builder(this)
                                .setTitle("Delete / हटवा")
                                .setMessage("Delete this announcement? / ही घोषणा हटवायची?")
                                .setPositiveButton("Yes", (d, w) -> {
                                    db.deleteAnnouncement(a.id);
                                    loadAnnouncements();
                                })
                                .setNegativeButton("No", null)
                                .show();
                    });
                } else {
                    btnDelete.setVisibility(View.GONE);
                }

                listContainer.addView(card);
            }
        }
    }
}
