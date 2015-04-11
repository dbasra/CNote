package com.example.michael.cnote;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Michael on 4/11/2015.
 */
public class ImportantMethods {
    public final static String FIREBASE_URL = "https://cnotes.firebaseio.com/";


    public static ArrayList<String> numbers;

    public final static ArrayList<String> COURSES = getCourses(new Firebase(FIREBASE_URL));
    public static ArrayList<String> COURSENUMBERS = getCourseNumbers(new Firebase(FIREBASE_URL));

    private static ArrayList<String> getCourseNumbers(Firebase ref) {
        numbers = new ArrayList<>();
        String uniqueID = uid(ref);
        STATICREF = ref.child("Courses/");
        STATICREF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(String course : COURSES){
                    numbers.addAll( (ArrayList)dataSnapshot.child(course).getValue());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return numbers;
    }

    public static ArrayList<String> getCourses(Firebase ref){
        final ArrayList<String> classes = new ArrayList<>();
        classes.add("Computer Science");
        classes.add("Mathematics");
        classes.add("English");
        classes.add("Music");
        return classes;
    }

    private static String FNAME = "", LNAME = "", STATUS = "", UID = "", EMAIL = "",
            PASSWORD = "", POSTS = "" , LEVEL = "", STARRATTING = "", PROFILEPIC = "";
    private static int numOfPosts, currentLevel, currentRating;
    private static Firebase STATICREF, STATICMYREF;

    public static String uid(Firebase ref) {
        UID = (String) ref.getAuth().getProviderData().get("email");
        UID = (UID.substring(0, UID.indexOf("@")) +
                (UID.substring((UID.indexOf("@") + 1), (UID.indexOf(".")))));
        return UID;
    }

    public static String firstName(Firebase ref) {

        String uniqueID = uid(ref);
        STATICREF = ref.child("Users/" + uniqueID + "/");
        STATICREF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FNAME = dataSnapshot.child("firstName").getValue().toString();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return FNAME;
    }

    public static String lastName(Firebase ref) {

        String uniqueID = uid(ref);
        ref = ref.child("Users/" + uniqueID + "/lastName");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LNAME = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return LNAME;
    }

    public static String status(Firebase ref) {

        String uniqueID = uid(ref);
        ref = ref.child("Users/" + uniqueID + "/status");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                STATUS = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return STATUS;
    }

    public static String email(Firebase ref) {

        String uniqueID = uid(ref);
        ref = ref.child("Users/" + uniqueID + "/email");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                EMAIL = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return EMAIL;
    }

    public static String password (Firebase ref) {

        String uniqueID = uid(ref);
        ref = ref.child("Users/" + uniqueID + "/password");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PASSWORD = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return PASSWORD;
    }

    public static int numberOfPost(Firebase ref) {

        String uniqueID = uid(ref);
        ref = ref.child("Users/" + uniqueID + "/Number of Posts");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                POSTS = (String) dataSnapshot.getValue();
                numOfPosts = Integer.valueOf(POSTS);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return numOfPosts;
    }

    public static int myLevel(Firebase ref) {

        String uniqueID = uid(ref);
        ref = ref.child("Users/" + uniqueID + "/Level");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LEVEL = (String) dataSnapshot.getValue();
                currentLevel = Integer.valueOf(LEVEL);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return currentLevel;
    }

    public static int myRating(Firebase ref) {

        String uniqueID = uid(ref);
        ref = ref.child("Users/" + uniqueID + "/Rating");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                STARRATTING = (String) dataSnapshot.getValue();
                currentRating = Integer.valueOf(STARRATTING);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return currentRating;
    }

    public static String profilePicture(Firebase ref) {
        String uniqueID = uid(ref);
        ref = ref.child("Users/" + uniqueID + "/Profile Picture");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PROFILEPIC = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return PROFILEPIC;
    }
}
