package com.example.note.howtonote;

import android.annotation.SuppressLint;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.AlteredCharSequence;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.note.howtonote.Database.NotesDB;
import com.example.note.howtonote.Database.NotesDao;
import com.example.note.howtonote.adapters.NoteAdapters;
import com.example.note.howtonote.callbacks.MainActionModeCallback;
import com.example.note.howtonote.callbacks.NoteEventListener;
import com.example.note.howtonote.model.Note;
import com.example.note.howtonote.utils.NoteUtils;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.note.howtonote.EditNoteActivity.NOTE_EXTRA_KEY;
import android.support.v4.app.NavUtils;

public class MainActivity extends AppCompatActivity implements NoteEventListener, Drawer.OnDrawerItemClickListener {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private ArrayList<Note> notes;
    private NoteAdapters adapter;
    private NotesDao dao;
    private MainActionModeCallback actionModeCallback;
    private int checkedCount = 0;
    private FloatingActionButton fab;
    public Toolbar toolbar;
    public static final String THEME_Key = "app_theme";
    public static final String APP_PREFERENCES="notepad_settings";
    private SharedPreferences settings;
    private int theme;
    private AnimationDrawable animationDrawable;
    RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        theme = settings.getInt(THEME_Key, R.style.AppTheme);
        setTheme(theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*

        //Animated Theme

        relativeLayout = (RelativeLayout)findViewById(R.id.content_main_layout);

        animationDrawable = (AnimationDrawable)relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.start();
*/
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupNavigation(savedInstanceState, toolbar);
        // init recyclerView
        recyclerView = findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // init fab Button
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 13/05/2018  add new note
                onAddNewNote();
            }
        });

        dao = NotesDB.getInstance(this).notesDao();
    }

    private void setupNavigation(Bundle savedInstanceState, Toolbar toolbar) {

        // Navigation menu items
        List<IDrawerItem> iDrawerItems = new ArrayList<>();
        iDrawerItems.add(new PrimaryDrawerItem().withName("Notes").withIcon(R.drawable.ic_note_black_24dp));

        // sticky DrawItems ; footer menu items

        List<IDrawerItem> stockyItems = new ArrayList<>();

        SwitchDrawerItem switchDrawerItem = new SwitchDrawerItem()
                .withName("Dark Theme")
                .withChecked(theme == R.style.AppTheme_Dark)
                .withIcon(R.drawable.ic_dark_theme)
                .withOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
                        // TODO: 02/10/2018 change to darck theme and save it to settings
                        if (isChecked) {
                            settings.edit().putInt(THEME_Key, R.style.AppTheme_Dark).apply();
                        } else {
                            settings.edit().putInt(THEME_Key, R.style.AppTheme).apply();
                        }

                        // recreate app or the activity // if it's not working follow this steps
                        // MainActivity.this.recreate();

                        // this lines means wi want to close the app and open it again to change theme
                        TaskStackBuilder.create(MainActivity.this)
                                .addNextIntent(new Intent(MainActivity.this, MainActivity.class))
                                .addNextIntent(getIntent()).startActivities();
                    }
                });

        stockyItems.add(new PrimaryDrawerItem().withName("Settings").withIcon(R.drawable.ic_settings_black_24dp));
        stockyItems.add(switchDrawerItem);

        // navigation menu header
        AccountHeader header = new AccountHeaderBuilder().withActivity(this)
                .addProfiles(new ProfileDrawerItem()
                        .withEmail("GuiProgramming@mugla.com")
                        .withName("Ceng 3505")
                        .withIcon(R.mipmap.ic_launcher_round))
                .withSavedInstance(savedInstanceState)
                .withHeaderBackground(R.drawable.ic_launcher_background)
                .withSelectionListEnabledForSingleProfile(false) // we need just one profile
                .build();

        // Navigation drawer
        new DrawerBuilder()
                .withActivity(this) // activity main
                .withToolbar(toolbar) // toolbar
                .withSavedInstance(savedInstanceState) // saveInstance of activity
                .withDrawerItems(iDrawerItems) // menu items
                .withTranslucentNavigationBar(true)
                .withStickyDrawerItems(stockyItems) // footer items
                .withAccountHeader(header) // header of navigation
                .withOnDrawerItemClickListener(this) // listener for menu items click
                .build();



    }


    private void loadNotes() {
        this.notes = new ArrayList<>();
        List<Note> list = dao.getNotes();// get All notes from DataBase
        this.notes.addAll(list);
        this.adapter = new NoteAdapters(this, this.notes);
        // set listener to adapter
        this.adapter.setListener(this);
        this.recyclerView.setAdapter(adapter);
        showEmptyView();
        // add swipe helper to recyclerView

        swipToDeleteHelper.attachToRecyclerView(recyclerView);
    }


     //when no notes show msg in main_layout

    private void showEmptyView() {
        if (notes.size() == 0) {
            this.recyclerView.setVisibility(View.GONE);
            findViewById(R.id.empty_notes_view).setVisibility(View.VISIBLE);

        } else {
            this.recyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.empty_notes_view).setVisibility(View.GONE);
        }
    }

    private void onAddNewNote() {
        // TODO: 12/30/2018
        startActivity(new Intent(this, EditNoteActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    @Override
    public void onNoteClick(Note note) {
        // TODO: 12/31/2018 , when note Clicked , edit note
        Intent edit = new Intent(this, EditNoteActivity.class);
        edit.putExtra(NOTE_EXTRA_KEY, note.getId());
        startActivity(edit);
    }

    @Override
    public void onNoteLongClick(Note note){
        // TODO: 12/31/2018 , when long hold, delete or share shows up
        note.setChecked(true);
        checkedCount=1;
        adapter.setMultiCheckMode(true);
        //sets new listeners to adapter
        adapter.setListener(new NoteEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onNoteClick(Note note) {
                note.setChecked(!note.isChecked()); // inverse Selected
                if(note.isChecked())
                    checkedCount++;
                else checkedCount--;

                if (checkedCount > 1) {
                    actionModeCallback.changeShareItemVisible(false);
                } else actionModeCallback.changeShareItemVisible(true);

                if (checkedCount == 0) {
                    //  finish multi selection  mode when checked count =0
                    //actionModeCallback.getAction().finish();
                }
                fab.setVisibility(View.GONE);
                actionModeCallback.setCount(checkedCount + "/" + notes.size());
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onNoteLongClick(Note note) {

            }
        });
        actionModeCallback = new MainActionModeCallback() {
            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_delete_note)
                    onDeleteMultiNotes();
                else if (menuItem.getItemId() == R.id.action_share_note)
                    onShareNotes();

                actionMode.finish();
                return false;
            }
        };
        //starts action mode
        startActionMode(actionModeCallback);
        actionModeCallback.setCount(checkedCount + "/" + notes.size());
    }

    private void onShareNotes() {
        // TODO: 1/2/2019 Share only 1 note not multiple notes
        Note note = adapter.getCheckedNotes().get(0);
        // TODO: 1/2/2019  do your logic here to share note ; on social or something else
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        String notetext = note.getNoteText() + "\n\n Create on : " +
                NoteUtils.dateFormLong(note.getNoteDate()) + "\n  By :" +
                getString(R.string.app_name);
        share.putExtra(Intent.EXTRA_TEXT, notetext);
        startActivity(share);
    }

    private void onDeleteMultiNotes() {
        // TODO: 1/2/2019 Delete multiple notes

        List<Note> checkedNotes = adapter.getCheckedNotes();
        if(checkedNotes.size() != 0){
            for(Note note: checkedNotes){
                dao.deleteNote(note);
            }
            loadNotes();
            if(checkedNotes.size() >1)
                Toast.makeText(this,checkedNotes.size() + " Notes Deleted successfully ! " , Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this,checkedNotes.size() + " Note Deleted successfully ! " , Toast.LENGTH_LONG).show();

        }
        else
            Toast.makeText(this,"No notes selected" , Toast.LENGTH_SHORT).show();

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);

        adapter.setMultiCheckMode(false);
        adapter.setListener(this); //set's back to old listener
        fab.setVisibility(View.VISIBLE);
    }
//swipe to right or left to delete a note
    private ItemTouchHelper swipToDeleteHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT
    | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            if(notes!=null){
                //get swiped note
                Note swipedNote  = notes.get(viewHolder.getAdapterPosition());
                swipeToDelete(swipedNote,viewHolder);
            }
        }
    });

    private void swipeToDelete(final Note swipedNote, final RecyclerView.ViewHolder viewHolder) {
        new AlertDialog.Builder(MainActivity.this).setMessage("Delete Note?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // TODO: 1/3/2019  Delete Note
                dao.deleteNote(swipedNote);
                notes.remove(swipedNote);
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                showEmptyView();


            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            //Cancel the action
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                recyclerView.getAdapter().notifyItemChanged(viewHolder.getAdapterPosition());

            }
        })
        .setCancelable(false).create().show();
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        return false;
    }


}
