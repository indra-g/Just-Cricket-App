package com.indrashekar.justcricket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView name_txt, nationality_txt, tplay_txt, tname_txt, covid_txt, status;
    ListView listview;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progress;
    private MenuItem item;
    ArrayList<String> names;
    ArrayList<String> status1;
    ArrayAdapter<String> adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        item = menu.findItem(R.id.upload);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, StartActivity.class));
                return true;
            case R.id.upload:
                startActivity(new Intent(MainActivity.this, CovidTestsActivity.class));
                return true;
            case R.id.allsuers:
                startActivity(new Intent(MainActivity.this, AllUsersActivity.class));
                return true;
            case R.id.edit:
                startActivity(new Intent(MainActivity.this, EditProfileActivity.class));
                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name_txt = findViewById(R.id.name_txt);
        nationality_txt = findViewById(R.id.nationality_txt);
        tplay_txt = findViewById(R.id.tplay_txt);
        tname_txt = findViewById(R.id.tname_txt);
        covid_txt = findViewById(R.id.covid_txt);
        status = findViewById(R.id.status);
        listview = findViewById(R.id.listview);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progress = new ProgressDialog(this);
        progress.setTitle("Retrieving Data...");
        progress.setMessage("Please Wait");
        progress.setCancelable(false);
        progress.show();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Just Cricket");
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userid = firebaseUser.getUid();
        DatabaseReference mRef = database.getReference("Users").child(userid);
        mRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name_txt.setText(snapshot.child("Name").getValue().toString());
                nationality_txt.setText("Nationality -  " + snapshot.child("Nationality").getValue().toString());
                tplay_txt.setText("Type of Play -  " + snapshot.child("Type of Play").getValue().toString());
                tname_txt.setText("Team name -  " + snapshot.child("Team Name").getValue().toString());
                covid_txt.setText("Covid History -  " + snapshot.child("Covid history").getValue().toString());
                status.setText(snapshot.child("Bio bubble Status").getValue().toString());
                item.setVisible(!status.getText().toString().equals("Your in Bio Bubble!"));
                if(status.equals("Your in Bio Bubble!")){
                    status.setTextColor(R.color.green);
                }
                else{
                    status.setTextColor(R.color.red);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progress.dismiss();
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        listview = findViewById(R.id.listview);
        names = new ArrayList<>();
        status1 = new ArrayList<>();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersdRef = rootRef.child("Users");
        names.clear();
        status1.clear();
        adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, names);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("Name").getValue(String.class);
                    String status = ds.child("Bio bubble Status").getValue(String.class);
                    names.add(name);
                    adapter.notifyDataSetChanged();
                }
                listview.setAdapter(adapter);
                progress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        usersdRef.addListenerForSingleValueEvent(eventListener);
    }
}