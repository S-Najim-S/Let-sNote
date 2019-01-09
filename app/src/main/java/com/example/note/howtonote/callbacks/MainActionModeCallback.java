package com.example.note.howtonote.callbacks;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.example.note.howtonote.R;

public abstract class MainActionModeCallback implements ActionMode.Callback {
    private ActionMode actionMode;
    private MenuItem countItem;
    private MenuItem shareItem;
    private ActionMode action;

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {

        actionMode.getMenuInflater().inflate(R.menu.main_action_mode,menu);
        this.actionMode = actionMode;
        this.countItem = menu.findItem(R.id.action_checked_count);
        this.shareItem = menu.findItem(R.id.action_share_note);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {

    }
    public void setCount(String checkedCount){
        if(countItem != null)
            this.countItem.setTitle(checkedCount);
    }
    //if checked item is >1 hide Share item else Show it
    //parameter "b" indicates the visibality 
    public void changeShareItemVisible(boolean b) {
        shareItem.setVisible(b);
    }

    public ActionMode getAction() {
        return action;
    }
}
