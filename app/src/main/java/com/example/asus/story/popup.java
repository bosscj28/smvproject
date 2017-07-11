package com.example.asus.story;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class popup extends Activity {

    boolean flag=true;
    int x=10;
    TextView textView,textView1,textView2;
    ImageView img,img2;
    Button b,share,like;
    Uri uri;
    Typeface monstRegular,monstBold;
    DatabaseHandler db;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);

        monstRegular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        monstBold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.ttf");
        TextView details = (TextView) findViewById(R.id.textView1);
        TextView cmmtvw = (TextView) findViewById(R.id.cmmtvw);
        textView = (TextView) findViewById(R.id.textView4);
        textView1 = (TextView) findViewById(R.id.textView5);
        textView2=(TextView)findViewById(R.id.likevw);
        b=(Button)findViewById(R.id.button1);
        share=(Button)findViewById(R.id.sharebutton);
        like=(Button)findViewById(R.id.likebutton);
        img = (ImageView) findViewById(R.id.imageView2);
        textView.setTypeface(monstBold);
        details.setTypeface(monstRegular);
        textView2.setTypeface(monstRegular);
        cmmtvw.setTypeface(monstRegular);
        textView1.setTypeface(monstRegular);
        //img2 = (ImageView) findViewById(R.id.imageView7);


        String s = getIntent().getStringExtra("title");
        String t=  getIntent().getStringExtra("caption");
        String i=  getIntent().getStringExtra("imageUri");
        textView.setText(t);
        textView1.setText(s);
        Picasso.with(this).load(i).into(img);
        /*String s = getIntent().getStringExtra("title");
        String t=  getIntent().getStringExtra("caption");
        final String i=  getIntent().getStringExtra("img");
        textView.setText(t);
        textView1.setText(s);

        byte[] decodedString = Base64.decode(i, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        img.setImageBitmap(decodedByte);*/

        // GET IT FROM PREV INTENT
  /*      Integer ID = getIntent().getIntExtra("CURRENT_ID",1);
        db = new DatabaseHandler(getApplicationContext());
        final ArrayList<Story> story = new ArrayList<>(db.getAllStoryByID(ID));
        textView.setText(story.get(0).getCaption());
        textView1.setText(story.get(0).getDesc());
        byte[] pic = story.get(0).getImage();
        Bitmap bp = convertToBitmap(pic);
        img.setImageBitmap(bp);
       Log.d("STORY","LIST"+ story.get(0).getID());

    */
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finish();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // uri = Uri.parse(i);
                Intent shared = new Intent(Intent.ACTION_SEND);
                String shareSub = "Your subject here";
                shared.setType("text/plain");
                String body="https://www.youtube.com/watch?v=7F37r50VUTQ";
                shared.putExtra(Intent.EXTRA_SUBJECT, shareSub);
            //  shared.putExtra(Intent.EXTRA_TEXT,uri);
                shared.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(shared,"Share via"));

            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                if(flag) {
                    flag=false;
                    ++x;
                    textView2.setText(+x + " likes");
                    like.setBackground(getResources().getDrawable(R.drawable.sv_detailedfeeds_13));
                    Toast.makeText(getApplicationContext(), "You liked this Image", Toast.LENGTH_SHORT).show();
                   }
                else{
                    flag=true;
                    --x;
                    textView2.setText(+x + " likes");
                    like.setBackground(getResources().getDrawable(R.drawable.sv_disliked));
                    Toast.makeText(getApplicationContext(), "You disliked this Image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private Bitmap convertToBitmap(byte[] b) {

        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }
}
