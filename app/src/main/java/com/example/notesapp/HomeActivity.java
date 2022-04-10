package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;
    private String userId;
    private RecyclerView rvNotes;
    private NoteAdapter adapter;
    private List<NoteModel> noteModelList = new ArrayList<>();
    private DatabaseReference mDatabaseNotes;

    private ChildEventListener childEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        userId = currentUser.getUid();

        mRootRef = FirebaseDatabase.getInstance().getReference();
        rvNotes = findViewById(R.id.rvNotes);
        noteModelList = new ArrayList<>();
        adapter = new NoteAdapter(this,noteModelList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvNotes.setLayoutManager(layoutManager);
        rvNotes.setAdapter(adapter);
        loadNotes();
    }

    private void loadNotes(){
        mDatabaseNotes = mRootRef.child("notes").child(userId);
        Query noteQuery = mDatabaseNotes.orderByKey();
        noteModelList.clear();
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
                NoteModel note = snapshot.getValue(NoteModel.class);
                if(!noteModelList.contains(note))
                {
                    noteModelList.add(note);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                NoteModel note = snapshot.getValue(NoteModel.class);
                noteModelList.remove(note);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        noteQuery.addChildEventListener(childEventListener);
    }

    public void btnSaveClick(View v){
        EditText mEditText = findViewById(R.id.etNote);
        String strNote = mEditText.getText().toString().trim();

        if (strNote.equals("")){
            mEditText.setError("Enter Note");
            return;
        }

        String current_user_ref = "notes/" + userId;
        DatabaseReference notes_push = mRootRef.child("notes").child(userId).push();
        String noteId = notes_push.getKey();
        Map noteMap = new HashMap();
        noteMap.put("noteID", noteId);
        noteMap.put("note", strNote);
        noteMap.put("addedOn", ServerValue.TIMESTAMP);
        noteMap.put("addedBy" , userId);

        Map noteNodeMap = new HashMap();
        noteNodeMap.put(current_user_ref + "/" + noteId,noteMap);

        mRootRef.updateChildren(noteNodeMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(HomeActivity.this, "Note Saved Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
//On click of menu profile open profile activity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if (id==R.id.mnuProfile){
            startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}