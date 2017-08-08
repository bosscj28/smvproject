package com.example.asus.story;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ashutosh on 7/27/2017.
 */

public class SliderAdapter extends PagerAdapter {
    Context context;
    ArrayList<Story> mstory;
    private LayoutInflater inflater;
    ProgressBar progressBar;
    Typeface monstRegular,monstBold;
    private int i=10;
    private int progressStatus;
public SliderAdapter(){};

public SliderAdapter(Context context,ArrayList<Story> story){
    super();
    this.context = context;
    inflater= LayoutInflater.from(context);
    this.mstory = story;
    /*for(int i=0; i<story.size(); i++)
    {
        Log.d("ADAPTER STORY","DATA OF STORY "+story.get(i).getID());
    }*/
    monstRegular = Typeface.createFromAsset(this.context.getAssets(), "fonts/Montserrat-Regular.ttf");
    monstBold = Typeface.createFromAsset(this.context.getAssets(), "fonts/Montserrat-Bold.ttf");

}
    @Override
    public int getCount(){
        return mstory.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
    public Object instantiateItem(ViewGroup container, final int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.slideritem, container, false);
        final Story current = mstory.get(position);
        final LinearLayout SLinear=(LinearLayout)v.findViewById(R.id.Slinear);
        final ImageView img = (ImageView) v.findViewById(R.id.Simage);
        TextView title  = (TextView)v.findViewById(R.id.StextView);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    img.buildDrawingCache();
                    Bitmap bitmap = img.getDrawingCache();
                    ByteArrayOutputStream stream=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                    byte[] image=stream.toByteArray();
                    String img_str = Base64.encodeToString(image, 0);


                    if(position!=9){
                        Intent i = new Intent(context,DetailActivity.class);
                        i.putExtra("STORY",mstory);
                        Log.d("Position","position"+position);
                        i.putExtra("POSITION",position);
                        i.putExtra("CATEGORY_NAME","Popular");
                        context.startActivity(i);
                    }


                } catch (ClassCastException exception) {
                    exception.printStackTrace();
                }
            }
        });

        dataAdapter da=new dataAdapter();
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(7*da.getScreenWidth()/10,3*da.getScreenHeight()/10);
        img.setLayoutParams(params);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(context)
                .load(current._url)
                .into(img);
        title.setText(current._caption);
        title.setWidth(7*da.getScreenWidth()/10);
        title.setHeight(2*da.getScreenHeight()/20);
        title.setMaxLines(1);
        LinearLayout.LayoutParams params1=new LinearLayout.LayoutParams(7*da.getScreenWidth()/10,8*da.getScreenHeight()/20);
        SLinear.setLayoutParams(params1);

        container.addView(v);
        return v;
    }
    @Override
    public void destroyItem(View container, int position, Object object) {
        container.refreshDrawableState();
    }
   /* public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }*/
}
