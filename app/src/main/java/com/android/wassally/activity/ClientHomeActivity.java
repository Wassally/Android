package com.android.wassally.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.wassally.Constants;
import com.android.wassally.R;
import com.android.wassally.fragment.HistoryFragment;
import com.android.wassally.fragment.MyOrdersFragment;
import com.android.wassally.helpers.PreferenceUtils;

public class ClientHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private View mHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_client_home);

        //use our toolBar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        //Handle menu icon at the top left to open and close the drawer with simple animation
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mHeader = navigationView.getHeaderView(0);
        populateNavHeaderData();

        FloatingActionButton mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNewOrderActivity();
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MyOrdersFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MyOrdersFragment()).commit();
                break;
            case R.id.nav_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HistoryFragment()).commit();
                break;
            case R.id.nav_logout:
                logOut();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void displayNewOrderActivity() {
            startActivity(new Intent(this, CreatePackageActivity.class));
    }

    /**
     * populate navigation drawer data which are client name and profile picture
     * now we are using shared preferences to get this data
     **/
    private void populateNavHeaderData() {
        TextView mClientNameTextView = mHeader.findViewById(R.id.header_client_name_tv);
        String fullName = PreferenceUtils.getFullName(this);
        mClientNameTextView.setText(fullName);

        mClientNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();
            }
        });
    }

    private void openProfile() {
        startActivity(new Intent(ClientHomeActivity.this,ProfileActivity.class));
        drawer.closeDrawer(GravityCompat.START);
    }

    /**
     * offline logout just clear the token saved in preference instance and start login activity
     **/
    private void logOut() {
        Intent intent = new Intent(ClientHomeActivity.this, LoginActivity.class);
        startActivity(intent);
        //clear token and finish activity
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ClientHomeActivity.this);
        preferences.getAll().clear();
        preferences.edit().putString(Constants.AUTH_TOKEN, "").apply();
        preferences.edit().putString(Constants.FULL_NAME, "").apply();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            super.onBackPressed();
        }

        //if drawer is open when pressing back button just close the navigation drawer
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
