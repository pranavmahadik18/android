package com.grampanchayat.app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.grampanchayat.app.R;
import com.grampanchayat.app.database.DatabaseHelper;
import com.grampanchayat.app.database.SessionManager;
import com.grampanchayat.app.models.WaterStatus;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WaterActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private SessionManager session;
    private LinearLayout adminPanel;
    private EditText etTime, etNote;
    private RadioGroup rgStatus;
    private Button btnUpdate;
    private TextView tvStatusValue, tvTimeValue, tvNoteValue, tvDateValue, tvStatusIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);

        db = DatabaseHelper.getInstance(this);
        session = new SessionManager(this);

        adminPanel = findViewById(R.id.adminPanel);
        etTime = findViewById(R.id.etTime);
        etNote = findViewById(R.id.etNote);
        rgStatus = findViewById(R.id.rgStatus);
        btnUpdate = findViewById(R.id.btnUpdate);
        tvStatusValue = findViewById(R.id.tvStatusValue);
        tvTimeValue = findViewById(R.id.tvTimeValue);
        tvNoteValue = findViewById(R.id.tvNoteValue);
        tvDateValue = findViewById(R.id.tvDateValue);
        tvStatusIcon = findViewById(R.id.tvStatusIcon);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        if (session.isAdmin()) {
            adminPanel.setVisibility(View.VISIBLE);
        } else {
            adminPanel.setVisibility(View.GONE);
        }

        btnUpdate.setOnClickListener(v -> {
            int selectedId = rgStatus.getCheckedRadioButtonId();
            String status = selectedId == R.id.rbOn ? "ON" : "OFF";
            String time = etTime.getText().toString().trim();
            String note = etNote.getText().toString().trim();
            if (time.isEmpty()) {
                Toast.makeText(this, "Enter time / वेळ टाका", Toast.LENGTH_SHORT).show();
                return;
            }
            String date = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
            if (db.setWaterStatus(status, time, note, date)) {
                Toast.makeText(this, "Updated! / अपडेट केले!", Toast.LENGTH_SHORT).show();
                loadWaterStatus();
            }
        });

        loadWaterStatus();
    }

    private void loadWaterStatus() {
        WaterStatus ws = db.getWaterStatus();
        if (ws != null) {
            tvStatusValue.setText(ws.status);
            tvTimeValue.setText(ws.time.isEmpty() ? "-" : ws.time);
            tvNoteValue.setText(ws.note.isEmpty() ? "-" : ws.note);
            tvDateValue.setText(ws.date);
            if ("ON".equals(ws.status)) {
                tvStatusValue.setTextColor(getResources().getColor(R.color.green));
                tvStatusIcon.setText("💧");
            } else {
                tvStatusValue.setTextColor(getResources().getColor(R.color.red));
                tvStatusIcon.setText("🚫");
            }
        } else {
            tvStatusValue.setText("No data / माहिती नाही");
        }
    }
}
