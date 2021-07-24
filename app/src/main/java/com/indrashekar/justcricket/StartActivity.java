package com.indrashekar.justcricket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StartActivity extends AppCompatActivity {
    TextView signup_txtview,login_txtview;
    FirebaseAuth mAuth;
    FirebaseDatabase database;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            ProgressDialog progress;
            progress = new ProgressDialog(StartActivity.this);
            progress.setTitle("Loading...");
            progress.setMessage("Please Wait");
            progress.setCancelable(false);
            progress.show();
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            String userid = firebaseUser.getUid();
            DatabaseReference mRef = database.getReference("Users").child(userid).child("Type");
            mRef.get().addOnCompleteListener(StartActivity.this, new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(String.valueOf(task.getResult().getValue()).equals("Team Player")){
                        startActivity(new Intent(StartActivity.this,MainActivity.class));
                        progress.dismiss();
                        finish();
                    }
                    else{
                        startActivity(new Intent(StartActivity.this,OffcialsActivity.class));
                        progress.dismiss();
                        finish();
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        signup_txtview=(TextView)findViewById(R.id.signup_txtview);
        login_txtview=(TextView)findViewById(R.id.login_txtview);
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        signup_txtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this,SignUpActivity.class));
                finish();
            }
        });
        login_txtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this,LoginActivity.class));
                finish();
            }
        });
    }
}