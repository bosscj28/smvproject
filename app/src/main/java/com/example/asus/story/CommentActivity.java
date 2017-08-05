package com.example.asus.story;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {
ArrayList<Comment> commentArrayList;
    RecyclerView commentRView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        commentArrayList=new ArrayList<>();
        commentRView=(RecyclerView)findViewById(R.id.CommentRView);
        int j = 0;
    }
}
