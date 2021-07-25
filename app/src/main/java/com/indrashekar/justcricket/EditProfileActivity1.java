package com.indrashekar.justcricket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

public class EditProfileActivity1 extends AppCompatActivity {
    TextInputEditText name_edittext12,nationality_edittext1,role_edittext1;
    MaterialButton getstarted_btn;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile1);
        name_edittext12=findViewById(R.id.name_edittext12);
        nationality_edittext1=findViewById(R.id.nationality_edittext1);
        role_edittext1=findViewById(R.id.role_edittext1);
        getstarted_btn=findViewById(R.id.getstarted_btn);
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        progress=new ProgressDialog(this);
        progress.setTitle("Updating your profile...");
        progress.setMessage("Please Wait");
        progress.setCancelable(false);
        getstarted_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_txt=name_edittext12.getText().toString();
                String nationality_txt=nationality_edittext1.getText().toString();
                String role_txt=role_edittext1.getText().toString();
                if(TextUtils.isEmpty(name_txt)||TextUtils.isEmpty(nationality_txt)||TextUtils.isEmpty(role_txt)){
                    Toast.makeText(EditProfileActivity1.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else{
                    progress.show();
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String userid = firebaseUser.getUid();
                    DatabaseReference myRef = database.getReference("Users").child(userid);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("Name", name_txt);
                    map.put("Nationality", nationality_txt);
                    map.put("Role Type",role_txt);
                    myRef.updateChildren(map).addOnCompleteListener(EditProfileActivity1.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(EditProfileActivity1.this, "Your profile is updated", Toast.LENGTH_SHORT).show();
                                finish();
                                progress.dismiss();
                            }
                            else{
                                progress.dismiss();
                                Toast.makeText(EditProfileActivity1.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}