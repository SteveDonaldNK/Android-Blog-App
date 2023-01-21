package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Composs extends AppCompatActivity {

    String date;
    BottomNavigationView bottomNavigationView;
    EditText postBody;
    Button publishBtn, cancelBtn;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    FirebaseAuth auth;
    CollectionReference collectionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_composs);

        bottomNavigationView = findViewById(R.id.navigation_bar);
        publishBtn = findViewById(R.id.confirm);
        cancelBtn = findViewById(R.id.cancel);
        postBody = findViewById(R.id.postBody);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        collectionRef = db.collection("posts");

        bottomNavigationView.setSelectedItemId(R.id.composs);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), accueil.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.bookmark:
                        startActivity(new Intent(getApplicationContext(), enregistrement.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.notification:
                        startActivity(new Intent(getApplicationContext(), Notification.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), compte.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.composs:
                        return true;
                }

                return false;
            }
        });

        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postContent = postBody.getText().toString();
                currentUser = auth.getCurrentUser();
                Map<String, Object> data = new HashMap<>();
                data.put("post", postContent);
                data.put("publishedBy", currentUser.getUid());
                data.put("publishedDate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                data.put("publishedAt", new SimpleDateFormat("HH:mm:ss").format(new Date()));
                collectionRef.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(Composs.this.getApplicationContext(), "saved", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Composs.this.getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }
}