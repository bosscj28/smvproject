package com.example.asus.story;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.asus.story.R.id.list1;

/**
 * Created by asus on 6/29/2017.
 */

public class CommonFragment extends Fragment {

    private ProgressDialog pDialog;
    private dataAdapter data;
    private RecyclerView lv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView.LayoutManager mlayoutmanager;

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

        String message = getArguments().getString(EXTRA_MESSAGE);
        Log.d("MESSAGE","ALLSTORY"+ message);
       // TextView TV = (TextView) v1.findViewById(R.id.textView2);
        //TV.setText(message);

        lv = (RecyclerView) v1.findViewById(list1);
        mlayoutmanager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        lv.setLayoutManager(mlayoutmanager);
        lv.setOnFlingListener(new RecyclerViewSwipeListener(true) {
            @Override
            public void onSwipeDown() {

            }
            @Override
            public void onSwipeUp() {

            }
        });

        getData(message);

        return v1;
    }


    public void getData(String id)
    {
        // Tag used to cancel the request

        String url = "http://kookyapps.com/smv/api/FetchNews/";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("cat_id", id);
        params.put("upper_limit","0");
        params.put("lower_limit","2");

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            VolleyLog.v("Response:%n %s", response.toString(4));
                            Log.d("Response:%n %s", response.toString());
                            JSONArray news = response.getJSONArray("data");

                            final ArrayList<Story> story = new ArrayList<>();
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

                            }

                            data=new dataAdapter(context, story);
                            lv.setAdapter(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        // Adding request to request queue
       RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(req);
    }
}
