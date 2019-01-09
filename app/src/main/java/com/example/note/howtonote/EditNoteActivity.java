package com.example.note.howtonote;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.note.howtonote.Database.NotesDB;
import com.example.note.howtonote.Database.NotesDao;
import com.example.note.howtonote.model.Note;

import java.util.Date;
import android.support.v7.widget.Toolbar;


public class EditNoteActivity extends AppCompatActivity {
    private EditText inputNote;
    private EditText inputTitle;
    private NotesDao dao;
    private Note temp;
    public static final String NOTE_EXTRA_KEY = "note_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // set theme
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        int theme = sharedPreferences.getInt(MainActivity.THEME_Key, R.style.AppTheme);
        setTheme(theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = findViewById(R.id.edit_note_activity_toolbar);
        setSupportActionBar(toolbar);

        inputNote = findViewById(R.id.input_note);
        inputTitle = findViewById(R.id.input_title_id);
        dao = NotesDB.getInstance(this).notesDao();
        if(getIntent().getExtras()!=null){
            int id = getIntent().getExtras().getInt(NOTE_EXTRA_KEY,0);
            temp = dao.getNoteById(id);
            inputNote.setText(temp.getNoteText());
            inputTitle.setText(temp.getNoteTitle());
        }
        else inputNote.setFocusable(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_note_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.save_note)
            onSaveNote();
        return super.onOptionsItemSelected(item);
    }

    private void onSaveNote() {
        // TODO: 12/30/2018
        String text =inputNote.getText().toString();
        String title = inputTitle.getText().toString();
        if(!text.isEmpty()){
            long date = new Date().getTime();
            //if note exsits ,update else create new
            if(temp == null){
                temp = new Note(title, text ,date);
                dao.insertNote(temp);//Creates new Note and inserts it to database
            } else{
                temp.setNoteTitle(title);
                temp.setNoteText(text);
                temp.setNoteDate(date);
                dao.updateNote(temp); //update the database with new inputs
            }
            finish();
        }
        else if(!title.isEmpty()){
            long date = new Date().getTime();
            //if note exsits ,update else create new
            if(temp == null){
                temp = new Note(title, text ,date);
                dao.insertNote(temp);
            }

            else{
                temp.setNoteTitle(title);
                temp.setNoteText(text);
                temp.setNoteDate(date);
                dao.updateNote(temp);
            }

            finish();
        }
    }
}
