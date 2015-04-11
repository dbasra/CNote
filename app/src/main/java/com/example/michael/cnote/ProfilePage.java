package com.example.michael.cnote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;


public class ProfilePage extends android.support.v4.app.Fragment {
    private TextView firstTxtView;
    private ImageView profilePic;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    private TextView progressNum;
    private Handler mHandler;

    private RatingBar mStar;
    RoundImage roundedImage;

    String changedPic;
    ListView editPListView;
    ListView myClasses;
    Button viewNotes, editProfile;
    ArrayAdapter<String> adapter;
    ArrayAdapter<CharSequence> adapterEdit;
    ArrayList<String> listMyClasses;
    TextView welcome, posts;

    PopupWindow popUp;

    private Firebase myFirebaseRef, ref, baseRef, fBaseRef;
    String key;
    ArrayList<String> classes;
    String viewFeed;
    View rootview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_profile_page, container,false);
        fBaseRef = new Firebase(ImportantMethods.FIREBASE_URL);
        String f = getActivity().getIntent().getExtras().getString("firstName");
        String l = getActivity().getIntent().getExtras().getString("lastName");
        String image = getActivity().getIntent().getExtras().getString("profilePic");
        firstTxtView = (TextView) rootview.findViewById(R.id.textView4);
                firstTxtView.setText(f + " " + l);

        profilePic = (ImageView) rootview.findViewById(R.id.imageView3);

//        mProgress = (ProgressBar) findViewById(R.id.progressBar);
//        progressNum = (TextView) findViewById(R.id.levelNum);
        mProgress.setMax(200);

        // mProgress.setProgress(ImportantMethods.myLevel(fBaseRef)); // get info from dataBase
        // progressNum.setText(mProgress.toString());
        mHandler = new Handler();

//        RatingBar ratingBar = (RatingBar) rootview.findViewById(R.id.ratingBar);
//        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
//        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        callParentMethod();

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.profile_picture);
        roundedImage = new RoundImage(bm);
        profilePic.setImageDrawable(roundedImage);
        Bitmap bpImage = StringToBitMap(image);
        Bitmap resized = Bitmap.createScaledBitmap(bpImage, 200, 200, true);
        Bitmap conv_bm = getRoundedRectBitmap(resized, 200);

        // RoundImage roundedImagePp = new RoundImage(image);

        profilePic.setImageBitmap(conv_bm);
        return rootview;
    }

    private void callParentMethod() {
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getActivity().getMenuInflater().inflate(R.menu.menu_profile_page, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
        Bitmap result  = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        try {

            Canvas canvas = new Canvas(result);

            int color = 0xff424242;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            RectF rectF = new RectF(rect);
            int roundPx = pixels;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
        } catch (NullPointerException e) {
// return bitmap;
        } catch (OutOfMemoryError o) {
        }
        return result;
    }

    public void changeProfilePic() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setTitle("Edit Profile");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Change Profile Picture");
        arrayAdapter.add("Change Menu Picture");
        arrayAdapter.add("Change Theme");
        alertDialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialogBuilder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String strName = arrayAdapter.getItem(which);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(getActivity());
                builderInner.setMessage(strName + "?");
                builderInner.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                if (strName.equals("Change Profile Picture")){
                                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    // if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                    // }
                                }
                            }
                        });
                builderInner.show();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
