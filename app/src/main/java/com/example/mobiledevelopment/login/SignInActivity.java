package com.example.mobiledevelopment.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.mobiledevelopment.DataValidator;
import com.example.mobiledevelopment.MainActivity;
import com.example.mobiledevelopment.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        mEmailField = findViewById(R.id.input_email);
        mPasswordField = findViewById(R.id.input_password);
        Button signUpSwitchButton = findViewById(R.id.button_sign_up_switch);
        Button loginButton = findViewById(R.id.button_sign_in);

        loginButton.setOnClickListener(v -> {
            String email = Objects.requireNonNull(mEmailField.getText()).toString();
            String pass = Objects.requireNonNull(mPasswordField.getText()).toString();
            signIn(email, pass);
        });

        signUpSwitchButton.setOnClickListener(v -> startActivity(new Intent(this,
                SignUpActivity.class)));
    }

    private void signIn(String email, String pass) {
        if (!DataValidator.isDataValid(mEmailField, email, mPasswordField, pass,
                getApplicationContext())) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        onSignInSuccess();
                    } else {
                        onSignInError();
                    }
                });
    }

    private void onSignInSuccess() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void onSignInError() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.auth_failed);
        alertDialog.setMessage(getString(R.string.check));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}