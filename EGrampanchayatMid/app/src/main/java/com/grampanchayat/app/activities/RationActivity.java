package com.grampanchayat.app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.grampanchayat.app.R;
import com.grampanchayat.app.database.DatabaseHelper;
import com.grampanchayat.app.database.SessionManager;
import com.grampanchayat.app.models.RationInfo;

public class RationActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private SessionManager session;
    private LinearLayout adminPanel;
    private EditText etItem, etStock, etDate, etNote;
    private Button btnUpdate;
    private TextView tvItem, tvStock, tvDate, tvNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ration);

        db = DatabaseHelper.getInstance(this);
        session = new SessionManager(this);

        adminPanel = findViewById(R.id.adminPanel);
        etItem = findViewById(R.id.etItem);
        etStock = findViewById(R.id.etStock);
        etDate = findViewById(R.id.etDate);
        etNote = findViewById(R.id.etNote);
        btnUpdate = findViewById(R.id.btnUpdate);
        tvItem = findViewById(R.id.tvItem);
        tvStock = findViewById(R.id.tvStock);
        tvDate = findViewById(R.id.tvDate);
        tvNote = findViewById(R.id.tvNote);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        if (session.isAdmin()) {
            adminPanel.setVisibility(View.VISIBLE);
        } else {
            adminPanel.setVisibility(View.GONE);
        }

        btnUpdate.setOnClickListener(v -> {
            String item = etItem.getText().toString().trim();
            String stock = etStock.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            String note = etNote.getText().toString().trim();
            if (item.isEmpty() || stock.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Fill required fields / आवश्यक माहिती भरा", Toast.LENGTH_SHORT).show();
                return;
            }
            if (db.setRationInfo(item, stock, date, note)) {
                Toast.makeText(this, "Updated! / अपडेट केले!", Toast.LENGTH_SHORT).show();
                loadRation();
            }
        });

        loadRation();
    }

    private void loadRation() {
        RationInfo ri = db.getRationInfo();
        if (ri != null) {
            tvItem.setText(ri.item);
            tvStock.setText(ri.stock);
            tvDate.setText(ri.date);
            tvNote.setText(ri.note.isEmpty() ? "-" : ri.note);
        } else {
            tvItem.setText("No data / माहिती नाही");
        }
    }
}
