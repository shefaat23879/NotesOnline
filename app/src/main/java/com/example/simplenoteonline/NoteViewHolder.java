package com.example.simplenoteonline;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    View mView;

    TextView textTitle,textTime;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        mView=itemView;

        textTitle=mView.findViewById(R.id.note_title);
        textTime=mView.findViewById(R.id.note_time);

    }
    public void setNoteTitle(String title){
        textTitle.setText(title);
    }
    public void setNoteTime(String time){
        textTime.setText(time);
    }



}
