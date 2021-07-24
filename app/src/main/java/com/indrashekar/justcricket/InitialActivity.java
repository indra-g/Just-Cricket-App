package com.indrashekar.justcricket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class InitialActivity extends AppCompatActivity {
    TextInputEditText name_edittext1,nationality_edittext1,tplay_edittext1,teamname_edittext1;
    MaterialButton register_btn;
    CheckBox covidtest;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        name_edittext1=findViewById(R.id.name_edittext1);
        nationality_edittext1=findViewById(R.id.nationality_edittext1);
        tplay_edittext1=findViewById(R.id.tplay_edittext1);
        teamname_edittext1=findViewById(R.id.teamname_edittext1);
        register_btn=findViewById(R.id.register_btn);
        covidtest=findViewById(R.id.covidtest);
        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        Intent intent=getIntent();
        String name=intent.getStringExtra("name");
        name_edittext1.setText(name);
        progress = new ProgressDialog(this);
        progress.setTitle("Setting your things...");
        progress.setMessage("Please Wait");
        progress.setCancelable(false);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String covidhistory="";
                String biobubblestatus="";
                if(covidtest.isChecked()){
                    covidhistory="Yes";
                    biobubblestatus="Your are put to Quarantine!";
                }
                else{
                    covidhistory="No";
                    biobubblestatus="Your are put to Bio Bubble!";
                }
                String name_txt=name_edittext1.getText().toString();
                String nationality_txt=nationality_edittext1.getText().toString();
                String tplay_txt=tplay_edittext1.getText().toString();
                String team_txt=teamname_edittext1.getText().toString();
                if(TextUtils.isEmpty(name_txt)||TextUtils.isEmpty(nationality_txt)||TextUtils.isEmpty(tplay_txt)||TextUtils.isEmpty(team_txt)){
                    Toast.makeText(InitialActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else {
                    progress.show();
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String userid = firebaseUser.getUid();
                    DatabaseReference myRef = database.getReference("Users").child(userid);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("Name", name_txt);
                    map.put("Nationality", nationality_txt);
                    map.put("Type of Play", tplay_txt);
                    map.put("Team Name", team_txt);
                    map.put("Covid history", covidhistory);
                    map.put("Bio bubble Status",biobubblestatus);
                    myRef.updateChildren(map).addOnCompleteListener(InitialActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Intent intent=new Intent(InitialActivity.this, MainActivity.class);
                                startActivity(intent);
                                progress.dismiss();
                                finish();
                            }
                            else{
                                progress.dismiss();
                                Toast.makeText(InitialActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}