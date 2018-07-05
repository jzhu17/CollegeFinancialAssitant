package com.example.matthewspc.financeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.matthewspc.financeapp.ProfileDatabase.DATABASE_TABLE;

/**
 * Created by Beast_07 on 11/28/2017.
 */

public class ExpensesHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1; //version
    private static final String DATABASE_NAME = "Expenses";
    //This is the tables name

    private static final String TABLE_NAME = "expenses";
    //These are the columns
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name_column";
    public static final String KEY_COST = "cost_column";
    public static final String KEY_DATE = "date_column";
    public static final String KEY_TAG = "tag_column";

    public ExpensesHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "create table " + TABLE_NAME +  " ("
                + KEY_ID + " integer primary key autoincrement, "
                + KEY_NAME +" text not null, "
                + KEY_COST +" text not null, "
                + KEY_DATE +" text not null,"
                + KEY_TAG +" text not null);";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old, int current) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(sqLiteDatabase);
    }
    // Adding new Expense
    long addExpense (ExpenseLogEntryData Expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, Expense.getName());
        values.put(KEY_COST, Expense.getCost());
        values.put(KEY_DATE, Expense.getDate());
        values.put(KEY_TAG, Expense.getTag());
        // Inserting Row
        long id = db.insert(TABLE_NAME, null, values);
        //Substract some monies from the big table.

        db.close(); // Closing database connection
        return id;
    }

    // Getting single Expense
    public ExpenseLogEntryData getExpense(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{ KEY_ID,
                        KEY_NAME,
                        KEY_COST,
                        KEY_DATE,
                        KEY_TAG },
                KEY_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);
        cursor.moveToFirst();
        //Log.d("Thisisatest",cursor.getCount()+"");
        ExpenseLogEntryData expense = new ExpenseLogEntryData((cursor.getString(1)),cursor.getString(2), cursor.getString(3), cursor.getString(4));

        return expense;
    }

    // Getting All Expenses
    public Cursor getAllExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME +";", null);
        if (c != null) {
            c.moveToFirst();
            return c;
        }
        else
        {
            return null;
        }
    }

    // Updating single expense
    public void updateExpense(ExpenseLogEntryData expense) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, expense.getName());
        values.put(KEY_COST, expense.getCost());
        values.put(KEY_DATE, expense.getDate());
        values.put(KEY_TAG, expense.getTag());

        // updating row
        db.update(ExpensesHelper.TABLE_NAME, values, KEY_ID + " = ?", null);
        db.close();
    }

    // Deleting single
    public boolean deleteExpense(int id) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();


        String q = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID
                + " = \"" + id + "\"";
        Cursor cursor = db.rawQuery(q, null);
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            db.delete(TABLE_NAME, KEY_ID + " = ?",
                    new String[] { String.valueOf(id)});
            cursor.close();
            result = true;
        }

        //Add back some monies from the big table
        db.close();
        return result;
    }

    public void removeAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_NAME, null, null);
    }


    // Getting list Count
    public int getExpenseCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }























}
