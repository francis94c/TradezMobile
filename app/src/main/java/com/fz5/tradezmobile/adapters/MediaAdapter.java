package com.fz5.tradezmobile.adapters;

import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fz5.tradezmobile.R;
import com.fz5.tradezmobile.model.Image;
import com.fz5.tradezmobile.model.LOCATION;
import com.fz5.tradezmobile.model.MEDIA_TYPE;
import com.fz5.tradezmobile.model.Media;

import java.util.ArrayList;

public class MediaAdapter extends BaseAdapter {

    private ArrayList<Image> images = new ArrayList<>();

    public void addImage(Image image) {
        images.add(image);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int i) {
        return images.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            view = inflater.inflate(R.layout.media_item, viewGroup, false);
        }
        if (images.get(i).getLocation() == LOCATION.OFFLINE) {
            ((ImageView) view).setImageBitmap(images.get(i).getBitmap());
        }
        return view;
    }

}
