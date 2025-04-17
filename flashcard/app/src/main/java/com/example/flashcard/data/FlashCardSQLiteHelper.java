package com.example.flashcard.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.SimpleCursorAdapter;

import java.time.LocalDateTime;

public class FlashCardSQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "flashcard";
    private static final int DB_VERSION = 2;

    public FlashCardSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //onCreate() get called when database first gets created, we using it to craet table + insert data
    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        updateMyDatabase(db, oldversion, newversion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldversion, int newversion) {
        if (oldversion < 1) {
            db.execSQL("PRAGMA foreign_keys = ON;");
            db.execSQL(" CREATE TABLE CARDSET ( " +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NAME TEXT NOT NULL, " +
                    "CREATEDAT TEXT NOT NULL, " +
                    "UPDATEDAT TEXT NOT NULL);");
            db.execSQL("CREATE TABLE CARD ( " +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "FRONTTEXT TEXT NOT NULL, " +
                    "BACKTEXT TEXT NOT NULL, " +
                    "CREATEDAT TEXT NOT NULL, " +
                    "UPDATEDAT TEXT NOT NULL, " +
                    "CARDSETID INTEGER NOT NULL, " +
                    "FOREIGN KEY (CARDSETID) REFERENCES CARDSET(_id) ON DELETE CASCADE);");
        }
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = ON;");
    }

    public static void insertCardSet(SQLiteDatabase db, String name) {
        ContentValues setValues = new ContentValues();
        setValues.put("NAME", name);
        setValues.put("CREATEDAT", LocalDateTime.now().toString());
        setValues.put("UPDATEDAT", LocalDateTime.now().toString());
        db.insert("CARDSET", null, setValues);
    }

    public static void insertCard(SQLiteDatabase db, int cardsetId, String frontText, String backText) {
        ContentValues cardValues = new ContentValues();
        cardValues.put("CARDSETID", cardsetId);
        cardValues.put("FRONTTEXT", frontText);
        cardValues.put("BACKTEXT", backText);
        cardValues.put("CREATEDAT", LocalDateTime.now().toString());
        cardValues.put("UPDATEDAT", LocalDateTime.now().toString());
        db.insert("CARD", null, cardValues);
    }

    public static void updateCardSet(SQLiteDatabase db, int id, String name) {
        ContentValues setValues = new ContentValues();
        setValues.put("NAME", name);
        db.update("CARDSET", setValues,"_id=?", new String[] {String.valueOf(id)});
    }
    public static void updateCard(SQLiteDatabase db, int id, String frontText, String backText) {
        ContentValues cardValues = new ContentValues();
        cardValues.put("FRONTTEXT", frontText);
        cardValues.put("BACKTEXT", backText);
        cardValues.put("UPDATEDAT", LocalDateTime.now().toString());
        db.update("CARD", cardValues, "_id=?", new String[] {String.valueOf(id)});
    }
    public static void deleteCardSet(SQLiteDatabase db, int id) {
        db.delete("CARDSET", "_id=?", new String[] {String.valueOf(id)});
        db.close();
    }
    public static void restoreCardSet(SQLiteDatabase db, int id, String name) {
        ContentValues setValues = new ContentValues();
        setValues.put("_id",id);
        setValues.put("NAME", name);
        setValues.put("CREATEDAT", LocalDateTime.now().toString());
        setValues.put("UPDATEDAT", LocalDateTime.now().toString());
        db.insert("CARDSET", null, setValues);
        db.close();
    }
}
