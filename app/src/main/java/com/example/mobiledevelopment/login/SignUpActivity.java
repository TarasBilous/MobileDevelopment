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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mNameField;
    private EditText mPhoneField;
    private EditText mPassField;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        Button signUpButton = findViewById(R.id.button_sign_up);
        Button loginSwitchButton = findViewById(R.id.button_sign_in_switch);

        initClassFields();

        signUpButton.setOnClickListener(v -> {
            String email = Objects.requireNonNull(mEmailField.getText()).toString();
            String name = Objects.requireNonNull(mNameField.getText()).toString();
            String phone = Objects.requireNonNull(mPhoneField.getText()).toString();
            String pass = Objects.requireNonNull(mPassField.getText()).toString();
            signUp(email, name, phone, pass);
        });

        loginSwitchButton.setOnClickListener(v ->
                startActivity(new Intent(this, SignInActivity.class)));
    }

    private void initClassFields() {
        mEmailField = findViewById(R.id.input_email);
        mNameField = findViewById(R.id.input_name);
        mPhoneField = findViewById(R.id.input_phone);
        mPassField = findViewById(R.id.input_password);
    }

    private void signUp(String email, String name, String phone, String pass) {
        if (!DataValidator.isDataValid(mEmailField, email, mPhoneField, phone, mNameField,
                name, mPassField, pass, getApplicationContext())) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        onCreateSuccess(name);
                    } else {
                        onCreateError();
                    }
                });
    }

    private void onCreateSuccess(String name) {
        FirebaseUser user = mAuth.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name).build();
        if (user != null) {
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(this, MainActivity.class));
                        }
                    });
        }
    }

    private void onCreateError() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.sign_up_failed);
        alertDialog.setMessage(getString(R.string.sign_up_explanation));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}