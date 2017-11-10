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

public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);

        Button mHabitHistoryButton = (Button) findViewById(R.id.habithistory_button);
        mHabitHistoryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Open HabitHistory UI
                Intent habitHistory_intent = new Intent(getApplicationContext(), HabitHistoryActivity.class);
                startActivity(habitHistory_intent);
            }
        });

        Button mMyHabitsButton = (Button) findViewById(R.id.myhabits_button);
        mMyHabitsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Open My Habits UI
                Intent myHabits_intent = new Intent(getApplicationContext(), MyHabitsActivity.class);
                startActivity(myHabits_intent);
            }
        });

        Button mProfileTestButton = (Button) findViewById(R.id.profileTest);
        mProfileTestButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Open My Habits UI
                Intent profile_intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                // Dummy1 login
                profile_intent.putExtra(UserProfileActivity.USERPROFILE_TAG, "dummy1");
                startActivity(profile_intent);
            }
        });


        Button mLoginTestButton = (Button) findViewById(R.id.loginTest);
        mLoginTestButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Open login ui
                Intent login_intent = new Intent(getApplicationContext(), LoginActivity.class);

                startActivity(login_intent);
            }
        });


        Button mHabitdetailButton = (Button) findViewById(R.id.habitDetail_test);
        mHabitdetailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Open login ui
                Intent login_intent = new Intent(getApplicationContext(), HabitDetail.class);

                startActivity(login_intent);
            }
        });

        Button mHabitEventDetailButton = (Button) findViewById(R.id.habiteventdetail_button);
        mHabitEventDetailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Open HabitHistory UI
                Intent habitEventDetail_intent = new Intent(getApplicationContext(), HabitEventDetailActivity.class);
                startActivity(habitEventDetail_intent);
            }
        });


        Button mMainmenuButton = (Button) findViewById(R.id.main_menu_test);
        mMainmenuButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Open HabitHistory UI
                Intent main_menu_intent = new Intent(getApplicationContext(), main_menu.class);
                startActivity(main_menu_intent);
            }
        });
    }
}
