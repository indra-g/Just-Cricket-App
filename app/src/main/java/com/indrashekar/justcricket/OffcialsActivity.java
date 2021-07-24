package com.indrashekar.justcricket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OffcialsActivity extends AppCompatActivity {
    TextView name_txt, nationality_txt, role_txt, covid_txt, status, time_txt;
    MaterialButton quarantine_done;
    ListView listview;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progress;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.logout) {
            mAuth.signOut();
            startActivity(new Intent(OffcialsActivity.this, StartActivity.class));
            finish();
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offcials);
        name_txt = findViewById(R.id.name_txt);
        nationality_txt = findViewById(R.id.nationality_txt);
        role_txt = findViewById(R.id.role_txt);
        covid_txt = findViewById(R.id.covid_txt);
        status = findViewById(R.id.status);
        quarantine_done = findViewById(R.id.quarantine_done);
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
        getSupportActionBar().setTitle("Home Screen");
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userid = firebaseUser.getUid();
        DatabaseReference mRef = database.getReference("Users").child(userid);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name_txt.setText(snapshot.child("Name").getValue().toString());
                nationality_txt.append(snapshot.child("Nationality").getValue().toString());
                role_txt.append(snapshot.child("Role Type").getValue().toString());
                covid_txt.append(snapshot.child("Covid history").getValue().toString());
                status.setText(snapshot.child("Bio bubble Status").getValue().toString());
                progress.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progress.dismiss();
                Toast.makeText(OffcialsActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        quarantine_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OffcialsActivity.this,CovidTestsActivity.class));
                finish();
            }
        });
    }
}