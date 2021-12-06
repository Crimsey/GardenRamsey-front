package com.example.myapplication.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.RotateDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.R;
import com.example.myapplication.ui.models.Plant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.core.Context;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class PlantSingleActivity extends AppCompatActivity {

    private static final String TAG = "EventSingleActivity";

    TextView name,text_view_progress,text_view_progress3;
    private int progr = 0, progr2 =0;
    ProgressBar progress_bar,progress_bar2;
    Button button,button_naslonecznienie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_single);

        name = findViewById(R.id.plantName);
        progress_bar = findViewById(R.id.progress_bar);
        progress_bar2 = findViewById(R.id.progress_bar2);
        button = findViewById(R.id.button);
        button_naslonecznienie = findViewById(R.id.button_naslonecznienie);
        text_view_progress = findViewById(R.id.text_view_progress);
        text_view_progress3 = findViewById(R.id.text_view_progress3);

        Bundle b = getIntent().getExtras();
        String plant_id = b.getString("plant_id");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query userPlants = db.collection("plants")
                .whereEqualTo("user_id", userId)
                .whereEqualTo("plant_id", plant_id);

        userPlants.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Plant plant = document.toObject(Plant.class);
                        name.setText(plant.getPlant_name());
                    }
                }
            }
        });

        updateProgressBar();

        button.setOnClickListener(v -> {
            if (progr <= 90){
                progr +=10;
                updateProgressBar();
            }
        });

        button_naslonecznienie.setOnClickListener(v -> {
            if (progr2 <= 90){
                progr2 +=10;
                updateProgressBar();
            }
        });


        //setupRules();

    }

    private void updateProgressBar() {
        progress_bar.setProgress(progr);
        text_view_progress.setText(progr +"%");
        progress_bar2.setProgress(progr2);
        text_view_progress3.setText(progr2 +"%");
    }
   /* private void handleErrors (ResponseBody response){

        ApiError apiError = Utils.converErrors(response);
        if (apiError.getErrors() != null) {
            Log.w("no errors", "apiError.getErrors()" + apiError.getErrors());

            for (Map.Entry<String, List<String>> error : apiError.getErrors().entrySet()) {
                if (error.getKey().equals("name")) {
                    name.setError(error.getValue().get(0));
                }
                if (error.getKey().equals("startdatetime")) {
                    startdatetime.setError(error.getValue().get(0));
                }
                if (error.getKey().equals("enddatetime")) {
                    enddatetime.setError(error.getValue().get(0));
                }
                if (error.getKey().equals("status")) {
                    status.setError(error.getValue().get(0));
                }
            }
        } else {
            Log.e("no errors", "weird");
        }
    }




    /*public void setupRules() {
        validator.addValidation(this, R.id.userFirstName, RegexTemplate.NOT_EMPTY, R.string.err_event_name);
        validator.addValidation(this, R.id.userLastName, RegexTemplate.NOT_EMPTY, R.string.err_event_name);
        validator.addValidation(this, R.id.userBirthDate, RegexTemplate.NOT_EMPTY, R.string.err_event_name);
        //validator.addValidation(this, R.id.userDescription, RegexTemplate.NOT_EMPTY, R.string.err_event_name);

    }*/


    //}

}

