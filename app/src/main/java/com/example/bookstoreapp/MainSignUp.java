package com.example.bookstoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainSignUp extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    EditText surname, name, phone, age, gender, username, password;
    ImageButton back;
    TextView create;
    AwesomeValidation validation;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sign_up);
        getSupportActionBar().hide();

        back = findViewById(R.id.ibSingUpBack);
        surname = findViewById(R.id.etSignUpSurname);
        name = findViewById(R.id.etSignUpFirstname);
        phone = findViewById(R.id.etSignUpPhone);
        age = findViewById(R.id.etSignUpAge);
        gender = findViewById(R.id.etSignUpGender);
        username = findViewById(R.id.etSignUpUsername);
        password = findViewById(R.id.etSignUpPassword);
        create = findViewById(R.id.etSignUpCreate);

        validation = new AwesomeValidation(ValidationStyle.BASIC);
        validation.addValidation(this, R.id.etSignUpSurname, RegexTemplate.NOT_EMPTY, R.string.InvalidName);
        validation.addValidation(this, R.id.etSignUpFirstname, RegexTemplate.NOT_EMPTY, R.string.InvalidName);
        validation.addValidation(this, R.id.etSignUpPhone, RegexTemplate.NOT_EMPTY, R.string.InvalidName);
        validation.addValidation(this, R.id.etSignUpAge, RegexTemplate.NOT_EMPTY, R.string.InvalidName);
        validation.addValidation(this, R.id.etSignUpGender, RegexTemplate.NOT_EMPTY, R.string.InvalidName);
        validation.addValidation(this, R.id.etSignUpUsername, ".{6,}", R.string.InvalidUsername);
        validation.addValidation(this, R.id.etSignUpPassword, ".{6,}", R.string.InvalidUsername);

        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp(v);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mySurname = surname.getText().toString();
                String myName = name.getText().toString();
                String myPhone = phone.getText().toString();
                String myAge = age.getText().toString();
                String myGender = gender.getText().toString();
                String myUsername = username.getText().toString();
                String myPassword = password.getText().toString();
                if(validation.validate()){
                    Query query = FirebaseDatabase.getInstance().getReference("user")
                            .orderByChild("username")
                            .equalTo(myUsername);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Toast.makeText(MainSignUp.this, "Username appeared, try again", Toast.LENGTH_LONG).show();
                            }else{
                                UserDetail user = new UserDetail(myUsername, myPassword, mySurname, myName, myPhone, myAge, myGender);
                                ref = FirebaseDatabase.getInstance().getReference("user");
                                ref.child(myUsername).setValue(user);
                                Toast.makeText(MainSignUp.this, "Successful Created", Toast.LENGTH_LONG).show();
                                goBack();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else{
                    Toast.makeText(MainSignUp.this, "Error appear, ,Please check", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void showPopUp(View v){
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.gender);
        popupMenu.show();
    }
    public void goBack(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                gender.setText("Male");
                return true;
            case R.id.item2:
                gender.setText("Female");
                return true;
            default:
                return false;
        }
    }
}