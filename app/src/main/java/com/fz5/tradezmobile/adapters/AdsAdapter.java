package com.fz5.tradezmobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fz5.tradezmobile.R;
import com.fz5.tradezmobile.model.Ad;
import com.fz5.tradezmobile.util.URLContract.*;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;

public class AdsAdapter extends BaseAdapter {

    private ArrayList<Ad> ads = new ArrayList<>();

    public void addAd(Ad ad) {
        ads.add(ad);
    }

    public void clearAds() {
        ads.clear();
    }

    public void addImage(int index, String image) {
        ads.get(index).addImage(image);
    }

    public int getAdId(int index) {
        return ads.get(index).getId();
    }

    @Override
    public int getCount() {
        return ads.size();
    }

    @Override
    public Object getItem(int i) {
        return ads.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            view = inflater.inflate(R.layout.ad_item, viewGroup, false);
        }
        Ad ad = ads.get(i);
        ((TextView) view.findViewById(R.id.titleTextView)).setText(ad.getTitle());
        ((TextView) view.findViewById(R.id.locationTextView)).setText(ad.getLocation());
        try {
            Picasso.with(viewGroup.getContext()).load(URLSkeleton.getImageUrl(ad.getImage(0))).into((ImageView) view.findViewById(R.id.adImageView));
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(viewGroup.getContext(), "Error Loading an Ad Image.", Toast.LENGTH_SHORT).show();
        }
        view.setTag(ad);
        return view;
    }

}
