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

public class InitialActivity2 extends AppCompatActivity {
    TextInputEditText name_edittext12,nationality_edittext1,role_edittext1;
    CheckBox covidtest1;
    MaterialButton getstarted_btn;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial2);
        name_edittext12=(TextInputEditText)findViewById(R.id.name_edittext12);
        nationality_edittext1=(TextInputEditText)findViewById(R.id.nationality_edittext1);
        role_edittext1=(TextInputEditText)findViewById(R.id.role_edittext1);
        getstarted_btn=(MaterialButton)findViewById(R.id.getstarted_btn);
        covidtest1=(CheckBox)findViewById(R.id.covidtest1);
        Intent intent=getIntent();
        String name=intent.getStringExtra("name");
        name_edittext12.setText(name);
        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        progress = new ProgressDialog(this);
        progress.setTitle("Setting your things...");
        progress.setMessage("Please Wait");
        progress.setCancelable(false);
        getstarted_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String covidhistory="";
                String biobubblestatus="";
                if(covidtest1.isChecked()){
                    covidhistory="Yes";
                    biobubblestatus="15 days";
                }
                else{
                    covidhistory="No";
                    biobubblestatus="0 days";
                }
                String name_txt=name_edittext12.getText().toString();
                String nationality_txt=nationality_edittext1.getText().toString();
                String role_txt=role_edittext1.getText().toString();
                if(TextUtils.isEmpty(name_txt)||TextUtils.isEmpty(nationality_txt)||TextUtils.isEmpty(role_txt)){
                    Toast.makeText(InitialActivity2.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else{
                    progress.show();
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String userid = firebaseUser.getUid();
                    DatabaseReference myRef = database.getReference("Officials").child(userid);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("Name", name_txt);
                    map.put("Nationality", nationality_txt);
                    map.put("Role Type",role_txt);
                    map.put("Covid history",covidhistory);
                    map.put("Bio bubble Status",biobubblestatus);
                    myRef.updateChildren(map).addOnCompleteListener(InitialActivity2.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(InitialActivity2.this, MainActivity.class));
                                progress.dismiss();
                                finish();
                            }
                            else{
                                progress.dismiss();
                                Toast.makeText(InitialActivity2.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}