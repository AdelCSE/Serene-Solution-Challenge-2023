package com.example.solutionchallengeapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class RegisterSuitActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    private ShapeableImageView registerProfilePicture;
    private Uri imageUri;
    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private TextInputEditText usernameEditTxt, phoneEditTxt;
    private TextInputLayout usernameLayout,countryLayout;
    private AppCompatButton continueBtn;
    private String imageUrl;
    private String[] countries = new String[]{"Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Palestine", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe"};
    private AutoCompleteTextView countryAutoComplete;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_suit);

        progressBar = findViewById(R.id.registerSuitProgressBar);
        countryLayout = findViewById(R.id.countryLayout);
        usernameLayout = findViewById(R.id.usernameLayout);
        usernameEditTxt = findViewById(R.id.usernameEditTxt);
        phoneEditTxt = findViewById(R.id.phoneEditTxt);
        countryAutoComplete = findViewById(R.id.countryEditTxt);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        CurrentUser = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference().child("profileImages");

        registerProfilePicture = findViewById(R.id.registerProfilePicture);
        registerProfilePicture.setOnClickListener(this);

        continueBtn = findViewById(R.id.continueButton);
        continueBtn.setOnClickListener(this);


        usernameEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                final String username = usernameEditTxt.getText().toString().trim();

                db.collection("users").whereEqualTo("Username", username)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    if (username.length()==0){
                                        usernameLayout.setError(null);

                                    }else if (username.length()>0 && username.length()<6){
                                        usernameLayout.setError("Username must contains at least 6 characters");

                                    }else if (task.getResult().size()>0) {
                                        usernameLayout.setError("This username is already taken. Please pick a new one!");

                                    } else usernameLayout.setError(null);
                                }
                                else Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        countryAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                final String country = countryAutoComplete.getText().toString().trim();
                if (!country.isEmpty()){
                    countryLayout.setError(null);
                }

            }
        });

        adapter = new ArrayAdapter<String>(this,R.layout.country_item,countries);
        countryAutoComplete.setAdapter(adapter);

        countryAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerProfilePicture:
                pickMedia();
                break;
            case R.id.continueButton:
                registerWithNewInfos();
                break;
        }
    }

    private void registerWithNewInfos() {
        final String usernameTxt = usernameEditTxt.getText().toString().trim();
        final String phoneTxt = phoneEditTxt.getText().toString().trim();
        final String countryTxt = countryAutoComplete.getText().toString().trim();

        if (usernameTxt.isEmpty()){
            usernameEditTxt.setError("Username is required!");
            usernameEditTxt.requestFocus();
            return;
        }

        if (usernameLayout.getError()!=null){
            usernameEditTxt.requestFocus();
            return;
        }

        if (countryTxt.isEmpty()){
            countryLayout.setError("Please select your country!");
            return;
        }else{
            countryLayout.setError(null);
        }

        DocumentReference df = db.collection("users").document(CurrentUser.getUid());

        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        HashMap<String, Object> user = new HashMap<>();
                        user.put("Username", usernameTxt);
                        user.put("Location", countryTxt);
                        if (!phoneTxt.isEmpty()) {
                            user.put("Phone", phoneTxt);
                        }
                        if (imageUri != null) {
                            uploadPicture(user,df);
                        }else{

                            df.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressBar.setVisibility(View.GONE);
                                    startActivity(new Intent(RegisterSuitActivity.this,RegisterSuitActivity2.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(RegisterSuitActivity.this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }
                }
            }
        });
    }

    private void uploadPicture(HashMap<String, Object> user, DocumentReference df) {

        StorageReference fileReference = storageReference.child(CurrentUser.getUid());

        fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUrl = uri.toString();

                        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot doc = task.getResult();
                                    if (doc.exists()) {
                                        user.put("ProfilePic", imageUrl);

                                        df.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressBar.setVisibility(View.GONE);
                                                startActivity(new Intent(RegisterSuitActivity.this,RegisterSuitActivity2.class));
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(RegisterSuitActivity.this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterSuitActivity.this, "Failed to Upload the picture. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void pickMedia() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        launcher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK
                        && result.getData() != null) {
                    imageUri = result.getData().getData();
                    registerProfilePicture.setBackground(null);
                    registerProfilePicture.setImageURI(imageUri);
                }
            }
    );
}