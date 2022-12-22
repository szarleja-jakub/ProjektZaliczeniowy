package com.example.projektzaliczeniowy.database;

import static com.example.projektzaliczeniowy.database.FeedReaderContract.FeedEntry.CLIENTS_COLUMN_EMAIL;
import static com.example.projektzaliczeniowy.database.FeedReaderContract.FeedEntry.CLIENTS_COLUMN_FULLNAME;
import static com.example.projektzaliczeniowy.database.FeedReaderContract.FeedEntry.CLIENTS_COLUMN_ID;
import static com.example.projektzaliczeniowy.database.FeedReaderContract.FeedEntry.CLIENTS_COLUMN_PASSWORD;
import static com.example.projektzaliczeniowy.database.FeedReaderContract.FeedEntry.CLIENTS_COLUMN_PHONE_NUMBER;
import static com.example.projektzaliczeniowy.database.FeedReaderContract.FeedEntry.CLIENTS_TABLE_NAME;
import static com.example.projektzaliczeniowy.database.FeedReaderContract.FeedEntry.COMPUTERS_COLUMN_ID;
import static com.example.projektzaliczeniowy.database.FeedReaderContract.FeedEntry.COMPUTERS_TABLE_NAME;
import static com.example.projektzaliczeniowy.database.FeedReaderContract.FeedEntry.ITEMS_COLUMN_NAME;
import static com.example.projektzaliczeniowy.database.FeedReaderContract.FeedEntry.ITEMS_COLUMN_PRICE;
import static com.example.projektzaliczeniowy.database.FeedReaderContract.FeedEntry.KEYBOARDS_COLUMN_ID;
import static com.example.projektzaliczeniowy.database.FeedReaderContract.FeedEntry.KEYBOARDS_TABLE_NAME;
import static com.example.projektzaliczeniowy.database.FeedReaderContract.FeedEntry.MICE_COLUMN_ID;
import static com.example.projektzaliczeniowy.database.FeedReaderContract.FeedEntry.MICE_TABLE_NAME;
import static com.example.projektzaliczeniowy.database.FeedReaderContract.FeedEntry.MONITORS_COLUMN_ID;
import static com.example.projektzaliczeniowy.database.FeedReaderContract.FeedEntry.MONITORS_TABLE_NAME;
import static com.example.projektzaliczeniowy.database.FeedReaderContract.FeedEntry.ORDERS_COLUMN_CLIENT_ID;
import static com.example.projektzaliczeniowy.database.FeedReaderContract.FeedEntry.ORDERS_TABLE_NAME;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FeedReaderDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Orders.db";

    private static final String SQL_CREATE_CLIENTS =
            "CREATE TABLE " + CLIENTS_TABLE_NAME + " (" +
                    CLIENTS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    FeedReaderContract.FeedEntry.CLIENTS_COLUMN_EMAIL + " TEXT NOT NULL, " +
                    FeedReaderContract.FeedEntry.CLIENTS_COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    FeedReaderContract.FeedEntry.CLIENTS_COLUMN_PHONE_NUMBER + " TEXT NOT NULL, " +
                    CLIENTS_COLUMN_FULLNAME + " TEXT NOT NULL)";

    private static final String SQL_INSERT_CLIENTS =
            "INSERT INTO " + CLIENTS_TABLE_NAME + " VALUES " +
                    "(0, 'admin@hotmail.com', '1234', '534611656', 'admin nowak'), " +
                    "(1, 'admin@gmail.com', '1234', '790216601', 'admin nowak')";

    private static final String SQL_DELETE_CLIENTS =
            "DROP TABLE IF EXISTS " + CLIENTS_TABLE_NAME;

    private static final String SQL_CREATE_KEYBOARDS =
            "CREATE TABLE " + KEYBOARDS_TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry.KEYBOARDS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    FeedReaderContract.FeedEntry.ITEMS_COLUMN_NAME + " TEXT NOT NULL, " +
                    FeedReaderContract.FeedEntry.ITEMS_COLUMN_DESC + " TEXT NOT NULL, " +
                    ITEMS_COLUMN_PRICE + " REAL NOT NULL)";

    private static final String SQL_GET_KEYBOARD_NAME =
            "SELECT " + ITEMS_COLUMN_NAME + " FROM " +
                    KEYBOARDS_TABLE_NAME;

    private static final String SQL_GET_KEYBOARD_PRICE =
            "SELECT " + ITEMS_COLUMN_PRICE + " FROM " +
                    KEYBOARDS_TABLE_NAME;

    private static final String SQL_GET_KEYBOARDS_NAMES =
            "SELECT " + FeedReaderContract.FeedEntry.KEYBOARDS_COLUMN_ID + ", " +
                    FeedReaderContract.FeedEntry.ITEMS_COLUMN_NAME + " FROM " +
                    KEYBOARDS_TABLE_NAME;

    private static final String SQL_GET_KEYBOARDS_PRICES =
            "SELECT " + FeedReaderContract.FeedEntry.KEYBOARDS_COLUMN_ID + ", " +
                    ITEMS_COLUMN_PRICE + " FROM " +
                    KEYBOARDS_TABLE_NAME;

    private static final String SQL_DELETE_KEYBOARDS =
            "DROP TABLE IF EXISTS " + KEYBOARDS_TABLE_NAME;

    private static final String SQL_INSERT_KEYBOARDS =
            "INSERT INTO " + KEYBOARDS_TABLE_NAME + " VALUES " +
                    "(0, 'Logitech MX', 'Wireless keyboard with a Bluetooth interface', 99.99), " +
                    "(1, 'Corsair Strafe', 'Wired keyboard with an USB interface', 169.99), " +
                    "(2, 'Logitech POP', 'Wireless keyboard with an USB interface', 129.99)";

    private static final String SQL_CREATE_MICE =
            "CREATE TABLE " + MICE_TABLE_NAME + " (" +
                    MICE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    FeedReaderContract.FeedEntry.ITEMS_COLUMN_NAME + " TEXT NOT NULL, " +
                    FeedReaderContract.FeedEntry.ITEMS_COLUMN_DESC + " TEXT NOT NULL, " +
                    ITEMS_COLUMN_PRICE + " REAL NOT NULL)";

    private static final String SQL_GET_MOUSE_NAME =
            "SELECT " + ITEMS_COLUMN_NAME + " FROM " +
                    MICE_TABLE_NAME;

    private static final String SQL_GET_MOUSE_PRICE =
            "SELECT " + ITEMS_COLUMN_PRICE + " FROM " +
                    MICE_TABLE_NAME;

    private static final String SQL_GET_MICE_NAMES =
            "SELECT " + MICE_COLUMN_ID + ", " +
                    FeedReaderContract.FeedEntry.ITEMS_COLUMN_NAME + " FROM " +
                    MICE_TABLE_NAME;

    private static final String SQL_GET_MICE_PRICES =
            "SELECT " + MICE_COLUMN_ID + ", " +
                    ITEMS_COLUMN_PRICE + " FROM " +
                    MICE_TABLE_NAME;

    private static final String SQL_DELETE_MICE =
            "DROP TABLE IF EXISTS " + MICE_TABLE_NAME;

    private static final String SQL_INSERT_MICE =
            "INSERT INTO " + MICE_TABLE_NAME + " VALUES " +
                    "(0, 'Logitech G', 'Wireless optic mouse having 7 buttons', 99.99), " +
                    "(1, 'Steelseries Prime', 'Wireless optic mouse having 5 buttons', 59.99), " +
                    "(2, 'Razer Deathadder', 'Wired optic mouse having 8 buttons', 39.99)";

    private static final String SQL_CREATE_MONITORS =
            "CREATE TABLE " + MONITORS_TABLE_NAME + " (" +
                    MONITORS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    FeedReaderContract.FeedEntry.ITEMS_COLUMN_NAME + " TEXT NOT NULL, " +
                    FeedReaderContract.FeedEntry.ITEMS_COLUMN_DESC + " TEXT NOT NULL, " +
                    ITEMS_COLUMN_PRICE + " REAL NOT NULL)";

    private static final String SQL_GET_MONITOR_NAME =
            "SELECT " + ITEMS_COLUMN_NAME + " FROM " +
                    MONITORS_TABLE_NAME;

    private static final String SQL_GET_MONITOR_PRICE =
            "SELECT " + ITEMS_COLUMN_PRICE + " FROM " +
                    MONITORS_TABLE_NAME;

    private static final String SQL_GET_MONITORS_NAMES =
            "SELECT " + MONITORS_COLUMN_ID + ", " +
                    FeedReaderContract.FeedEntry.ITEMS_COLUMN_NAME + " FROM " +
                    MONITORS_TABLE_NAME;

    private static final String SQL_GET_MONITORS_PRICES =
            "SELECT " + MONITORS_COLUMN_ID + ", " +
                    ITEMS_COLUMN_PRICE + " FROM " +
                    MONITORS_TABLE_NAME;

    private static final String SQL_DELETE_MONITORS =
            "DROP TABLE IF EXISTS " + MONITORS_TABLE_NAME;

    private static final String SQL_INSERT_MONITORS =
            "INSERT INTO " + MONITORS_TABLE_NAME + " VALUES " +
                    "(0, 'Acer Nitro', '27\", LED, 1920x1080, IPS, reaction: 1ms', 129.99), " +
                    "(1, 'Gigabyte M27Q', '27\", LED, 2560x1440, IPS, reaction: 1ms', 359.99), " +
                    "(2, 'LG UltraFine', '31.5\", LED, 3840x2160, IPS, reaction: 5ms', 449.99)";

    private static final String SQL_CREATE_COMPUTERS =
            "CREATE TABLE " + COMPUTERS_TABLE_NAME + " (" +
                    COMPUTERS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    FeedReaderContract.FeedEntry.ITEMS_COLUMN_NAME + " TEXT NOT NULL, " +
                    FeedReaderContract.FeedEntry.ITEMS_COLUMN_DESC + " TEXT NOT NULL, " +
                    ITEMS_COLUMN_PRICE + " REAL NOT NULL)";

    private static final String SQL_GET_COMPUTER_NAME =
            "SELECT " + ITEMS_COLUMN_NAME + " FROM " +
                    COMPUTERS_TABLE_NAME;

    private static final String SQL_GET_COMPUTER_PRICE =
            "SELECT " + ITEMS_COLUMN_PRICE + " FROM " +
                    COMPUTERS_TABLE_NAME;

    private static final String SQL_GET_COMPUTERS_NAMES =
            "SELECT " + COMPUTERS_COLUMN_ID + ", " +
                    FeedReaderContract.FeedEntry.ITEMS_COLUMN_NAME + " FROM " +
                    COMPUTERS_TABLE_NAME;

    private static final String SQL_GET_COMPUTERS_PRICES =
            "SELECT " + COMPUTERS_COLUMN_ID + ", " +
                    ITEMS_COLUMN_PRICE + " FROM " +
                    COMPUTERS_TABLE_NAME;

    private static final String SQL_DELETE_COMPUTERS =
            "DROP TABLE IF EXISTS " + COMPUTERS_TABLE_NAME;

    private static final String SQL_INSERT_COMPUTERS =
            "INSERT INTO " + COMPUTERS_TABLE_NAME + " VALUES " +
                    "(0, 'Dell Alienware', 'i7-12700KF, RTX 3080Ti, 32GB DDR5 4400MHz, 1TB SSD, 1TB HDD', 4999.99), " +
                    "(1, 'Predator Orion', 'i5-12400, GTX 1660, 16GB DDR4 3200MHz, 512GB SSD', 999.99), " +
                    "(2, 'Corsair Vengeance', 'i9-10850K, RTX 3080, 32GB DDR4 3200MHZ, 1TB SSD, 2TB HDD', 3299.99)";

    private static final String SQL_CREATE_ORDERS =
            "CREATE TABLE " + ORDERS_TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry.ORDERS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    FeedReaderContract.FeedEntry.ORDERS_COLUMN_CLIENT_ID + " INTEGER, " +
                    FeedReaderContract.FeedEntry.ORDERS_COLUMN_KEYBOARD_ID + " INTEGER, " +
                    FeedReaderContract.FeedEntry.ORDERS_COLUMN_MOUSE_ID + " INTEGER, " +
                    FeedReaderContract.FeedEntry.ORDERS_COLUMN_MONITOR_ID + " INTEGER, " +
                    FeedReaderContract.FeedEntry.ORDERS_COLUMN_COMPUTER_ID + " INTEGER, " +
                    FeedReaderContract.FeedEntry.ORDERS_COLUMN_ORDER_DATE + " TEXT NOT NULL, " +
                    FeedReaderContract.FeedEntry.ORDERS_COLUMN_ORDER_PRICE + " REAL NOT NULL, FOREIGN KEY (" +
                    FeedReaderContract.FeedEntry.ORDERS_COLUMN_CLIENT_ID + ") REFERENCES " +
                    FeedReaderContract.FeedEntry.CLIENTS_TABLE_NAME + "(" +
                    CLIENTS_COLUMN_ID + "), FOREIGN KEY (" +
                    FeedReaderContract.FeedEntry.ORDERS_COLUMN_KEYBOARD_ID + ") REFERENCES " +
                    FeedReaderContract.FeedEntry.KEYBOARDS_TABLE_NAME + "(" +
                    FeedReaderContract.FeedEntry.KEYBOARDS_COLUMN_ID + "), FOREIGN KEY (" +
                    FeedReaderContract.FeedEntry.ORDERS_COLUMN_MOUSE_ID + ") REFERENCES " +
                    FeedReaderContract.FeedEntry.MICE_TABLE_NAME + "(" +
                    MICE_COLUMN_ID + "), FOREIGN KEY (" +
                    FeedReaderContract.FeedEntry.ORDERS_COLUMN_MONITOR_ID + ") REFERENCES " +
                    FeedReaderContract.FeedEntry.MONITORS_TABLE_NAME + "(" +
                    MONITORS_COLUMN_ID + "), FOREIGN KEY (" +
                    FeedReaderContract.FeedEntry.ORDERS_COLUMN_COMPUTER_ID + ") REFERENCES " +
                    FeedReaderContract.FeedEntry.COMPUTERS_TABLE_NAME + "(" +
                    COMPUTERS_COLUMN_ID + "))";

    private static final String SQL_DELETE_ALL_FROM_ORDERS =
            "DELETE FROM " + ORDERS_TABLE_NAME;

    private static final String SQL_DELETE_ORDERS =
            "DROP TABLE IF EXISTS " + ORDERS_TABLE_NAME;

    private static final String SQL_GET_ORDERS =
            "SELECT * FROM " + ORDERS_TABLE_NAME;

    private static final String SQL_GET_USER_PASSWORD =
            "SELECT " + CLIENTS_COLUMN_PASSWORD + " FROM " +
                    CLIENTS_TABLE_NAME;

    private static final String SQL_GET_USER_EMAIL =
            "SELECT " + CLIENTS_COLUMN_EMAIL + " FROM " +
                    CLIENTS_TABLE_NAME;

    private static final String SQL_GET_USER_PHONE_NUMBER =
            "SELECT " + CLIENTS_COLUMN_PHONE_NUMBER + " FROM " +
                    CLIENTS_TABLE_NAME;

    private static final String SQL_GET_USER_FULLNAME =
            "SELECT " + CLIENTS_COLUMN_FULLNAME + " FROM " +
                    CLIENTS_TABLE_NAME;

    private static final String SQL_GET_USER_ID =
            "SELECT " + CLIENTS_COLUMN_ID + " FROM " +
                    CLIENTS_TABLE_NAME;

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CLIENTS);
        db.execSQL(SQL_CREATE_KEYBOARDS);
        db.execSQL(SQL_CREATE_MICE);
        db.execSQL(SQL_CREATE_MONITORS);
        db.execSQL(SQL_CREATE_COMPUTERS);
        db.execSQL(SQL_CREATE_ORDERS);
        db.execSQL(SQL_INSERT_CLIENTS);
        db.execSQL(SQL_INSERT_KEYBOARDS);
        db.execSQL(SQL_INSERT_MICE);
        db.execSQL(SQL_INSERT_MONITORS);
        db.execSQL(SQL_INSERT_COMPUTERS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ORDERS);
        db.execSQL(SQL_DELETE_CLIENTS);
        db.execSQL(SQL_DELETE_KEYBOARDS);
        db.execSQL(SQL_DELETE_MICE);
        db.execSQL(SQL_DELETE_MONITORS);
        db.execSQL(SQL_DELETE_COMPUTERS);
        onCreate(db);
    }

    public boolean insertUser(String email, String password, String phoneNumber, String fullname) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CLIENTS_COLUMN_EMAIL, email);
        values.put(CLIENTS_COLUMN_PASSWORD, password);
        values.put(CLIENTS_COLUMN_PHONE_NUMBER, phoneNumber);
        values.put(CLIENTS_COLUMN_FULLNAME, fullname);

        long newRowId = db.insert(CLIENTS_TABLE_NAME, null, values);
        return newRowId != -1;
    }

    public boolean insertOrder(int clientId, int keyboardId, int mouseId, int monitorId, int computerId, double price) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.ORDERS_COLUMN_CLIENT_ID, clientId);

        if(keyboardId < 3) {
            values.put(FeedReaderContract.FeedEntry.ORDERS_COLUMN_KEYBOARD_ID, keyboardId);
        } else {
            values.putNull(FeedReaderContract.FeedEntry.ORDERS_COLUMN_KEYBOARD_ID);
        }

        if(mouseId < 3) {
            values.put(FeedReaderContract.FeedEntry.ORDERS_COLUMN_MOUSE_ID, mouseId);
        } else {
            values.putNull(FeedReaderContract.FeedEntry.ORDERS_COLUMN_MOUSE_ID);
        }

        if(monitorId < 3) {
            values.put(FeedReaderContract.FeedEntry.ORDERS_COLUMN_MONITOR_ID, monitorId);
        } else {
            values.putNull(FeedReaderContract.FeedEntry.ORDERS_COLUMN_MONITOR_ID);
        }

        if(computerId < 3) {
            values.put(FeedReaderContract.FeedEntry.ORDERS_COLUMN_COMPUTER_ID, computerId);
        } else {
            values.putNull(FeedReaderContract.FeedEntry.ORDERS_COLUMN_COMPUTER_ID);
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());

        values.put(FeedReaderContract.FeedEntry.ORDERS_COLUMN_ORDER_DATE, date);

        values.put(FeedReaderContract.FeedEntry.ORDERS_COLUMN_ORDER_PRICE, price);

        long newRowId = db.insert(ORDERS_TABLE_NAME, null, values);
        return newRowId != -1;
    }

    public long truncate() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(SQL_DELETE_ORDERS);
        db.execSQL(SQL_DELETE_CLIENTS);
        db.execSQL(SQL_DELETE_KEYBOARDS);
        db.execSQL(SQL_DELETE_MICE);
        db.execSQL(SQL_DELETE_MONITORS);
        db.execSQL(SQL_DELETE_COMPUTERS);

        onCreate(db);

        return DatabaseUtils.queryNumEntries(db, ORDERS_TABLE_NAME);
    }

    public Cursor getNames(String item) {
        SQLiteDatabase db = this.getReadableDatabase();
        switch(item) {
            case "keyboards":
                return db.rawQuery(SQL_GET_KEYBOARDS_NAMES, null);
            case "mice":
                return db.rawQuery(SQL_GET_MICE_NAMES, null);
            case "monitors":
                return db.rawQuery(SQL_GET_MONITORS_NAMES, null);
            case "computers":
                return db.rawQuery(SQL_GET_COMPUTERS_NAMES, null);
        }
        return db.rawQuery(SQL_GET_KEYBOARDS_NAMES, null);
    }

    public Cursor getPrices(String item) {
        SQLiteDatabase db = this.getReadableDatabase();
        switch(item) {
            case "keyboards":
                return db.rawQuery(SQL_GET_KEYBOARDS_PRICES, null);
            case "mice":
                return db.rawQuery(SQL_GET_MICE_PRICES, null);
            case "monitors":
                return db.rawQuery(SQL_GET_MONITORS_PRICES, null);
            case "computers":
                return db.rawQuery(SQL_GET_COMPUTERS_PRICES, null);
        }
        return db.rawQuery(SQL_GET_KEYBOARDS_PRICES, null);
    }

    public Cursor getKeyboardName(int keyboardId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL_GET_KEYBOARD_NAME_BY_ID = SQL_GET_KEYBOARD_NAME + " WHERE " + KEYBOARDS_COLUMN_ID + " = " + keyboardId;
        return db.rawQuery(SQL_GET_KEYBOARD_NAME_BY_ID, null);
    }

    public Cursor getMouseName(int mouseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL_GET_MOUSE_NAME_BY_ID = SQL_GET_MOUSE_NAME + " WHERE " + MICE_COLUMN_ID + " = " + mouseId;
        return db.rawQuery(SQL_GET_MOUSE_NAME_BY_ID, null);
    }

    public Cursor getMonitorName(int monitorId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL_GET_MONITOR_NAME_BY_ID = SQL_GET_MONITOR_NAME + " WHERE " + MONITORS_COLUMN_ID + " = " + monitorId;
        return db.rawQuery(SQL_GET_MONITOR_NAME_BY_ID, null);
    }

    public Cursor getComputerName(int computerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL_GET_COMPUTER_NAME_BY_ID = SQL_GET_COMPUTER_NAME + " WHERE " + COMPUTERS_COLUMN_ID + " = " + computerId;
        return db.rawQuery(SQL_GET_COMPUTER_NAME_BY_ID, null);
    }

    public Cursor getKeyboardPrice(int keyboardId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL_GET_KEYBOARD_PRICE_BY_ID = SQL_GET_KEYBOARD_PRICE + " WHERE " + KEYBOARDS_COLUMN_ID + " = " + keyboardId;
        return db.rawQuery(SQL_GET_KEYBOARD_PRICE_BY_ID, null);
    }

    public Cursor getMousePrice(int mouseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL_GET_MOUSE_PRICE_BY_ID = SQL_GET_MOUSE_PRICE + " WHERE " + MICE_COLUMN_ID + " = " + mouseId;
        return db.rawQuery(SQL_GET_MOUSE_PRICE_BY_ID, null);
    }

    public Cursor getMonitorPrice(int monitorId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL_GET_MONITOR_PRICE_BY_ID = SQL_GET_MONITOR_PRICE + " WHERE " + MONITORS_COLUMN_ID + " = " + monitorId;
        return db.rawQuery(SQL_GET_MONITOR_PRICE_BY_ID, null);
    }

    public Cursor getComputerPrice(int computerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL_GET_COMPUTER_PRICE_BY_ID = SQL_GET_COMPUTER_PRICE + " WHERE " + COMPUTERS_COLUMN_ID + " = " + computerId;
        return db.rawQuery(SQL_GET_COMPUTER_PRICE_BY_ID, null);
    }

    public Cursor getPassword(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL_GET_USER_PASSWORD = FeedReaderDbHelper.SQL_GET_USER_PASSWORD + " WHERE " + CLIENTS_COLUMN_EMAIL + " = '" + email + "'";
        return db.rawQuery(SQL_GET_USER_PASSWORD, null);
    }

    public Cursor getName(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL_GET_USER_NAME = FeedReaderDbHelper.SQL_GET_USER_FULLNAME + " WHERE " + CLIENTS_COLUMN_ID + " = " + userId;
        return db.rawQuery(SQL_GET_USER_NAME, null);
    }

    public Cursor getEmail(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL_GET_USER_EMAIL = FeedReaderDbHelper.SQL_GET_USER_EMAIL + " WHERE " + CLIENTS_COLUMN_ID + " = " + userId;
        return db.rawQuery(SQL_GET_USER_EMAIL, null);
    }

    public Cursor getPhoneNumber(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL_GET_USER_NUMBER = FeedReaderDbHelper.SQL_GET_USER_PHONE_NUMBER + " WHERE " + CLIENTS_COLUMN_ID + " = " + userId;
        return db.rawQuery(SQL_GET_USER_NUMBER, null);
    }

    public Cursor getIdByEmail(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL_GET_USER_ID = FeedReaderDbHelper.SQL_GET_USER_ID + " WHERE " + CLIENTS_COLUMN_EMAIL + " = '" + userEmail + "'";
        return db.rawQuery(SQL_GET_USER_ID, null);
    }

    public Cursor getIdByPhone(String phoneNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL_GET_USER_ID = FeedReaderDbHelper.SQL_GET_USER_ID + " WHERE " + CLIENTS_COLUMN_PHONE_NUMBER + " = '" + phoneNumber + "'";
        return db.rawQuery(SQL_GET_USER_ID, null);
    }
//    public Cursor query() {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        return db.rawQuery(SQL_GET_ORDERS, null);
//    }

    public Cursor query(int clientId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String SQL_GET_USER_ORDERS = SQL_GET_ORDERS + " WHERE " + ORDERS_COLUMN_CLIENT_ID + " = " + clientId;

        return db.rawQuery(SQL_GET_USER_ORDERS, null);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

//    public void deleteFromOrders() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL(SQL_DELETE_ALL_FROM_ORDERS);
//    }
}
