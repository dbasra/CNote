package com.example.michael.cnote;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Michael on 4/11/2015.
 */
public class MainFragment extends Fragment {

    private String firstName;
    private String lastName;
    private String key;

    final Firebase myRef = new Firebase("https://cnotes.firebaseio.com");

    String posts = String.valueOf(0);
    String level = String.valueOf(0);
    String starRating = String.valueOf(0);
    Uri imageBitmap1;
    Bitmap bmp;

    private CallbackManager mCallBackManager;
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            ArrayList<String> classes = new ArrayList<>();
            classes.add("blank");
            ArrayList<String> classMates = new ArrayList<>();
            classMates.add("blank");
            ArrayList<String> pinnedNotes = new ArrayList<>();
            pinnedNotes.add("blank");
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
           if (profile != null) {
               key = profile.getId();
               firstName = profile.getFirstName();
               lastName = profile.getLastName();
               imageBitmap1 = profile.getProfilePictureUri(100,100);

               HashMap<String, Object> user = new HashMap<String, Object>();
               user.put("firstName", firstName);
               user.put("lastName", lastName);
               user.put("key", key);
               user.put("Number of Posts" , posts);
               user.put("Level" , level);
               user.put("Rating", starRating);
               user.put("Classes", classes);
               user.put("Classmates", classMates);
               user.put("PinnedNotes", pinnedNotes);
               user.put("p", imageBitmap1.toString());

               Firebase userRef = myRef.child("Users/" + key);
               userRef.setValue(user);

               Intent goProfilePage = new Intent(getActivity(), MenuActivity.class);
               goProfilePage.putExtra("firstName", firstName);
               goProfilePage.putExtra("lastName", lastName);
               goProfilePage.putExtra("profilePic", imageBitmap1.toString());
               startActivity(goProfilePage);
           }
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    public MainFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplication());
        mCallBackManager = CallbackManager.Factory.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallBackManager,mCallback);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallBackManager.onActivityResult(requestCode,resultCode,data);
    }
}
