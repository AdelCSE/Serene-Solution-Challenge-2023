package com.example.solutionchallengeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GetStartedActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatButton loginBtn, registerBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        loginBtn = findViewById(R.id.login_button);
        registerBtn = findViewById(R.id.register_button);

        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_button:
                startActivity(new Intent(GetStartedActivity.this,LoginActivity.class));
                break;
            case R.id.register_button:
                startActivity(new Intent(GetStartedActivity.this,RegisterActivity.class));
                break;
        }
    }
}