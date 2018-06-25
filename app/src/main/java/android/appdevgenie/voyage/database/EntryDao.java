package android.appdevgenie.voyage.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface EntryDao {

    @Insert
    void insertEntry(NewEntry newEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEntry(NewEntry newEntry);

    @Delete
    void deleteEntry(NewEntry newEntry);

    @Query("SELECT * FROM voyage WHERE id = :id")
    List<NewEntry> loadEntryById(int id);

    @Query("SELECT * FROM voyage ORDER By updated_on DESC")
    List<NewEntry> loadAllEntries();
}
