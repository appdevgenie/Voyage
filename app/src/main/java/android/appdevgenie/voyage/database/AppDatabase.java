package android.appdevgenie.voyage.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.util.Log;

@Database(entities = {NewEntry.class}, version = 2, exportSchema = false)
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
                        .addMigrations(MIGRATION_1_2)
                        .build();
            }
        }
        Log.d(TAG, "getInstance: getting database instance");
        return dbInstance;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE voyage "
                    + " ADD COLUMN mood INTEGER");
        }
    };

    public abstract EntryDao entryDao();
}
