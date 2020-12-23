package com.example.bookstoreapp;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    TextView title, login, signUp;
    EditText username, password;
    ImageView icon;
    DatabaseReference ref;
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
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myUsername = username.getText().toString();
                String myPassword = password.getText().toString();
                Query query = FirebaseDatabase.getInstance().getReference("user")
                        .orderByChild("username")
                        .equalTo(myUsername);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String dataPassword = snapshot.child(myUsername).child("password").getValue().toString();
                            if(dataPassword.equals(myPassword)){
                                Toast.makeText(MainActivity.this, "Successful login", Toast.LENGTH_SHORT).show();
                            }else{
                                password.setError("Wrong password");
                            }
                        }else{
                            username.setError("Invalid username");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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