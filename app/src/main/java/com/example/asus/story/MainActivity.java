package com.example.asus.story;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.example.asus.story.utils.Session;
import com.example.asus.story.utils.Utils;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.tagmanager.Container;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView maintext,uploadtext;
    private DrawerLayout drawer;
    private View underlineview1,underlineview2;
    Intent intent;
    String upper,lower;
    int up,initLow;
    int currentPage;
    Handler handlervwpgr,handlerpgrs;
    Runnable update;
    Context context;
    ViewPager viewPager,vwpager;
    private LayoutInflater inflater;
    private ProgressBar progressBar;
    private int progressStatus;
    private Container container;
    ArrayList<Story> Sstory;
    Menu menu;
    SliderAdapter sliderAdapter,Sadapter;
    String popular;
    boolean AppendDataFlag = false;
    TabLayout tabLayout;
    Typeface monstRegular,monstBold;
    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pd;
    ImageView userImgNav;
    TextView userNameNav,userEmailNav;
    public static final String KEY_FB_ID = "fb_id";
    ArrayList<HashMap<String,String>> categoryList;
    SharedPreferences sharedPreProfile;
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = Session.getSession(MainActivity.this);
        overridePendingTransition(R.anim.lefttoright, R.anim.hold);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);
        upper = "0";
        lower = "10";
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
        menu=navigationView.getMenu();
        View header = navigationView.getHeaderView(0);
        vwpager=(ViewPager)findViewById(R.id.view2);
       // vwpager.beginFakeDrag();
        dataAdapter da=new dataAdapter();
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(da.getScreenWidth()-20,36*da.getScreenHeight()/100);
        vwpager.setLayoutParams(params);
        vwpager.setPageMargin(-2*da.getScreenWidth()/10);
        //vwpager.setPadding(10,10,0,0);

        //pager timer
        handlervwpgr = new Handler();
        currentPage=vwpager.getCurrentItem();
        vwpager.addOnPageChangeListener(viewPagerPageChangeListener);
        update = new Runnable() {
            public void run() {
                if (currentPage ==Integer.parseInt(lower)-1) {
                    currentPage = 0;
                }
                vwpager.setCurrentItem(currentPage++, true);

            }
        };
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handlervwpgr.post(update);
            }
        }, 5000, 5000);

        userImgNav = (ImageView) header.findViewById(R.id.userImgNav);
        userNameNav = (TextView) header.findViewById(R.id.userNameNav);
        userEmailNav = (TextView) header.findViewById(R.id.userEmailNav);
       /* sharedPreProfile = getSharedPreferences("Logged_in_user",MODE_PRIVATE);
        String Logged_name = sharedPreProfile.getString("Logged_user_name","Internet Failure");
        String Logged_email = sharedPreProfile.getString("Logged_user_email","Internet Failure");
        String Logged_img = sharedPreProfile.getString("Logged_user_img","Internet Failure");
        Log.d("TAG","LOGGED DATA"+Logged_name);
        userNameNav.setText(Logged_name);
        userEmailNav.setText(Logged_email);*/

        userEmailNav.setText(session.getUserEmail());
        userNameNav.setText(session.getUserName());

        userImgNav.setImageResource(R.drawable.user);

        //CATEGORY AS ARRAYLIST
        categoryList = new ArrayList<>();
        if(isOnline(this))
        {
            Glide.with(MainActivity.this)
                    .load(session.getUserImg())
                    .into(userImgNav);
            GetCategorys();

        }
        else
        {
            initMenufull();
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
                                        .load(session.getUserImg())
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
        Toast.makeText(MainActivity.this,"TITLE - "+item.getTitle()+"ID - "+item.getItemId(), Toast.LENGTH_LONG).show();
        if(item.getTitle().equals("My Feeds"))
        {

        }else if (item.getTitle().equals("My Favorite"))
        {

        }else if(item.getTitle().equals("About us")){
            Intent intent = new Intent(MainActivity.this,NavCommonActivity.class);
            intent.putExtra("Title",item.getTitle());
            startActivity(intent);
        }else if(item.getTitle().equals("Contact us")){
            Intent intent = new Intent(MainActivity.this,NavCommonActivity.class);
            intent.putExtra("Title",item.getTitle());
            startActivity(intent);
        }else if(item.getTitle().equals("Donation")){
            Intent intent = new Intent(MainActivity.this,NavCommonActivity.class);
            intent.putExtra("Title",item.getTitle());
            startActivity(intent);
        }else if(item.getTitle().equals("Share")){

        }else if (item.getTitle().equals("Rate us")){

        }else if(item.getTitle().equals("Logout")){
            LoginManager.getInstance().logOut();
            /*SharedPreferences.Editor editor2 = sharedPreProfile.edit();
            editor2.clear();
            editor2.commit();*/
            session.ClearSession(MainActivity.this);
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            MainActivity.this.finish();
        }else {
            String Str = item.getTitle().toString();
            if(categoryList.size()!=0){
                for (int i = 0; i < categoryList.size(); i++)
                {
                    if(categoryList.get(i).get("name").equals(Str)){
                        //Call Intent
                        // int cat_id = categoryList.get(i).get("id");
                        //Cat id as PutExtra

                    }

                }

            }

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void GetCategorys()
    {
        String url = Utils.BASE_URL+Utils.CATEGORY_URL;
        pd = ProgressDialog.show(this, "", "Please Wait!", true, false);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url,
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
                            for(int j=0;j<categoryList.size();j++)
                            {
                                Log.d("CATEGORY","category"+categoryList.get(j).get("name"));
                                if(categoryList.get(j).get("name").equals("Popular"))
                                    popular=categoryList.get(j).get("id");
                            }
                            initMenufull();
                            if (pd.isShowing())
                                pd.dismiss();

                            viewPager = (ViewPager) findViewById(R.id.viewpagermain);
                            viewPager.setHorizontalScrollBarEnabled(true);
                            viewPager.setPageTransformer(true,new DepthPageTransformer());
                            setupViewPager(viewPager);
                            viewPager.setOffscreenPageLimit(3);
                            tabLayout = (TabLayout) findViewById(R.id.tabs);
                            tabLayout.setupWithViewPager(viewPager);
                            changeTabsFont();

                            popularStories(popular,upper,lower);

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


    public void popularStories(String id,String upperparam, String lowerparam){
        String url = Utils.BASE_URL+Utils.FETCH_NEWS_URL;

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("cat_id", id);
        params.put("upper_limit",upperparam);
        params.put("lower_limit",lowerparam);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            VolleyLog.v("Response:%n %s", response.toString(4));
                            Log.d("Response:%n %s", response.toString());
                            JSONArray news = response.getJSONArray("data");
                            Sstory = new ArrayList<>();
                            if(upper.equals("0")){
                                Sstory.clear();
                                Log.d("CJ STORY","M CLEARED");
                            }
                            Log.d("length","len"+news.length());
                            for (int i = 0; i < news.length(); i++) {
                                Story storyobj = new Story();
                                if(i==news.length()-1){
                                    JSONObject s = news.getJSONObject(0);
                                    int n = Integer.parseInt(s.get("n_id").toString());
                                    storyobj.setID(n);
                                    storyobj.setcaption(s.getString("n_title"));
                                    storyobj.setdesc(s.getString("n_desc"));
                                    storyobj.setCat_id(s.getString("n_cat"));
                                    storyobj.setUrl(s.getString("n_img"));
                                    storyobj.setEmail(s.getString("n_email"));

                                }else {
                                    JSONObject s = news.getJSONObject(i);
                                    int n = Integer.parseInt(s.get("n_id").toString());
                                    storyobj.setID(n);
                                    storyobj.setcaption(s.getString("n_title"));
                                    storyobj.setdesc(s.getString("n_desc"));
                                    storyobj.setCat_id(s.getString("n_cat"));
                                    storyobj.setUrl(s.getString("n_img"));
                                    storyobj.setEmail(s.getString("n_email"));
                                }
                                Sstory.add(storyobj);
                                Log.d("CJ SIZE","STORY SIZE - "+Sstory.size());


                            }

                            sliderAdapter=new SliderAdapter(MainActivity.this,Sstory);
                            vwpager.setAdapter(sliderAdapter);
                            vwpager.setOffscreenPageLimit(9);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                up = Integer.parseInt(upper);
                up = up - initLow;
                upper = String.valueOf(up);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(req);
    }
    public void initMenufull(){
        // Menu Initialization
        menu.clear();
        menu.add(0, 1, Menu.FIRST, "My Feeds").setIcon(R.drawable.ic_menu_camera);
        menu.add(0, 2, Menu.FIRST, "My Favorite").setIcon(R.drawable.ic_menu_gallery);
        if(categoryList.size()!=0) {
        for (int i = 0; i <categoryList.size(); i++) {
            String category = categoryList.get(i).get("name");
            String id = categoryList.get(i).get("id");
            menu.add(1, 3+i, Menu.FIRST, category).setIcon(R.drawable.ic_menu_category);
        }
    }
    int lastid = categoryList.size();
    lastid = lastid + 3;
        menu.add(2,lastid,Menu.FIRST,"About us").setIcon(R.drawable.ic_menu_about);
        menu.add(2,lastid+1,Menu.FIRST,"Contact us").setIcon(R.drawable.ic_menu_contact);
        menu.add(2,lastid+2,Menu.FIRST,"Donation").setIcon(R.drawable.ic_menu_donation);
        menu.add(2,lastid+3,Menu.FIRST,"Share").setIcon(R.drawable.ic_menu_share);
        menu.add(2,lastid+4,Menu.FIRST,"Rate us").setIcon(R.drawable.ic_menu_rateus);
        menu.add(2,lastid+5,Menu.FIRST,"Logout").setIcon(R.drawable.ic_logout);

        menu.setGroupCheckable(0,true,true);
        menu.setGroupCheckable(1,true,true);
        menu.setGroupCheckable(2,true,true);
}

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            currentPage=vwpager.getCurrentItem();
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {


        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
}



