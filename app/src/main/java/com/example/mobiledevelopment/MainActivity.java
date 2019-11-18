package com.example.mobiledevelopment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        String userName = Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName();
        TextView welcomeText = findViewById(R.id.text_welcome);
        Button signOutButton = findViewById(R.id.button_sign_out);

        welcomeText.setText(String.format(getString(R.string.welcome), userName));

        signOutButton.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(this, SignInActivity.class));
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
