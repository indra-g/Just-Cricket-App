package com.indrashekar.justcricket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllUsersActivity extends AppCompatActivity {
    ListView listview;
    ArrayList<String> names;
    ArrayList<String> userid;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        listview=findViewById(R.id.listview);
        names=new ArrayList<>();
        userid=new ArrayList<>();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersdRef = rootRef.child("Users");
        names.clear();
        userid.clear();
        adapter= new ArrayAdapter(AllUsersActivity.this, android.R.layout.simple_list_item_1,names);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("Name").getValue(String.class);
                    String uid= ds.child("User id").getValue(String.class);
                    names.add(name);
                    userid.add(uid);
                    adapter.notifyDataSetChanged();
                }
                listview.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AllUsersActivity.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        usersdRef.addListenerForSingleValueEvent(eventListener);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String uid=userid.get(position);
                Intent intent=new Intent(AllUsersActivity.this,ShowUserActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });
    }
}