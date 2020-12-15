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
import android.widget.TextView;


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



    private FirebaseAuth fAuth;

    private DatabaseReference fNotesDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();


        recyclerView = findViewById(R.id.main_notes_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this , 2));
        noteModels = new ArrayList<>();
        noteAdapter = new NoteAdapter(noteModels ,this , this);
        recyclerView.setAdapter(noteAdapter);




        fNotesDatabase = FirebaseDatabase.getInstance().getReference("Notes").child(fAuth.getCurrentUser().getUid());

        noteModels.clear();

        fNotesDatabase.addChildEventListener(new ChildEventListener() {
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

//        fNotesDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    NoteModel data = ds.getValue(NoteModel.class);
//                    noteModels.add(data);
//
//                }
//                noteAdapter = new NoteAdapter(noteModels, getApplicationContext());
//                recyclerView.setAdapter(noteAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });




        if (fAuth.getCurrentUser() != null) {
            fNotesDatabase = FirebaseDatabase.getInstance().getReference().child("Notes").child(fAuth.getCurrentUser().getUid());
        }
        updateUI();
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
        Intent intent = new Intent(MainActivity.this , ShowNoteActivity.class);
        intent.putExtra("Title" , noteModel.getTitle());
        intent.putExtra("Content" , noteModel.getContent());
        intent.putExtra("Timestamp" , noteModel.getTimestamp().toString());
        startActivity(intent);
    }
}
