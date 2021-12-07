package com.example.myapplication.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.myapplication.R;
import com.example.myapplication.ui.models.Plant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.core.Context;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import butterknife.ButterKnife;


public class PlantAddActivity extends AppCompatActivity {

    private static final String TAG = "PlantAddActivity";

    TextInputLayout plantName;
    Spinner plantType;
    TextInputLayout plantNote;
    TextInputEditText datePlanting;
    //TextInputLayout plantPicture;
    TextInputLayout plantInsolation;
    TextInputLayout soilHumidity;
    TextInputLayout airHumidity;
    TextInputLayout plantNutrient;
    //Spinner plantIsPoison;
    AwesomeValidation validator;

    private IPlantAddActivity mIPlantAddActivity;
    private ImageView profilePic;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Button pickPicFromPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.activity_plantadd);
        validator = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);

        plantName = findViewById(R.id.plantName);
        plantType = findViewById(R.id.plantType);
        plantNote = findViewById(R.id.plantNote);
        datePlanting = findViewById(R.id.datePlanting2);
        plantInsolation = findViewById(R.id.plantInsolation);
        soilHumidity = findViewById(R.id.soilHumidity);
        //profilePic = findViewById(R.id.profilePic);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        pickPicFromPhone=findViewById(R.id.pick_picture_button);
        pickPicFromPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });
/*
        profilePic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                choosePicture();            }
        }
        );
*/
        setupRules();

        datePlanting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(datePlanting);
            }
        });

        Button addPlant;
        addPlant = findViewById(R.id.CreateEventButton);

        addPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlant();
            }
        });

    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            profilePic.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture() {
        //final ProgressBar pd = new ProgressBar();
        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("images/" + randomKey);

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Snackbar.make(findViewById(R.layout.activity_plantadd), "Image Uploaded.", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),"Image Uploaded.",Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Failed To Upload",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progressPercent = (100.00 *taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        //pd.showContextMenu("Percetage: " + (int) progressPercent);
                    }
                });

    }

    private void showDateTimeDialog(final TextInputEditText eventDatetime) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            }
        };
        new DatePickerDialog(PlantAddActivity.this, R.style.datepicker, dateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void addPlant() {
        /* TextInputLayout plantName;
    TextInputLayout plantType;
    TextInputLayout plantNote;
    TextInputEditText datePlating;
    TextInputLayout plantPicture;
    TextInputLayout plantInsolation;
    TextInputLayout soilHumidity;
    TextInputLayout airHumidity;
    TextInputLayout plantNutrient;
    Spinner plantIsPoison;*/
        String name = plantName.getEditText().getText().toString();
        //String type = plantType.getEditText().getText().toString();
        String note = plantNote.getEditText().getText().toString();
        //String date = datePlanting.getText().toString();
        //String picture = plantPicture.getEditText().getText().toString();
        String insolation = plantInsolation.getEditText().getText().toString();
        String humidity = soilHumidity.getEditText().getText().toString();
        //String air = airHumidity.getEditText().getText().toString();


        validator.clear();
        if (validator.validate()) {
           /* Date dateOfPlanting;
            Date dt = new Date();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:m:00");*/
            try {
                //dateOfPlanting = sdf.parse(date);
                //if (dateOfPlanting.before(Date.from(Instant.from(LocalDateTime.from(dt.toInstant()).minusDays(1))))) {

                    //mIPlantAddActivity.addNewPlant(name,note,date,insolation,humidity,air);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    DocumentReference newPlantRef = db
                            .collection("plants")
                            .document();

                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    Plant plant = new Plant();

                    plant.setPlant_name(name);
                    plant.setPlant_note(note);
                    plant.setPlant_id(newPlantRef.getId());
                    plant.setUser_id(userId);

                    newPlantRef.set(plant).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(PlantAddActivity.this, "Enter a title", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(PlantAddActivity.this, "Failed! Check log", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                                   //}
            } catch (Exception e) {
                System.out.println("Error occurred " + e.getMessage());
            }
        }
    }


    /*public void addNewPlant(String name, String note,String date,String insolation,String humidity,String air){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference newPlantRef = db
                .collection("plants")
                .document();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Plant plant = new Plant();

        plant.setPlant_name(name);
        plant.setPlant_note(note);
        plant.setPlant_id(newPlantRef.getId());
        plant.setUser_id(userId);

        newPlantRef.set(plant).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(PlantAddActivity.this, "Enter a title", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(PlantAddActivity.this, "Failed! Check log", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mIPlantAddActivity = (IPlantAddActivity)getActivity();
    }*/

    public void setupRules() {
       /* validator.addValidation(this, R.id.eventName, "[a-zA-Z0-9 ]{3,50}", R.string.err_event_name);
        validator.addValidation(this, R.id.eventLocation, "[a-zA-Z0-9 ]{3,100}", R.string.err_event_location);
        validator.addValidation(this, R.id.eventStartDatetime, RegexTemplate.NOT_EMPTY, R.string.err_event_datetime);
        validator.addValidation(this, R.id.eventEndDatetime, RegexTemplate.NOT_EMPTY, R.string.err_event_datetime);

        validator.addValidation(this, R.id.eventZipcode, "^[0-9]{2}-[0-9]{3}$|^\\s*$", R.string.err_event_zipcode);
        validator.addValidation(this, R.id.eventDescription, ".{1,200}|^\\s*$", R.string.err_event_description);
        validator.addValidation(this, R.id.eventHouseNum, "[a-zA-Z0-9 ]{1,10}|^\\s*$", R.string.err_event_housenumber);
        validator.addValidation(this, R.id.eventStreet, "[a-zA-Z0-9 ]{1,50}|^\\s*$", R.string.err_event_street);
    */
    }


}
