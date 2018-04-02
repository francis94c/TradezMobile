package com.fz5.tradezmobile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fz5.tradezmobile.model.SharedPreferencesHelper.*;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle = "Menu";
    private CharSequence title;
    private NavigationView navigationView;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        title = getTitle();
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                setTitle(title);
                //invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setTitle(drawerTitle);
                //invalidateOptionsMenu();
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();

        initializeNavigationView();
    }

    private void initializeNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.inflateMenu(R.menu.navigation);
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.nameTextView)).setText(Configs.getFullName(this));
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigationCreateAd:
                        Intent intent = new Intent(getApplicationContext(), CreateAdActivity.class);
                        startActivityForResult(intent, OnActivityResultContract.RequestCodes.CREATE_AD_ACTIVITY);
                        break;
                    case R.id.navigationViewProfile:
                        break;
                    case R.id.navigationAbout:
                        break;
                }
                return true;
            }
        });
    }

}
