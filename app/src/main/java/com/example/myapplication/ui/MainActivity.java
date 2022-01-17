package com.example.myapplication.ui;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.widget.SearchView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.ui.models.Plant;
import com.example.myapplication.ui.models.Watering;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.myapplication.ui.notification.AlarmReceiver;

public class MainActivity extends NavigationActivity
        implements SearchView.OnQueryTextListener{

    protected RecyclerView recyclerView;
    protected AdapterPlant adapterPlant;
    protected SearchView searchEvent;

    private ExtendedFloatingActionButton mFab;
    private LocalDate localDate = LocalDate.now();

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.eventsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        searchEvent = findViewById(R.id.searchEvent);
        searchEvent.setOnQueryTextListener(this);
        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this,"Fill a form to add a plant",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,PlantAddActivity.class));
        });

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
                            adapterPlant = new AdapterPlant((ArrayList<Plant>) plantList, context);
                            recyclerView.setAdapter(adapterPlant);
                        }
                    }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
                        }
                    });


        rootRef.collection("watering")
                .whereEqualTo("date", String.valueOf(localDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))))
                .whereEqualTo("user_id", userId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            Log.d(TAG, "onSuccess: LIST EMPTY");
                        } else {
                            handleNotification();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Database error!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void handleNotification() {
        CharSequence name = "ReminderChannel";
        String description = "Channel for reminder";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("notifyWater",name,importance);
        channel.setDescription(description);


        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        Intent intent = new Intent(MainActivity.this,AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //long timeAtButtonClick = System.currentTimeMillis();

        long tenSecondsToMillies = 1000 *10;

        alarmManager.set(AlarmManager.RTC_WAKEUP,
                tenSecondsToMillies, pendingIntent );
        /*Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5000, pendingIntent);*/
        //AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        /*Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");

        Calendar calendar = Calendar.getInstance();
        Intent intent1 = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager)this.getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 60, pendingIntent);*/
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        /*String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

        rootRef.collection("watering")
                .whereEqualTo("date", CalendarUtils.formattedDate(localDate))
                .whereEqualTo("user_id", userId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        handleNotification();
                    }
                });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

        rootRef.collection("watering")
                .whereEqualTo("date", CalendarUtils.formattedDate(localDate))
                .whereEqualTo("user_id", userId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        handleNotification();
                    }
                });*/
    }



    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapterPlant.getFilter().filter(newText);
        adapterPlant.notifyDataSetChanged();
        return true;
    }
}