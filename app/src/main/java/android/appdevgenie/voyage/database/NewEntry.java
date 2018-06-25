package android.appdevgenie.voyage.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "new_entry")
public class NewEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String thoughts;
    private int priority;
    @ColumnInfo(name = "updated_on")
    private Date updatedOn;

    public NewEntry(String thoughts, int priority, Date updatedOn) {
        this.thoughts = thoughts;
        this.priority = priority;
        this.updatedOn = updatedOn;
    }

    public NewEntry(int id, String thoughts, int priority, Date updatedOn) {
        this.id = id;
        this.thoughts = thoughts;
        this.priority = priority;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
}
