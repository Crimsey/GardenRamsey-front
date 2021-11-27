package com.example.myapplication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.usage.UsageEvents;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.ui.models.Note;
import com.example.myapplication.ui.models.Plant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.ui.CalendarUtils.daysInMonthArray;
import static com.example.myapplication.ui.CalendarUtils.daysInWeekArray;
import static com.example.myapplication.ui.CalendarUtils.monthYearFromDate;

public class WeekViewActivity extends NavigationActivity implements CalendarAdapter.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;

    protected RecyclerView recyclerView;
    protected AdapterEvent adapterEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        initWidgets();
        setWeekView();
    }

    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

        recyclerView = (RecyclerView) findViewById(R.id.eventsCalendarRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        /*Query userPlants = db.collection("notes")
                .whereEqualTo("user_id", userId)
                .whereEqualTo("date", "23 November 2021");*/

        db.collection("notes").whereEqualTo("date", CalendarUtils.formattedDate(CalendarUtils.selectedDate)).whereEqualTo("user_id", userId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        List<Note> noteList = documentSnapshots.toObjects(Note.class);
                        adapterEvent = new AdapterEvent((ArrayList<Note>) noteList, context);
                        recyclerView.setAdapter(adapterEvent);
                    }
                });
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYear);
    }

    public void previousWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
            CalendarUtils.selectedDate = date;
            setWeekView();
    }

    public void newEventAction(View view) {
        startActivity(new Intent(this, EventEditActivity.class));
    }
}