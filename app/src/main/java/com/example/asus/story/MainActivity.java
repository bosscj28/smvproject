package com.example.asus.story;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView maintext,uploadtext;
    private DrawerLayout drawer;
    private View underlineview1,underlineview2;
    Intent intent;
    ViewPager viewPager;
    TabLayout tabLayout;
    Typeface monstRegular,monstBold;
    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pd;
    ImageView userImgNav;
    TextView userNameNav,userEmailNav;
    private static final String CATEGORY_URL = "http://kookyapps.com/smv/api/newsType";
    public static final String PROFILE_URL = " http://kookyapps.com/smv/api/profile/";
    public static final String KEY_FB_ID = "fb_id";
    ArrayList<HashMap<String,String>> categoryList;
    SharedPreferences sharedPreProfile;
    int[][] state = new int[][] {
            new int[] {android.R.attr.state_checked}, // checked
            new int[] {-android.R.attr.state_checked}
    };

    int[] color = new int[] {
            Color.rgb(255,46,84),
            (Color.BLACK)
    };

    ColorStateList csl = new ColorStateList(state, color);

    int[][] state2 = new int[][] {
            new int[] {android.R.attr.state_checked}, // checked
            new int[] {-android.R.attr.state_checked}
    };

    int[] color2 = new int[] {
            Color.rgb(255,46,84),
            Color.GRAY
    };

    ColorStateList csl2 = new ColorStateList(state2, color2);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.lefttoright, R.anim.hold);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);

        monstRegular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        monstBold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.ttf");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.drawer);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();
        // CALL DRAWER
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        userImgNav = (ImageView) header.findViewById(R.id.userImgNav);
        userNameNav = (TextView) header.findViewById(R.id.userNameNav);
        userEmailNav = (TextView) header.findViewById(R.id.userEmailNav);
        sharedPreProfile = getSharedPreferences("Logged_in_user",MODE_PRIVATE);
        String Logged_name = sharedPreProfile.getString("Logged_user_name","Internet Failure");
        String Logged_email = sharedPreProfile.getString("Logged_user_email","Internet Failure");
        String Logged_img = sharedPreProfile.getString("Logged_user_img","Internet Failure");
        Log.d("TAG","LOGGED DATA"+Logged_name);
        userNameNav.setText(Logged_name);
        userEmailNav.setText(Logged_email);

        userImgNav.setImageResource(R.drawable.user);

        //CATEGORY AS ARRAYLIST
        categoryList = new ArrayList<>();
        if(isOnline(this))
        {
            Glide.with(MainActivity.this)
                    .load(Logged_img)
                    .into(userImgNav);
            GetCategorys();
        }
        else
        {
            //Load profile img as empty if online
            userImgNav.setImageResource(R.drawable.user);
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Internet is not Connected", 10000)
                    .setAction("RETRY",new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(isOnline(MainActivity.this))
                            {
                                sharedPreProfile = getSharedPreferences("Logged_in_user",MODE_PRIVATE);
                                String Logged_img = sharedPreProfile.getString("Logged_user_img","Internet Failure");
                                Glide.with(MainActivity.this)
                                        .load(Logged_img)
                                        .into(userImgNav);
                                GetCategorys();
                            }
                        }
                    });
            View snackbarView = snackbar.getView();
            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbarView.setBackgroundColor(Color.BLACK);
            snackbar.show();

        }

        Typeface monstRegular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        maintext = (TextView) findViewById(R.id.descDetail);
        uploadtext = (TextView) findViewById(R.id.likevw);
        maintext.setTypeface(monstRegular);
        uploadtext.setTypeface(monstRegular);
        underlineview1 = (View) findViewById(R.id.underlineView);
        underlineview2 = (View) findViewById(R.id.underlineView1);
        uploadtext.setTextColor(getResources().getColor(R.color.tabcolor));
        uploadtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("CATEGORY",categoryList);
                startActivity(intent);
                finish();

            }
        });

        /* ADD CATEGORY IN DB
        db = new DatabaseHandler(getApplicationContext());
        int count = db.CountCategory();
        if (count == 0)
        {
            //Log.d("ADD","ADD");
            long id1 = db.createCategory(new Categories(1,"Popular"));
            long id2 = db.createCategory(new Categories(2,"Featured"));
            long id3 = db.createCategory(new Categories(3,"My Favourite"));
            //Log.d("id1 "+id1,"id2 "+id2);
        }
        */


    }

    private boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
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

    private void setupViewPager(ViewPager viewPager) {
        List<Fragment> fragments = getFragments();
        List<String> Title = getTitleArray();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments, Title);

        viewPager.setAdapter(adapter);
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();

        for (int i=0;i<categoryList.size();i++)
        {
            fList.add(CommonFragment.newInstance(categoryList.get(i).get("id")));
        }

        return fList;
    }
    private List<String> getTitleArray() {
        List<String> fList = new ArrayList<String>();
        for (int i=0;i<categoryList.size();i++)
        {
            fList.add(categoryList.get(i).get("name"));
        }

        return fList;
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList;
        private List<String> mFragmentTitleList;

        public ViewPagerAdapter(FragmentManager manager,List<Fragment> mFragmentList, List<String> mFragmentTitleList) {
            super(manager);
            this.mFragmentList = mFragmentList;
            this.mFragmentTitleList = mFragmentTitleList;

        }

        @Override
        public Fragment getItem(int position) {return mFragmentList.get(position);}

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

    @Override
    public void onStart(){
        super.onStart();
        maintext.setTextColor(getResources().getColor(R.color.tabactivecolor));
        underlineview2.setVisibility(View.GONE);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
        nav.setItemTextColor(csl);
        nav.setItemIconTintList(csl2);
        nav.setItemBackgroundResource(R.color.bg);
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            LoginManager.getInstance().logOut();
            SharedPreferences.Editor editor2 = sharedPreProfile.edit();
            editor2.clear();
            editor2.commit();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            MainActivity.this.finish();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void GetCategorys()
    {
        pd = ProgressDialog.show(this, "", "Please Wait!", true, false);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,CATEGORY_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            VolleyLog.v("Response:%n %s", response.toString(4));
                            Log.d("Response:%n %s", response.toString());
                            JSONArray news = response.getJSONArray("data");

                            for (int i = 0; i < news.length(); i++) {
                                JSONObject s = news.getJSONObject(i);

                                String id = s.getString("c_id");
                                String name = s.getString("c_name");

                                HashMap<String, String> cats = new HashMap<>();
                                cats.put("id",id);
                                cats.put("name",name);
                                categoryList.add(cats);

                            }
                                if (pd.isShowing())
                                pd.dismiss();

                            viewPager = (ViewPager) findViewById(R.id.viewpagermain);
                            viewPager.setPageTransformer(true,new DepthPageTransformer());
                            setupViewPager(viewPager);
                            viewPager.setOffscreenPageLimit(3);
                            tabLayout = (TabLayout) findViewById(R.id.tabs);
                            tabLayout.setupWithViewPager(viewPager);
                            changeTabsFont();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse errorRes = error.networkResponse;
                String stringData = "";
                if(errorRes != null && errorRes.data != null){
                    stringData = new String(errorRes.data);
                }
                Log.e("Error",stringData);
                if (pd.isShowing())
                    pd.dismiss();

            }
        });

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(req);

    }

}



