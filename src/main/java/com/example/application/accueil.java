package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class accueil extends AppCompatActivity {
    ArrayList<DataModel> dataModels;
    BottomNavigationView bottomNavigationView;
    ConstraintLayout container;
    private ListView listView;
    private static PostAdapter adapter;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    CollectionReference userRef;
    CollectionReference infoRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        bottomNavigationView = findViewById(R.id.navigation_bar);
        bottomNavigationView.setSelectedItemId(R.id.home);
        container = findViewById(R.id.container);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userRef = firestore.collection("posts");
        infoRef = firestore.collection("users");

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.bookmark:
                        startActivity(new Intent(getApplicationContext(), enregistrement.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.composs:
                        startActivity(new Intent(getApplicationContext(), Composs.class));
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
                    case R.id.home:
                        return true;
                }

                return false;
            }
        });

        addItems();
    }

    public void addItems() {
        listView = (ListView)findViewById(R.id.post);
        dataModels = new ArrayList<>();

        userRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String data = document.getString("post");
                        infoRef.whereEqualTo("uid", document.getString("publishedBy")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    dataModels.add(new DataModel(doc.getString("user"), data, "1min de lecture", "10h", document.getId()));
                                }
                                adapter = new PostAdapter(dataModels, getApplicationContext());
                                listView.setAdapter(adapter);
                            }
                        });
                    }
                }
            }
        });

    }
}