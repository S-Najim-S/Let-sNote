package com.example.note.howtonote.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.note.howtonote.R;
import com.example.note.howtonote.callbacks.NoteEventListener;
import com.example.note.howtonote.model.Note;
import com.example.note.howtonote.utils.NoteUtils;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapters extends RecyclerView.Adapter<NoteAdapters.NoteHolder> {

    private Context context;
    private ArrayList<Note>notes;
    private NoteEventListener listener;
    private boolean multiCheckMode = false;

    public NoteAdapters(Context context, ArrayList<Note> notes) {
        this.context = context;
        this.notes = notes;
    }


    @Override
    public NoteHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.note_layout,parent,false);
        return new NoteHolder(v);
    }

    @Override
    public void onBindViewHolder( NoteHolder holder, int position) {
        final Note note = getNote(position);
        if(note!=null){
            holder.noteTitle.setText(note.getNoteTitle());
            holder.noteText.setText(note.getNoteText());
            holder.noteDate.setText(NoteUtils.dateFormLong(note.getNoteDate()));


            //init note click event
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onNoteClick(note);
                }
            });
            // init note long click
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onNoteLongClick(note);
                    return false;
                }
            });

            //check the checkbox if not selected
            if(multiCheckMode){
                holder.checkBox.setVisibility(View.VISIBLE);//show's the check box
                holder.checkBox.setChecked(note.isChecked());
            }
            else{
                holder.checkBox.setVisibility(View.GONE);//hides the check box if not selected
            }
        }
    }
    @Override
    public int getItemCount() {
        return notes.size();
    }
    private Note getNote(int position){
        return notes.get(position);
    }

    //Get's all checked notes and returns An Array of them
    public List<Note> getCheckedNotes() {
        List<Note> checkedNotes = new ArrayList<>();
        for(Note n :this.notes){
            if(n.isChecked()){
                checkedNotes.add(n);
            }
        }
        return checkedNotes;
    }

    class NoteHolder extends RecyclerView.ViewHolder{

        TextView noteTitle,noteText,noteDate;
        CheckBox checkBox;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.note_title);
            noteText = itemView.findViewById(R.id.note_text);
            noteDate = itemView.findViewById(R.id.note_date);
            checkBox = itemView.findViewById(R.id.checkBox);

        }
    }

    public void setListener(NoteEventListener listener) {
        this.listener = listener;
    }

    public void setMultiCheckMode(boolean multiCheckMode){
        this.multiCheckMode = multiCheckMode;
        notifyDataSetChanged();
    }
    public void updateList(ArrayList<Note> newList){
        notes = new ArrayList<>();
        notes.addAll(newList);
        notifyDataSetChanged();
    }
}
