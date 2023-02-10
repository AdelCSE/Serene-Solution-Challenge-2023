package com.example.solutionchallengeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterSuitActivity2 extends AppCompatActivity {

    private ProgressBar progressBar;
    private AppCompatButton joinBtn;
    private RadioGroup radioGroup;
    private RadioButton organisationButton,volunteerButton, radioButton;
    private Chip cause1, cause2, cause3, cause4, cause5, cause6, cause7, cause8, cause9, cause10;
    private ArrayList<String> selectedCauses = new ArrayList<>();

    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_suit2);

        progressBar = findViewById(R.id.registrationSuit2ProgressBar);
        joinBtn = findViewById(R.id.join_button);
        radioGroup = findViewById(R.id.radio_grp);
        organisationButton = findViewById(R.id.radio_organisation);
        volunteerButton = findViewById(R.id.radio_volunteer);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        CurrentUser = mAuth.getCurrentUser();

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishRegistrationProcess();
            }
        });

        //Init Chips
        cause1 = findViewById(R.id.cause1);
        cause2 = findViewById(R.id.cause2);
        cause3 = findViewById(R.id.cause3);
        cause4 = findViewById(R.id.cause4);
        cause5 = findViewById(R.id.cause5);
        cause6 = findViewById(R.id.cause6);
        cause7 = findViewById(R.id.cause7);
        cause8 = findViewById(R.id.cause8);
        cause9 = findViewById(R.id.cause9);
        cause10 = findViewById(R.id.cause10);

        SetCheckedCauses();

    }

    private void SetCheckedCauses()
    {
        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    selectedCauses.add(compoundButton.getText().toString());
                }
                else
                {
                    selectedCauses.remove(compoundButton.getText().toString());
                }
            }
        };

        cause1.setOnCheckedChangeListener(checkedChangeListener);
        cause2.setOnCheckedChangeListener(checkedChangeListener);
        cause3.setOnCheckedChangeListener(checkedChangeListener);
        cause4.setOnCheckedChangeListener(checkedChangeListener);
        cause5.setOnCheckedChangeListener(checkedChangeListener);
        cause6.setOnCheckedChangeListener(checkedChangeListener);
        cause7.setOnCheckedChangeListener(checkedChangeListener);
        cause8.setOnCheckedChangeListener(checkedChangeListener);
        cause9.setOnCheckedChangeListener(checkedChangeListener);
        cause10.setOnCheckedChangeListener(checkedChangeListener);
    }

    private void finishRegistrationProcess(){
        final boolean IsOrganisation;

        if (organisationButton.isChecked() || volunteerButton.isChecked() ){
            int selectedId = radioGroup.getCheckedRadioButtonId();
            radioButton = (RadioButton) findViewById(selectedId);
            String TypeTxt = radioButton.getText().toString().trim();

            if(TypeTxt.isEmpty()){
                Toast.makeText(this, "Please select your role", Toast.LENGTH_SHORT).show();
                return;
            }else if (TypeTxt.equals("Organisation")){
                IsOrganisation = true;
            }else{
                IsOrganisation = false;
            }
        } else {
            Toast.makeText(this, "Please select your role", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedCauses.size()<1)
        {
            Toast.makeText(this, "Please select at least one cause", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        DocumentReference df = db.collection("users").document(CurrentUser.getUid());

        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        HashMap<String, Object> user = new HashMap<>();
                        user.put("IsOrganisation", IsOrganisation);
                        user.put("Causes", selectedCauses);

                        df.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(RegisterSuitActivity2.this,MainActivity.class));
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(RegisterSuitActivity2.this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }
            }
        });
    }
}