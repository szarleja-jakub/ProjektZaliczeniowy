package com.example.projektzaliczeniowy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projektzaliczeniowy.R;
import com.example.projektzaliczeniowy.database.FeedReaderDbHelper;

public class MainActivity extends AppCompatActivity {

    private TextView registerRedirect, errorText, polishLang, englishLang;
    private Button loginButton;
    private EditText emailInput, passwordInput;
    public static Activity loginActivity;
    FeedReaderDbHelper dbHelper;
    Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginActivity = this;

        utils = new Utils(this);

        if(!utils.redirectIfSavedLoginInfo()) {
            utils.resetUserId();

            dbHelper = new FeedReaderDbHelper(this);

            emailInput = findViewById(R.id.emailInput);
            passwordInput = findViewById(R.id.passwordInput);
            registerRedirect = findViewById(R.id.registerRedirect);
            loginButton = findViewById(R.id.loginButton);
            errorText = findViewById(R.id.errorText);
            polishLang = findViewById(R.id.langPolish);
            englishLang = findViewById(R.id.langEnglish);

            String localeInfo = utils.getLocaleInfo();
            String locale = utils.getLocale();

            if(utils.getLocaleInfo().equals("pl")) {
                if(!utils.getLocale().contains("pl")) {
                    utils.setLocale("pl");
                    refreshActivity();
                } else {
                    String locale2 = utils.getLocale();
                }
                polishLang.setBackground(getDrawable(R.color.chosenLanguage));
            } else {
                if(!utils.getLocale().contains("en")) {
                    utils.setLocale("en");
                    refreshActivity();
                } else {
                    englishLang.setBackground(getDrawable(R.color.chosenLanguage));
                }
            }

            addListeners();
        }
    }

    private void addListeners() {
        englishLang.setOnClickListener(view -> {
            if(!utils.getLocale().contains("en")) {
                utils.setLocale("en");
                utils.setLocaleInfo("en");
                refreshActivity();
            }
        });

        polishLang.setOnClickListener(view -> {
            if(!utils.getLocale().contains("pl")) {
                utils.setLocale("pl");
                utils.setLocaleInfo("pl");
                refreshActivity();
            }
        });

        registerRedirect.setOnClickListener(view -> {
            utils.openRegisterActivity();
            clearErrorText();
        });

        loginButton.setOnClickListener(view -> {
//            dbHelper.truncate();
            if(!checkErrors()) {
                utils.openOrderActivity();
                finish();
            } else {
                resetEmail();
                resetPassword();
                setFocusOnEmailInput();
            }
        });
    }

    private void refreshActivity() {
        Intent refresh = new Intent(this, MainActivity.class);
        finish();
        startActivity(refresh);
    }

    private boolean checkErrors() {
        String email = emailInput.getText().toString();
        if(!emailEmpty() && !passwordEmpty()) {
            if(utils.validateEmail(email)) {
                String password = passwordInput.getText().toString();
                if(utils.validatePassword(password)) {
                    if(checkLoginData(email)) {
                        if(addUserIdToSharedPref(email)) {
                            showLoginSuccess();
                            return false;
                        } else {
                            showLoginError(0);
                        }
                    } else {
                        showLoginError(1);
                    }
                } else {
                    showLoginError(-1);
                }
            } else {
                showLoginError(2);
            }
        } else {
            showLoginError(3);
        }
        return true;
    }

    private boolean addUserIdToSharedPref(String email) {
        Cursor cursor = dbHelper.getIdByEmail(email);
        if(cursor.getCount() > 0 && cursor.moveToFirst()) {
            SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int id = cursor.getInt(0);
            editor.putInt("userId", id);
            editor.apply();
            return true;
        }
            return false;
    }

    private boolean checkLoginData(String email) {
        String pass = passwordInput.getText().toString();
        Cursor cursor = dbHelper.getPassword(email);
        if(cursor.getCount() > 0 && cursor.moveToFirst()) {
            String storedPass = cursor.getString(0);
            return pass.equals(storedPass);
        }
        return false;
    }

    private void setFocusOnEmailInput() {
        emailInput.requestFocus();
    }

    private void resetEmail() {
        emailInput.setText("");
    }

    private void resetPassword() {
        passwordInput.setText("");
    }

    private boolean emailEmpty() {
        return emailInput.getText().toString().trim().equals("");
    }

    private boolean passwordEmpty() {
        return passwordInput.getText().toString().trim().equals("");
    }

    private void showLoginSuccess(){
        showText(getString(R.string.login_success));
    }

    private void showLoginError(int errorCode) {
        switch(errorCode) {
            case 1:
                showText(getString(R.string.login_error1));
                break;
            case 2:
                showText(getString(R.string.login_error2));
                break;
            case 3:
                showText(getString(R.string.login_error3));
                break;
            default:
                showText(getString(R.string.login_error_default));
        }

    }

    private void showText(String text) {
        errorText.setText(text);
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    };

    private void clearErrorText() {
        errorText.setText("");
    }
}