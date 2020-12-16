package com.example.simplenoteonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import saman.zamani.persiandate.PersianDate;

public class ShowNoteActivity extends AppCompatActivity {

    private Button btnCreate , btnDelete;
    private EditText etTitle, etContent;
    private FirebaseAuth fAuth;
    private DatabaseReference fNotesDatabase;



    private NoteModel noteModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);


        btnCreate = findViewById(R.id.new_note_btn);
        etTitle = findViewById(R.id.new_notee_title);
        etContent = findViewById(R.id.new_notee_content);
        btnDelete = findViewById(R.id.delete_note_btn);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote();
            }
        });


        noteModel = new NoteModel(getIntent().getStringExtra("Title"),
                getIntent().getStringExtra("Content"),
                Long.parseLong(getIntent().getStringExtra("Timestamp")));

        etTitle.setText(noteModel.getTitle());
        etContent.setText(noteModel.getContent());
    }

    private void deleteNote() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query noteQuery = ref.child("Notes").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .orderByChild("timestamp").equalTo(noteModel.getTimestamp());

        noteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();

                }
                //back to home fragment
                startActivity(new Intent(ShowNoteActivity.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("NoteAdapter", "onCancelledDeleteNote", databaseError.toException());
            }
        });
    }


}