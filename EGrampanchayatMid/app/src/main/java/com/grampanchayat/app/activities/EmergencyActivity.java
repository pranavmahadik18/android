package com.grampanchayat.app.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.grampanchayat.app.R;
import com.grampanchayat.app.database.DatabaseHelper;
import com.grampanchayat.app.database.SessionManager;
import com.grampanchayat.app.models.Emergency;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EmergencyActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private SessionManager session;
    private LinearLayout adminPanel, listContainer;
    private EditText etTitle, etMessage;
    private Spinner spinnerType;
    private Button btnPost;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        db = DatabaseHelper.getInstance(this);
        session = new SessionManager(this);

        adminPanel = findViewById(R.id.adminPanel);
        listContainer = findViewById(R.id.listContainer);
        etTitle = findViewById(R.id.etTitle);
        etMessage = findViewById(R.id.etMessage);
        spinnerType = findViewById(R.id.spinnerType);
        btnPost = findViewById(R.id.btnPost);
        tvEmpty = findViewById(R.id.tvEmpty);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        String[] types = {"🚨 Emergency", "⚠️ Warning", "ℹ️ Info", "🔔 Notice"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        if (session.isAdmin()) {
            adminPanel.setVisibility(View.VISIBLE);
        } else {
            adminPanel.setVisibility(View.GONE);
        }

        btnPost.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String message = etMessage.getText().toString().trim();
            String type = spinnerType.getSelectedItem().toString();
            if (title.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "Fill all fields / सर्व माहिती भरा", Toast.LENGTH_SHORT).show();
                return;
            }
            String date = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(new Date());
            if (db.addEmergency(title, message, type, date)) {
                Toast.makeText(this, "Alert posted! / इशारा पाठवला!", Toast.LENGTH_SHORT).show();
                etTitle.setText(""); etMessage.setText("");
                loadAlerts();
            }
        });

        loadAlerts();
    }

    private void loadAlerts() {
        listContainer.removeAllViews();
        List<Emergency> list = db.getAllEmergencies();
        if (list.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            for (Emergency e : list) {
                View card = getLayoutInflater().inflate(R.layout.item_emergency, listContainer, false);
                TextView tvType = card.findViewById(R.id.tvType);
                TextView tvTitle = card.findViewById(R.id.tvTitle);
                TextView tvMessage = card.findViewById(R.id.tvMessage);
                TextView tvDate = card.findViewById(R.id.tvDate);
                ImageButton btnDelete = card.findViewById(R.id.btnDelete);

                tvType.setText(e.alertType);
                tvTitle.setText(e.title);
                tvMessage.setText(e.message);
                tvDate.setText(e.date);

                // Color-code card based on type
                if (e.alertType.contains("Emergency")) {
                    card.setBackgroundColor(getResources().getColor(R.color.emergency_red_bg));
                } else if (e.alertType.contains("Warning")) {
                    card.setBackgroundColor(getResources().getColor(R.color.warning_orange_bg));
                } else {
                    card.setBackgroundColor(getResources().getColor(R.color.info_blue_bg));
                }

                if (session.isAdmin()) {
                    btnDelete.setVisibility(View.VISIBLE);
                    btnDelete.setOnClickListener(v -> {
                        new AlertDialog.Builder(this)
                                .setTitle("Delete Alert / इशारा हटवा")
                                .setMessage("Delete this alert? / हा इशारा हटवायचा?")
                                .setPositiveButton("Yes", (d, w) -> {
                                    db.deleteEmergency(e.id);
                                    loadAlerts();
                                })
                                .setNegativeButton("No", null).show();
                    });
                } else {
                    btnDelete.setVisibility(View.GONE);
                }

                listContainer.addView(card);
            }
        }
    }
}
