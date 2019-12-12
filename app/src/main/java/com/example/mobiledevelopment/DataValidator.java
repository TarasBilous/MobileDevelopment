package com.example.mobiledevelopment;

import android.content.Context;
import android.widget.EditText;

public final class DataValidator {

    private final static String IS_NAME_REGEX = "^[A-Za-z]+$";
    private final static int MIN_PASSWORD_LENGTH = 8;

    public static boolean isDataValid(EditText emailField, String email, EditText passwordField,
                                      String password, Context context) {
        boolean valid = true;

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError(context.getString(R.string.email_error));
            valid = false;
        } else {
            emailField.setError(null);
        }

        if (password.isEmpty() || password.length() < MIN_PASSWORD_LENGTH) {
            passwordField.setError(context.getString(R.string.password_error));
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return valid;
    }

    public static boolean isDataValid(EditText emailField, String email, EditText phoneField,
                                      String phone, EditText nameField, String name,
                                      EditText passwordField, String password, Context context) {
        boolean valid = isDataValid(emailField, email, passwordField, password, context);

        if (phone.isEmpty() || !android.util.Patterns.PHONE.matcher(phone).matches()) {
            phoneField.setError(context.getString(R.string.phone_error));
            valid = false;
        } else {
            phoneField.setError(null);
        }

        if (!name.matches(IS_NAME_REGEX)) {
            nameField.setError(context.getString(R.string.name_error));
            valid = false;
        } else {
            nameField.setError(null);
        }

        return valid;
    }

    public static boolean isEmailValid(String email) {
        return !email.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isNameValid(String name) {
        return name.matches(IS_NAME_REGEX);
    }

}