package com.grampanchayat.app.activities;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.grampanchayat.app.R;
import com.grampanchayat.app.database.SessionManager;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SessionManager session = new SessionManager(this);

        TextView tvRole = findViewById(R.id.tvRole);
        TextView tvLanguage = findViewById(R.id.tvLanguage);
        TextView tvMode = findViewById(R.id.tvMode);

        tvRole.setText(session.isAdmin() ? "Role: Admin / भूमिका: प्रशासक" : "Role: User / भूमिका: वापरकर्ता");
        tvLanguage.setText("Language / भाषा: " + (session.isMarathi() ? "मराठी" : "English"));
        tvMode.setText("Mode / मोड: " + (session.isDarkMode() ? "Dark / गडद" : "Light / प्रकाश"));

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }
}
