package com.example.bookstoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

public class MainUser extends AppCompatActivity {
    TextView name;
    ListView list;
    LinearLayout detail, borrow, wishlist;

    String myUser;
    ArrayList<String> bookName, bookDate, bookAuthor, bookBorrow;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        getSupportActionBar().hide();

        name = findViewById(R.id.tvUserName);
        detail = findViewById(R.id.linearUserDetail);
        borrow = findViewById(R.id.linearUserBorrow);
        wishlist = findViewById(R.id.linearUserWishlist);
        list = findViewById(R.id.listViewUser);

        myUser = getIntent().getStringExtra("username");
        Query query = FirebaseDatabase.getInstance().getReference("user")
                .orderByChild("username")
                .equalTo(myUser);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String mySurname = snapshot.child(myUser).child("surname").getValue().toString();
                    name.setText(mySurname);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        bookName = new ArrayList<>();
        bookAuthor = new ArrayList<>();
        bookDate = new ArrayList<>();
        bookBorrow = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("book");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookName.clear();
                bookAuthor.clear();
                bookBorrow.clear();
                bookDate.clear();
                for(DataSnapshot s1 : snapshot.getChildren()){
                    bookName.add(s1.child("book_name").getValue().toString());
                    bookAuthor.add(s1.child("author").getValue().toString());
                    bookBorrow.add(s1.child("borrowed").getValue().toString());
                    bookDate.add(s1.child("date").getValue().toString());
                }
                customAdapter adapter = new customAdapter();
                list.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainWishList.class);
                intent.putExtra("username", myUser);
                startActivity(intent);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookDetail book = new BookDetail(bookName.get(position), bookAuthor.get(position), bookDate.get(position), bookBorrow.get(position));
;               ref = FirebaseDatabase.getInstance().getReference("wishlist").child(myUser);
                ref.child(bookName.get(position)).setValue(book);
                Toast.makeText(MainUser.this, bookName.get(position) + "added to Wish List", Toast.LENGTH_LONG).show();
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainUser.this, bookName.get(position) + "added to borrow", Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }
    class customAdapter extends BaseAdapter{

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
            View view1 = getLayoutInflater().inflate(R.layout.user_book_list, null);
            TextView name = (TextView)view1.findViewById(R.id.tvBookListName);
            TextView author = (TextView)view1.findViewById(R.id.tvBookListAuthor);
            TextView date = (TextView)view1.findViewById(R.id.tvBookListDate);
            TextView borrow = (TextView)view1.findViewById(R.id.tvBookListBorrowed);

            name.setText(bookName.get(position));
            author.setText(bookAuthor.get(position));
            date.setText(bookDate.get(position));
            borrow.setText(bookBorrow.get(position));
            return view1;
        }
    }
}