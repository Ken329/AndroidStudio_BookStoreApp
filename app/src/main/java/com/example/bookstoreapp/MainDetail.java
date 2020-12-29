package com.example.bookstoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainDetail extends AppCompatActivity {
    ImageButton back;
    TextView update;
    EditText surname, firstName, gender, age, phone, username, password;

    String myUser, mySurname, myFirstName, myGender, myAge, myPhone, myUsername, myPassword;
    DatabaseReference ref;
    AwesomeValidation validate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detail);
        getSupportActionBar().hide();

        back = findViewById(R.id.ibDetailBack);
        update = findViewById(R.id.tvDetailUpdate);
        surname = findViewById(R.id.etDetailSurname);
        firstName = findViewById(R.id.etDetailFirstname);
        gender = findViewById(R.id.etDetailGender);
        age = findViewById(R.id.etDetailAge);
        phone = findViewById(R.id.etDetailPhone);
        username = findViewById(R.id.etDetailUsername);
        password = findViewById(R.id.etDetailPassword);

        username.setEnabled(false);
        myUser = getIntent().getStringExtra("username");

        ref = FirebaseDatabase.getInstance().getReference("user").child(myUser);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mySurname = snapshot.child("surname").getValue().toString();
                myFirstName = snapshot.child("firstname").getValue().toString();
                myGender = snapshot.child("gender").getValue().toString();
                myAge = snapshot.child("age").getValue().toString();
                myPhone = snapshot.child("phone").getValue().toString();
                myUsername = snapshot.child("username").getValue().toString();
                myPassword = snapshot.child("password").getValue().toString();

                surname.setText(mySurname);
                firstName.setText(myFirstName);
                gender.setText(myGender);
                age.setText(myAge);
                phone.setText(myPhone);
                username.setText(myUsername);
                password.setText(myPassword);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainUser.class);
                intent.putExtra("username", myUser);
                startActivity(intent);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySurname = surname.getText().toString();
                myFirstName = firstName.getText().toString();
                myGender = gender.getText().toString();
                myAge = age.getText().toString();
                myPhone = phone.getText().toString();
                myUsername = username.getText().toString();
                myPassword = password.getText().toString();

                UserDetail user = new UserDetail(myUsername, myPassword, mySurname, myFirstName, myPhone, myAge, myGender);
                ref = FirebaseDatabase.getInstance().getReference("user").child(myUser);
                ref.setValue(user);
                Toast.makeText(MainDetail.this, "Successful update user detail", Toast.LENGTH_LONG).show();
            }
        });
    }
}