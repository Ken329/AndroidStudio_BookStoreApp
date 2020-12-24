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
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainAdminAdd extends AppCompatActivity {
    ImageButton back;
    TextView book, add;
    EditText name, author, date;

    AwesomeValidation validate;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin_add);
        getSupportActionBar().hide();

        back = findViewById(R.id.ibAdminAddBack);
        book = findViewById(R.id.tvAdminAddBook);
        add = findViewById(R.id.tvAdminAdd);
        name = findViewById(R.id.etAdminAddBookname);
        author = findViewById(R.id.etAdminAddBookAuthor);
        date = findViewById(R.id.etAdminAddBookDate);

        validate = new AwesomeValidation(ValidationStyle.BASIC);
        validate.addValidation(this, R.id.etAdminAddBookname, RegexTemplate.NOT_EMPTY, R.string.InvalidName);
        validate.addValidation(this, R.id.etAdminAddBookAuthor, RegexTemplate.NOT_EMPTY, R.string.InvalidName);
        validate.addValidation(this, R.id.etAdminAddBookDate, RegexTemplate.NOT_EMPTY, R.string.InvalidName);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainAdminPage.class);
                startActivity(intent);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookName = name.getText().toString();
                String myAuthor = author.getText().toString();
                String myDate = date.getText().toString();
                if(validate.validate()){
                    Query query = FirebaseDatabase.getInstance().getReference("book")
                            .orderByChild("book_name")
                            .equalTo(bookName);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Toast.makeText(MainAdminAdd.this, "Book Appeared, please check", Toast.LENGTH_LONG).show();
                            }else{
                                BookDetail book = new BookDetail(bookName, myAuthor, myDate, "none");
                                ref = FirebaseDatabase.getInstance().getReference("book");
                                ref.child(bookName).setValue(book);
                                Toast.makeText(MainAdminAdd.this, "New book added", Toast.LENGTH_SHORT).show();
                                name.setText("");
                                author.setText("");
                                date.setText("");
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else{
                    Toast.makeText(MainAdminAdd.this, "Do not leave anything empty", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}