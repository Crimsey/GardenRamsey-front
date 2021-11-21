package com.example.myapplication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.myapplication.R;
import com.example.myapplication.ui.models.Note;
import com.example.myapplication.ui.models.Plant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.Date;

public class EventEditActivity extends AppCompatActivity {

    private EditText eventNameET;
    private TextView eventDateTV;
    Button saveEventButton;
    AwesomeValidation validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        eventDateTV.setText("Date: "+ CalendarUtils.formattedDate(CalendarUtils.selectedDate));

        saveEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotes();
            }
        });
    }

    private void initWidgets() {
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        saveEventButton = findViewById(R.id.saveEventButton);
        validator = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
    }

    private void addNotes() {
        String name = eventNameET.getText().toString();
        String date = CalendarUtils.formattedDate(CalendarUtils.selectedDate);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference newNoteRef = db
                .collection("notes")
                .document();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Note note = new Note();
        note.setTitle(name);
        note.setDate(date);
        note.setNote_id(newNoteRef.getId());
        note.setUser_id(userId);

        newNoteRef.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(EventEditActivity.this,"Add new note!",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(EventEditActivity.this,"Error!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}