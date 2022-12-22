package com.example.projektzaliczeniowy.activities;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projektzaliczeniowy.R;
import com.example.projektzaliczeniowy.adapters.SpinnerAdapter;
import com.example.projektzaliczeniowy.database.FeedReaderDbHelper;
import com.example.projektzaliczeniowy.email.EmailManager;
import com.example.projektzaliczeniowy.inventory.Data;
import com.example.projektzaliczeniowy.sms.SmsManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class OrderActivity extends AppCompatActivity {

    CheckBox keyboardCheckbox, mouseCheckbox, monitorCheckbox, computerCheckbox;
    Spinner keyboardSpinner, mouseSpinner, monitorSpinner, computerSpinner;
    ArrayList<CheckBox> checkBoxes;
    ArrayList<Spinner> spinners;
    Data data;
    SpinnerAdapter keyboardSpinnerAdapter, mouseSpinnerAdapter, monitorSpinnerAdapter, computerSpinnerAdapter;
    TextView priceInfo;
    Button orderButton;
    Utils utils;
    FeedReaderDbHelper dbHelper;

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
                deleteOrderSharedPref();
                break;
            case R.id.allOrders:
                utils.openAllOrdersActivity();
                break;
            case R.id.author:
                utils.openAuthorActivity();
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
    protected void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.activity_order);

        utils = new Utils(this);

        if(utils.getLocaleInfo().equals("pl")) {
            utils.setLocale("pl");
        } else {
            utils.setLocale("en");
        }

        data = new Data(this, getResources());

        keyboardSpinner = findViewById(R.id.keyboardSpinner);
        mouseSpinner = findViewById(R.id.mouseSpinner);
        monitorSpinner = findViewById(R.id.monitorSpinner);
        computerSpinner = findViewById(R.id.computerSpinner);

        spinners = new ArrayList<>(Arrays.asList(keyboardSpinner, mouseSpinner, monitorSpinner, computerSpinner));

        keyboardCheckbox = findViewById(R.id.keyboardCheckbox);
        mouseCheckbox = findViewById(R.id.mouseCheckbox);
        monitorCheckbox = findViewById(R.id.monitorCheckbox);
        computerCheckbox = findViewById(R.id.computerCheckbox);

        checkBoxes = new ArrayList<>(Arrays.asList(keyboardCheckbox, mouseCheckbox, monitorCheckbox, computerCheckbox));

        priceInfo = findViewById(R.id.orderPrice);

        orderButton = findViewById(R.id.orderButton);

        dbHelper = new FeedReaderDbHelper(this);

        setAdapters();
        addListeners();
        restoreOrder();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if(checkIfAnySelected()) {
            outState.putBoolean("anySelected", true);
            SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("anySelected", true);

            if(keyboardCheckbox.isChecked()) {
                int keyboardId = Integer.parseInt(keyboardSpinner.getSelectedItem().toString());
                outState.putInt("keyboardId", keyboardId);
                editor.putInt("keyboardId", keyboardId);
            } else {
                outState.putInt("keyboardId", -1);
            }
            if(mouseCheckbox.isChecked()) {
                int mouseId = Integer.parseInt(mouseSpinner.getSelectedItem().toString());
                outState.putInt("mouseId", mouseId);
                editor.putInt("mouseId", mouseId);
            }  else {
                outState.putInt("mouseId", -1);
            }
            if(monitorCheckbox.isChecked()) {
                int monitorId = Integer.parseInt(monitorSpinner.getSelectedItem().toString());
                outState.putInt("monitorId", monitorId);
                editor.putInt("monitorId", monitorId);
            }  else {
                outState.putInt("monitorId", -1);
            }
            if(computerCheckbox.isChecked()) {
                int computerId = Integer.parseInt(computerSpinner.getSelectedItem().toString());
                outState.putInt("computerId", computerId);
                editor.putInt("computerId", computerId);
            }  else {
                outState.putInt("computerId", -1);
            }
            editor.apply();
        } else {
            deleteOrderSharedPref();
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle outState) {
        restoreOrder(outState);
    }

    private void setAdapters() {
        keyboardSpinnerAdapter = new SpinnerAdapter(this, data.getKeyboardList());
        keyboardSpinner.setAdapter(keyboardSpinnerAdapter);

        mouseSpinnerAdapter = new SpinnerAdapter(this, data.getMouseList());
        mouseSpinner.setAdapter(mouseSpinnerAdapter);

        monitorSpinnerAdapter = new SpinnerAdapter(this, data.getMonitorList());
        monitorSpinner.setAdapter(monitorSpinnerAdapter);

        computerSpinnerAdapter = new SpinnerAdapter(this, data.getComputerList());
        computerSpinner.setAdapter(computerSpinnerAdapter);
    }

    @SuppressLint("SetTextI18n")
    private void setPriceText() {
        if(getPrice() == 0) {
            priceInfo.setText(getString(R.string.order_price) + ": " + getString(R.string.price_info_no_item));
        } else {
            priceInfo.setText(getString(R.string.order_price) + ": $" + getPrice());
        }
    }

    private float getPrice() {
        float price = 0;
        if(keyboardCheckbox.isChecked()) {
            price += data.getKeyboardPrice(Integer.parseInt(keyboardSpinner.getSelectedItem().toString()));
        }
        if(mouseCheckbox.isChecked()) {
            price += data.getMousePrice(Integer.parseInt(mouseSpinner.getSelectedItem().toString()));
        }
        if(monitorCheckbox.isChecked()) {
            price += data.getMonitorPrice(Integer.parseInt(monitorSpinner.getSelectedItem().toString()));
        }
        if(computerCheckbox.isChecked()) {
            price += data.getComputerPrice(Integer.parseInt(computerSpinner.getSelectedItem().toString()));
        }
        return (float) Math.round(price * 100) / 100;
    }

    private void addListeners() {
        checkBoxes.forEach(checkBox -> checkBox.setOnClickListener(view -> setPriceText()));

        spinners.forEach(spinner -> spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setPriceText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }));

