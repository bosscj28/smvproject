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
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class DetailPageAdapter extends PagerAdapter {

    Context context;
    ArrayList<Story> mstory;
    private LayoutInflater inflater;
    Typeface monstRegular,monstBold;
    Button share;


    public DetailPageAdapter(Context context, ArrayList<Story> story) {
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
        View v = inflater.inflate(R.layout.item_detail_activity,container,false);
        final Story current = mstory.get(position);
        ImageView img =(ImageView)v.findViewById(R.id.imgDetail);
        //TextView title  = (TextView)v.findViewById(R.id.titleDetail);
        TextView desc  = (TextView)v.findViewById(R.id.descDetail);
        share = (Button)v.findViewById(R.id.sharebutton);
        share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent shared = new Intent(Intent.ACTION_SEND);
                String shareSub = current._caption;
                shared.setType("text/plain");
                String body=current._url;
                shared.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                shared.putExtra(Intent.EXTRA_TEXT,body);
                context.startActivity(Intent.createChooser(shared,"Share via"));

            }
        });
       //title.setTypeface(monstBold);
        desc.setTypeface(monstRegular);

        Glide.with(context)
                .load(current._url)
                .into(img);
        //title.setText(current._caption);

        if (Build.VERSION.SDK_INT >= 24) {
            desc.setText(Html.fromHtml(current._desc,1)); // for 24 api and more
        } else {
            desc.setText(Html.fromHtml(current._desc)); // for for older api
        }


        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        container.refreshDrawableState();
    }
}
