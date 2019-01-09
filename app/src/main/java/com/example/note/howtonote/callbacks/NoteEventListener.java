package com.example.note.howtonote.callbacks;

import com.example.note.howtonote.model.Note;

public interface NoteEventListener {

    //called when note clicked
    //param note: note item

    void onNoteClick(Note note);

    //called when long click on note

    void onNoteLongClick(Note note);
}
