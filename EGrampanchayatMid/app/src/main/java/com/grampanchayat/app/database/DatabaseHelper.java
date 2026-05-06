package com.grampanchayat.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.grampanchayat.app.models.Announcement;
import com.grampanchayat.app.models.Complaint;
import com.grampanchayat.app.models.WaterStatus;
import com.grampanchayat.app.models.RationInfo;
import com.grampanchayat.app.models.Poll;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "grampanchayat.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_ADMIN = "admin";
    public static final String TABLE_ANNOUNCEMENTS = "announcements";
    public static final String TABLE_COMPLAINTS = "complaints";
    public static final String TABLE_WATER = "water_status";
    public static final String TABLE_RATION = "ration_info";
    public static final String TABLE_POLL = "poll";
    public static final String TABLE_POLL_VOTES = "poll_votes";
    public static final String TABLE_EMERGENCY = "emergency";

    // Common columns
    public static final String COL_ID = "id";
    public static final String COL_DATE = "date";

    // Admin columns
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";

    // Announcement columns
    public static final String COL_TITLE = "title";
    public static final String COL_CONTENT = "content";

    // Complaint columns
    public static final String COL_NAME = "name";
    public static final String COL_ISSUE = "issue";
    public static final String COL_STATUS = "status";
    public static final String COL_PHONE = "phone";

    // Water columns
    public static final String COL_WATER_STATUS = "water_status";
    public static final String COL_WATER_TIME = "water_time";
    public static final String COL_WATER_NOTE = "water_note";

    // Ration columns
    public static final String COL_RATION_ITEM = "item_name";
    public static final String COL_RATION_STOCK = "stock";
    public static final String COL_RATION_DATE = "distribution_date";
    public static final String COL_RATION_NOTE = "note";

    // Poll columns
    public static final String COL_QUESTION = "question";
    public static final String COL_OPT1 = "option1";
    public static final String COL_OPT2 = "option2";
    public static final String COL_OPT3 = "option3";
    public static final String COL_VOTES1 = "votes1";
    public static final String COL_VOTES2 = "votes2";
    public static final String COL_VOTES3 = "votes3";
    public static final String COL_ACTIVE = "active";

    // Emergency columns
    public static final String COL_MESSAGE = "message";
    public static final String COL_ALERT_TYPE = "alert_type";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Admin table
        db.execSQL("CREATE TABLE " + TABLE_ADMIN + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_USERNAME + " TEXT," +
                COL_PASSWORD + " TEXT)");

        // Announcements table
        db.execSQL("CREATE TABLE " + TABLE_ANNOUNCEMENTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_TITLE + " TEXT," +
                COL_CONTENT + " TEXT," +
                COL_DATE + " TEXT)");

        // Complaints table
        db.execSQL("CREATE TABLE " + TABLE_COMPLAINTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " TEXT," +
                COL_PHONE + " TEXT," +
                COL_ISSUE + " TEXT," +
                COL_STATUS + " TEXT DEFAULT 'Pending'," +
                COL_DATE + " TEXT)");

        // Water status table
        db.execSQL("CREATE TABLE " + TABLE_WATER + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_WATER_STATUS + " TEXT," +
                COL_WATER_TIME + " TEXT," +
                COL_WATER_NOTE + " TEXT," +
                COL_DATE + " TEXT)");

        // Ration table
        db.execSQL("CREATE TABLE " + TABLE_RATION + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_RATION_ITEM + " TEXT," +
                COL_RATION_STOCK + " TEXT," +
                COL_RATION_DATE + " TEXT," +
                COL_RATION_NOTE + " TEXT)");

        // Poll table
        db.execSQL("CREATE TABLE " + TABLE_POLL + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_QUESTION + " TEXT," +
                COL_OPT1 + " TEXT," +
                COL_OPT2 + " TEXT," +
                COL_OPT3 + " TEXT," +
                COL_VOTES1 + " INTEGER DEFAULT 0," +
                COL_VOTES2 + " INTEGER DEFAULT 0," +
                COL_VOTES3 + " INTEGER DEFAULT 0," +
                COL_ACTIVE + " INTEGER DEFAULT 1," +
                COL_DATE + " TEXT)");

        // Emergency table
        db.execSQL("CREATE TABLE " + TABLE_EMERGENCY + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_TITLE + " TEXT," +
                COL_MESSAGE + " TEXT," +
                COL_ALERT_TYPE + " TEXT," +
                COL_DATE + " TEXT)");

        // Insert default admin
        ContentValues adminValues = new ContentValues();
        adminValues.put(COL_USERNAME, "admin");
        adminValues.put(COL_PASSWORD, "admin123");
        db.insert(TABLE_ADMIN, null, adminValues);

        // Insert sample announcement
        ContentValues ann = new ContentValues();
        ann.put(COL_TITLE, "Welcome / स्वागत");
        ann.put(COL_CONTENT, "Welcome to E-Grampanchayat portal. / ई-ग्रामपंचायत पोर्टलमध्ये आपले स्वागत आहे.");
        ann.put(COL_DATE, "2025-01-01");
        db.insert(TABLE_ANNOUNCEMENTS, null, ann);

        // Insert sample water status
        ContentValues water = new ContentValues();
        water.put(COL_WATER_STATUS, "ON");
        water.put(COL_WATER_TIME, "6:00 AM - 9:00 AM");
        water.put(COL_WATER_NOTE, "Water supply is available. / पाणी पुरवठा उपलब्ध आहे.");
        water.put(COL_DATE, "2025-01-01");
        db.insert(TABLE_WATER, null, water);

        // Insert sample ration
        ContentValues ration = new ContentValues();
        ration.put(COL_RATION_ITEM, "Rice / तांदूळ");
        ration.put(COL_RATION_STOCK, "Available / उपलब्ध");
        ration.put(COL_RATION_DATE, "15 Jan 2025");
        ration.put(COL_RATION_NOTE, "Bring your ration card. / रेशन कार्ड आणा.");
        db.insert(TABLE_RATION, null, ration);

        // Insert sample poll
        ContentValues poll = new ContentValues();
        poll.put(COL_QUESTION, "Which road needs repair first? / कोणता रस्ता आधी दुरुस्त करावा?");
        poll.put(COL_OPT1, "Main Road / मुख्य रस्ता");
        poll.put(COL_OPT2, "School Road / शाळेचा रस्ता");
        poll.put(COL_OPT3, "Market Road / बाजार रस्ता");
        poll.put(COL_VOTES1, 0);
        poll.put(COL_VOTES2, 0);
        poll.put(COL_VOTES3, 0);
        poll.put(COL_ACTIVE, 1);
        poll.put(COL_DATE, "2025-01-01");
        db.insert(TABLE_POLL, null, poll);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANNOUNCEMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WATER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POLL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMERGENCY);
        onCreate(db);
    }

    // ==================== ADMIN ====================
    public boolean checkAdmin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ADMIN, null,
                COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    // ==================== ANNOUNCEMENTS ====================
    public boolean addAnnouncement(String title, String content, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, title);
        values.put(COL_CONTENT, content);
        values.put(COL_DATE, date);
        long result = db.insert(TABLE_ANNOUNCEMENTS, null, values);
        return result != -1;
    }

    public List<Announcement> getAllAnnouncements() {
        List<Announcement> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ANNOUNCEMENTS, null, null, null, null, null,
                COL_ID + " DESC");
        if (cursor.moveToFirst()) {
            do {
                Announcement a = new Announcement(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTENT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE))
                );
                list.add(a);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean deleteAnnouncement(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ANNOUNCEMENTS, COL_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    // ==================== COMPLAINTS ====================
    public boolean addComplaint(String name, String phone, String issue, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PHONE, phone);
        values.put(COL_ISSUE, issue);
        values.put(COL_STATUS, "Pending");
        values.put(COL_DATE, date);
        long result = db.insert(TABLE_COMPLAINTS, null, values);
        return result != -1;
    }

    public List<Complaint> getAllComplaints() {
        List<Complaint> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COMPLAINTS, null, null, null, null, null,
                COL_ID + " DESC");
        if (cursor.moveToFirst()) {
            do {
                Complaint c = new Complaint(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_ISSUE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE))
                );
                list.add(c);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean updateComplaintStatus(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STATUS, status);
        return db.update(TABLE_COMPLAINTS, values, COL_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    // ==================== WATER ====================
    public boolean setWaterStatus(String status, String time, String note, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_WATER);
        ContentValues values = new ContentValues();
        values.put(COL_WATER_STATUS, status);
        values.put(COL_WATER_TIME, time);
        values.put(COL_WATER_NOTE, note);
        values.put(COL_DATE, date);
        long result = db.insert(TABLE_WATER, null, values);
        return result != -1;
    }

    public WaterStatus getWaterStatus() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_WATER, null, null, null, null, null,
                COL_ID + " DESC LIMIT 1");
        WaterStatus ws = null;
        if (cursor.moveToFirst()) {
            ws = new WaterStatus(
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_WATER_STATUS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_WATER_TIME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_WATER_NOTE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE))
            );
        }
        cursor.close();
        return ws;
    }

    // ==================== RATION ====================
    public boolean setRationInfo(String item, String stock, String date, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_RATION);
        ContentValues values = new ContentValues();
        values.put(COL_RATION_ITEM, item);
        values.put(COL_RATION_STOCK, stock);
        values.put(COL_RATION_DATE, date);
        values.put(COL_RATION_NOTE, note);
        long result = db.insert(TABLE_RATION, null, values);
        return result != -1;
    }

    public RationInfo getRationInfo() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RATION, null, null, null, null, null,
                COL_ID + " DESC LIMIT 1");
        RationInfo ri = null;
        if (cursor.moveToFirst()) {
            ri = new RationInfo(
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_RATION_ITEM)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_RATION_STOCK)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_RATION_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_RATION_NOTE))
            );
        }
        cursor.close();
        return ri;
    }

    // ==================== POLL ====================
    public boolean addPoll(String question, String opt1, String opt2, String opt3, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Deactivate all old polls
        ContentValues deactivate = new ContentValues();
        deactivate.put(COL_ACTIVE, 0);
        db.update(TABLE_POLL, deactivate, null, null);

        ContentValues values = new ContentValues();
        values.put(COL_QUESTION, question);
        values.put(COL_OPT1, opt1);
        values.put(COL_OPT2, opt2);
        values.put(COL_OPT3, opt3);
        values.put(COL_VOTES1, 0);
        values.put(COL_VOTES2, 0);
        values.put(COL_VOTES3, 0);
        values.put(COL_ACTIVE, 1);
        values.put(COL_DATE, date);
        long result = db.insert(TABLE_POLL, null, values);
        return result != -1;
    }

    public Poll getActivePoll() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_POLL, null, COL_ACTIVE + "=1",
                null, null, null, COL_ID + " DESC LIMIT 1");
        Poll poll = null;
        if (cursor.moveToFirst()) {
            poll = new Poll(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_QUESTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_OPT1)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_OPT2)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_OPT3)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_VOTES1)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_VOTES2)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_VOTES3))
            );
        }
        cursor.close();
        return poll;
    }

    public boolean voteOnPoll(int pollId, int optionNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        String col = optionNumber == 1 ? COL_VOTES1 : (optionNumber == 2 ? COL_VOTES2 : COL_VOTES3);
        db.execSQL("UPDATE " + TABLE_POLL + " SET " + col + " = " + col + " + 1 WHERE " + COL_ID + " = " + pollId);
        return true;
    }

    // ==================== EMERGENCY ====================
    public boolean addEmergency(String title, String message, String type, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, title);
        values.put(COL_MESSAGE, message);
        values.put(COL_ALERT_TYPE, type);
        values.put(COL_DATE, date);
        long result = db.insert(TABLE_EMERGENCY, null, values);
        return result != -1;
    }

    public List<com.grampanchayat.app.models.Emergency> getAllEmergencies() {
        List<com.grampanchayat.app.models.Emergency> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EMERGENCY, null, null, null, null, null,
                COL_ID + " DESC");
        if (cursor.moveToFirst()) {
            do {
                com.grampanchayat.app.models.Emergency e = new com.grampanchayat.app.models.Emergency(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MESSAGE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_ALERT_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE))
                );
                list.add(e);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean deleteEmergency(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_EMERGENCY, COL_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }
}
