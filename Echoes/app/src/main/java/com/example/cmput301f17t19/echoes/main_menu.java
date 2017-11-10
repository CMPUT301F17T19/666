/*
 * Copyright (c) Team cmput301f17t19, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta
 */

package com.example.cmput301f17t19.echoes;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import static com.example.cmput301f17t19.echoes.LoginActivity.LOGIN_USERNAME;

public class main_menu extends AppCompatActivity {

    // The user name of the login user
    private static String login_UserName;

    // The user Profile of the login user
    private UserProfile login_UserProfile;

    private Button myHabitsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.primary_dark));
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
      
        myHabitsButton = (Button) findViewById(R.id.View_My_Habits);

        Intent intent = getIntent();

        login_UserName = intent.getStringExtra(LOGIN_USERNAME);

        setLogin_UserProfile(login_UserName);
    }


    public void viewMyHabits(View view) {
        // Pass the login User Name to the MyHabits Activity
        Intent intent = new Intent(this, MyHabitsActivity.class);
        intent.putExtra(LOGIN_USERNAME, login_UserName);

        startActivity(intent);

        finish();
    }

    public void Following(View view) {
//        Intent intent = new Intent(this, Following.class);
//        startActivity(intent);
    }

    public void Habits_todo_today (View view) {
//        Intent intent = new Intent(this, AddNewActivity.class);
//        startActivity(intent);
    }


    public void Habits_history(View view) {
//        // Pass the login User Name to the HabitHistory Activity
//        Intent intent = new Intent(this, HabitHistoryActivity.class);
//        intent.putExtra(LOGIN_USERNAME, login_UserName);
//
//        startActivity(intent);
//
//        finish();
    }

    public void Habits_events_map(View view) {
//        Intent intent = new Intent(this, AddNewActivity.class);
//        startActivity(intent);
    }
  
    public void Logout(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Read the offline UserProfile data with logined name
     * Sync offline data with online storage
     *
     * @param login_Username: String, the username of the Login User
     */
    private void setLogin_UserProfile(String login_Username) {

        OfflineStorageController offlineStorageController = new OfflineStorageController(this, login_Username);

        // Get the Offline Storage file of this user
        login_UserProfile = offlineStorageController.readFromFile();

        // Sync the offline file with online data storage
        ElasticSearchController.syncOnlineWithOffline(login_UserProfile);
    }
}
