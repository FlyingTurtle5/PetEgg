package bth.mobile_applications.petegg;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class SQLConnection extends SQLiteOpenHelper implements BaseColumns{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PetEgg.db";

    public static final String TABLE_NAME = "petegg_data";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + SQLConnection.TABLE_NAME + " (" +
                    SQLConnection._ID + " INTEGER PRIMARY KEY, " +
                    "petname TEXT, " +
                    "status INTEGER, " +
                    "age INTEGER, " +
                    "birthday INTEGER, " +
                    "health INTEGER, " +
                    "hunger INTEGER, " +
                    "lastfed INTEGER, " +
                    "happyness INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SQLConnection.TABLE_NAME;

    public SQLConnection(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
