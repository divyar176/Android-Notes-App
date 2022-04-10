package com.example.notesapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity {
    private EditText etName;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ImageView ivProfile;
    private StorageReference mRootStorage;

    Uri localFileUri, serverFileUri;
    ProgressDialog pd;

    ActivityResultLauncher<Intent> pick = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode()== Activity.RESULT_OK){
                Intent data = result.getData();
                localFileUri = data.getData();
                ivProfile.setImageURI(localFileUri);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        mRootStorage = FirebaseStorage.getInstance().getReference();
        etName = findViewById(R.id.etName);
        mAuth = FirebaseAuth.getInstance();
        ivProfile = findViewById(R.id.ivProfile);
        user = mAuth.getCurrentUser();
        if (user != null) {
            etName.setText(user.getDisplayName());
            Uri photoUri = user.getPhotoUrl();
            if(photoUri!=null)
            {
                Glide.with(this)
                        .load(photoUri)
                        .placeholder(R.drawable.profile)
                        .into(ivProfile);
            }
        }
    }

    public void pickImage(View v)
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pick.launch(intent);
    }
    private void updateOnlyName() {
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setDisplayName(etName.getText().toString())
                .build();

        user.updateProfile(request).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to update Profile:" + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void updateNameAndPhoto() {

        String file_name = user.getUid() + ".jpg";
        final StorageReference fileRef = mRootStorage.child("images/" + file_name);
        fileRef.putFile(localFileUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                serverFileUri = uri;
                                UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(etName.getText().toString())
                                        .setPhotoUri(serverFileUri)
                                        .build();
                                user.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ProfileActivity.this, "Failed to update Profile:" + task.getException(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
    }

    public void btnSaveClick(View view) {
        if (etName.getText().toString().trim().equals("")) {
            etName.setError("Enter Name");
        } else {
            if (localFileUri != null) {
                updateNameAndPhoto();
            } else {
                updateOnlyName();
            }
        }
    }

    public  void btnLogoutClick(View view)
    {
        mAuth.signOut();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}