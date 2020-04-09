package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPass;

    private TextView textViewLoginAct;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPass=findViewById(R.id.editTextPass);
        textViewLoginAct=findViewById(R.id.textViewLoginAct);
        mAuth = FirebaseAuth.getInstance();
        textViewLoginAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    public void onClikcAddUser(View view) {
        String email=editTextEmail.getText().toString().trim();
        String pass=editTextPass.getText().toString().trim();
        if (email.equals("")|| pass.equals("")){
            Toast.makeText(this, "Заполните поля", Toast.LENGTH_SHORT).show();
        }else {
            mAuth.createUserWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                            Intent intent =new Intent(RegisterActivity.this,MainActivity.class);
                            startActivity(intent);}
                            else {
                                Toast.makeText(RegisterActivity.this, "Fail "+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }
}
