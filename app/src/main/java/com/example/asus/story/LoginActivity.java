package com.example.asus.story;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketOption;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.SocketHandler;

import static com.facebook.login.LoginBehavior.NATIVE_WITH_FALLBACK;

public class LoginActivity extends AppCompatActivity {
    Typeface yellowtail,monstRegular;
    private CallbackManager callbackManager;
    private String facebook_id,f_name,m_name,l_name,gender,profile_image,email,contact,country;
    private LoginButton lb;
    ProgressDialog pd;
    SharedPreferences sharedPreProfile;
    public static final String REGISTER_URL = "http://kookyapps.com/smv/api/fblogin/";
    public static final String KEY_FB_ID = "fb_id";
    public static final String KEY_FNAME = "fb_name";
    public static final String KEY_LNAME = "fb_lname";
    public static final String KEY_IMG = "fb_img";
    public static final String KEY_CONTACT = "fb_contact";
    public static final String KEY_EMAIL = "fb_email";
    public static final String KEY_COUNTRY = "fb_country";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebook_id=f_name=m_name=l_name=gender=profile_image=contact=email=country="";
        FacebookSdk.sdkInitialize( getApplication());
        AppEventsLogger.activateApp(this);
        callbackManager= CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        yellowtail = Typeface.createFromAsset(getAssets(), "fonts/Yellowtail-Regular.ttf");
        monstRegular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
        TextView welcome = (TextView) findViewById(R.id.welcome);
        TextView to = (TextView) findViewById(R.id.to);
        TextView EK = (TextView) findViewById(R.id.EK);
        welcome.setTypeface(monstRegular);
        to.setTypeface(monstRegular);
        EK.setTypeface(yellowtail);

        sharedPreProfile = getSharedPreferences("Logged_in_user",MODE_PRIVATE);

        lb = (LoginButton)findViewById(R.id.button);
        lb.setLoginBehavior(NATIVE_WITH_FALLBACK);
        lb.setReadPermissions(Arrays.asList("public_profile","email"));
        //lb.setReadPermissions(Arrays.asList("user_location"));
        //lb.setReadPermissions("user_location");
        lb.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            ProfileTracker mProfileTracker;
            @Override
            public void onSuccess(LoginResult loginResult) {
                String accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);
                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.i("LoginActivity", response.toString());
                                // Get facebook data from login
                                Bundle bFacebookData = getFacebookData(object);
                                facebook_id = bFacebookData.getString("idFacebook");
                                f_name = bFacebookData.getString("first_name");
                                l_name = bFacebookData.getString("last_name");
                                profile_image = bFacebookData.getString("profile_pic");
                                contact = bFacebookData.getString("contact");
                                email = bFacebookData.getString("email");
                                country = bFacebookData.getString("location");
                                Log.d("PARAM DATA","USER 1"+facebook_id);
                                Log.d("PARAM DATA","USER 2"+f_name);
                                Log.d("PARAM DATA","USER 3"+l_name);
                                Log.d("PARAM DATA","USER 4"+profile_image);
                                Log.d("PARAM DATA","USER 5"+contact);
                                Log.d("PARAM DATA","USER 6"+email);
                                Log.d("PARAM DATA","USER 7"+country);
                                if(contact == null) contact="123";
                                if(country == null) country="123";
                                RegisterUser();
                            }

                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();
            }
            @Override
            public void onCancel() {
                Log.d("A","HEHHEEHEH");
                //email.setText("CANCELED");
            }
            @Override
            public void onError(FacebookException error) {
                //email.setText("error");
                Log.d("ERROR","FB ERR"+error);
            }
        });
        //if (!facebook_id.equals("") && facebook_id==null)
        //RegisterUser();
    }
    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();
        try {

            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=400&height=400");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));
        }
        catch(JSONException e) {
            Log.d("TAG","Error parsing JSON");
        }
        return bundle;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("CJ", "onActivityResult called requestCode "+requestCode+" resultCode "+resultCode);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void RegisterUser(){

        pd = ProgressDialog.show(LoginActivity.this, "", "Verifying user", true, false);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(KEY_FB_ID,facebook_id);
        params.put(KEY_FNAME,f_name);
        params.put(KEY_LNAME,l_name);
        params.put(KEY_IMG,profile_image);
        params.put(KEY_CONTACT,contact);
        params.put(KEY_EMAIL,email);
        params.put(KEY_COUNTRY,country);

        Log.d("PARAM DATA 1","USER 1"+facebook_id);
        Log.d("PARAM DATA 1","USER 2"+f_name);
        Log.d("PARAM DATA 1","USER 3"+l_name);
        Log.d("PARAM DATA 1","USER 4"+profile_image);
        Log.d("PARAM DATA 1","USER 5"+contact);
        Log.d("PARAM DATA 1","USER 6"+email);
        Log.d("PARAM DATA 1","USER 7"+country);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,REGISTER_URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            Log.d("Response:%n %s", response.toString(4));

                            int responseFlag = response.getInt("response");
                            String message = response.getString("message");

                            pd.dismiss();
                            if(responseFlag == 1)
                            {
                                JSONObject jobj = response.getJSONObject("data");
                                Log.d("JSOS","JSON"+jobj.get("user_img"));
                                String id = jobj.getString("user_id");
                                String fname = jobj.getString("user_name");
                                String lname = jobj.getString("user_lname");
                                String img = jobj.getString("user_img");
                                String contact = jobj.getString("user_contact");
                                String email = jobj.getString("user_email");
                                String country = jobj.getString("user_country");
                                String fb_id = jobj.getString("user_fb_id");

                                SharedPreferences.Editor editor = sharedPreProfile.edit();
                                editor.putString("Logged_user_img",img);
                                editor.putString("Logged_user_fb_id",fb_id);
                                editor.putString("Logged_user_id",id);
                                editor.putString("Logged_user_name",fname+' '+lname);
                                editor.putString("Logged_user_email",email);
                                editor.apply();
                            }
                            if(responseFlag == 1 && message.equals("Account create Successfully")) {
                                //Toast.makeText(LoginActivity.this, "User Registered Successful", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                            else if(responseFlag == 1 && message.equals("Login Success")){
                                //Toast.makeText(LoginActivity.this, "User Verified", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "User Regsisteration Failed! Try Again", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                pd.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(req);
    }
}
