package com.example.mastekapp.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mastekapp.Admin.RequestModel;
import com.example.mastekapp.BuildConfig;
import com.example.mastekapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class UserDashboard extends AppCompatActivity {

    EditText etname, etemail, etpurpose, etflat, etcategory;
    Button btnRequest, btnImage, btnSelect;
    File file;
    Uri imageurl;

    private static final int PICK_IMG = 1;

    int count = 0;
    String title = "no title";
    String reqId, name, email, purpose, flat, category, referral;
    ArrayList<Uri> imageList = new ArrayList<>();
    ArrayList<String> firebaseImageList = new ArrayList<>();
    Task<Uri> downloadUrl;
    int counter = 0;
    int c = 0;
    boolean flag = true;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference requestNode = FirebaseDatabase.getInstance().getReference().child("Admin").child("Request");
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        etname = findViewById(R.id.etname);
        etemail = findViewById(R.id.etemail);
        etpurpose = findViewById(R.id.etpurpose);
        etflat = findViewById(R.id.etflat);
        etcategory = findViewById(R.id.etcategory);
        btnImage = findViewById(R.id.btnImage);
        btnRequest = findViewById(R.id.btnRequest);
        btnSelect = findViewById(R.id.btnSelect);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_IMG);
            }
        });

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                StorageReference storageRef = firebaseStorage.getReference().child(etemail.getText().toString()).child(title);
                if (hasPermissionsCamera()) {
//                    takePhoto();
//                    uploadImage();
                } else {
                    requestPermissionCamera();
                }
            }
        });

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = etname.getText().toString();
                purpose = etpurpose.getText().toString();
                flat = etflat.getText().toString();
                category = etcategory.getText().toString();
                referral = firebaseAuth.getCurrentUser().getEmail();
                email = etemail.getText().toString();
                while(flag) {
                    if (counter == 0) {
                        flag = upload();
                        counter += 1;
//                        Log.d("shloka", String.valueOf(counter));
                    }
                }
//                uploadImage();
//                uploadFile(name, email, purpose, flat, category, referral);
                Toast.makeText(getApplicationContext(), "Request sent!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*  Method for checking permission for Camera  */
    public boolean hasPermissionsCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    /* Method for requesting to grant permission for Camera */
    public void requestPermissionCamera() {
        String[] permissions = new String[]{Manifest.permission.CAMERA};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 1);
        }
    }

    /* Method for taking photo through Camera */
    private void takePhoto() {
        for(int i = count; i < 10; i++){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            file = new File(this.getExternalCacheDir(), String.valueOf(System.currentTimeMillis() + "jpg"));
            imageurl = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);
            imageList.add(imageurl);
            count += 1;
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageList);
            this.startActivityForResult(intent, 0);
        }
    }
//
//    private void uploadImage(){
//        StorageReference storageRef = firebaseStorage.getReference().child(email).child(title);
//        for(int i = 0; i < imageList.size(); i++){
//            int c = i;
//            Log.d("shloka", imageList.get(i).toString());
//            UploadTask uploadTask = storageRef.putFile(imageList.get(i));
//            downloadUrl = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                @Override
//                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                    if (!task.isSuccessful()) {
//                        throw task.getException();
//                    }
//                    Toast.makeText(getApplicationContext(),"Upload Complete!!"+ c,Toast.LENGTH_SHORT).show();
//                    return storageRef.getDownloadUrl();
//                }
//            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if (task.isSuccessful()) {
//                        Uri downloadUrl = task.getResult();
//                        //Log.d("shloka", downloadUrl.toString());
//                        firebaseImageList.add(downloadUrl.toString());
//                    } else {
//                        Toast.makeText(getApplicationContext(),"Upload image failed!!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK) {
//            if (requestCode == 0) {
//                title = getTitleOfFile(file.toString());
//            }
//            if (requestCode == 1) {
//                imageurl = data.getData();
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                String picturePath = getPath(filePathColumn);
//                title = getTitleOfFile(picturePath);
//            }
//        }
//    }
//
//    /* Method for getting name of file */
//    public String getTitleOfFile(String filePath) {
//        char[] title = filePath.toCharArray();
//        String finalTitle = "";
//        for (int count = title.length - 1; count >= 0; count--) {
//            if (title[count] == '/')
//                break;
//            finalTitle = finalTitle + title[count];
//        }
//        return new StringBuilder(finalTitle).reverse().toString();
//    }
//
//    /* Method for getting path of file */
//    public String getPath(String[] filePathColumn) {
//        Cursor cursor = getContentResolver().query(imageurl,
//                filePathColumn, null, null, null);
//        cursor.moveToFirst();
//        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//        String filepath = cursor.getString(columnIndex);
//        cursor.close();
//        return filepath;
//    }

    /* Method for uploading image in Firebase Storage and send data to Database */
    public void uploadFile(final String name, final String email, final String purpose, final String flat, final String category, final String referral) {
        //if (!title.equals("no title")) {
            // Create request object and add to database
            String key = requestNode.push().getKey();
            RequestModel request = new RequestModel();
            request.setName(name);
            request.setEmail(email);
            request.setPuroseOfVisit(purpose);
            request.setFlatNumber(flat);
            request.setUserCategory(category);
            request.setReferral(firebaseAuth.getUid());
            request.setImage(firebaseImageList);
            request.setReqId(key);
            requestNode.child(key).setValue(request);
//            Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(intent);
//        } else {
//            Toast.makeText(getApplicationContext(),"Upload images!!", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMG) {
            if (resultCode == RESULT_OK) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();

                    int CurrentImageSelect = 0;

                    while (CurrentImageSelect < count) {
                        Uri imageuri = data.getClipData().getItemAt(CurrentImageSelect).getUri();
                        imageList.add(imageuri);
                        CurrentImageSelect = CurrentImageSelect + 1;
                    }
                }
            }
        }
    }

    public boolean upload() {
        final StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child(email);
        for (int i = 0; i < imageList.size(); i++) {
            Uri Image = imageList.get(i);
            final StorageReference imagename = ImageFolder.child("image/" + Image.getLastPathSegment());
            c = i;
            imagename.putFile(imageList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            firebaseImageList.add(uri.toString());
                            if(imageList.size()==firebaseImageList.size()){
                                uploadFile(name, email, purpose, flat, category, referral);
                            }
                        }
                    });
                }
            });
        }
        return false;
    }
}