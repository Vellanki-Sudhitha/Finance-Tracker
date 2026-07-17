package com.example.financetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.financetracker.model.Expense;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FinanceTracker.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_EXPENSE = "expenses";

    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_AMOUNT = "amount";
    public static final String COL_CATEGORY = "category";
    public static final String COL_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE =
                "CREATE TABLE " + TABLE_EXPENSE + "("
                        + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COL_TITLE + " TEXT,"
                        + COL_AMOUNT + " REAL,"
                        + COL_CATEGORY + " TEXT,"
                        + COL_DATE + " TEXT"
                        + ")";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        onCreate(db);

    }

    // Insert Expense
    public boolean insertExpense(String title,
                                 double amount,
                                 String category,
                                 String date) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_TITLE, title);
        values.put(COL_AMOUNT, amount);
        values.put(COL_CATEGORY, category);
        values.put(COL_DATE, date);

        long result = db.insert(TABLE_EXPENSE, null, values);

        return result != -1;
    }

    // Get All Expenses
    public Cursor getAllExpenses() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_EXPENSE + " ORDER BY id DESC",
                null
        );
    }

    // Delete Expense
    public void deleteExpense(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_EXPENSE,
                COL_ID + "=?",
                new String[]{String.valueOf(id)});
    }

    // Update Expense
    public boolean updateExpense(int id,
                                 String title,
                                 double amount,
                                 String category,
                                 String date) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_TITLE, title);
        values.put(COL_AMOUNT, amount);
        values.put(COL_CATEGORY, category);
        values.put(COL_DATE, date);

        int rows = db.update(
                TABLE_EXPENSE,
                values,
                COL_ID + "=?",
                new String[]{String.valueOf(id)}
        );

        return rows > 0;
    }

    // Get Total Expense
    public double getTotalExpense() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT SUM(amount) FROM " + TABLE_EXPENSE,
                null
        );

        double total = 0;

        if (cursor.moveToFirst()) {

            total = cursor.getDouble(0);

        }

        cursor.close();

        return total;
    }

    // Delete All Expenses
    public void deleteAllExpenses() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_EXPENSE, null, null);
    }

    // Get All Expenses as a List<Expense>, ready for the adapter/UI to use directly
    public List<Expense> getAllExpensesList() {

        List<Expense> expenses = new ArrayList<>();

        Cursor cursor = getAllExpenses();

        if (cursor != null) {

            while (cursor.moveToNext()) {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_AMOUNT));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE));

                expenses.add(new Expense(id, title, amount, category, date));
            }

            cursor.close();
        }

        return expenses;
    }

    // Get total amount spent per category, used by the Insights screen
    public Map<String, Double> getCategoryTotals() {

        Map<String, Double> totals = new LinkedHashMap<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT " + COL_CATEGORY + ", SUM(" + COL_AMOUNT + ") FROM "
                        + TABLE_EXPENSE + " GROUP BY " + COL_CATEGORY
                        + " ORDER BY SUM(" + COL_AMOUNT + ") DESC",
                null
        );

        if (cursor != null) {

            while (cursor.moveToNext()) {
                String category = cursor.getString(0);
                double total = cursor.getDouble(1);
                totals.put(category, total);
            }

            cursor.close();
        }

        return totals;
    }

}