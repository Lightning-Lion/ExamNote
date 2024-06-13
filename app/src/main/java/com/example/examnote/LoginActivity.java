package com.example.examnote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private CheckBox rememberMeCheckBox;
    private Button loginButton;
    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "PrefsFile";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PASSWORD = "password";
    private static final String PREF_REMEMBER_ME = "remember_me";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);
        loginButton = findViewById(R.id.loginButton);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loadPreferences();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (authenticate(username, password)) {
                    savePreferences(username, password, rememberMeCheckBox.isChecked());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    showAlert("Login Failed", "Username or Password is incorrect.");
                }
            }
        });
    }

    private void loadPreferences() {
        String username = sharedPreferences.getString(PREF_USERNAME, "");
        String password = sharedPreferences.getString(PREF_PASSWORD, "");
        boolean rememberMe = sharedPreferences.getBoolean(PREF_REMEMBER_ME, false);

        if (rememberMe) {
            usernameEditText.setText(username);
            passwordEditText.setText(password);
        }
        rememberMeCheckBox.setChecked(rememberMe);
    }

    private void savePreferences(String username, String password, boolean rememberMe) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (rememberMe) {
            editor.putString(PREF_USERNAME, username);
            editor.putString(PREF_PASSWORD, password);
        } else {
            editor.remove(PREF_USERNAME);
            editor.remove(PREF_PASSWORD);
        }
        editor.putBoolean(PREF_REMEMBER_ME, rememberMe);
        editor.apply();
    }

    private boolean authenticate(String username, String password) {
        // Replace this with your own authentication logic
        return "ling".equals(username) && "jiahui".equals(password);
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}