//        priceInfo.setOnClickListener(view -> {
//            deleteOrders();
//            Toast.makeText(this, "Orders have been removed from the database", Toast.LENGTH_SHORT).show();
//        });

        orderButton.setOnClickListener(view -> {
            if(!checkOrderErrors()) {
                showOrderError();
            } else {
                sendOrder();
                resetOrder();
            }
        });
    }

    private void restoreOrder() {
        SharedPreferences sharedPref = getSharedPreferences("sharedPref", MODE_PRIVATE);
        if(sharedPref.contains("anySelected")) {
            int keyboardId = sharedPref.getInt("keyboardId", -1);
            int mouseId = sharedPref.getInt("mouseId", -1);
            int monitorId = sharedPref.getInt("monitorId", -1);
            int computerId = sharedPref.getInt("computerId", -1);
            if(keyboardId != -1) {
                keyboardCheckbox.setChecked(true);
                keyboardSpinner.setSelection(keyboardId);
            }
            if(mouseId != -1) {
                mouseCheckbox.setChecked(true);
                mouseSpinner.setSelection(mouseId);
            }
            if(monitorId != -1) {
                monitorCheckbox.setChecked(true);
                monitorSpinner.setSelection(monitorId);
            }
            if(computerId != -1) {
                computerCheckbox.setChecked(true);
                computerSpinner.setSelection(computerId);
            }
        }
    }

    private void deleteOrderSharedPref() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
        if(sharedPreferences.contains("anySelected")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("keyboardId");
            editor.remove("mouseId");
            editor.remove("monitorId");
            editor.remove("computerId");
            editor.apply();
        }
    }

    private void restoreOrder(Bundle outState) {
        if(outState != null) {
            if(outState.getBoolean("anySelected")) {
                int keyboardId = outState.getInt("keyboardId");
                int mouseId = outState.getInt("mouseId");
                int monitorId = outState.getInt("monitorId");
                int computerId = outState.getInt("computerId");
                if(outState.getInt("keyboardId") != -1) {
                    keyboardCheckbox.setChecked(true);
                    keyboardSpinner.setSelection(keyboardId);
                }
                if(outState.getInt("mouseId") != -1) {
                    mouseCheckbox.setChecked(true);
                    mouseSpinner.setSelection(mouseId);
                }
                if(outState.getInt("monitorId") != -1) {
                    monitorCheckbox.setChecked(true);
                    monitorSpinner.setSelection(monitorId);
                }
                if(outState.getInt("computerId") != -1) {
                    computerCheckbox.setChecked(true);
                    computerSpinner.setSelection(computerId);
                }
            }
        }
    }

    private void showOrderError() {
        Toast.makeText(this, R.string.order_error, Toast.LENGTH_SHORT).show();
    }

    private void showOrderSuccess() {
        Toast.makeText(this, R.string.order_success, Toast.LENGTH_SHORT).show();
    }

    private boolean checkOrderErrors() {
        SharedPreferences preferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1);
        if(userId == -1) {
            return true;
        } else {
            return checkIfAnySelected();
        }
    }

    private boolean checkIfAnySelected() {
        AtomicBoolean anyCheckBoxChecked = new AtomicBoolean(false);
        try {
            checkBoxes.forEach(checkBox -> {
                if (checkBox.isChecked()) {
                    anyCheckBoxChecked.set(true);
                    throw new RuntimeException();
                }
            });
        } catch (Exception ignored) {}
        return anyCheckBoxChecked.get();
    }

    private void resetOrder() {
        spinners.forEach(spinner -> spinner.setSelection(0));
        checkBoxes.forEach(checkBox -> checkBox.setChecked(false));
        setPriceText();
    }

    private void sendOrder() {
        EmailManager emailManager = new EmailManager(this);
        SmsManager smsManager = new SmsManager(this);
        int userId = utils.getUserId();
        int keyboardId = 4;
        int mouseId = 4;
        int monitorId = 4;
        int computerId = 4;
        if(keyboardCheckbox.isChecked()) {
            keyboardId = Integer.parseInt(keyboardSpinner.getSelectedItem().toString());
        }
        if(mouseCheckbox.isChecked()) {
            mouseId = Integer.parseInt(mouseSpinner.getSelectedItem().toString());
        }
        if(monitorCheckbox.isChecked()) {
            monitorId = Integer.parseInt(monitorSpinner.getSelectedItem().toString());
        }
        if(computerCheckbox.isChecked()) {
            computerId = Integer.parseInt(computerSpinner.getSelectedItem().toString());
        }
        boolean checkInsertion = dbHelper.insertOrder(userId, keyboardId, mouseId, monitorId, computerId, getPrice());
        if (checkInsertion) {
            String userEmail = "", userPhoneNumber = "", userName = "", keyboardName = "", mouseName = "", monitorName = "", computerName = "";
            float keyboardPrice = 0, mousePrice = 0, monitorPrice = 0, computerPrice = 0, orderPrice = 0;
            Cursor cursorName, cursorPhoneNumber, cursorPrice, cursorEmail, cursorFullname;
            cursorEmail = dbHelper.getEmail(userId);
            cursorPhoneNumber = dbHelper.getPhoneNumber(userId);
            cursorFullname = dbHelper.getName(userId);
            if(cursorPhoneNumber.getCount() > 0 && cursorPhoneNumber.moveToFirst()) {
                userPhoneNumber = cursorPhoneNumber.getString(0);
                cursorPhoneNumber.close();
            }
            if(cursorEmail.getCount() > 0 && cursorEmail.moveToFirst()) {
                userEmail = cursorEmail.getString(0);
                cursorEmail.close();
            }
            if(cursorFullname.getCount() > 0 && cursorFullname.moveToFirst()) {
                userName = cursorFullname.getString(0);
                cursorFullname.close();
            }
            if(keyboardId != 4) {
                cursorName = dbHelper.getKeyboardName(keyboardId);
                cursorPrice = dbHelper.getKeyboardPrice(keyboardId);
                if(cursorName.getCount() > 0 && cursorName.moveToFirst()) {
                    keyboardName = cursorName.getString(0);
                    cursorName.close();
                }
                if(cursorPrice.getCount() > 0 && cursorPrice.moveToFirst()) {
                    keyboardPrice = cursorPrice.getFloat(0);
                    cursorPrice.close();
                }
            }
            if(mouseId != 4) {
                cursorName = dbHelper.getMouseName(mouseId);
                cursorPrice = dbHelper.getMousePrice(mouseId);
                if(cursorName.getCount() > 0 && cursorName.moveToFirst()) {
                    mouseName = cursorName.getString(0);
                    cursorName.close();
                }
                if(cursorPrice.getCount() > 0 && cursorPrice.moveToFirst()) {
                    mousePrice = cursorPrice.getFloat(0);
                    cursorPrice.close();
                }
            }
            if(monitorId != 4) {
                cursorName = dbHelper.getMonitorName(monitorId);
                cursorPrice = dbHelper.getMonitorPrice(monitorId);
                if(cursorName.getCount() > 0 && cursorName.moveToFirst()) {
                    monitorName = cursorName.getString(0);
                    cursorName.close();
                }
                if(cursorPrice.getCount() > 0 && cursorPrice.moveToFirst()) {
                    monitorPrice = cursorPrice.getFloat(0);
                    cursorPrice.close();
                }
            }
            if(computerId != 4) {
                cursorName = dbHelper.getComputerName(computerId);
                cursorPrice = dbHelper.getComputerPrice(computerId);
                if(cursorName.getCount() > 0 && cursorName.moveToFirst()) {
                    computerName = cursorName.getString(0);
                    cursorName.close();
                }
                if(cursorPrice.getCount() > 0 && cursorPrice.moveToFirst()) {
                    computerPrice = cursorPrice.getFloat(0);
                    cursorPrice.close();
                }
            }
            orderPrice = getPrice();
            smsManager.sendSms(userPhoneNumber, userName, keyboardName, keyboardPrice, mouseName, mousePrice, monitorName, monitorPrice, computerName, computerPrice, orderPrice);
            emailManager.sendOrderEmail(userEmail, userName, keyboardName, keyboardPrice, mouseName, mousePrice, monitorName, monitorPrice, computerName, computerPrice, orderPrice);
        } else {
            showOrderError();
        }
    }

//    private void deleteOrders() {
//        dbHelper.deleteFromOrders();
//    }
}