package com.grampanchayat.app.activities;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.grampanchayat.app.R;
import com.grampanchayat.app.database.SessionManager;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SessionManager session = new SessionManager(this);

        Switch switchDark = findViewById(R.id.switchDark);
        Switch switchMarathi = findViewById(R.id.switchMarathi);
        ImageButton btnBack = findViewById(R.id.btnBack);

        switchDark.setChecked(session.isDarkMode());
        switchMarathi.setChecked(session.isMarathi());

        switchDark.setOnCheckedChangeListener((btn, isChecked) -> {
            session.setDarkMode(isChecked);
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            recreate();
        });

        switchMarathi.setOnCheckedChangeListener((btn, isChecked) -> {
            session.setLanguage(isChecked ? "marathi" : "english");
            Toast.makeText(this, isChecked ? "मराठी निवडले" : "English selected", Toast.LENGTH_SHORT).show();
        });

        btnBack.setOnClickListener(v -> finish());
    }
}
