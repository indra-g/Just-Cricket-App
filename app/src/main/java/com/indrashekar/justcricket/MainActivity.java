package com.indrashekar.justcricket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {
    TextView name_txt, nationality_txt, tplay_txt, tname_txt, covid_txt, status;
    ListView listview;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progress;
    private MenuItem item;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        item= menu.findItem(R.id.upload);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, StartActivity.class));
                return true;
            case R.id.upload:
                startActivity(new Intent(MainActivity.this, CovidTestsActivity.class));
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
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name_txt.setText(snapshot.child("Name").getValue().toString());
                nationality_txt.setText("Nationality -  "+snapshot.child("Nationality").getValue().toString());
                tplay_txt.setText("Type of Play -  "+snapshot.child("Type of Play").getValue().toString());
                tname_txt.setText("Team name -  "+snapshot.child("Team Name").getValue().toString());
                covid_txt.setText("Covid History -  "+snapshot.child("Covid history").getValue().toString());
                status.setText(snapshot.child("Bio bubble Status").getValue().toString());
                if(status.getText().toString().equals("Your in Bio Bubble!")){
                    item.setVisible(false);
                }
                else{
                    item.setVisible(true);
                }
                progress.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progress.dismiss();
                Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}