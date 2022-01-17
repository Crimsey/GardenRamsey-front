package com.example.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class NavigationActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "NavigationActivity";
    protected Toolbar toolbar;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;

    Context context;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setupDrawer();
    }

    void setupDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                null,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getTitle().toString()) {
            case "Main":
                goMain();
                break;
            /*case "Somewhere":
                goSomewhere();
                break;*/
            case "Account":
                goAccount();
                break;
            case "Calendar":
                goCalendar();
                break;
            case "All Plants":
                goAllPlants();
                break;
            case "Logout":
                logout();
                break;
        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    void goMain() {
        Toast.makeText(this, "Going to main", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    void goSomewhere() {
        Toast.makeText(this, "Going somewhere", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, SomewhereActivity.class);
        startActivity(i);
        finish();
    }

    private void goAccount() {
        Toast.makeText(this, "Going account info", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, AccountActivity.class);
        startActivity(i);
        finish();
    }

    void goCalendar() {
        Toast.makeText(this, "Going calendar", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, CalendarActivity.class);
        startActivity(i);
        finish();
    }

    void goAllPlants() {
        Toast.makeText(this, "Going plant list", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, PlantListAllActivity.class);
        startActivity(i);
        finish();
    }

    void logout() {
        Toast.makeText(this, "Logout", Toast.LENGTH_LONG).show();
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(this, StartActivity.class);
        startActivity(i);
        finish();
    }
}