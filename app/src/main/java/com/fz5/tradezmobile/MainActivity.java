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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.fz5.tradezmobile.model.SharedPreferencesHelper.*;
import com.fz5.tradezmobile.util.URLContract.*;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle = "Menu";
    private CharSequence title;

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

        refreshCategories();
    }

    private void initializeNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.inflateMenu(R.menu.navigation);
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.nameTextView)).setText(Configs.getFullName(this));
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.phoneTextView)).setText(Configs.getPhone(this));
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

    private void refreshCategories() {
        JsonArrayRequest categoriesRequest = new JsonArrayRequest(URLSkeleton.getCategoriesUrl(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Configs.setCategories(getApplicationContext(), response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error Getting Categories from the server.", Toast.LENGTH_SHORT).show();
            }
        });
        JsonArrayRequest subCategoriesRequest = new JsonArrayRequest(URLSkeleton.getSubCategoriesUrl(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Configs.setSubCategories(getApplicationContext(), response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error Getting Sub Categories from the server.", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(categoriesRequest);
        Volley.newRequestQueue(this).add(subCategoriesRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
