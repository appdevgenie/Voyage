package android.appdevgenie.voyage.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "voyage")
public class NewEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String thoughts;
    private String entryTime;
    private String entryDate;
    @ColumnInfo(name = "updated_on")
    private Date updatedOn;

    @Ignore
    public NewEntry(String thoughts, String entryTime, String entryDate, Date updatedOn) {
        this.thoughts = thoughts;
        this.entryTime = entryTime;
        this.entryDate = entryDate;
        this.updatedOn = updatedOn;
    }

    public NewEntry(int id, String thoughts, String entryTime, String entryDate, Date updatedOn) {
        this.id = id;
        this.thoughts = thoughts;
        this.entryTime = entryTime;
        this.entryDate = entryDate;
        this.updatedOn = updatedOn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThoughts() {
        return thoughts;
    }

    public void setThoughts(String thoughts) {
        this.thoughts = thoughts;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
}
