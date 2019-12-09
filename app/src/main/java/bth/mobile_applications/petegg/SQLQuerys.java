package bth.mobile_applications.petegg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Class for handling the sql querys: Load and Save.
 */
public class SQLQuerys {

    /**
     * Constructor
     */
    public SQLQuerys(){

    }

    /**
     * Retrieves an Int from the database
     * @param context Activity which executes the query
     * @return int result
     */
    public static int loadIntFromDatabase(long id, Context context, String column){
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


    /**
     * Retrieves a String from the database
     * @param context Activity which executes the query
     * @return String result
     */
    public static String loadStringFromDatabase(long id, Context context, String column){
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

    /**
     * Loads the id of the pet.
     * @param context Activity which executes the query
     * @return long id
     */
    public static long getIdLastLivedEgg(Context context){
        SQLConnection dbHelper = new SQLConnection(context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String column = "status";
        String[] projection = {
                BaseColumns._ID,
                column
        };

        String selection = column + " = ?";
        String[] selectionArgs = { Integer.toString(0) };

        Long result = 0L;
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
                result = cursor.getLong(cursor.getColumnIndex("_id"));
            }
            cursor.close();

        } catch (SQLException e){
            e.printStackTrace();
        }

        dbHelper.close();

        return result;

    }

    /**
     * Saves an integer to the database entry of the id in use.
     * @param id id of the pet
     * @param context Activity which executed the query
     * @param column column to save to
     * @param dataToInsert data to save
     * @return true for successfull save, false for error
     */
    public static Boolean saveStringToDB(long id, Context context, String column, String dataToInsert){
        SQLConnection dbHelper = new SQLConnection(context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(column, dataToInsert);

        String selection = dbHelper._ID + " = ?";
        String[] selectionArgs = { (String) String.valueOf(id)};

        int rows = 0;
        try {
            rows = db.update("petegg_data",values,selection,selectionArgs);


        } catch (SQLException e){
            e.printStackTrace();
        }

        dbHelper.close();

        if(rows > 0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Saves an integer to the database entry of the id in use.
     * @param id id of the pet
     * @param context Activity which executed the query
     * @param column column to save to
     * @param dataToInsert data to save
     * @return true for successfull save, false for error
     */
    public static Boolean saveIntToDB(long id, Context context, String column, int dataToInsert){
        SQLConnection dbHelper = new SQLConnection(context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(column, dataToInsert);

        String selection = dbHelper._ID + " = ?";
        String[] selectionArgs = { (String) String.valueOf(id)};

        int rows = 0;
        try {
            rows = db.update("petegg_data",values,selection,selectionArgs);


        } catch (SQLException e){
            e.printStackTrace();
        }

        dbHelper.close();

        if(rows > 0){
            return true;
        }else{
            return false;
        }
    }
}
