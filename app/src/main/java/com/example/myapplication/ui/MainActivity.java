package com.example.myapplication.ui;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;

import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.ui.models.Plant;
import com.example.myapplication.ui.models.Watering;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.example.myapplication.ui.notification.AlarmReceiver;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MainActivity extends NavigationActivity
        implements SearchView.OnQueryTextListener {

    protected RecyclerView recyclerView;
    protected AdapterPlant adapterPlant;
    protected SearchView searchEvent;

    private FloatingActionButton mFab;

    public List<Watering> wateringList;
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
            Toast.makeText(MainActivity.this, "Fill a form to add a plant", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, PlantAddActivity.class));
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
        //notyfikacja();

        rootRef.collection("watering").whereEqualTo("date", CalendarUtils.formattedDate(localDate)).whereEqualTo("user_id", userId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        wateringList = documentSnapshots.toObjects(Watering.class);

                        //Toast.makeText(MainActivity.this, "Wateringlist size: "+wateringList.size()+" date: "+CalendarUtils.formattedDate(localDate), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(context, MainActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                        if(wateringList.size() > 0) {
                            int i = 1;
                            for(Watering watering : wateringList) {
                                //handleNotification(watering.getPlant(), watering.getNote(), i);
                                Log.i(TAG, "witam "+i);

                                Notification notification = new NotificationCompat.Builder(context, "notifyWater")
                                        .setSmallIcon(R.drawable.water)
                                        //.addAction(replyAction)
                                        .setColor(Color.BLUE)
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                        .setContentIntent(pendingIntent)
                                        .setAutoCancel(true)
                                        .setOnlyAlertOnce(true)
                                        .build();

                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                                notificationManager.notify(1, notification);

                                /*Notification builder = new NotificationCompat.Builder(context, "notifyWater")
                                        .setSmallIcon(R.drawable.water)
                                        .setContentTitle(watering.getPlant())
                                        .setContentText(watering.getNote())
                                        .setColor(0xff123456)
                                        .setColorized(true)
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .build();
                                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                                SystemClock.sleep(2000);
                                notificationManagerCompat.notify(i, builder);*/

                                i++;
                            }
                        }
                    }
                });
        //handleNotification();
        //onSendNotificationsButtonClick();
    }

    /*public void notyfikacja(){
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY,21);
        calendar.set(Calendar.MINUTE,15);
        calendar.set(Calendar.SECOND,0);

        Intent intent = new Intent(getApplicationContext(), Notification_receiver.class);
        intent.setAction("MY_NOTIFICATION_MESSAGE");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }

    /*public void onSendNotificationsButtonClick() {
        NotificationEventReceiver.setupAlarm(getApplicationContext());
    }*/

    private void handleNotification(String wateringName, String note, int id) {

        CharSequence name = "ReminderChannel";
        String description = "Channel for reminder";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("notifyWater", name, importance);
        channel.setDescription(description);
        Log.i(TAG, "alram");

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        intent.putExtra("wateringName", wateringName);
        intent.putExtra("note", note);
        intent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //long timeAtButtonClick = System.currentTimeMillis();

        long tenSecondsToMillies = 1000 * 60;

        alarmManager.set(AlarmManager.RTC_WAKEUP,
                tenSecondsToMillies, pendingIntent);
        Log.i(TAG, alarmManager.toString());
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
        //handleNotification();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //handleNotification();
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