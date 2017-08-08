package com.example.asus.story;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.asus.story.utils.Utils;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.asus.story.R.id.list1;
import static com.example.asus.story.R.id.swipyrefreshlayout;

public class CommonFragment extends Fragment {

    private ProgressDialog pDialog;
    private dataAdapter data;
    private RecyclerView lv;
    private SwipyRefreshLayout swipeRefreshLayout;
    private RecyclerView.LayoutManager mlayoutmanager;
    String upper,lower;
    int up,initLow;
    String CategoryName;
    boolean AppendDataFlag = false; // false - SetAdapter() and true - notifyDataSetChanged()
    String message;
    private ArrayList<Story> story;

    public CommonFragment(){

    }
    private Context context;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.context=context;
    }
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public static final CommonFragment newInstance(String message)
    {
        CommonFragment f = new CommonFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(EXTRA_MESSAGE, message);
        f.setArguments(bdl);
        return f;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v1 =inflater.inflate(R.layout.fragment_common, container, false);
        // Intial PARAMS
        message = getArguments().getString(EXTRA_MESSAGE);
        CategoryName = ((MainActivity) getActivity()).categoryList.get(Integer.parseInt(message)-1).get("name");
        upper = "0";
        lower = "5";
        initLow = Integer.parseInt(lower);
        story = new ArrayList<>();
        lv = (RecyclerView) v1.findViewById(list1);
        mlayoutmanager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        lv.setLayoutManager(mlayoutmanager);
        swipeRefreshLayout = (SwipyRefreshLayout) v1.findViewById(swipyrefreshlayout);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorAccent);
        //ON CREATE FRAGMENT CALL DATA
        swipeRefreshLayout.setRefreshing(true);
        if(isOnline(context))
        {
            getData(message,upper,lower);
            swipeRefreshLayout.setRefreshing(false);
        }
        else
        {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(context, "Internet is not Connected", Toast.LENGTH_LONG).show();
        }



        swipeRefreshLayout.setDistanceToTriggerSync(100);
        swipeRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if(direction == SwipyRefreshLayoutDirection.TOP)
                {
                    Log.d("CJ VAL","UPPER"+upper);
                    if(isOnline(context))
                    {
                        AppendDataFlag = false;
                        upper = "0";
                        getData(message,upper,lower);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                        else
                    {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(context, "Internet is not Connected", Toast.LENGTH_LONG).show();
                    }
                }
                if(direction == SwipyRefreshLayoutDirection.BOTTOM)
                {
                    if(isOnline(context))
                    {
                        AppendDataFlag = true;
                        up = Integer.parseInt(upper);
                        up = up + initLow;
                        upper = String.valueOf(up);
                        Log.d("MESS" + message, "UPP" + upper);
                        Log.d("LOWER", "Low" + lower);
                        getData(message,upper,lower);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    else
                    {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(context, "Internet is not Connected", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return v1;

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
    public void getData(String id, String upperparam, String lowerparam)
    {
        // Tag used to cancel the request

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

                                if(upper.equals("0")){
                                    story.clear();
                                    Log.d("CJ STORY","M CLEARED");
                                }
                            for (int i = 0; i < news.length(); i++) {
                                JSONObject s = news.getJSONObject(i);
                                Story storyobj = new Story();

                                int n = Integer.parseInt(s.get("n_id").toString());
                                storyobj.setID(n);
                                storyobj.setcaption(s.getString("n_title"));
                                storyobj.setdesc(s.getString("n_desc"));
                                storyobj.setCat_id(s.getString("n_cat"));
                                storyobj.setUrl(s.getString("n_img"));
                                storyobj.setEmail(s.getString("n_email"));

                                story.add(storyobj);
                                Log.d("CJ SIZE","STORY SIZE - "+story.size());


                            }

                            data=new dataAdapter(context, story,CategoryName);
                            if (AppendDataFlag)
                            {
                                Log.d("CJ DATA FLAG","M APPENDED");
                               data.notifyDataSetChanged();

                            }
                            else {
                                Log.d("CJ DATA FLAG","M UPDATED");
                                lv.setAdapter(data);
                            }



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

       RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(req);
    }

}
