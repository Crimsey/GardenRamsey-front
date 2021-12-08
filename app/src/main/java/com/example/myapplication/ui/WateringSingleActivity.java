package com.example.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myapplication.R;
import com.example.myapplication.ui.models.Watering;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class WateringSingleActivity extends NavigationActivity {

    private static final String TAG = "WateringSingleActivity";

    private String plant_id;

    TextView wateringName;
    TextView noteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watering_single);

        wateringName = findViewById(R.id.wateringName);
        noteText = findViewById(R.id.noteText);

        Bundle bundle = getIntent().getExtras();
        String watering_id = bundle.getString("watering_id");
        plant_id = bundle.getString("plant_id");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query simpleWatering = db.collection("watering")
                .whereEqualTo("user_id", userId)
                .whereEqualTo("watering_id", watering_id);

        simpleWatering.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Watering watering = documentSnapshot.toObject(Watering.class);
                        wateringName.setText(watering.getPlant());

                        if (watering.getNote().isEmpty()) {
                            noteText.setText("-----");
                        } else {
                            noteText.setText(watering.getNote());
                        }
                    }
                }
            }
        });
    }

    public void goToPlantInfo(View view) {
        Intent myIntent = new Intent(this, PlantSingleActivity.class);
        myIntent.putExtra("plant_id", plant_id);
        startActivity(myIntent);
    }
}