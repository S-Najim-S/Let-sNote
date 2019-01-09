package com.example.note.howtonote.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id ; //default Value for id
    @ColumnInfo(name  = "titleText")
    private String noteTitle;
    @ColumnInfo(name  = "text")
    private String noteText;
    @ColumnInfo(name  = "date")
    private long noteDate;
    @Ignore // ignore it because not storing the value in DB
    private boolean checked = false;

    public Note() {
    }

    public Note(String noteTitle, String noteText, long noteDate) {
        this.noteTitle = noteTitle;
        this.noteText = noteText;
        this.noteDate = noteDate;

    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public long getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(long noteDate) {
        this.noteDate = noteDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {

        this.checked = checked;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", noteDate=" + noteDate +
                '}';
    }
}
