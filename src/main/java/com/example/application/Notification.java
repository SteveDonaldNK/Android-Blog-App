package com.example.application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Notification extends AppCompatActivity {
    ArrayList<NotificationModel> notificationModels;
    ListView listView;
    CollectionReference colRef;
    BottomNavigationView bottomNavigationView;
    public static NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        bottomNavigationView = findViewById(R.id.navigation_bar);
        bottomNavigationView.setSelectedItemId(R.id.notification);
        
        colRef = FirebaseFirestore.getInstance().collection("posts");

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
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), accueil.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), compte.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.notification:
                        return true;
                }

                return false;
            }
        });

        addItems();
    }

    public void addItems() {
        listView = (ListView)findViewById(R.id.notifications);
        notificationModels = new ArrayList<>();
        
        colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange change : value.getDocumentChanges()) {
                    if (change.getType() == DocumentChange.Type.ADDED) {
                        Toast.makeText(Notification.this, "added", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        notificationModels.add(new NotificationModel("Nouvelle publication de Steve Donald", "2h"));
        notificationModels.add(new NotificationModel("Nouvelle publication de Andrew Garfield", "3h"));
        notificationModels.add(new NotificationModel("Nouvelle publication de Steve Evans", "10h"));
        notificationModels.add(new NotificationModel("Nouvelle publication de Erling Halland", "12h"));
        notificationModels.add(new NotificationModel("Nouvelle publication de Paul Edwards", "4h"));

        adapter = new NotificationAdapter(notificationModels, getApplicationContext());

        listView.setAdapter(adapter);
    }
}