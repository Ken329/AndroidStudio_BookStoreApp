package com.example.bookstoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainBorrow extends AppCompatActivity {
    ImageButton back;
    ListView list;
    TextView user;

    ArrayList<String> bookName, bookAuthor, bookDate, bookBorrow;
    DatabaseReference ref;
    String myUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_borrow);
        getSupportActionBar().hide();

        back = findViewById(R.id.ibBorrowBack);
        list = findViewById(R.id.listViewBorrow);
        user = findViewById(R.id.tvBorrow);

        myUser = getIntent().getStringExtra("username");
        bookName = new ArrayList<>();
        bookAuthor = new ArrayList<>();
        bookDate = new ArrayList<>();
        bookBorrow = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("borrow").child(myUser);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookName.clear();
                bookAuthor.clear();
                bookDate.clear();
                bookBorrow.clear();
                for(DataSnapshot s1 : snapshot.getChildren()){
                    bookName.add(s1.child("book_name").getValue().toString());
                    bookAuthor.add(s1.child("author").getValue().toString());
                    bookDate.add(s1.child("date").getValue().toString());
                    bookBorrow.add(s1.child("borrowed").getValue().toString());
                }
                customAdapter adapter = new customAdapter();
                list.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ref = FirebaseDatabase.getInstance().getReference("borrow").child(myUser);
                ref.child(bookName.get(position)).removeValue();
                ref = FirebaseDatabase.getInstance().getReference("book").child(bookName.get(position)).child("borrowed");
                ref.setValue("none");
                Toast.makeText(MainBorrow.this, bookName.get(position) + " has been returned successfully", Toast.LENGTH_LONG).show();
                return false;
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
    }
    class customAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return bookName.size();
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int color = getResources().getColor(R.color.red);
            View view1 = getLayoutInflater().inflate(R.layout.user_book_list, null);
            TextView name = (TextView)view1.findViewById(R.id.tvBookListName);
            TextView author = (TextView)view1.findViewById(R.id.tvBookListAuthor);
            TextView date = (TextView)view1.findViewById(R.id.tvBookListDate);
            TextView borrow = (TextView)view1.findViewById(R.id.tvBookListBorrowed);
            LinearLayout LinearBorrow = (LinearLayout)view1.findViewById(R.id.linearBookListBorrow);

            name.setText(bookName.get(position));
            author.setText(bookAuthor.get(position));
            date.setText(bookDate.get(position));
            borrow.setText(bookBorrow.get(position));
            LinearBorrow.setBackgroundColor(color);
            return view1;
        }
    }
}