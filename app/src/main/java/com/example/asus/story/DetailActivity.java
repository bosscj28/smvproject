package com.example.asus.story;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;


public class DetailActivity extends AppCompatActivity {
    private DetailPageAdapter detailData;
    ArrayList<Story> storydata;
    ViewPager viewPager;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        storydata = new ArrayList<Story>();
        storydata = (ArrayList<Story>) getIntent().getSerializableExtra("STORY");
        int pos = getIntent().getIntExtra("POSITION",0);
        back = (Button) findViewById(R.id.backDetail); //BACK BUTTON
        viewPager = (ViewPager)findViewById(R.id.viewpagerDetail);
        detailData=new DetailPageAdapter(this, storydata);
        viewPager.setAdapter(detailData);
        viewPager.setCurrentItem(pos);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });
    }
}
