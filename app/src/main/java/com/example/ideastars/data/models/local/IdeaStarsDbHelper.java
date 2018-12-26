package com.example.ideastars.data.models.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IdeaStarsDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "IdeaStars.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String INT_TYPE = " INTEGER";

    private static final String BOOLEAN_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_IDEA_TABLE =
            "CREATE TABLE " + IdeaStarsLocalEntity.IDEA_TABLE_NAME + " (" +
                    IdeaStarsLocalEntity.IDEA_COLUMN_ID + INT_TYPE + " PRIMARY KEY" + COMMA_SEP +
                    IdeaStarsLocalEntity.IDEA_COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    IdeaStarsLocalEntity.IDEA_COLUMN_PRIORITY + INT_TYPE +
                    " )";

    private static final String SQL_CREATE_WORD_TABLE =
            "CREATE TABLE " + IdeaStarsLocalEntity.WORD_TABLE_NAME + " (" +
                    IdeaStarsLocalEntity.WORD_COLUMN_ID + INT_TYPE + " PRIMARY KEY" + COMMA_SEP +
                    IdeaStarsLocalEntity.WORD_COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    IdeaStarsLocalEntity.WORD_COLUMN_COLOR + INT_TYPE + COMMA_SEP +
                    IdeaStarsLocalEntity.WORD_COLUMN_IDEA_ID + INT_TYPE + COMMA_SEP +
                    IdeaStarsLocalEntity.WORD_COLUMN_PRIORITY + INT_TYPE +
                    " )";

    private static final String SQL_CREATE_ITEM_TABLE =
            "CREATE TABLE " + IdeaStarsLocalEntity.ITEM_TABLE_NAME + " (" +
                    IdeaStarsLocalEntity.ITEM_COLUMN_ID + INT_TYPE + " PRIMARY KEY" + COMMA_SEP +
                    IdeaStarsLocalEntity.ITEM_COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    IdeaStarsLocalEntity.ITEM_COLUMN_WORD_ID + INT_TYPE + COMMA_SEP +
                    IdeaStarsLocalEntity.ITEM_COLUMN_PRIORITY + INT_TYPE +
                    " )";

    private static final String SQL_CREATE_FAV_TABLE =
            "CREATE TABLE " + IdeaStarsLocalEntity.FAVORITE_TABLE_NAME + " (" +
                    IdeaStarsLocalEntity.FAVORITE_COLUMN_ID + INT_TYPE + " PRIMARY KEY" + COMMA_SEP +
                    IdeaStarsLocalEntity.FAVORITE_COLUMN_FAV + INT_TYPE +
                    " )";

    private static final String SQL_CREATE_FAV_ITEM_TABLE =
            "CREATE TABLE " + IdeaStarsLocalEntity.FAVORITE_ITEM_TABLE_NAME + " (" +
                    IdeaStarsLocalEntity.FAVORITE_ITEM_COLUMN_ID + INT_TYPE + " PRIMARY KEY" + COMMA_SEP +
                    IdeaStarsLocalEntity.FAVORITE_ITEM_COLUMN_FAV_ID + INT_TYPE + COMMA_SEP +
                    IdeaStarsLocalEntity.FAVORITE_ITEM_COLUMN_ITEM_ID + INT_TYPE +
                    " )";

    public IdeaStarsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void deleteDatabase(Context context)
    {
        SQLiteDatabase.deleteDatabase(context.getDatabasePath(getDatabaseName()));
    }

    public void onCreate(SQLiteDatabase db)
    {
        db.beginTransaction();
        try {
            db.execSQL(SQL_CREATE_IDEA_TABLE);
            db.execSQL(SQL_CREATE_WORD_TABLE);
            db.execSQL(SQL_CREATE_ITEM_TABLE);
            db.execSQL(SQL_CREATE_FAV_TABLE);
            db.execSQL(SQL_CREATE_FAV_ITEM_TABLE);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Not required as at version 1
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Not required as at version 1
    }

}
