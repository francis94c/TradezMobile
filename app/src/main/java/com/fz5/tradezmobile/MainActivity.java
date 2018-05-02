package com.fz5.tradezmobile;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fz5.tradezmobile.adapters.AdsAdapter;
import com.fz5.tradezmobile.model.Ad;
import com.fz5.tradezmobile.model.SharedPreferencesHelper.*;
import com.fz5.tradezmobile.util.URLContract.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle = "Menu";
    private CharSequence title;
    private AdsAdapter adapter = new AdsAdapter();

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
        adapter = new AdsAdapter();
        loadAds();
        ((ListView) findViewById(R.id.adsListView)).setAdapter(adapter);
    }

    private void loadAds() {
        JsonArrayRequest request = new JsonArrayRequest(URLSkeleton.getLoadAdsUrl(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int x = 0; x < response.length(); x++) {
                        JSONObject object = response.getJSONObject(x);
                        Ad ad = new Ad(object.getInt("id"), object.getString("title"), object.getString("location"), object.getInt("user_id"));
                        ad.setCategoryId(object.getInt("category_id"));
                        ad.setCategoryId(object.getInt("sub_category_id"));
                        adapter.addAd(ad);
                    }
                    loadImages();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error Loading Ads", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(this).add(request);
        try {
            Thread.currentThread().wait(10000);
        }catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    public void loadImages() {
        for (int x = 0; x < adapter.getCount(); x++) {
            makeRequest(x);
        }
    }

    private void makeRequest(final int x) {
        JsonArrayRequest request = new JsonArrayRequest(URLSkeleton.getImagesUrl(adapter.getAdId(x)), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int y = 0; y < response.length(); y++) {
                        adapter.addImage(x, response.getJSONObject(y).getString("name"));
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error Pulling Ad Image", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    public void adItem_Click(View view) {
        Intent intent = new Intent(this, AdActivity.class);
        Ad ad = (Ad) view.getTag();
        intent.putExtra("title", ad.getTitle());
        intent.putExtra("location", ad.getLocation());
        intent.putExtra("uid", ad.getUserId());
        intent.putExtra("ad_id", ad.getId());
        startActivity(intent);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        MenuItem searchItem  = menu.findItem(R.id.searchAds);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setFocusable(true);
        searchView.setQueryHint("Search Ads");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                JsonArrayRequest request = new JsonArrayRequest(URLSkeleton.getSearchAdsUrl(query), new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int x = 0; x < response.length(); x++) {
                                JSONObject object = response.getJSONObject(x);
                                Ad ad = new Ad(object.getInt("id"), object.getString("title"), object.getString("location"), object.getInt("user_id"));
                                ad.setCategoryId(object.getInt("category_id"));
                                ad.setCategoryId(object.getInt("sub_category_id"));
                                adapter.addAd(ad);
                            }
                            loadImages();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error Searching", Toast.LENGTH_SHORT).show();
                    }
                });
                if (!query.equals("")) {
                    adapter.clearAds();
                    Volley.newRequestQueue(getApplicationContext()).add(request);
                } else {
                    adapter.clearAds();
                    loadAds();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                JsonArrayRequest request = new JsonArrayRequest(URLSkeleton.getSearchAdsUrl(newText), new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int x = 0; x < response.length(); x++) {
                                JSONObject object = response.getJSONObject(x);
                                Ad ad = new Ad(object.getInt("id"), object.getString("title"), object.getString("location"), object.getInt("user_id"));
                                ad.setCategoryId(object.getInt("category_id"));
                                ad.setCategoryId(object.getInt("sub_category_id"));
                                adapter.addAd(ad);
                            }
                            loadImages();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error Searching", Toast.LENGTH_SHORT).show();
                    }
                });
                if (!newText.equals("")) {
                    adapter.clearAds();
                    Volley.newRequestQueue(getApplicationContext()).add(request);
                } else {
                    adapter.clearAds();
                    loadAds();
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
