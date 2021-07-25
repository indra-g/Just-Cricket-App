package com.indrashekar.justcricket;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity{
    TextView login_txt;
    MaterialButton signup_btn;
    TextInputEditText name_edittxt1,email_edittxt1,pass_edittxt1;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    RadioGroup role;
    RadioButton radioButton,radioButton2;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        login_txt=(TextView)findViewById(R.id.login_txt);
        signup_btn=(MaterialButton)findViewById(R.id.signup_btn);
        name_edittxt1=findViewById(R.id.name_edittxt1);
        email_edittxt1=findViewById(R.id.email_edittxt1);
        pass_edittxt1=findViewById(R.id.pass_edittxt1);
        radioButton=findViewById(R.id.radioButton);
        radioButton2=findViewById(R.id.radioButton2);
        role=findViewById(R.id.role);
        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        progress = new ProgressDialog(this);
        progress.setTitle("Registering you...");
        progress.setMessage("Please Wait");
        progress.setCancelable(false);
        login_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            }
        });
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String txt_name=name_edittxt1.getText().toString();
                String txt_email=email_edittxt1.getText().toString();
                String txt_pass=pass_edittxt1.getText().toString();
                if(TextUtils.isEmpty(txt_name)||TextUtils.isEmpty(txt_email)||TextUtils.isEmpty(txt_pass)){
                    Toast.makeText(SignUpActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else if ( txt_pass.length() < 6){
                    Toast.makeText(SignUpActivity.this, "Password must be atleast 6 characters", Toast.LENGTH_SHORT).show();
                }
                else if(radioButton.isChecked()||radioButton2.isChecked()){
                    signup(txt_name,txt_email,txt_pass);
                    progress.show();
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Choose your Role", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void signup(String name,String email,String password){
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(radioButton.isChecked()){
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                String userid =firebaseUser.getUid();
                                DatabaseReference myRef = database.getReference("Users").child(userid);
                                HashMap<String,String> hashMap = new HashMap<>();
                                hashMap.put("Type","Team Player");
                                hashMap.put("Name","Default");
                                hashMap.put("Nationality","Default");
                                hashMap.put("Type of Play","Default");
                                hashMap.put("Team Name","Default");
                                hashMap.put("Covid history","Default");
                                hashMap.put("Bio bubble Status","Default");
                                hashMap.put("User id",userid);
                                myRef.setValue(hashMap).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Intent intent=new Intent(SignUpActivity.this,InitialActivity.class);
                                            intent.putExtra("name",name);
                                            startActivity(intent);
                                            progress.dismiss();
                                        }
                                        else{
                                            progress.dismiss();
                                            Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else if(radioButton2.isChecked()){
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                String userid =firebaseUser.getUid();
                                DatabaseReference myRef = database.getReference("Users").child(userid);
                                HashMap<String,String> hashMap = new HashMap<>();
                                hashMap.put("User id",userid);
                                hashMap.put("Type","Officials");
                                hashMap.put("Name","Default");
                                hashMap.put("Nationality","Default");
                                hashMap.put("Role Type","Default");
                                hashMap.put("Covid history","Default");
                                hashMap.put("Bio bubble Status","Default");
                                myRef.setValue(hashMap).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Intent intent=new Intent(SignUpActivity.this,InitialActivity2.class);
                                            intent.putExtra("name",name);
                                            startActivity(intent);
                                            progress.dismiss();
                                        }
                                        else{
                                            progress.dismiss();
                                            Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                        else{
                            progress.dismiss();
                            Toast.makeText(SignUpActivity.this,task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}