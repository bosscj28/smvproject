package com.example.asus.story;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class DetailActivity extends AppCompatActivity {
    private DetailPageAdapter detailData;
    ArrayList<Story> storydata;
    ArrayList<Comment> commentdata;
    ViewPager viewPager;
    EditText comment;
    RecyclerView recyclerView;
    LinearLayout Clayout,Hlayout;
    ImageView back;
    TextView heading;
    Typeface monstBold,monstRegular;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        storydata = new ArrayList<>();
        commentdata=new ArrayList<>();
        storydata = (ArrayList<Story>) getIntent().getSerializableExtra("STORY");
        int pos = getIntent().getIntExtra("POSITION",0);
        monstRegular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        monstBold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.ttf");
        back = (ImageView) findViewById(R.id.backDetail); //BACK BUTTON
        heading = (TextView) findViewById(R.id.headingDetail); //BACK BUTTON
        viewPager = (ViewPager)findViewById(R.id.viewpagerDetail);
        viewPager.setPageTransformer(true,new ZoomOutPageTransformer());
        recyclerView=(RecyclerView)findViewById(R.id.cmnts_Rview);
        detailData=new DetailPageAdapter(this, storydata);
        viewPager.setAdapter(detailData);
        viewPager.setOffscreenPageLimit(4);
        //viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        viewPager.setCurrentItem(pos);
        heading.setTypeface(monstBold);
        comment=(EditText)findViewById(R.id.editTextc_mnt);
        comment.setImeOptions(EditorInfo.IME_ACTION_SEND);
        dataAdapter da =new dataAdapter();
        Clayout=(LinearLayout)findViewById(R.id.detail_cmnt_layout);
        Hlayout=(LinearLayout)findViewById(R.id.detail_tab_layout);
        Hlayout.setLayoutParams(new LinearLayout.LayoutParams(da.getScreenWidth(),15*da.getScreenHeight()/200));
        comment.setLayoutParams(new LinearLayout.LayoutParams(7*da.getScreenWidth()/10,8*da.getScreenHeight()/100));
        viewPager.setLayoutParams(new LinearLayout.LayoutParams(da.getScreenWidth(),81*da.getScreenHeight()/100));
        Clayout.setLayoutParams(new LinearLayout.LayoutParams(da.getScreenWidth(),17*da.getScreenHeight()/200));
        heading.setText(getIntent().getStringExtra("CATEGORY_NAME"));
        /*if (storydata.get(pos).getCaption().toCharArray().length > 20) {
            String str = storydata.get(pos).getCaption().substring(0,20)+".....";
            heading.setText(str);
        }
        else
        {
            heading.setText(storydata.get(pos).getCaption());
        }*/
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });
    }

  /*  ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

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
    };*/
}
