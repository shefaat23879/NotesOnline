package com.example.simplenoteonline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.OnClickAction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NoteAdapter.NoteItemClickListener {

    List<NoteModel> noteModels;
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    Button btn,btn2;



    private FirebaseAuth fAuth;

    private DatabaseReference fNotesDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();
        fNotesDatabase = FirebaseDatabase.getInstance().getReference();

        btn=findViewById(R.id.button2);
        btn2=findViewById(R.id.button3);


        recyclerView = findViewById(R.id.main_notes_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this , 2));
        noteModels = new ArrayList<>();
        noteAdapter = new NoteAdapter(noteModels ,this , this);
        recyclerView.setAdapter(noteAdapter);

        getAllNote();

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fAuth.getCurrentUser() != null) {
                     fAuth.signOut();
                     Toast.makeText(MainActivity.this, "Sign out!" , Toast.LENGTH_SHORT).show();
                     finish();
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(MainActivity.this, NewNoteActivity.class);
                startActivity(newIntent);
            }
        });


        if (fAuth.getCurrentUser() != null) {
            fNotesDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(fAuth.getCurrentUser().getUid());
        }
        updateUI();
    }

    private void getAllNote() {
        noteModels.clear();
        fNotesDatabase.child("Notes").child(fAuth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String title = Objects.requireNonNull(snapshot.child("title").getValue()).toString();
                Long timestamp =(Long) snapshot.child("timestamp").getValue();
                String content =Objects.requireNonNull(snapshot.child("content").getValue().toString());
                NoteModel note = new NoteModel(title , content , timestamp);
                noteModels.add(note);
                noteAdapter.notifyDataSetChanged();
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {


        }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void updateUI() {
        if (fAuth.getCurrentUser() != null) {
            Log.i("MainActivity", "fAuth != null");
        } else {
            Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
            startActivity(startIntent);
            finish();
            Log.i("MainActivity", "fAuth == null");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.main_new_note_btn:
                Intent newIntent = new Intent(MainActivity.this, NewNoteActivity.class);
                startActivity(newIntent);
                break;
        }

        return true;
    }

    @Override
    public void onNoteClick(NoteModel noteModel) {
        Intent intent = new Intent(MainActivity.this , ShowNoteActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("Title" , noteModel.getTitle());
        intent.putExtra("Content" , noteModel.getContent());
        intent.putExtra("Timestamp" , noteModel.getTimestamp().toString());
        startActivity(intent);
        finish();
    }


}
