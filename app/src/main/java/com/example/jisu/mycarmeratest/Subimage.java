package com.example.jisu.mycarmeratest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class Subimage extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerview;
    private LinearLayoutManager manager;
    private MyAdapter adapter;
    private ArrayList<DateSaveList> list = new ArrayList<>();
    private Button recycBtn;
    public static final String TAG="Subimage";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        recyclerview=findViewById(R.id.recyclerview);
        recycBtn=findViewById(R.id.recycBtn);
        list=(ArrayList<DateSaveList>)getIntent().getSerializableExtra("list");
        Log.d(TAG,list.toString());
        adapter=new MyAdapter(R.layout.re_item,list);
        manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(adapter);
        recycBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.recycBtn:
                finish();
                break;
        }
    }
}
