package com.example.myapplication.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.ui.models.Plant;
import com.example.myapplication.ui.models.Rating;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
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


public class PlantSingleAllActivity extends AppCompatActivity {

    private static final String TAG = "PlantSingleAllActivity";

    TextView name, plantType, plantDate, plantPoison;
    Button button_Back, button_Rate;
    ImageView plantPic;
    EditText comment;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_single_all);

        name = findViewById(R.id.plantNameEdit);
        button_Back = findViewById(R.id.button_back);

        plantPic = findViewById(R.id.plantPic);

        comment = findViewById(R.id.comment);
        ratingBar = findViewById(R.id.rating);
        button_Rate = findViewById(R.id.rateButton);

        plantType = findViewById(R.id.plantType);
        plantDate = findViewById(R.id.plantDate);
        plantPoison = findViewById(R.id.plantIsPoison);
        Bundle b = getIntent().getExtras();
        String plant_id = b.getString("plant_id");
        String user_id_owner = b.getString("user_id_owner");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query userPlants = db.collection("plants")
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

                        storeRef.child("images/").child(plant.getUser_id() + "/" + plant_id)
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

        button_Rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rate(comment.getText().toString(), ratingBar.getRating(), plant_id, user_id_owner, name.getText().toString());
            }
        });

        button_Back.setOnClickListener(v -> {
            Intent intent = new Intent(PlantSingleAllActivity.this, PlantListAllActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void rate(String comment, float rate, String plant_id, String user_id_owner, String name) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String user_id_rating = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String user_name_rating = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        DocumentReference newRating = db
                .collection("rating")
                .document();

        Rating rating = new Rating();
        rating.setRating_id(newRating.getId());
        rating.setRate(rate);
        rating.setUser_id_owner(user_id_owner);
        rating.setPlant_id(plant_id);
        rating.setPlant_name(name);
        rating.setComment(comment);
        rating.setUser_id_rating(user_id_rating);
        rating.setUser_name_rating(user_name_rating);

        newRating.set(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(PlantSingleAllActivity.this, PlantListAllActivity.class));
                    Toast.makeText(PlantSingleAllActivity.this,"Add new rating!",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(PlantSingleAllActivity.this,"Error!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

