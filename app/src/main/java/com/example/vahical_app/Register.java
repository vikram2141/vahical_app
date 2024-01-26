package com.example.vahical_app;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText mFullname,mEmail, mPassword,mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fstore;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFullname = findViewById(R.id.Fullname);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.Password);
        mPhone = findViewById(R.id.phone);
        mRegisterBtn =findViewById(R.id.RegisterBtn);
        mLoginBtn =findViewById(R.id.creatText);
        fAuth =FirebaseAuth.getInstance();
        progressBar =findViewById(R.id.ProgressBar);
        fstore =FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email  =mEmail.getText().toString().trim();
                String password =mPassword.getText().toString().trim();
                String fullname =mFullname.getText().toString();
                String phone =mPhone.getText().toString();

                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email  is Required.");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("password is required.");
                    return;
                }
                if (password.length() <6){
                    mPassword.setError("password Must  be >= 6 characters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
//                register the user in firebase
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Register.this, "user created", Toast.LENGTH_SHORT).show();
                            userId =fAuth.getCurrentUser().getUid();

                            DocumentReference documentReference =fstore.collection("user").document(userId);
                            Map<String,Object>user =new HashMap<>();
                            user.put("fName",fullname);
                            user.put("email",email);
                            user.put("phone",phone);

                            documentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG,"onSuccess : user profile is created for"+userId);
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else {
                            Toast.makeText(Register.this, "Error !"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }

}