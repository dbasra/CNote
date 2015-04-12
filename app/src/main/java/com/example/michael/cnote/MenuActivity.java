package com.example.michael.cnote;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import crash.CrashReporter;

/**
 * Created by Michael on 2/12/2015.
 */
public class MenuActivity extends FragmentActivity implements View.OnClickListener{

    CrashReporter reporter;
    private ResideMenu resideMenu;
    private MenuActivity mContext;
    private ResideMenuItem itemClass,itemMessages;
    private ResideMenuItem itemHome, itemAbout;
    private ResideMenuItem itemProfile;
    private ResideMenuItem itemCalendar;
    private ResideMenuItem itemSettings;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f);
        mContext = this;
        setUpMenu();
        changeFragment(new ProfilePage());

        reporter = CrashReporter.getInstance();
        reporter.init(this);
        if (reporter.isThereAnyErrorFile()) {
            showAlertDialog();
        }
    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(MenuActivity.this);
        //set background of menu
        resideMenu.setBackground(R.drawable.menubg_new);
        resideMenu.attachToActivity(MenuActivity.this);
        // resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemHome     = new ResideMenuItem(this, R.mipmap.icon_home,     "Add Class");
        itemClass    = new ResideMenuItem(this, R.mipmap.icon_profile,  "Profile");
        itemProfile  = new ResideMenuItem(this, R.mipmap.icon_calendar,  "Remove Class");
        itemMessages = new ResideMenuItem(this, R.mipmap.icon_settings, "Messages");
        itemCalendar = new ResideMenuItem(this, R.mipmap.icon_home, "Logout");
        itemSettings = new ResideMenuItem(this, R.mipmap.icon_settings, "Settings");
        itemAbout = new ResideMenuItem(this, R.mipmap.icon_calendar, "About");

        itemClass.setOnClickListener(this);
        itemHome.setOnClickListener(this);
        itemProfile.setOnClickListener(this);
        itemCalendar.setOnClickListener(this);
        itemMessages.setOnClickListener(this);
        itemSettings.setOnClickListener(this);

        resideMenu.addMenuItem(itemClass, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemMessages, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemCalendar, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemAbout, ResideMenu.DIRECTION_RIGHT);

        // You can disable a direction by setting ->
        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);


        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        findViewById(R.id.title_bar_right_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
        if (view == itemHome){
            changeFragment(new AddClass());
        }else if (view == itemProfile){
            changeFragment(new RemoveClass());
        }else if (view == itemCalendar){
            startActivity(new Intent(MenuActivity.this, MainActivity.class));
        }else if (view == itemSettings){
            changeFragment(new Settings());
        }else if(view == itemClass) {
            changeFragment(new ProfilePage());
        }else if (view == itemMessages) {
            changeFragment(new DirectMessages());
        }else if (view == itemAbout) {
            changeFragment(new About());
        }

        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    private void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // What good method is to access resideMenuï¼Ÿ
    public ResideMenu getResideMenu(){
        return resideMenu;
    }

    private void showAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Send Error Log?");
        alertDialogBuilder.setMessage(
                "A previous crash was reported. Would you like to send"
                        + " the developer the error log to fix this issue in the future?")
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        reporter.checkErrorAndSendMail();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
