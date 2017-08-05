package com.example.asus.story;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Ashutosh on 8/1/2017.
 */

public class commentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public
    Context context;
    ImageView profileimg;
    TextView name,cmnt;
    ArrayList<Comment> mcomments;
    boolean isTextViewClicked = false;
    private LayoutInflater inflater;
    public commentAdapter(){
    }
    public commentAdapter(Context context, ArrayList<Comment> comments) {
        super();
        this.context = context;
        inflater= LayoutInflater.from(context);
        this.mcomments = comments;
        for(int i=0; i<comments.size(); i++)
        {
            //Log.d("ADAPTER COMMENT","DATA OF COMMENT "+comments.get(i).getID());
        }
    }
    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.comment_item, parent,false);
        profileimg=(ImageView)view.findViewById(R.id.userImgComment);
        name=(TextView)view.findViewById(R.id.textView6);
        cmnt=(TextView)view.findViewById(R.id.textView5);
        MyHolder holder=new MyHolder(view,context);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        Comment current = mcomments.get(position);

        myHolder.id = current._userid;
        myHolder.name.setText(current._name);
        myHolder.comment.setText(current._comment);
        myHolder.url=current._url;
        Glide.with(context)
                .load(current._url)
                .into(myHolder.img);
    }

    @Override
    public int getItemCount() {
        return mcomments.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{

        TextView name,comment;
        ImageView img;
        Integer id;
        String url;

        // create constructor to get widget reference
        public MyHolder(View itemView, Context context) {
            super(itemView);
            final Context context1 = context;
            name=(TextView)itemView.findViewById(R.id.textView6);
            comment=(TextView)itemView.findViewById(R.id.textView5);
            img = (ImageView)itemView.findViewById(R.id.userImgComment);
            //descText = (TextView) itemView.findViewById(R.id.textView3);
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isTextViewClicked){
                        //This will shrink textview to 2 lines if it is expanded.
                        cmnt.setMaxLines(2);
                        isTextViewClicked = false;
                    } else {
                        //This will expand the textview if it is of 2 lines
                        cmnt.setMaxLines(Integer.MAX_VALUE);
                        isTextViewClicked = true;
                    }
                }
            });


        }

    }

}
