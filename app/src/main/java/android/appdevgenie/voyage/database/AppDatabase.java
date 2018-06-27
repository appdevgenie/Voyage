package android.appdevgenie.voyage.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import java.util.Calendar;

@Database(entities = {NewEntry.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = AppDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "voyage_db";
    private static final Object LOCK = new Object();
    private static AppDatabase dbInstance;

    public static AppDatabase getInstance(Context context){

        if(dbInstance == null){
            synchronized (LOCK){
                Log.d(TAG, "getInstance: creating new database instance");
                dbInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(TAG, "getInstance: getting database instance");
        return dbInstance;
    }

    public abstract EntryDao entryDao();
}
