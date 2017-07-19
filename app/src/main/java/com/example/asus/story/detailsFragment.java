package com.example.asus.story;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.story.multispinner.MultiSpinnerSearch;
import com.example.asus.story.multispinner.KeyPairBoolData;
import com.example.asus.story.multispinner.SpinnerListener;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class detailsFragment extends Fragment {
    TextView next1;
    EditText title,desc;
    ImageView img;
    private Uri resultUri = null;
    private DatabaseHandler db;
    MultiSpinnerSearch spinner;
    ArrayList<Categories> cat;
    Typeface monstRegular,monstBold;
    String imagefilePath = null;
    private int selected = 0;
    private String UploadedUrl;
    private ProgressDialog pd;
    boolean FTPstatus,uploaded;
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {

            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (msg.what == 0) {
            }
            else if (msg.what == 1) {

            } else if (msg.what == 2) {
                //Toast.makeText(getContext(), "Uploaded Successfully!", Toast.LENGTH_LONG).show();
            } else if (msg.what == 3) {
                Toast.makeText(getContext(), "Disconnected Successfully!",
                        Toast.LENGTH_LONG).show();
            }  else if (msg.what == 4) {
                Toast.makeText(getContext(), "Failed Uploading!",
                        Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getContext(), "Unable to Perform Action!",
                        Toast.LENGTH_LONG).show();
            }

        }

    };


    public detailsFragment(){

    }
    private Context context;
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.context=context;
        /*db = new DatabaseHandler(context);
         cat = new ArrayList<>(db.getAllCategory());
        Log.d("ARR","ARR"+cat);*/

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v1 = inflater.inflate(R.layout.details_frag , container, false);
        next1 = (TextView) v1.findViewById(R.id.next1);
        title = (EditText) v1.findViewById(R.id.title);
        desc = (EditText) v1.findViewById(R.id.desc);
        img = (ImageView)getActivity().findViewById(R.id.upimg);
        monstRegular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-Regular.ttf");
        monstBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-Bold.ttf");
        next1.setTypeface(monstRegular);
        title.setTypeface(monstRegular);
        desc.setTypeface(monstRegular);
        next1.setCompoundDrawablePadding(10);
        spinner = (MultiSpinnerSearch) v1.findViewById(R.id.spinner);

        View Vsearch = inflater.inflate(R.layout.alert_dialog_listview_search , container, false);
        EditText searchspinner = (EditText) Vsearch.findViewById(R.id.alertSearchEditText);
        TextView titlecat = (TextView) Vsearch.findViewById(R.id.textView3);
        titlecat.setTypeface(monstRegular, Typeface.BOLD);
        searchspinner.setTypeface(monstRegular);

        final List<KeyPairBoolData> listArray0 = new ArrayList<>();

        for (int i = 0; i < ((Main2Activity) getActivity()).catlist.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            String id = ((Main2Activity) getActivity()).catlist.get(i).get("id");
            long l = Long.parseLong(id);
            h.setId(l);
            h.setName(((Main2Activity) getActivity()).catlist.get(i).get("name"));
            h.setSelected(true);
            selected++;
            spinner.SetSelected(selected);
            listArray0.add(h);
        }


        spinner.setItems(listArray0, -1, new SpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        Log.d("DETAIL FRAGMENT", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                    }
                }
            }
        });

        /*
        spinner.setLimit(2, new MultiSpinnerSearch.LimitExceedListener() {
            @Override
            public void onLimitListener(KeyPairBoolData data) {
                Toast.makeText(getActivity(),
                        "Limit exceed ", Toast.LENGTH_LONG).show();
            }
        });
        */


        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NEXT FRAGMENT
                    Log.d("DTA", "SAVER" + ((Main2Activity) getActivity()).maindatasaver);
                List<Long> selCat = new ArrayList<>();
                selCat = spinner.getSelectedIds();
                String catids = null;
                for (int i = 0; i < selCat.size(); i++) {
                    Log.d("CAT","SELECTED"+selCat.get(i));
                    if(i==0)
                    {
                        catids = selCat.get(i).toString();
                    }
                    else
                    {
                        catids = catids+","+selCat.get(i).toString();
                    }
                }
                Log.d("CAT","SELECTED"+catids);

                if (img.getDrawable() == null || resultUri == null) {
                        Toast.makeText(getActivity(), "Picture Please", Toast.LENGTH_SHORT).show();
                        img.invalidate();
                    } else if (title.getText().toString().trim().equals("")) {
                        Toast.makeText(getActivity(), "Title Please", Toast.LENGTH_SHORT).show();
                        title.setError("Title is required!");
                    } else if (desc.getText().toString().trim().equals("")) {
                        Toast.makeText(getActivity(), "Description Please", Toast.LENGTH_SHORT).show();
                        desc.setError("Desc is required!");
                    } else if (catids == null) {
                        Toast.makeText(getActivity(), "Select Category Please", Toast.LENGTH_SHORT).show();
                    }
                    else if (imagefilePath == null) {
                        Toast.makeText(getActivity(), "ImagePath is Empty", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String Savetitle, Savedesc;
                        Savetitle = title.getText().toString();
                        Savedesc = desc.getText().toString();
                         Log.d("CJ NEXT CLICK","IMAGE - "+imagefilePath);
                        ((Main2Activity) getActivity()).storysaver.setcaption(Savetitle);
                        ((Main2Activity) getActivity()).storysaver.setdesc(Savedesc);
                        ((Main2Activity) getActivity()).storysaver.setPath(imagefilePath);
                        ((Main2Activity) getActivity()).storysaver.setCat_id(catids);

                        new ImageUploadAsyncTask().execute();
                        //Navigate


                        /* WHEN STORING IN DB
                        byte[] inputData = null;
                        try {
                            InputStream iStream = getActivity().getContentResolver().openInputStream(resultUri);
                            inputData = getBytes(iStream);
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d("PHOTO", "PIC" + inputData);
                        if (inputData != null) {
                            Savetitle = title.getText().toString();
                            Savedesc = desc.getText().toString();
                            String selected_cat = spinner.getSelectedItem().toString();
                            long cid = db.getCategoryId(selected_cat);

                            ((Main2Activity) getActivity()).maindatasaver.setStoryData(Savetitle, Savedesc, inputData);
                            ((Main2Activity) getActivity()).maindatasaver.set_cat_id(cid);
                        }*/


                    }


            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
               selectImage();
            }
        });


        //return inflater.inflate(R.layout.details_frag, container, false);

        return v1;
    }
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void selectImage() {
        Uri uri = null;
        CropImage.activity(uri)
                .setAspectRatio(1,1)
                .setFixAspectRatio(true)
                .setAutoZoomEnabled(true)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityTitle("Upload Picture")
                .start(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                img.setImageURI(resultUri);
                imagefilePath = takeScreenshotAndSave(img);
                Log.d("TAG","imagePath::"+imagefilePath);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private String takeScreenshotAndSave(View u){
        u.setDrawingCacheEnabled(true);
        u.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(u.getDrawingCache());
        u.setDrawingCacheEnabled(false);

        //Save bitmap
        File file = new File(Environment.getExternalStorageDirectory()
                .getPath(), "SMV Folder");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating Image folder");
            }
        }
        String uriSting = (file.getAbsolutePath() + "/"
                + System.currentTimeMillis() + ".jpg");

        File myPath = new File(uriSting);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), b, "Screen", "screen");
            return uriSting;
        }catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }


    //Convert bitmap to bytes
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private byte[] profileImage(Bitmap b) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();

    }
    private class ImageUploadAsyncTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog pd;
        MyFTPClientFunctions mFtpClient = new MyFTPClientFunctions();


        @Override
        protected void onPreExecute() {
            //Create progress dialog here and show it
            pd = ProgressDialog.show(getContext(), "", "Image Uploading", true, false);
        }

        @Override
        protected Void doInBackground(Void... params) {

            FTPstatus = mFtpClient.ftpConnect("166.62.28.118","smv@kookyapps.com","kooky_smv",21);
            if (FTPstatus) {
                //Log.d("TAG", "Connection Success");
                String getCurrentDir = mFtpClient.ftpGetCurrentWorkingDirectory();
                //Log.d("TAG", "CURRENT DIR"+getCurrentDir);
                //Log.d("TAG", "FILE PATH"+imagefilePath);
                String newname = System.currentTimeMillis() + ".jpg";
                UploadedUrl = "http://kookyapps.com/smv/uploads/"+newname;
                // Log.d("URL","URL - "+url);

                String imagefilePath = ((Main2Activity) getActivity()).storysaver.getPath();
                Log.d("CJ PATH","IMAGE - "+imagefilePath);
                uploaded = mFtpClient.ftpUploadImage(imagefilePath,newname,"/",getActivity());
                if(uploaded) {
                    Log.d("UPLOAD","YES"); handler.sendEmptyMessage(2);
                    ((Main2Activity) getActivity()).storysaver.setUrl(UploadedUrl);
                }
                else{Log.d("UPLOAD","NO"); handler.sendEmptyMessage(4);}

                mFtpClient.ftpDisconnect();

            }else {
                handler.sendEmptyMessage(-1);
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pd.dismiss();
            if(FTPstatus) {
                if (uploaded) {
                    ((Main2Activity) getActivity()).viewPager.setCurrentItem(1, true);
                }
            }

        }
    }

}
  /*  private void connectToFTPAddress() {
        mFtpClient = new MyFTPClientFunctions();
        pd = ProgressDialog.show(getContext(), "", "Uploading...",
                true, false);
        new Thread(new Runnable() {
            public void run() {
                FTPstatus = mFtpClient.ftpConnect("166.62.28.118","smv@kookyapps.com","kooky_smv",21);
                if (FTPstatus) {
                    boolean uploaded = false;
                    //Log.d("TAG", "Connection Success");
                    String getCurrentDir = mFtpClient.ftpGetCurrentWorkingDirectory();
                    //Log.d("TAG", "CURRENT DIR"+getCurrentDir);

                    //Log.d("TAG", "FILE PATH"+imagefilePath);
                    String newname = System.currentTimeMillis() + ".jpg";
                    UploadedUrl = "kookyapps.com/smv/uploads/"+newname;
                   // Log.d("URL","URL - "+url);

                    uploaded = mFtpClient.ftpUploadImage(imagefilePath,newname,"/",getActivity());
                    if(uploaded) {Log.d("UPLOAD","YES"); handler.sendEmptyMessage(2);}
                    else{Log.d("UPLOAD","NO"); handler.sendEmptyMessage(4);}

                    mFtpClient.ftpDisconnect();

                } else {
                    handler.sendEmptyMessage(-1);
                }
            }
        }).start();
    } */



