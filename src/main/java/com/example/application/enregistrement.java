package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class enregistrement extends AppCompatActivity {
    ArrayList<saveModel> saveModels;
    BottomNavigationView bottomNavigationView;
    ConstraintLayout container;
    private ListView listView;
    private static saveAdapter adapter;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    FirebaseUser user;
    CollectionReference colRef;
    CollectionReference postRef;
    CollectionReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enregistrement);

        bottomNavigationView = findViewById(R.id.navigation_bar);
        bottomNavigationView.setSelectedItemId(R.id.bookmark);
        container = findViewById(R.id.container);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        colRef = firestore.collection("saved");
        postRef = firestore.collection("posts");
        userRef = firestore.collection("users");

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), accueil.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.notification:
                        startActivity(new Intent(getApplicationContext(), Notification.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.composs:
                        startActivity(new Intent(getApplicationContext(), Composs.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), compte.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.bookmark:
                        return true;
                }

                return false;
            }
        });

        addItems();
    }

    public void addItems() {
        listView = (ListView)findViewById(R.id.savedPost);
        saveModels = new ArrayList<>();


        colRef.whereEqualTo("savedBy", user.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String postid = document.getString("postid");
                        postRef.document(postid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot res = task.getResult();
                                userRef.whereEqualTo("uid", user.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (QueryDocumentSnapshot doc : task.getResult()) {
                                            saveModels.add(new saveModel(doc.getString("user"), res.getString("post"), "1min de lecture", "10h"));
                                        }
                                        adapter = new saveAdapter(saveModels, getApplicationContext());
                                        listView.setAdapter(adapter);}
                                });
                            }
                        });
                    }
                }
            }
        });

    }
}