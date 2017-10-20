package com.example.administrator.pasugoon;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.administrator.pasugoon.adapter.CustomAdapter;

import java.util.ArrayList;

public class CheckActivity extends AppCompatActivity {

    Context context;
    int memberSeq;

    CustomAdapter customAdapter;

    private ArrayList<ListData> listDataArray = new ArrayList<ListData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        ListData data1 = new ListData("1 - 첫번째줄","test");
        listDataArray.add(data1);

        ListData data2 = new ListData("2 - 첫번째줄","test");
        listDataArray.add(data2);

        ListData data3 = new ListData("3 - 첫번째줄","test");
        listDataArray.add(data3);

        ListData data4 = new ListData("4 - 첫번째줄","test");
        listDataArray.add(data4);

        ListView listView = (ListView)findViewById(R.id.custom_list_listView);
        CustomAdapter customAdapter = new CustomAdapter(this, R.layout.custom_list_row, listDataArray);
        listView.setAdapter(customAdapter);
    }
}
