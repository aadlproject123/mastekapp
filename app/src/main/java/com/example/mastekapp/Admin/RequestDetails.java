package com.example.mastekapp.Admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mastekapp.R;
import com.example.mastekapp.Admin.RequestModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RequestDetails extends AppCompatActivity  {
    ImageView ivimage;
    TextView etname, etemail, etflat, etpurpose, etreferral, etcategory;;
    Button grant, deny;


    String reqId;
    String name;
    String email;
    String flatNumber;
    String purposeOfVisit;
    String referral;
    String userCategory;
    String untrainedUserId;
    Uri image;

    ArrayList<String> imageList = new ArrayList<>();

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance ();
    DatabaseReference requestNode;
    DatabaseReference untrainedUserNode = database.getReference().child("UntrainedUsers");
    DatabaseReference userNode = database.getReference().child("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        ivimage = findViewById(R.id.ivimage);
        etname = findViewById(R.id.etname);
        etemail = findViewById(R.id.etemail);
        etflat = findViewById(R.id.etflat);
        etpurpose = findViewById(R.id.etpurpose);
        etreferral = findViewById(R.id.etreferral);
        etcategory = findViewById(R.id.etcategory);
        grant = findViewById(R.id.grant);
        deny = findViewById(R.id.deny);



        Intent i = getIntent();
        reqId = i.getStringExtra("id");
        name = i.getStringExtra("name");
        email = i.getStringExtra("email");
        flatNumber = i.getStringExtra("flatNumber");
        purposeOfVisit = i.getStringExtra("purposeOfVisit");
        referral = i.getStringExtra("referral");
        userCategory = i.getStringExtra("userCategory");
        image = Uri.parse(i.getStringExtra("image"));

        requestNode = database.getReference().child("Admin").child("Request").child(reqId);

        requestNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                imageList = (ArrayList<String>) snapshot.child("image").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        etname.setText(name);
        etemail.setText(email);
        etflat.setText(flatNumber);
        etpurpose.setText(purposeOfVisit);
        etreferral.setText(referral);
        etcategory.setText(userCategory);
        Picasso.get().load(image).into(ivimage);

        grant.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.createUserWithEmailAndPassword(email, "123456").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Fetching the uid after user creation
                        untrainedUserId = task.getResult().getUser().getUid();

                        // Set user details to untrainedUserNode for training the user data
                        untrainedUserNode.child(untrainedUserId).child("uId").setValue(untrainedUserId);
                        untrainedUserNode.child(untrainedUserId).child("reqId").setValue(reqId);
                        untrainedUserNode.child(untrainedUserId).child("name").setValue(name);
                        untrainedUserNode.child(untrainedUserId).child("email").setValue(email);
                        untrainedUserNode.child(untrainedUserId).child("flatNumber").setValue(flatNumber);
                        untrainedUserNode.child(untrainedUserId).child("purposeOfVisit").setValue(purposeOfVisit);
                        untrainedUserNode.child(untrainedUserId).child("referral").setValue(referral);
                        untrainedUserNode.child(untrainedUserId).child("userCategory").setValue(userCategory);
                        untrainedUserNode.child(untrainedUserId).child("image").setValue(imageList);
                        untrainedUserNode.child(untrainedUserId).child("approvedBy").setValue(firebaseAuth.getCurrentUser().getEmail());

                        // Store user data to userNode
                        userNode.child(untrainedUserId).child("uId").setValue(untrainedUserId);
                        userNode.child(untrainedUserId).child("reqId").setValue(reqId);
                        userNode.child(untrainedUserId).child("name").setValue(name);
                        userNode.child(untrainedUserId).child("email").setValue(email);
                        userNode.child(untrainedUserId).child("flatNumber").setValue(flatNumber);
                        userNode.child(untrainedUserId).child("purposeOfVisit").setValue(purposeOfVisit);
                        userNode.child(untrainedUserId).child("referral").setValue(referral);
                        userNode.child(untrainedUserId).child("userCategory").setValue(userCategory);
                        userNode.child(untrainedUserId).child("image").setValue(imageList);
                        userNode.child(untrainedUserId).child("approvedBy").setValue(firebaseAuth.getCurrentUser().getEmail());

                        // Delete request from the requestNode
                        requestNode.removeValue();

                        Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                        startActivity(i);
                        finish();
                    }
                });
            }
        });

        deny.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                requestNode.removeValue();

                Intent i = new Intent(getApplicationContext(), AdminDashboard.class);
                startActivity(i);
                finish();
            }
        });


    }


}