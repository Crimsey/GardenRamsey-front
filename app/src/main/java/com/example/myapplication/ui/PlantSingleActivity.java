package com.example.myapplication.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.ui.models.Plant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class PlantSingleActivity extends AppCompatActivity {

    private static final String TAG = "EventSingleActivity";

    TextView name, text_view_progress, text_view_progress3, plantType, plantDate, plantPoison;
    public int progr, progr2;
    ProgressBar progress_bar, progress_bar2;
    Button button, button_naslonecznienie, button_Back, button_Edit, button_Ratings;
    ImageView plantPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_single);

        name = findViewById(R.id.plantNameEdit);
        progress_bar = findViewById(R.id.progress_bar);
        progress_bar2 = findViewById(R.id.progress_bar2);
        button = findViewById(R.id.button);
        button_naslonecznienie = findViewById(R.id.button_naslonecznienie);
        button_Back = findViewById(R.id.button_back);
        button_Edit = findViewById(R.id.button_edit);
        button_Ratings = findViewById(R.id.button_rating);

        text_view_progress = findViewById(R.id.text_view_progress);
        text_view_progress3 = findViewById(R.id.text_view_progress3);
        plantPic = findViewById(R.id.plantPic);

        plantType = findViewById(R.id.plantType);
        plantDate = findViewById(R.id.plantDate);
        plantPoison = findViewById(R.id.plantIsPoison);
        Bundle b = getIntent().getExtras();
        String plant_id = b.getString("plant_id");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query userPlants = db.collection("plants")
                .whereEqualTo("user_id", userId)
                .whereEqualTo("plant_id", plant_id);
        StorageReference storeRef = FirebaseStorage.getInstance().getReference();


        userPlants.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Plant plant = document.toObject(Plant.class);
                        name.setText(plant.getPlant_name());
                        progr = plant.getPlant_irrigation();
                        progress_bar.setProgress(progr);
                        text_view_progress.setText(progr + "%");

                        storeRef.child("images/").child(FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + plant_id)
                                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Picasso.get().load(uri).into(plantPic);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });
                        if (plant.getPlant_type() != null) {
                            plantType.setText("Species: " + plant.getPlant_type());
                        } else {
                            plantType.setText("Species: ");
                        }
                        if (plant.getDate_plating() != null) {
                            plantDate.setText("Planting date: " + plant.getDate_plating());
                        } else {
                            plantDate.setText("Planting date: ");
                        }
                        plantPoison.setText("Plant is poisoning: " + plant.isPlant_is_poison());

                        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);

                        SpannableString spannableType = new SpannableString(plantType.getText());
                        SpannableString spannableDate = new SpannableString(plantDate.getText());
                        SpannableString spannablePoison = new SpannableString(plantPoison.getText());

                        spannableType.setSpan(boldSpan, 0, 8, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                        spannableDate.setSpan(boldSpan, 0, 14, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                        spannablePoison.setSpan(boldSpan, 0, 19, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

                        plantType.setText(spannableType);
                        plantDate.setText(spannableDate);
                        plantPoison.setText(spannablePoison);
                    }
                }
            }
        });

        updateProgressBar(progr);

        button.setOnClickListener(v -> {
            if (progr <= 90) {
                progr += 10;
                updateProgressBar(progr);
                updateIrrigation(progr);

            }
        });

        button_naslonecznienie.setOnClickListener(v -> {
            if (progr2 <= 90) {
                progr2 += 10;
                updateProgressBar(progr);
                updateIrrigation(progr);

            }
        });

        button_Back.setOnClickListener(v -> {
            Intent intent = new Intent(PlantSingleActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        button_Ratings.setOnClickListener( v -> {
            Intent intent = new Intent(PlantSingleActivity.this, RatingActivity.class);
            intent.putExtra("plant_id", plant_id);
            intent.putExtra("plant_name", name.getText().toString());
            startActivity(intent);
            finish();
        });

        button_Edit.setOnClickListener(v -> {
            Intent intent = new Intent(PlantSingleActivity.this, PlantEditActivity.class);
            intent.putExtra("plant_id", plant_id);
            startActivity(intent);
            finish();
        });

    }

    private void updateIrrigation(int progr) {

        Bundle b = getIntent().getExtras();
        String plant_id = b.getString("plant_id");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference newPlantRef = db
                .collection("plants")
                .document(plant_id);

        //String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Plant plant = new Plant();
        Map<String, Object> updates = new HashMap<>();
        updates.put("plant_irrigation", progr);

        newPlantRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(PlantSingleActivity.this, "IRRIGATION ACTUALIZED", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PlantSingleActivity.this, "Failed! Check log", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updateProgressBar(int progr) {
        progress_bar.setProgress(progr);
        text_view_progress.setText(progr + "%");
        progress_bar2.setProgress(progr2);
        text_view_progress3.setText(progr2 + "%");
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

