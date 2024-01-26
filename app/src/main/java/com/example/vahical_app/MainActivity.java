package com.example.vahical_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {

    TextView fullname,email,phone;
    ImageView imageView;

    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phone =findViewById(R.id.pphone);
        fullname =findViewById(R.id.pfullname);
        email =findViewById(R.id.pEamil);
        imageView =findViewById(R.id.pimage);
        fAuth= FirebaseAuth.getInstance();
        fstore =FirebaseFirestore.getInstance();
        userId= fAuth.getCurrentUser().getUid();
        DocumentReference documentReference= fstore.collection("user").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
    //         phone.setText(DocumentSnapshot.getString("phone"));
//             fullname.setText(documentSnapshot.getString("fName"));
//             email.setText(documentSnapshot.getString("femail"));

            }
        });

    }
    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }


    public void logout(){
       String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        System.out.println(uid);

    }

}