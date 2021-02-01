package com.example.mastekapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.mastekapp.Admin.AdminDashboard;
import com.example.mastekapp.User.UserDashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    String email, password, choice;
    TextView forgotPassword;
    EditText etEmail, etPassword;
    Button btnSignIn;
    RadioButton rbAdmin, rbUser;
    RadioGroup rgSignIn;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        rbAdmin = findViewById(R.id.rbAdmin);
        rbUser = findViewById(R.id.rbUser);
        rgSignIn = findViewById(R.id.rgSignIn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        rgSignIn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb) {
                    choice = rb.getText().toString();
                    Toast.makeText(getApplicationContext(), rb.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //LINKING TO FIREBASE
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        final DatabaseReference userNode = database.getReference("User");
        final DatabaseReference adminNode = database.getReference("Admin");
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(getApplicationContext());
                mDialog.setMessage("Please Wait...");
                mDialog.dismiss();

                if(choice.equals("User")){
                    email = etEmail.getText().toString();
                    password = etPassword.getText().toString();
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                userNode.child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.getValue() != null){
                                            Toast.makeText(getApplicationContext(), "Sign In successful ", Toast.LENGTH_SHORT).show();
                                            Intent AdminMenu =new Intent(getApplicationContext(), UserDashboard.class);
                                            startActivity(AdminMenu);
                                            finish();
                                        }
                                        else{
                                            firebaseAuth.signOut();
                                            Toast.makeText(getApplicationContext(), "Sign In failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                        }
                    });
                } else if(choice.equals("Admin")){
                    email = etEmail.getText().toString();
                    password = etPassword.getText().toString();
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                adminNode.child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.getValue() != null){
                                            Toast.makeText(getApplicationContext(), "Sign In successful ", Toast.LENGTH_SHORT).show();
                                            Intent AdminMenu =new Intent(getApplicationContext(), AdminDashboard.class);
                                            startActivity(AdminMenu);
                                            finish();
                                        }
                                        else{
                                            firebaseAuth.signOut();
                                            Toast.makeText(getApplicationContext(), "Sign In failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                        }
                    });
                } else{
                    Toast.makeText(getApplicationContext(), "Select your role", Toast.LENGTH_SHORT).show();
                }


            }
        });

//        forgotPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String email = etEmail.getText().toString().trim();
//
//                if (TextUtils.isEmpty(email)) {
//                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                progressBar.setVisibility(View.VISIBLE);
//
//                firebaseAuth.sendPasswordResetEmail(email)
//
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    Toast.makeText(getApplicationContext(), "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(getApplicationContext(), "Failed to send reset email!", Toast.LENGTH_SHORT).show();
//                                }
//
//                                progressBar.setVisibility(View.GONE);
//                            }
//                        });
//            }
//        });

    }
    public void onLoginClick(View View){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }

}

