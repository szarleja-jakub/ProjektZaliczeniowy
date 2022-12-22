package com.example.projektzaliczeniowy.activities;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.example.projektzaliczeniowy.R;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    Context context;
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", Pattern.CASE_INSENSITIVE);
    private static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("^(?=\\S+$).{4,32}$");
    private static final Pattern VALID_NAME_REGEX =
            Pattern.compile("^[^- '](?=(?![A-Z]?[A-Z]))(?=(?![a-z]+[A-Z]))(?=(?!.*[A-Z][A-Z]))(?=(?!.*[- '][- '.]))(?=(?!.*[.][-'.]))[A-Za-z- '.]{1,32}$");
    private static final Pattern VALID_PHONE_REGEX =
            Pattern.compile("^[0-9]{9}$");

    public Utils(Context context) {
        this.context = context;
    }

    public void logout() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("userId");
        editor.putBoolean("saveLoginInfo", false);
        editor.apply();
        Toast.makeText(context, R.string.info_logged_out, Toast.LENGTH_SHORT).show();
    }

    public void openLanguageActivity() {
        Intent intent = new Intent(context, Language.class);
        context.startActivity(intent);
    }

    public void openAllOrdersActivity() {
        Intent intent = new Intent(context, AllOrdersActivity.class);
        context.startActivity(intent);
    }

    public void openRegisterActivity() {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    public void openOrderActivity() {
        Intent intent = new Intent(context, OrderActivity.class);
        context.startActivity(intent);
    }

    public boolean validatePhoneNumber(String number) {
        Matcher matcher = VALID_PHONE_REGEX.matcher(number);
        if(matcher.find()) {
            String matchedNumber = matcher.group();
            return number.equals(matchedNumber);
        }
        return false;
    }

    public boolean validateName(String name) {
        Matcher matcher = VALID_NAME_REGEX.matcher(name);
        if(matcher.find()) {
            String matchedName = matcher.group();
            return name.equals(matchedName);
        }
        return false;
    }

    public boolean validateEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        if(matcher.find()) {
            String matchedEmail = matcher.group();
            return email.equals(matchedEmail);
        }
        return false;
    }

    public boolean validatePassword(String password) {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(password);
        if(matcher.find()) {
            String matchedPassword = matcher.group();
            return password.equals(matchedPassword);
        }
        return false;
    }

    public void openAuthorActivity() {
        Intent intent = new Intent(context, AuthorInformation.class);
        context.startActivity(intent);
    }

    public void resetUserId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPref", MODE_PRIVATE);
        if(sharedPreferences.contains("userId")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("userId");
            editor.apply();
        }
    }

    public boolean redirectIfSavedLoginInfo() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPref", MODE_PRIVATE);
        boolean saving = sharedPreferences.getBoolean("saveLoginInfo", false);
        if(saving) {
            openOrderActivity();
            MainActivity.loginActivity.finish();
            return true;
        }
        return false;
    }

    public void setLocale(String lang) {
        Locale locale = new Locale(lang);
        locale.setDefault(locale);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
    }

    public String getLocale() {
        return context.getResources().getConfiguration().locale.toString();
    }

    public String getLocaleInfo() {
        SharedPreferences preferences = context.getSharedPreferences("sharedPref", MODE_PRIVATE);
        return preferences.getString("locale", "pl");
    }

    public void setLocaleInfo(String localeInfo) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPref", MODE_PRIVATE);
        String locale = sharedPreferences.getString("locale", "pl");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(localeInfo != locale) {
            editor.putString("locale", localeInfo);
            editor.apply();
        }
    }

    public void saveLoginInfo() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPref", MODE_PRIVATE);
        boolean saving = sharedPreferences.getBoolean("saveLoginInfo", false);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(saving) {
            editor.putBoolean("saveLoginInfo", false);
            editor.apply();
            Toast.makeText(context, R.string.login_info_not_saved, Toast.LENGTH_SHORT).show();
        } else {
            editor.putBoolean("saveLoginInfo", true);
            editor.apply();
            Toast.makeText(context, R.string.login_info_saved, Toast.LENGTH_SHORT).show();
        }
    }

    public void openLoginActivity() {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public int getUserId() {
        SharedPreferences preferences = context.getSharedPreferences("sharedPref", MODE_PRIVATE);
        return preferences.getInt("userId", -1);
    }

}
