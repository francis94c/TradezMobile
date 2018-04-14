package com.fz5.tradezmobile.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fz5.tradezmobile.R;

import java.util.List;

/**
 * Created by Francis Ilechukwu 23/01/2018.
 */

public class SpinnerAdapterView extends ArrayAdapter<String> {

    private List<String> items;
    private Activity activity;
    private String label;

    public SpinnerAdapterView(Activity activity, List<String> items, String label) {
        super(activity, android.R.layout.simple_list_item_1, items);
        this.items = items;
        this.label = label;
        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.spinner_view, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.spinnerItemTextView)).setText(label + " {" + items.get(position) + "}");
        return convertView;
    }

}
