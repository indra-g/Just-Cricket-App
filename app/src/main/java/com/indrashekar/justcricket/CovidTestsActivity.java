package com.indrashekar.justcricket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class CovidTestsActivity extends AppCompatActivity {
    Switch test1,test2,test3;
    TextView result1,result2,result3;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    MaterialButton update_btn;
    ProgressDialog progress;

    public void clicked(View view){
        if(test1.isChecked()){
            result1.setText("1st Test= Positive");
        }
        else{
            result1.setText("1st Test= Negative");
        }
        if(test2.isChecked()){
            result2.setText("2st Test= Positive");
        }
        else{
            result2.setText("2st Test= Negative");
        }
        if(test3.isChecked()){
            result3.setText("3st Test= Positive");
        }
        else{
            result3.setText("3st Test= Negative");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_tests);
        test1=findViewById(R.id.test1);
        test2=findViewById(R.id.test2);
        test3=findViewById(R.id.test3);
        result1=findViewById(R.id.result1);
        result2=findViewById(R.id.result2);
        result3=findViewById(R.id.result3);
        update_btn=findViewById(R.id.update_btn);
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        progress = new ProgressDialog(this);
        progress.setTitle("We are uploading your details");
        progress.setMessage("Please Wait");
        progress.setCancelable(false);
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                FirebaseUser fuser=mAuth.getCurrentUser();
                String userid=fuser.getUid();
                DatabaseReference mRef=database.getReference("Users").child(userid);
                if(test1.isChecked()||test2.isChecked()||test3.isChecked()){
                    String status="Your in Quarantine!";
                    HashMap<String, Object>map=new HashMap<>();
                    map.put("Bio bubble Status",status);
                    mRef.updateChildren(map).addOnCompleteListener(CovidTestsActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progress.dismiss();
                                Toast.makeText(CovidTestsActivity.this, "Your Information is updated", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }
                else{
                    String status1="Your in Bio Bubble!";
                    HashMap<String, Object>map1=new HashMap<>();
                    map1.put("Bio bubble Status",status1);
                    mRef.updateChildren(map1).addOnCompleteListener(CovidTestsActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progress.dismiss();
                                Toast.makeText(CovidTestsActivity.this, "Your Information is updated", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
}