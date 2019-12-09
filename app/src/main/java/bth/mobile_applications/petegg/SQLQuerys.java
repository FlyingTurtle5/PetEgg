package bth.mobile_applications.petegg;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class SQLQuerys {

    public SQLQuerys(){

    }

    public int loadIntFromDatabase(long id, Context context, String column){
        SQLConnection dbHelper = new SQLConnection(context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                column
        };

        String selection = dbHelper._ID + " = ?";
        String[] selectionArgs = { (String) String.valueOf(id)};

        int result = 0;
        try {
            Cursor cursor = db.query(
                    "petegg_data",   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null);


            while(cursor.moveToNext()) {
                result = cursor.getInt(cursor.getColumnIndex(column));
            }
            cursor.close();

        } catch (SQLException e){
            e.printStackTrace();
        }

        dbHelper.close();

        return result;
    }


    public String loadStringFromDatabase(long id, Context context, String column){
        SQLConnection dbHelper = new SQLConnection(context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                column
        };

        String selection = dbHelper._ID + " = ?";
        String[] selectionArgs = { (String) String.valueOf(id)};

        String result = null;
        try {
            Cursor cursor = db.query(
                    "petegg_data",   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null);


            while(cursor.moveToNext()) {
                result = cursor.getString(cursor.getColumnIndex(column));
            }
            cursor.close();

        } catch (SQLException e){
            e.printStackTrace();
        }

        dbHelper.close();

        return result;
    }
}
