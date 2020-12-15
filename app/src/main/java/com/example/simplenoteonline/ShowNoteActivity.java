package com.example.simplenoteonline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class ShowNoteActivity extends AppCompatActivity {

    private Button btnCreate;
    private EditText etTitle, etContent;

    private NoteModel noteModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);

        btnCreate = findViewById(R.id.new_note_btn);
        etTitle = findViewById(R.id.new_note_title);
        etContent = findViewById(R.id.new_note_content);

        noteModel = new NoteModel(getIntent().getStringExtra("Title"),
                getIntent().getStringExtra("Content"),
                Long.parseLong(getIntent().getStringExtra("Timestamp")));

        etTitle.setText(noteModel.getTitle());
        etContent.setText(noteModel.getContent());
    }
}
