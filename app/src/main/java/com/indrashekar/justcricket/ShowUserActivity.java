package com.indrashekar.justcricket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class ShowUserActivity extends AppCompatActivity {
    TextView name_txt,nationality_txt,role_txt,tplay_txt,tname_txt,covid_txt,status;
    MaterialButton back_btn1;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);
        name_txt=findViewById(R.id.name_txt);
        nationality_txt=findViewById(R.id.nationality_txt);
        role_txt=findViewById(R.id.role_txt);
        tplay_txt=findViewById(R.id.tplay_txt);
        tname_txt=findViewById(R.id.tname_txt);
        covid_txt=findViewById(R.id.covid_txt);
        status=findViewById(R.id.status);
        back_btn1=findViewById(R.id.back_btn1);
        progress=new ProgressDialog(this);
        progress.setTitle("Retrieving Data....");
        progress.setMessage("Please wait");
        progress.setCancelable(false);
        progress.show();
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        back_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent=getIntent();
        String uid=intent.getStringExtra("uid");
        DatabaseReference mRef=database.getReference("Users").child(uid).child("Type");
        mRef.get().addOnCompleteListener(ShowUserActivity.this, new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(String.valueOf(task.getResult().getValue()).equals("Team Player")){
                    DatabaseReference m1Ref=database.getReference("Users").child(uid);
                    m1Ref.child("Name").get().addOnCompleteListener(ShowUserActivity.this, new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            name_txt.setText(String.valueOf(task.getResult().getValue()));
                        }
                    });
                    m1Ref.child("Nationality").get().addOnCompleteListener(ShowUserActivity.this, new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            nationality_txt.setText("Nationality -  "+String.valueOf(task.getResult().getValue()));
                        }
                    });
                    m1Ref.child("Type of Play").get().addOnCompleteListener(ShowUserActivity.this, new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            tplay_txt.setText("Type of Play -  "+String.valueOf(task.getResult().getValue()));
                        }
                    });
                    m1Ref.child("Team Name").get().addOnCompleteListener(ShowUserActivity.this, new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            tname_txt.setText("Team name -  "+String.valueOf(task.getResult().getValue()));
                        }
                    });
                    m1Ref.child("Covid history").get().addOnCompleteListener(ShowUserActivity.this, new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            covid_txt.setText("Covid History -  "+String.valueOf(task.getResult().getValue()));
                        }
                    });
                    m1Ref.child("Bio bubble Status").get().addOnCompleteListener(ShowUserActivity.this, new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            String status1=String.valueOf(task.getResult().getValue());
                            if(status1.equals("Your in Quarantine!")){
                                status.setText("Currently in Quarantine");
                                status.setTextColor(Color.RED);
                            }
                            else{
                                status.setText("Currently in Bio Bubble");
                                status.setTextColor(Color.GREEN);
                            }
                        }
                    });
                    role_txt.setText("Role Type - NA");
                    role_txt.setTextColor(Color.RED);
                    progress.dismiss();
                }
                else{
                    DatabaseReference m2Ref=database.getReference("Users").child(uid);
                    m2Ref.child("Name").get().addOnCompleteListener(ShowUserActivity.this, new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            name_txt.setText(String.valueOf(task.getResult().getValue()));
                        }
                    });
                    m2Ref.child("Nationality").get().addOnCompleteListener(ShowUserActivity.this, new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            nationality_txt.setText("Nationality -  "+String.valueOf(task.getResult().getValue()));
                        }
                    });
                    m2Ref.child("Role Type").get().addOnCompleteListener(ShowUserActivity.this, new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            role_txt.setText("Role Type -  "+String.valueOf(task.getResult().getValue()));
                        }
                    });
                    m2Ref.child("Covid history").get().addOnCompleteListener(ShowUserActivity.this, new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            covid_txt.setText("Covid History -  "+String.valueOf(task.getResult().getValue()));
                        }
                    });
                    m2Ref.child("Bio bubble Status").get().addOnCompleteListener(ShowUserActivity.this, new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            String status1=String.valueOf(task.getResult().getValue());
                            if(status1.equals("Your in Quarantine!")){
                                status.setText("Currently in Quarantine");
                                status.setTextColor(Color.RED);
                            }
                            else{
                                status.setText("Currently in Bio Bubble");
                                status.setTextColor(Color.GREEN);
                            }
                        }
                    });
                    tplay_txt.setText("Type of Play - NA");
                    tplay_txt.setTextColor(Color.RED);
                    tname_txt.setText("Team name - NA");
                    tname_txt.setTextColor(Color.RED);
                    progress.dismiss();
                }
            }
        });
    }
}