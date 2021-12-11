package com.example.myapplication.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;


public class PlantEditActivity extends AppCompatActivity {

    private static final String TAG = "PlantAddActivity";

    TextInputEditText plantName;
    Spinner plantType;
    TextInputEditText plantNote;
    TextInputEditText datePlanting;
    //TextInputEditText plantPicture;
    TextInputEditText plantInsolation;
    TextInputEditText soilHumidity;
    TextInputEditText airHumidity;
    TextInputEditText plantNutrient;
    //Spinner plantIsPoison;
    AwesomeValidation validator;
    Button backToMap,editPlant,deletePlant;

    private IPlantAddActivity mIPlantAddActivity;
    private ImageView plantPic;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Button pickPicFromPhone;

    public int picButtonHasBeenClicked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.activity_plant_edit);
        validator = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);

        plantName = findViewById(R.id.plantName2);
        plantType = findViewById(R.id.plantType);
        plantNote = findViewById(R.id.plantNote2);
        datePlanting = findViewById(R.id.datePlanting2);
        plantInsolation = findViewById(R.id.plantInsolation2);
        soilHumidity = findViewById(R.id.soilHumidity2);

        deletePlant = findViewById(R.id.deletePlant);

        plantPic = findViewById(R.id.profilePic);
        backToMap = findViewById(R.id.backToMap);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        pickPicFromPhone=findViewById(R.id.pick_picture_button);
        pickPicFromPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
                picButtonHasBeenClicked=1;
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
        Bundle b = getIntent().getExtras();
        String plant_id = b.getString("plant_id");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query userPlants = db.collection("plants")
                .whereEqualTo("user_id", userId)
                .whereEqualTo("plant_id", plant_id);
        StorageReference storeRef= FirebaseStorage.getInstance().getReference();


        userPlants.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Plant plant = document.toObject(Plant.class);
                        plantName.setText(plant.getPlant_name());
                        datePlanting.setText(plant.getDate_plating());


                        storeRef.child("images/").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+plantName.getText().toString())
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

                    }
                }
            }
        });


        setupRules();

        datePlanting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(datePlanting);
            }
        });

        Button editPlant;
        editPlant = findViewById(R.id.CreateEventButton);

        editPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPlant();
                Intent myIntent = new Intent(PlantEditActivity.this, PlantSingleActivity.class);
                myIntent.putExtra("plant_id", plant_id);
                startActivity(myIntent);
                finish();            }
        });

        backToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(PlantEditActivity.this, PlantSingleActivity.class);
                myIntent.putExtra("plant_id", plant_id);
                startActivity(myIntent);
                finish();
            }
        });

        deletePlant.setOnClickListener(v -> {
            storeRef.child("plants").child(plant_id).delete();
            Intent myIntent = new Intent(PlantEditActivity.this, MainActivity.class);
            startActivity(myIntent);
            finish();
        });


    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/* ");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            plantPic.setImageURI(imageUri);

        }
    }

    private void uploadPicture(String plant_id) {
        //final ProgressBar pd = new ProgressBar();
        //final String randomKey = UUID.randomUUID().toString();
        String plantID = plant_id;
        String nothing;
        StorageReference riversRef = storageReference.child("images/").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+plant_id);

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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                eventDatetime.setText(simpleDateFormat.format(calendar.getTime()));

            }
        };
        new DatePickerDialog(PlantEditActivity.this, R.style.datepicker, dateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void editPlant() {
        /* TextInputEditText plantName;
    TextInputEditText plantType;
    TextInputEditText plantNote;
    TextInputEditText datePlating;
    TextInputEditText plantPicture;
    TextInputEditText plantInsolation;
    TextInputEditText soilHumidity;
    TextInputEditText airHumidity;
    TextInputEditText plantNutrient;
    Spinner plantIsPoison;*/
        String name = plantName.getText().toString();
        //String type = plantType.getEditText().getText().toString();
        String note = plantNote.getText().toString();
        String date = datePlanting.getText().toString();
        //String picture = plantPicture.getEditText().getText().toString();
        String insolation = plantInsolation.getText().toString();
        String humidity = soilHumidity.getText().toString();
        //String air = airHumidity.getEditText().getText().toString();


        validator.clear();
        if (validator.validate()) {
            try {

                Bundle b = getIntent().getExtras();
                String plant_id = b.getString("plant_id");

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                DocumentReference newPlantRef = db
                        .collection("plants")
                        .document(plant_id);

                //String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                //Plant plant = new Plant();
                Map<String,Object> updates = new HashMap<>();
                updates.put("plant_name",name);
                updates.put("plant_note",note);
                updates.put("date_plating",date); //PLATING NIE PLANTING BO LITERÓWKA W MODELU JEST

                if(picButtonHasBeenClicked!=0){
                    uploadPicture(plant_id);
                }

                newPlantRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //Toast.makeText(PlantEditActivity.this, "IRRIGATION ACTUALIZED", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //Toast.makeText(PlantSingleActivity.this, "Failed! Check log", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                /*FirebaseFirestore db = FirebaseFirestore.getInstance();

                DocumentReference newPlantRef = db
                        .collection("plants")
                        .document();

                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Plant plant = new Plant();

                plant.setPlant_name(name);
                plant.setPlant_note(note);
                plant.setPlant_id(newPlantRef.getId());
                plant.setUser_id(userId);
                plant.setDate_plating(date);

                uploadPicture();

                newPlantRef.set(plant).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(PlantEditActivity.this, "Plant edited", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(PlantEditActivity.this, "Failed! Check log", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/

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
