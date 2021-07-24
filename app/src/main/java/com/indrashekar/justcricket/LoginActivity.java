package com.indrashekar.justcricket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    TextView signup_txt,forget_pass;
    TextInputEditText email_edittxt11,pass_edittxt11;
    MaterialButton login_btn1;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signup_txt=(TextView)findViewById(R.id.signup_txt);
        forget_pass=(TextView)findViewById(R.id.forget_pass);
        email_edittxt11=(TextInputEditText)findViewById(R.id.email_edittxt11);
        pass_edittxt11=(TextInputEditText)findViewById(R.id.pass_edittxt11);
        login_btn1=(MaterialButton)findViewById(R.id.login_btn1);
        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        progress = new ProgressDialog(this);
        progress.setTitle("We are logging you in...");
        progress.setMessage("Please Wait");
        progress.setCancelable(false);
        login_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email1=email_edittxt11.getText().toString();
                String txt_pass1=pass_edittxt11.getText().toString();
                if(TextUtils.isEmpty(txt_email1)||TextUtils.isEmpty(txt_pass1)){
                    Toast.makeText(LoginActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else if ( txt_pass1.length() < 6){
                    Toast.makeText(LoginActivity.this, "Enter your Password Correctly", Toast.LENGTH_SHORT).show();
                }
                else{
                    progress.show();
                    login(txt_email1,txt_pass1);
                }
            }
        });
        signup_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                finish();
            }
        });
        forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ResetPassActivity.class));
                finish();
            }
        });
    }
    public void login(String email,String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String userid = firebaseUser.getUid();
                    DatabaseReference mRef = database.getReference("Users").child(userid).child("Type");
                    mRef.get().addOnCompleteListener(LoginActivity.this, new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(String.valueOf(task.getResult().getValue()).equals("Team Player")){
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                progress.dismiss();
                                finish();
                            }
                            else{
                                startActivity(new Intent(LoginActivity.this,OffcialsActivity.class));
                                progress.dismiss();
                                finish();
                            }
                        }
                    });
                }
                else{
                    progress.dismiss();
                    Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}