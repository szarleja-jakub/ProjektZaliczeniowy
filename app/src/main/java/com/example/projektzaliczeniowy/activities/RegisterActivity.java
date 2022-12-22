package com.example.projektzaliczeniowy.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projektzaliczeniowy.R;
import com.example.projektzaliczeniowy.database.FeedReaderDbHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class RegisterActivity extends AppCompatActivity {

    private TextView loginRedirect, errorText;
    private EditText emailInput, passwordInput, confirmPasswordInput, phoneInput, nameInput;
    private ArrayList<EditText> inputs;
    Button registerButton;
    Utils utils;
    FeedReaderDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        utils = new Utils(this);

        dbHelper = new FeedReaderDbHelper(this);

        registerButton = findViewById(R.id.registerButton);
        errorText = findViewById(R.id.errorText);
        loginRedirect = findViewById(R.id.loginRedirect);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        phoneInput = findViewById(R.id.numberInput);
        nameInput = findViewById(R.id.nameInput);

        inputs = new ArrayList<>(Arrays.asList(emailInput, passwordInput, confirmPasswordInput, phoneInput, nameInput));

        addListeners();
    }

    private void showRegisterError(int errorId) {
        switch(errorId) {
            case 0:
                showText(getString(R.string.register_error0));
                break;
            case 1:
                showText(getString(R.string.register_error1));
                break;
            case 2:
                showText(getString(R.string.register_error2));
                break;
            case 3:
                showText(getString(R.string.register_error3));
                break;
            case 4:
                showText(getString(R.string.register_error4));
                break;
            case 5:
                showText(getString(R.string.register_error5));
                break;
            case 6:
                showText(getString(R.string.register_error6));
                break;
            case 7:
                showText(getString(R.string.register_error7));
                break;
            default:
                showText(getString(R.string.register_error_default));
                break;
        }
    }

    private void showText(String text) {
        errorText.setText(text);
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private boolean checkIfEmpty() {
        AtomicBoolean anyInputEmpty = new AtomicBoolean(false);
        try {
            inputs.forEach(item -> {
                if (item.getText().toString().equals("")) {
                    anyInputEmpty.set(true);
                }
            });
        } catch (Exception ignored) {}
        return anyInputEmpty.get();
    }

    private void closeCurrentActivity() {
        finish();
    }

    private boolean checkRegisterErrors() {
        if(!checkIfEmpty()) {
            String email = emailInput.getText().toString();
            if(utils.validateEmail(email)) {
                String number = phoneInput.getText().toString();
                if(utils.validatePhoneNumber(number)) {
                    String password = passwordInput.getText().toString();
                    if(password.equals(confirmPasswordInput.getText().toString())) {
                        if(utils.validatePassword(password)) {
                            String name = nameInput.getText().toString();
                            if(utils.validateName(name)) {
                                if(!userAlreadyExists()) {
                                    if(!numberTaken()) {
                                        if(registerUser()) {
                                            return false;
                                        } else showRegisterError(-1);
                                    } else {
                                        showRegisterError(7);
                                    }
                                } else {
                                    showRegisterError(6);
                                }
                            } else {
                                showRegisterError(5);
                            }
                        } else {
                            showRegisterError(4);
                        }
                    } else {
                        showRegisterError(3);
                    }
                } else {
                    showRegisterError(2);
                }
            } else {
                showRegisterError(1);
            }
        } else {
            showRegisterError(0);
        }
        return true;
    }

    private void addListeners() {
        loginRedirect.setOnClickListener(view -> {
            closeCurrentActivity();
            clearErrorText();
        });

        registerButton.setOnClickListener(view -> {
            if(!checkRegisterErrors()) {
                showRegisterSuccess();
            }
            resetInputs();
        });
    }

    private boolean userAlreadyExists() {
        String email = emailInput.getText().toString();
        Cursor cursor = dbHelper.getIdByEmail(email);
        return cursor.getCount() > 0 && cursor.moveToFirst();
    }

    private boolean numberTaken() {
        String phoneNumber = phoneInput.getText().toString();
        Cursor cursor = dbHelper.getIdByPhone(phoneNumber);
        return cursor.getCount() > 0 && cursor.moveToFirst();
    }

    private boolean registerUser() {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String phoneNumber = phoneInput.getText().toString();
        String name = nameInput.getText().toString();
        return dbHelper.insertUser(email, password, phoneNumber, name);
    }

    private void showRegisterSuccess() {
        showText(getString(R.string.register_success));
    }

    private void resetInputs() {
        inputs.forEach(input -> input.setText(""));
    }

    private void clearErrorText() {
        errorText.setText("");
    }
}