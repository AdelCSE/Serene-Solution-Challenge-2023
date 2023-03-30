package com.example.solutionchallengeapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.solutionchallengeapp.Models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddEventActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView image;
    private TextView username;
    private AppCompatButton publishButton;
    private ImageView closeBtn, addPictureBtn;
    private EditText titleEditText, descriptionEditText;

    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private UserModel userModel;

    private Uri resultUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        image = findViewById(R.id.addEventOrgImage);
        username = findViewById(R.id.addEventUsername);
        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);

        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("eventImages/"+CurrentUser.getUid());

        publishButton = findViewById(R.id.publishButton);
        publishButton.setOnClickListener(this);

        closeBtn = findViewById(R.id.closeAddPostButton);
        closeBtn.setOnClickListener(this);

        addPictureBtn = findViewById(R.id.addEventPicture);
        addPictureBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.closeAddPostButton:
                onBackPressed();
                break;
            case R.id.publishButton:
                publishEvent();
                break;
            case R.id.addEventPicture:
                pickMedia();
                break;
        }
    }



    private void publishEvent() {
    }

    private void pickMedia() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startForMediaPickerResult.launch(intent);
    }

    private final ActivityResultLauncher<Intent> startForMediaPickerResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null && result.getResultCode() == Activity.RESULT_OK) {
                    resultUri = data.getData();
                    if (resultUri != null)
                    {
                        Toast.makeText(AddEventActivity.this, "Great", Toast.LENGTH_SHORT).show();
                        addPictureBtn.setImageURI(resultUri);
                    }
                }
                else {
                    Toast.makeText(AddEventActivity.this, "Some error has occurred, try again later!", Toast.LENGTH_SHORT).show();
                }
            });
}