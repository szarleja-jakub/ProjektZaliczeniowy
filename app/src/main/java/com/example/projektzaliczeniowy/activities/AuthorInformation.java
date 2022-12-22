package com.example.projektzaliczeniowy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.projektzaliczeniowy.R;

public class AuthorInformation extends AppCompatActivity {

    Utils utils;
    ImageView authorImage;

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
            case R.id.order:
                utils.openOrderActivity();
                finish();
                break;
            case R.id.saveLoginInfo:
                utils.saveLoginInfo();
                break;
            case R.id.language:
                utils.openLanguageActivity();
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_information);

        utils = new Utils(this);

        authorImage = findViewById(R.id.authorPhoto);

        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setDuration(1200);
        authorImage.startAnimation(animation);
    }
}