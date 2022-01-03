package com.example.myapplication.ui;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.ui.models.Rating;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RatingActivity extends NavigationActivity {

    protected RecyclerView recyclerView;
    protected AdapterRating adapterRating;
    protected TextView title;

    private static final String TAG = "RatingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        Bundle b = getIntent().getExtras();
        String plant_id = b.getString("plant_id");
        String plant_name = b.getString("plant_name");

        title = findViewById(R.id.eventsTitle);
        title.setText(title.getText().toString() + plant_name);

        recyclerView = (RecyclerView) findViewById(R.id.eventsRecyclerViewRate);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

        rootRef.collection("rating").whereEqualTo("plant_id", plant_id).whereEqualTo("user_id_owner", userId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d(TAG, "onSuccess: LIST EMPTY");
                        } else {
                            List<Rating> rateList = documentSnapshots.toObjects(Rating.class);
                            adapterRating = new AdapterRating((ArrayList<Rating>) rateList, context);
                            recyclerView.setAdapter(adapterRating);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
                    }
                });
    }
}