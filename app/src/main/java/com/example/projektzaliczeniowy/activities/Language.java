package com.example.projektzaliczeniowy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.projektzaliczeniowy.R;

public class Language extends AppCompatActivity {

    LinearLayout english, polish;
    Utils utils;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.logout:
                utils.logout();
                utils.openLoginActivity();
                finish();
                break;
            case R.id.allOrders:
                utils.openAllOrdersActivity();
                finish();
                break;
            case R.id.author:
                utils.openAuthorActivity();
                finish();
                break;
            case R.id.order:
                utils.openOrderActivity();
                finish();
                break;
            case R.id.saveLoginInfo:
                utils.saveLoginInfo();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        utils = new Utils(this);

        english = findViewById(R.id.english);
        polish = findViewById(R.id.polish);

        if(utils.getLocale().contains("pl")) {
            polish.setBackground(AppCompatResources.getDrawable(this, R.color.chosenLanguage));
        } else {
            english.setBackground(AppCompatResources.getDrawable(this, R.color.chosenLanguage));
        }

        addListeners();
    }

    private void addListeners() {
        english.setOnClickListener(view -> {
            if(!utils.getLocale().contains("en")) {
                utils.setLocale("en");
                utils.setLocaleInfo("en");
                refreshActivity();
            }
        });

        polish.setOnClickListener(view -> {
            if(!utils.getLocale().contains("pl")) {
                utils.setLocale("pl");
                utils.setLocaleInfo("pl");
                refreshActivity();
            }
        });
    }

    private void refreshActivity() {
        Intent refresh = new Intent(this, Language.class);
        finish();
        startActivity(refresh);
    }
}