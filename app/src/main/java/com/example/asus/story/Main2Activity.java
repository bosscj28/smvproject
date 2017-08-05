package com.example.asus.story;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    TextView maintext,uploadtext,newsfeed;
    private View underlineview1,underlineview2;
    Intent intent;
    ImageView arrow1;
    public DataSaver maindatasaver;
    public Story storysaver;
    private TabLayout tabLayout;
    public CustomViewPager viewPager;
    private DatabaseHandler db;
    ArrayList<HashMap<String, String>> catlist;
    Typeface monstBold,monstRegular;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.righttoleft, R.anim.hold);
        setContentView(R.layout.activity_main2);
        maindatasaver = new DataSaver();
        storysaver = new Story();
        Log.d("MAIN","2"+maindatasaver);
        catlist = new ArrayList<HashMap<String, String>>();
        catlist = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("CATEGORY");
        if(catlist.size() > 0)
        catlist.remove(0);
        Log.d("CATEGORY LIST","SIZE"+catlist.size());
        monstRegular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        monstBold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.ttf");
        maintext = (TextView) findViewById(R.id.descDetail);
        uploadtext = (TextView) findViewById(R.id.likevw);
        newsfeed = (TextView) findViewById(R.id.nwsfd);
        underlineview1 = (View) findViewById(R.id.underlineView);
        underlineview2 = (View) findViewById(R.id.underlineView1);
        //arrow1 = (ImageView) findViewById(R.id.arrow1);
        maintext.setTextColor(getResources().getColor(R.color.tabcolor));
        maintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent);
                supportFinishAfterTransition();
            }
        });

        maintext.setTypeface(monstRegular);
        uploadtext.setTypeface(monstRegular);
        newsfeed.setTypeface(monstBold);
        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.disableScroll(true);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        //tabLayout.setupWithViewPager(viewPager);
        //setupTabIcons();
        changeTabsFont();
        tabLayout.setupWithViewPager(viewPager);

        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }

    }
    private void changeTabsFont() {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(monstBold);
                }
            }
        }
    }
    @Override
    public void onStart(){
        super.onStart();
        uploadtext.setTextColor(getResources().getColor(R.color.tabactivecolor));
        underlineview1.setVisibility(View.GONE);
    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new detailsFragment(), "Details");
        adapter.addFragment(new locationsFragment(), "Location");
        adapter.addFragment(new confirmationFragment(), "Confirmation");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }
}
