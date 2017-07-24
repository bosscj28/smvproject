package com.example.asus.story;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class DetailActivity extends AppCompatActivity {
    private DetailPageAdapter detailData;
    ArrayList<Story> storydata;
    ViewPager viewPager;
    ImageView back;
    TextView heading;
    Typeface monstBold,monstRegular;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        storydata = new ArrayList<>();
        storydata = (ArrayList<Story>) getIntent().getSerializableExtra("STORY");
        int pos = getIntent().getIntExtra("POSITION",0);
        monstRegular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        monstBold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.ttf");
        back = (ImageView) findViewById(R.id.backDetail); //BACK BUTTON
        heading = (TextView) findViewById(R.id.headingDetail); //BACK BUTTON
        viewPager = (ViewPager)findViewById(R.id.viewpagerDetail);
        viewPager.setPageTransformer(true,new ZoomOutPageTransformer());
        detailData=new DetailPageAdapter(this, storydata);
        viewPager.setAdapter(detailData);
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        viewPager.setCurrentItem(pos);
        heading.setTypeface(monstBold);

        if (storydata.get(pos).getCaption().toCharArray().length > 20) {
            String str = storydata.get(pos).getCaption().substring(0,20)+".....";
            heading.setText(str);
        }
        else
        {
            heading.setText(storydata.get(pos).getCaption());
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            if (storydata.get(position).getCaption().toCharArray().length > 20) {
                String str = storydata.get(position).getCaption().substring(0,20)+".....";
                heading.setText(str);
            }
            else
            {
                heading.setText(storydata.get(position).getCaption());
            }

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
}
