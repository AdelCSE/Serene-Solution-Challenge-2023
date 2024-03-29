package com.example.solutionchallengeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.solutionchallengeapp.BottomNavigationFragments.CharityFragment;
import com.example.solutionchallengeapp.BottomNavigationFragments.HomeFragment;
import com.example.solutionchallengeapp.BottomNavigationFragments.NotificationsFragment;
import com.example.solutionchallengeapp.BottomNavigationFragments.ProfileFragment;
import com.example.solutionchallengeapp.Models.UserModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private FloatingActionButton fab;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottomNavigationLayout);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        userModel = (UserModel) getIntent().getSerializableExtra("UserInfos");

        if (userModel!=null){
            if(!userModel.getOrganisation()) {
                fab.setImageResource(R.drawable.ic_maps);
            }else {
                fab.setImageResource(R.drawable.plus);
            }
        }

        //setEnabled(false) for the 2nd index because we're using a floating button instead
        bottomNav.setBackground(null);
        bottomNav.getMenu().getItem(2).setEnabled(false);
        bottomNav.setOnItemSelectedListener(navListener);

        //Here we're setting the Home fragment as default fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentContainer,
                new HomeFragment()).commit();


        //Start NearbyActivity
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel!=null){
                    if(!userModel.getOrganisation()) {
                        startActivity(new Intent(getApplicationContext(), NearbyActivity.class));
                    }else {
                        startActivity(new Intent(getApplicationContext(), AddEventActivity.class));
                    }
                }
            }
        });

    }

    private BottomNavigationView.OnItemSelectedListener navListener =
            new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_charity:
                            selectedFragment = new CharityFragment();
                            break;
                        case R.id.nav_notif:
                            selectedFragment = new NotificationsFragment();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentContainer,
                            selectedFragment).commit();
                    return true;
                }
            };

    @Override
    public void onBackPressed() {
        finish();
    }
}