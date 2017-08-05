package com.example.asus.story;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class dataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<Story> mstory;
    Typeface monstRegular,monstBold;
    CardView cardView;
    LinearLayout linearLayout;
    ImageView imageView;
    String CategoryName;
    private LayoutInflater inflater;

    public dataAdapter(){
    }

    public dataAdapter(Context context, ArrayList<Story> story,String CategoryName) {
        super();
        this.context = context;
        inflater= LayoutInflater.from(context);
        this.mstory = story;
        this.CategoryName=CategoryName;
        for(int i=0; i<story.size(); i++)
        {
            Log.d("ADAPTER STORY","DATA OF STORY "+story.get(i).getID());
        }
    }


    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                    .inflate(R.layout.liststrory, parent,false);

        cardView=(CardView)view.findViewById(R.id.card);
        cardView.setLayoutParams(new LinearLayout.LayoutParams(getScreenWidth()-getScreenWidth()/25,getScreenHeight()/8));
        linearLayout=(LinearLayout)view.findViewById(R.id.LL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(getScreenWidth(),2*getScreenHeight()/15));
        linearLayout.setPaddingRelative(20,0,20,0);
        //linearLayout.setVerticalFadingEdgeEnabled(true);
        //cardView.setRotationX(10);
        //cardView.setRotationY(1.4f);
        //linearLayout.setBackgroundColor(Color.rgb(204,204,255));
        imageView=(ImageView)view.findViewById(R.id.imgView);
        imageView.setLayoutParams(new  LinearLayout.LayoutParams(getScreenHeight()/8,getScreenHeight()/8));
        TextView txt = (TextView) view.findViewById(R.id.txtViewer);
        txt.setLayoutParams(new LinearLayout.LayoutParams((getScreenWidth()-20*getScreenWidth()/100),getScreenHeight()/14));
        TextView likes = (TextView) view.findViewById(R.id.likes);
        TextView comments = (TextView) view.findViewById(R.id.comments);
        monstRegular = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Regular.ttf");
        monstBold = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Bold.ttf");
        txt.setTypeface(monstBold);
        likes.setTypeface(monstRegular);
        comments.setTypeface(monstRegular);
        MyHolder holder=new MyHolder(view,context);
        return holder;
    }
    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        Story current = mstory.get(position);

        myHolder.id = current._id;
        myHolder.captionText.setText(current._caption);
        myHolder.descText = current._desc ;
        myHolder.url=current._url;
        //GET IMAGE FROM DB
        //myHolder.img.setImageBitmap(convertToBitmap(current.getImage()));

        // load image into imageview using glide from API (url)

        Glide.with(context)
                .load(current._url)
                .into(myHolder.img);

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return mstory.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView captionText;
        ImageView img;
        String url;
        String descText;
        Integer id;


        // create constructor to get widget reference
        public MyHolder(View itemView, Context context) {
            super(itemView);
            final Context context1 = context;
            captionText = (TextView) itemView.findViewById(R.id.txtViewer);
            img = (ImageView) itemView.findViewById(R.id.imgView);

            //descText = (TextView) itemView.findViewById(R.id.textView3);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        //String s=descText.toString();
                        String t=captionText.getText().toString();
                        img.buildDrawingCache();
                        Bitmap bitmap = img.getDrawingCache();
                        ByteArrayOutputStream stream=new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                        byte[] image=stream.toByteArray();
                        String img_str = Base64.encodeToString(image, 0);

                       /* Integer currID=id;
                        //Log.d("ID","CURRENT :::"+currID);
                        Intent i=new Intent(context1,popup.class);
                        i.putExtra("CURRENT_ID",currID);
                        context1.startActivity(i);
                       */
                       // POPUP CLASS
                        /*
                       Intent i=new Intent(context1,popup.class);
                        i.putExtra("title",descText);
                        i.putExtra("caption",t);
                        //i.putExtra("img",img_str);
                        Log.d("TAG",url);
                        i.putExtra("imageUri", url);
                        context1.startActivity(i);*/

                        Intent i = new Intent(context1,DetailActivity.class);
                        i.putExtra("STORY",mstory);
                        i.putExtra("POSITION",getLayoutPosition());
                        i.putExtra("CATEGORY_NAME",CategoryName);
                        context1.startActivity(i);


                    } catch (ClassCastException exception) {

                    }
                }
            });


        }

    }


    //get bitmap image from byte array

    private Bitmap convertToBitmap(byte[] b) {

            return BitmapFactory.decodeByteArray(b, 0, b.length);
    }
    public static int getScreenWidth() {
    return Resources.getSystem().getDisplayMetrics().widthPixels;
}

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}

