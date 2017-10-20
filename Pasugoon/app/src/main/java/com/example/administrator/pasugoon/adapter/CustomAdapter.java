package com.example.administrator.pasugoon.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.pasugoon.ListData;
import com.example.administrator.pasugoon.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-10-20.
 */

public class CustomAdapter extends ArrayAdapter<ListData>{
    private Context context;
    private int layoutResourceId;
    private ArrayList<ListData> listData;


    public CustomAdapter(Context context, int layoutResourceId, ArrayList<ListData> listData) {
        super(context, layoutResourceId, listData);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.listData = listData;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;

        if(row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId,parent,false);
        }

        EditText editText = row.findViewById(R.id.custom_row_editText);

        editText.setText(listData.get(position).getText());

        ImageView imageView = row.findViewById(R.id.custom_row_imageView);

        imageView.setImageResource(R.drawable.button_round_red);
        return row;
    }
}
