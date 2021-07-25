package com.indrashekar.justcricket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

public class EditProfileActivity extends AppCompatActivity {
    TextInputEditText name_edittext1,nationality_edittext1,tplay_edittext1,teamname_edittext1;
    MaterialButton register_btn;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        name_edittext1=findViewById(R.id.name_edittext1);
        nationality_edittext1=findViewById(R.id.nationality_edittext1);
        tplay_edittext1=findViewById(R.id.tplay_edittext1);
        teamname_edittext1=findViewById(R.id.teamname_edittext1);
        register_btn=findViewById(R.id.register_btn);
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        progress=new ProgressDialog(this);
        progress.setTitle("Updating your profile...");
        progress.setMessage("Please Wait");
        progress.setCancelable(false);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name_edittext1.getText().toString();
                String nationality = nationality_edittext1.getText().toString();
                String type = tplay_edittext1.getText().toString();
                String tname = teamname_edittext1.getText().toString();
                if(TextUtils.isEmpty(name)||TextUtils.isEmpty(nationality)||TextUtils.isEmpty(type)||TextUtils.isEmpty(tname)){
                    Toast.makeText(EditProfileActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else {
                    progress.show();
                    FirebaseUser fuser = mAuth.getCurrentUser();
                    String userid = fuser.getUid();
                    DatabaseReference mRef = database.getReference("Users").child(userid);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("Name", name);
                    map.put("Nationality", nationality);
                    map.put("Type of Play", type);
                    map.put("Team Name", tname);
                    mRef.updateChildren(map).addOnCompleteListener(EditProfileActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progress.dismiss();
                                Toast.makeText(EditProfileActivity.this, "Your Profile is Updated", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                progress.dismiss();
                                Toast.makeText(EditProfileActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}