package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.myapplication.R;
import com.example.myapplication.ui.models.Plant;
import com.example.myapplication.ui.models.Watering;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EventEditActivity extends NavigationActivity {

    private EditText eventNoteET;
    private TextView eventDateTV;
    Button saveEventButton;
    AwesomeValidation validator;
    private Spinner spinner;
    private String plant_id;

    protected AdapterPlant adapterPlant;

    private static final String TAG = "EventEditActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        rootRef.collection("plants").whereEqualTo("user_id", userId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d(TAG, "onSuccess: LIST EMPTY");
                        } else {
                            List<Plant> plantList = documentSnapshots.toObjects(Plant.class);
                            //adapterPlant = new AdapterPlant((ArrayList<Plant>) plantList, context);
                            List<String> stringList = new ArrayList<>(plantList.size());
                            for (Plant plant : plantList) {
                                stringList.add(plant.getPlant_name());
                            }

                            List<String> stringListID = new ArrayList<>(plantList.size());
                            for (Plant plant : plantList) {
                                stringListID.add(plant.getPlant_id());
                            }
                            //ArrayAdapter<Plant> adapterPlant = new ArrayAdapter<Plant>(EventEditActivity.this, R.layout.spinner_item, plantList);
                            ArrayAdapter<String> adapterPlant = new ArrayAdapter<String>(EventEditActivity.this, R.layout.spinner_item, stringList);
                            adapterPlant.setDropDownViewResource(R.layout.spinner_item_dropdown);
                            spinner.setAdapter(adapterPlant);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    plant_id = stringListID.get(position);
                                    Toast.makeText(getApplicationContext(), "Selected: "+parent.getItemAtPosition(position), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
                    }
                });

        eventDateTV.setText("Date: "+ CalendarUtils.formattedDate(CalendarUtils.selectedDate));

        saveEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotes();
            }
        });
    }

    private void initWidgets() {
        eventNoteET = findViewById(R.id.eventNoteET);
        eventDateTV = findViewById(R.id.eventDateTV);
        saveEventButton = findViewById(R.id.saveEventButton);
        validator = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        spinner = findViewById(R.id.spinner);
    }

    private void addNotes() {
        String note = eventNoteET.getText().toString();
        String date = CalendarUtils.formattedDate(CalendarUtils.selectedDate);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference newWatering = db
                .collection("watering")
                .document();

        Watering watering = new Watering();
        watering.setDate(date);
        watering.setUser_id(userId);
        watering.setPlant(spinner.getSelectedItem().toString());
        watering.setWatering_id(newWatering.getId());
        watering.setNote(note);
        watering.setPlant_id(plant_id);

        newWatering.set(watering).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(EventEditActivity.this, WeekViewActivity.class));
                    Toast.makeText(EventEditActivity.this,"Add new watering!",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(EventEditActivity.this,"Error!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}