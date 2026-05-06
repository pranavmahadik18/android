package com.grampanchayat.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import com.grampanchayat.app.R;
import com.grampanchayat.app.database.SessionManager;

public class HomeActivity extends AppCompatActivity {

    private SessionManager session;
    private LinearLayout drawerMenu;
    private boolean isDrawerOpen = false;
    private TextView tvUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        session = new SessionManager(this);
        drawerMenu = findViewById(R.id.drawerMenu);
        tvUserType = findViewById(R.id.tvUserType);
        ImageButton btnMenu = findViewById(R.id.btnMenu);

        if (session.isAdmin()) {
            tvUserType.setText("Admin");
            tvUserType.setVisibility(View.VISIBLE);
        } else {
            tvUserType.setText("User");
            tvUserType.setVisibility(View.VISIBLE);
        }

        btnMenu.setOnClickListener(v -> toggleDrawer());

        // Close drawer on overlay click
        View overlay = findViewById(R.id.overlay);
        if (overlay != null) {
            overlay.setOnClickListener(v -> closeDrawer());
        }

        // Drawer menu items
        setupDrawer();

        // Grid cards
        setupCards();
    }

    private void setupDrawer() {
        TextView menuProfile = findViewById(R.id.menuProfile);
        TextView menuMode = findViewById(R.id.menuMode);
        TextView menuLanguage = findViewById(R.id.menuLanguage);
        TextView menuSettings = findViewById(R.id.menuSettings);
        TextView menuLogout = findViewById(R.id.menuLogout);

        // Set current mode label
        menuMode.setText(session.isDarkMode() ? "☀️ Light Mode" : "🌙 Dark Mode");
        menuLanguage.setText(session.isMarathi() ? "🌐 English" : "🌐 मराठी");

        menuProfile.setOnClickListener(v -> {
            closeDrawer();
            startActivity(new Intent(this, ProfileActivity.class));
        });

        menuMode.setOnClickListener(v -> {
            boolean newMode = !session.isDarkMode();
            session.setDarkMode(newMode);
            AppCompatDelegate.setDefaultNightMode(
                    newMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            closeDrawer();
            recreate();
        });

        menuLanguage.setOnClickListener(v -> {
            String newLang = session.isMarathi() ? "english" : "marathi";
            session.setLanguage(newLang);
            closeDrawer();
            recreate();
        });

        menuSettings.setOnClickListener(v -> {
            closeDrawer();
            startActivity(new Intent(this, SettingsActivity.class));
        });

        menuLogout.setOnClickListener(v -> {
            session.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void setupCards() {
        CardView cardAnnouncement = findViewById(R.id.cardAnnouncement);
        CardView cardComplaint = findViewById(R.id.cardComplaint);
        CardView cardWater = findViewById(R.id.cardWater);
        CardView cardRation = findViewById(R.id.cardRation);
        CardView cardPoll = findViewById(R.id.cardPoll);
        CardView cardEmergency = findViewById(R.id.cardEmergency);

        cardAnnouncement.setOnClickListener(v -> startActivity(new Intent(this, AnnouncementActivity.class)));
        cardComplaint.setOnClickListener(v -> startActivity(new Intent(this, ComplaintActivity.class)));
        cardWater.setOnClickListener(v -> startActivity(new Intent(this, WaterActivity.class)));
        cardRation.setOnClickListener(v -> startActivity(new Intent(this, RationActivity.class)));
        cardPoll.setOnClickListener(v -> startActivity(new Intent(this, PollActivity.class)));
        cardEmergency.setOnClickListener(v -> startActivity(new Intent(this, EmergencyActivity.class)));
    }

    private void toggleDrawer() {
        if (isDrawerOpen) closeDrawer();
        else openDrawer();
    }

    private void openDrawer() {
        drawerMenu.setVisibility(View.VISIBLE);
        View overlay = findViewById(R.id.overlay);
        if (overlay != null) overlay.setVisibility(View.VISIBLE);
        isDrawerOpen = true;
    }

    private void closeDrawer() {
        drawerMenu.setVisibility(View.GONE);
        View overlay = findViewById(R.id.overlay);
        if (overlay != null) overlay.setVisibility(View.GONE);
        isDrawerOpen = false;
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpen) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
