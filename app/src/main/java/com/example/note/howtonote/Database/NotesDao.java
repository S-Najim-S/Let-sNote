package com.example.note.howtonote.Database;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.note.howtonote.model.Note;
import java.util.List;

// TODO: 12/30/2018
@Dao
public interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Update
    void updateNote(Note note);

   //List All Notes From Database
    @Query("SELECT * FROM notes")
    List<Note> getNotes();

    //@param noteId note id
    // @return Note
    @Query("SELECT * FROM notes WHERE id = :noteId")
    Note getNoteById(int noteId);
    //Delete Note by Id from DataBase
    //@param noteId
    @Query("DELETE FROM notes WHERE id = :noteId")
    void deleteNoteById(int noteId);

}