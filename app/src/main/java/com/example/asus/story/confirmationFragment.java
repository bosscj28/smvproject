package com.example.asus.story;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class confirmationFragment extends Fragment {
    public static final String REGISTER_URL = "http://kookyapps.com/smv/api/uploadNews/";
    public static final String OTP_URL = "http://kookyapps.com/smv/api/verifyOtp/";

    public static final String KEY_TITLE = "n_title";
    public static final String KEY_DESC = "n_desc";
    public static final String KEY_CAT = "n_cat";
    public static final String KEY_IMG = "n_img";
    public static final String KEY_EMAIL = "n_email";
    int responseFlag = 0,OtpresponseFlag = 0;
    LinearLayout rl;
    public EditText email,otp;
    Button otpButton;
    TextView upload;
    private DatabaseHandler db;
    private ProgressDialog pd;

    public confirmationFragment(){
    }
    private Context context;
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.context=context;
        db = new DatabaseHandler(context);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v3 = inflater.inflate(R.layout.confirmation_frag , container, false);
        upload = (TextView) v3.findViewById(R.id.upload);
        otpButton = (Button) v3.findViewById(R.id.otpbtn);
        rl = (LinearLayout) v3.findViewById(R.id.rl3);
        email = (EditText) v3.findViewById(R.id.email);
        otp = (EditText) v3.findViewById(R.id.otp);
        Typeface monstRegular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-Regular.ttf");
        email.setTypeface(monstRegular);
        email.getText().equals("");
        otp.setTypeface(monstRegular);
        upload.setTypeface(monstRegular);
        otpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(responseFlag == 1) {
                    Toast.makeText(getActivity(),
                            "Already Uploaded, Please check your email for OTP!",
                            Toast.LENGTH_LONG).show();
                }
                else
                    {
                    if (email.getText().toString().trim().equals("")) {
                        email.setError("Email is Required");
                    } else {
                        String em = email.getText().toString();
                        ((Main2Activity) getActivity()).storysaver.setEmail(em);

                        if (isOnline(getContext())) {
                            //new DataSendAsyncTask().execute();
                            send();

                        } else {
                            Toast.makeText(getActivity(),
                                    "Please check your internet connection!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DB OPERATION
              /*  String title =  ((Main2Activity)getActivity()).maindatasaver.getStoryTitle();
                String desc =  ((Main2Activity)getActivity()).maindatasaver.getStoryDesc();
                byte[] img = ((Main2Activity)getActivity()).maindatasaver.getStoryImg();
                long cid = ((Main2Activity)getActivity()).maindatasaver.get_c_id();
                long sid = db.addstory(new Story(title, img, desc));
                long tid = db.createTransaction(sid,cid);
               */

                  if(otp.getText().toString().trim().equals(""))
                  {
                        Toast.makeText(getContext(), "Otp Please!", Toast.LENGTH_LONG).show();
                        otp.setError("Otp Required");
                  }
                  else
                  {
                      if (isOnline(getContext())) {
                          if(responseFlag == 1) {
                              verifyOtp();
                          }
                          else
                          {
                              Toast.makeText(getContext(), "Generate Otp Please ! ", Toast.LENGTH_LONG).show();
                          }

                      } else {
                      Toast.makeText(getActivity(),
                              "Please check your internet connection!",
                              Toast.LENGTH_LONG).show();
                      }

                  }
            }
        });

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.confirmation_frag, container, false);
        return v3;
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


    private void send(){

        String title = ((Main2Activity) getActivity()).storysaver.getCaption();
        String desc = ((Main2Activity) getActivity()).storysaver.getDesc();
        String cat = ((Main2Activity) getActivity()).storysaver.getCat_id();
        String img = ((Main2Activity) getActivity()).storysaver.getUrl();
        String email = ((Main2Activity) getActivity()).storysaver.getEmail();

        pd = ProgressDialog.show(getContext(), "", "Uploading Data", true, false);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(KEY_TITLE,title);
        params.put(KEY_DESC,desc);
        params.put(KEY_CAT,cat);
        params.put(KEY_IMG,img);
        params.put(KEY_EMAIL,email);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,REGISTER_URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            Log.d("Response:%n %s", response.toString(4));
                            responseFlag = response.getInt("response");
                            pd.dismiss();
                            if(responseFlag == 1) {
                                Toast.makeText(getActivity(), "Otp generated Check your Email", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(getActivity(), "OTP Generated Error ! Try Again", Toast.LENGTH_LONG).show();
                            }
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(req);
    }

    private void verifyOtp(){

            String email = ((Main2Activity) getActivity()).storysaver.getEmail();
            String otpParam = otp.getText().toString();

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("n_email", email);
            params.put("n_otp", otpParam);


        pd = ProgressDialog.show(getContext(), "", "Verfying Otp", true, false);

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, OTP_URL, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                VolleyLog.v("Response:%n %s", response.toString(4));
                                Log.d("Response:%n %s", response.toString(4));
                                OtpresponseFlag = response.getInt("response");
                                pd.dismiss();
                                if(OtpresponseFlag == 1)
                                {
                                    Toast.makeText(getActivity(), "OTP verified", Toast.LENGTH_SHORT).show();
                                    getActivity().startActivity(new Intent(getActivity(),MainActivity.class));
                                    getActivity().finish();
                                    //overridePendingTransition( R.anim.lefttoright, R.anim.stable );
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "OTP Verification Failed ! Try Again", Toast.LENGTH_SHORT).show();
                                }
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
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(req);
    }

}