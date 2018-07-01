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
    private String username;
    private String thoughts;
    private String entryTime;
    private String entryDate;
    private int mood;
    @ColumnInfo(name = "updated_on")
    private Date updatedOn;

    @Ignore
    public NewEntry(String username, String thoughts, String entryTime, String entryDate, int mood, Date updatedOn) {
        this.username = username;
        this.thoughts = thoughts;
        this.entryTime = entryTime;
        this.entryDate = entryDate;
        this.mood = mood;
        this.updatedOn = updatedOn;
    }

    public NewEntry(int id, String username, String thoughts, String entryTime, String entryDate, int mood, Date updatedOn) {
        this.id = id;
        this.username = username;
        this.thoughts = thoughts;
        this.entryTime = entryTime;
        this.entryDate = entryDate;
        this.mood = mood;
        this.updatedOn = updatedOn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
}
