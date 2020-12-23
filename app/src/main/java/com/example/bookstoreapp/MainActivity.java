package com.example.bookstoreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView title, login, signUp;
    EditText username, password;
    ImageView icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        title = findViewById(R.id.tvLoginTitle);
        icon = findViewById(R.id.ivLogin);
        username = findViewById(R.id.etLoginName);
        password = findViewById(R.id.etLoginPassword);
        login = findViewById(R.id.tvLoginEnter);
        signUp = findViewById(R.id.tvLoginSignUp);

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim);
                title.startAnimation(animation);
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainSignUp.class);
                startActivity(intent);
            }
        });
    }
}