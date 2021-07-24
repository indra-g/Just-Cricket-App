package com.indrashekar.justcricket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class ResetPassActivity extends AppCompatActivity {
    MaterialButton reset_btn;
    TextInputEditText email_edittxt121;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        reset_btn=(MaterialButton)findViewById(R.id.reset_btn);
        email_edittxt121=(TextInputEditText)findViewById(R.id.email_edittxt121);
        mAuth = FirebaseAuth.getInstance();
        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email121=email_edittxt121.getText().toString();
                if(TextUtils.isEmpty(txt_email121)){
                    Toast.makeText(ResetPassActivity.this, "All the fields are required", Toast.LENGTH_SHORT).show();
                }
                else{
                    resetpass(txt_email121);
                }
            }
        });
    }
    public void resetpass(String email){
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(ResetPassActivity.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ResetPassActivity.this, "Check your Email", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ResetPassActivity.this,LoginActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(ResetPassActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}