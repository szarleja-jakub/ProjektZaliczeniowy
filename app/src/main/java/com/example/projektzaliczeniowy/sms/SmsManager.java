package com.example.projektzaliczeniowy.sms;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.projektzaliczeniowy.R;
import com.example.projektzaliczeniowy.activities.OrderActivity;

public class SmsManager {
    Context context;
    String permission = Manifest.permission.SEND_SMS;

    public SmsManager(Context context) {
        this.context = context;
    }

    public void sendSms(String userPhoneNumber, String userName, String keyboardName, float keyboardPrice, String mouseName, float mousePrice, String monitorName, float monitorPrice, String computerName, float computerPrice, float price) {
        if (!userPhoneNumber.equals("")) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + userPhoneNumber));
            String greeting = context.getString(R.string.greeting);
            String confirmation = context.getString(R.string.order_confirmation);
            String userFirstname = userName.substring(0, userName.indexOf(" "));
            String message = confirmation + "\n\n" + greeting + " " + userFirstname + ", \n\n" +
                    context.getString(R.string.order_mail_part1) + "\n" +
                    context.getString(R.string.order_mail_part2) + "\n" +
                    context.getString(R.string.order_mail_part3) + "\n\n" +
                    context.getString(R.string.order_mail_part4);
            if (!keyboardName.equals("")) {
                message += "\n\n" + context.getString(R.string.keyboard) + ": " + keyboardName + "\n" + context.getString(R.string.price) + ": $" + keyboardPrice;
            }
            if (!mouseName.equals("")) {
                message += "\n\n" + context.getString(R.string.mouse) + ": " + mouseName + "\n" + context.getString(R.string.price) + ": $" + mousePrice;
            }
            if (!monitorName.equals("")) {
                message += "\n\n" + context.getString(R.string.monitor) + ": " + monitorName + "\n" + context.getString(R.string.price) + ": $" + monitorPrice;
            }
            if (!computerName.equals("")) {
                message += "\n\n" + context.getString(R.string.computer) + ": " + computerName + "\n" + context.getString(R.string.price) + ": $" + computerPrice;
            }
            message += "\n\n" + context.getString(R.string.order_price) + ": $" + price;

            intent.putExtra("sms_body", message);
            try {
                context.startActivity(intent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
