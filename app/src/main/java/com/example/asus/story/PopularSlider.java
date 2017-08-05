package com.example.asus.story;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class PopularSlider extends PagerAdapter {

    Context context;
    ArrayList<Story> mstory;
    private LayoutInflater inflater;
    Typeface monstRegular,monstBold;
    Button share;
    ProgressBar progressBar;

    public PopularSlider(Context context, ArrayList<Story> story) {
        super();
        this.context = context;
        inflater= LayoutInflater.from(context);
        this.mstory = story;
        for(int i=0; i<story.size(); i++)
        {
            Log.d("ADAPTER STORY","DATA OF STORY "+story.get(i).getID());
        }
        monstRegular = Typeface.createFromAsset(this.context.getAssets(), "fonts/Montserrat-Regular.ttf");
        monstBold = Typeface.createFromAsset(this.context.getAssets(), "fonts/Montserrat-Bold.ttf");
    }

    @Override
    public int getCount() {
        return mstory.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_popular_slider,container,false);
        final Story current = mstory.get(position);
        ImageView img =(ImageView)v.findViewById(R.id.imageView2);

        Glide.with(context)
                .load(current._url)
                .into(img);

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        container.refreshDrawableState();
    }
}
