package com.grampanchayat.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.grampanchayat.app.R;
import com.grampanchayat.app.database.DatabaseHelper;
import com.grampanchayat.app.database.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private LinearLayout layoutChoice, layoutAdminLogin;
    private Button btnUserLogin, btnAdminOption, btnAdminLogin, btnBack;
    private EditText etUsername, etPassword;
    private TextView tvTitle, tvSubtitle;

    private SessionManager session;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(this);
        db = DatabaseHelper.getInstance(this);

        layoutChoice = findViewById(R.id.layoutChoice);
        layoutAdminLogin = findViewById(R.id.layoutAdminLogin);
        btnUserLogin = findViewById(R.id.btnUserLogin);
        btnAdminOption = findViewById(R.id.btnAdminOption);
        btnAdminLogin = findViewById(R.id.btnAdminLogin);
        btnBack = findViewById(R.id.btnBack);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        // User login - no password needed
        btnUserLogin.setOnClickListener(v -> {
            session.createSession(SessionManager.USER_TYPE_USER);
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        });

        // Show admin login form
        btnAdminOption.setOnClickListener(v -> {
            layoutChoice.setVisibility(View.GONE);
            layoutAdminLogin.setVisibility(View.VISIBLE);
        });

        // Admin login with credentials
        btnAdminLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter username and password / युजरनेम आणि पासवर्ड टाका", Toast.LENGTH_SHORT).show();
                return;
            }
            if (db.checkAdmin(username, password)) {
                session.createSession(SessionManager.USER_TYPE_ADMIN);
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials / चुकीचे तपशील", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> {
            layoutAdminLogin.setVisibility(View.GONE);
            layoutChoice.setVisibility(View.VISIBLE);
            etUsername.setText("");
            etPassword.setText("");
        });
    }
}
