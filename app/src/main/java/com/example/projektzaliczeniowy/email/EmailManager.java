package com.example.projektzaliczeniowy.email;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.projektzaliczeniowy.R;

public class EmailManager {
    Context context;

    public EmailManager(Context context) {
        this.context = context;
    }

    public void sendOrderEmail(String userEmail, String userName, String keyboardName, float keyboardPrice, String mouseName, float mousePrice, String monitorName, float monitorPrice, String computerName, float computerPrice, float price) {
        if(!userEmail.equals("")) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            String greeting = context.getString(R.string.greeting);
            String userFirstname = userName.substring(0, userName.indexOf(" "));
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, userEmail);
            intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.order_confirmation));
            String message = greeting + " " + userFirstname + ", \n\n" +
                    context.getString(R.string.order_mail_part1) + "\n" +
                    context.getString(R.string.order_mail_part2) + "\n" +
                    context.getString(R.string.order_mail_part3) + "\n\n" +
                    context.getString(R.string.order_mail_part4);
            if(!keyboardName.equals("")) {
                message += "\n\n" + context.getString(R.string.keyboard) + ": " + keyboardName + "\n" + context.getString(R.string.price) + ": $" + keyboardPrice;
            }
            if(!mouseName.equals("")) {
                message += "\n\n" + context.getString(R.string.mouse) + ": " + mouseName + "\n" + context.getString(R.string.price) + ": $" + mousePrice;
            }
            if(!monitorName.equals("")) {
                message += "\n\n" + context.getString(R.string.monitor) + ": " + monitorName + "\n" + context.getString(R.string.price) + ": $" + monitorPrice;
            }
            if(!computerName.equals("")) {
                message += "\n\n" + context.getString(R.string.computer) + ": " + computerName + "\n" + context.getString(R.string.price) + ": $" + computerPrice;
            }
            message += "\n\n" + context.getString(R.string.order_price) + ": $" + price;
            intent.putExtra(Intent.EXTRA_TEXT, message);
            context.startActivity(intent);
        }
    }


}
