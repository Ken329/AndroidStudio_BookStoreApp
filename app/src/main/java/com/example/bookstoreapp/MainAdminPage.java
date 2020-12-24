package com.example.bookstoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainAdminPage extends AppCompatActivity {
    ImageButton back;
    TextView add;
    ListView list;
    DatabaseReference ref;
    ArrayList<String> name, author, borrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin_page);
        getSupportActionBar().hide();

        back = findViewById(R.id.ibAdminAddBack);
        add = findViewById(R.id.tvAdminPageAdd);
        list = findViewById(R.id.listViewAdminPage);

        name = new ArrayList<>();
        author = new ArrayList<>();
        borrow = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("book");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name.clear();
                author.clear();
                borrow.clear();
                for(DataSnapshot s1 : snapshot.getChildren()){
                    name.add(s1.child("book_name").getValue().toString());
                    author.add(s1.child("author").getValue().toString());
                    borrow.add(s1.child("borrowed").getValue().toString());
                }
                customAdapter adapter = new customAdapter();
                list.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainAdminAdd.class);
                startActivity(intent);
            }
        });
    }
    class customAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return name.size();
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
            View view1 = getLayoutInflater().inflate(R.layout.admin_book_list, null);
            TextView tvName = (TextView)view1.findViewById(R.id.tvListName);
            TextView tvAuthor = (TextView)view1.findViewById(R.id.tvListAuthor);
            TextView tvBorrow = (TextView)view1.findViewById(R.id.tvListBorrow);

            tvName.setText(name.get(position));
            tvAuthor.setText(author.get(position));
            tvBorrow.setText(borrow.get(position));
            return view1;
        }
    }
}