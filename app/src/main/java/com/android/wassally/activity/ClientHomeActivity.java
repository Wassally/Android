package com.android.wassally.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.wassally.R;
import com.android.wassally.fragment.CompletedFragment;
import com.android.wassally.fragment.FavoriteFragment;
import com.android.wassally.fragment.MyOrdersFragment;
import com.android.wassally.fragment.ProfileFragment;

import java.util.jar.Manifest;

public class ClientHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String AUTH_TOKEN = "auth_token";

    private DrawerLayout drawer;
    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;
    private View mHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);
        String clientName = getIntent().getStringExtra("full_name");

        //use our toolBar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        //Handle menu icon at the top left to open and close the drawer with simple animation
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mHeader = navigationView.getHeaderView(0);
        populateNavHeaderData();


        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MyOrdersFragment()).commit();
        }

    }

    /**
     * populate navigation drawer data which are client name and profile picture
     * now we are using shared preferences to get this data
     **/
    private void populateNavHeaderData() {
        TextView mClientNameTextView = mHeader.findViewById(R.id.header_client_name_tv);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ClientHomeActivity.this);
        String name = preferences.getString("name", "");
        mClientNameTextView.setText(name);

        mClientNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();
            }
        });
    }

    private void openProfile() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new ProfileFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_profile);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CompletedFragment()).commit();
                break;

            case R.id.nav_favorite:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FavoriteFragment()).commit();
                break;
            case R.id.bottom_nav_orders:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MyOrdersFragment()).commit();
                break;
            case R.id.bottom_nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;
            case R.id.bottom_nav_newOrder:
                displayNewOrderActivity();
                break;
            case R.id.nav_logout:
                logOut();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void displayNewOrderActivity() {
            startActivity(new Intent(this, CreatePackageActivity.class));
            overridePendingTransition(R.anim.slide_in_up, R.anim.splash_fade_out);

    }

    /**
     * offline logout just clear the token saved in preference instance and start login activity
     **/
    private void logOut() {
        Intent intent = new Intent(ClientHomeActivity.this, LoginActivity.class);
        startActivity(intent);
        //clear token and finish activity
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ClientHomeActivity.this);
        preferences.edit().putString(AUTH_TOKEN, "").apply();
        preferences.edit().putString("name", "").apply();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_orders);
    }

    @Override
    public void onBackPressed() {
        if (!drawer.isDrawerOpen(GravityCompat.START) && bottomNavigationView.getSelectedItemId() == R.id.bottom_nav_orders) {
            super.onBackPressed();
        }

        //if drawer is open when pressing back button just close the navigation drawer
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        //home is the default fragment
        if (bottomNavigationView.getSelectedItemId() != R.id.bottom_nav_orders) {
            bottomNavigationView.setSelectedItemId(R.id.bottom_nav_orders);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MyOrdersFragment()).commit();
        }
    }
}
