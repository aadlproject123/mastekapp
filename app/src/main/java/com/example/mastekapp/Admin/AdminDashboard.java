package com.example.mastekapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mastekapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminDashboard extends AppCompatActivity {


    ArrayList<RequestModel> requestList = new ArrayList<>();

    RecyclerView requestRecycler;

    RequestAdapter requestAdapter;

    DatabaseReference requestNode = FirebaseDatabase.getInstance().getReference().child("Admin").child("Request");
    //FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        requestRecycler = findViewById(R.id.requestRecycler);

    }

    @Override
    public void onStart() {
        super.onStart();

        requestNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                requestList = new ArrayList<>();

                requestList.clear();
                int count = 0;
                for (DataSnapshot menuSnapshot: dataSnapshot.getChildren()) {
//                    if(menuSnapshot.getValue() instanceof String){
//                        Log.d("shloka", String.valueOf(menuSnapshot.getValue()));
//                        continue;
//                    }
                    RequestModel request = menuSnapshot.getValue(RequestModel.class);
                    requestList.add(request);
                    count++;
                }
                requestRecycler.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                requestRecycler.setLayoutManager(layoutManager);
                requestAdapter = new RequestAdapter(getApplicationContext(), requestList);
                requestRecycler.setAdapter(requestAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}