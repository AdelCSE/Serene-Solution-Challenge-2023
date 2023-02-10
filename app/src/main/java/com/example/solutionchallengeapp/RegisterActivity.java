package com.example.solutionchallengeapp;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.solutionchallengeapp.Models.UserModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backBtn;
    private TextView loginBtn;
    private AppCompatButton SignUpBtn;
    private LinearLayout googleAuthBtn;
    private TextInputEditText nameEditTxt,emailEditTxt,passwordEditTxt,confirm_passwordEditTxt;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser CurrentUser;
    private UserModel userModel;

    private GoogleSignInClient mGoogleSignInClient;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEditTxt = findViewById(R.id.NameEditTxt);
        emailEditTxt = findViewById(R.id.EmailEditTxt);
        passwordEditTxt = findViewById(R.id.PasswordEditTxt);
        confirm_passwordEditTxt = findViewById(R.id.ConfirmPasswordEditTxt);
        progressBar = findViewById(R.id.registerProgressBar);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        backBtn = findViewById(R.id.sign_up_back_button);
        backBtn.setOnClickListener(this);
        loginBtn = findViewById(R.id.sign_in_switch_button);
        loginBtn.setOnClickListener(this);
        SignUpBtn = findViewById(R.id.sign_up_button);
        SignUpBtn.setOnClickListener(this);
        googleAuthBtn = findViewById(R.id.continue_google_button);
        googleAuthBtn.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_up_back_button:
                onBackPressed();
                break;
            case R.id.sign_in_switch_button:
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                break;
            case R.id.sign_up_button:
                firebaseAuthWithEmail();
                break;
            case R.id.continue_google_button:
                SignIn();
                break;
        }
    }

    private void SignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        launcher.launch(signInIntent);
    }

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK
                        && result.getData() != null) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    handleSignInResult(task);
                }
            }
    );

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        Exception exception = task.getException();
        if (task.isSuccessful()){
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken(),account.getEmail(),account.getDisplayName(),account.getFamilyName());


            } catch (ApiException e) {
                Toast.makeText(this, "signInResult:failed code=" + e.getStatusCode() , Toast.LENGTH_SHORT).show();
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            }
        }else{
            if (exception != null) {
                Log.w("RegisterActivity", exception.getMessage());
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken,String email,String dName,String fName) {
        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Check if user is already a member//
                            CurrentUser = mAuth.getCurrentUser();
                            db.collection("users").document(CurrentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot doc = task.getResult();
                                        if(doc.exists()) {
                                            userModel = doc.toObject(UserModel.class);
                                            String username = userModel.getUsername();
                                            ArrayList<String> causes = userModel.getCauses();
                                            if (username == null) {
                                                startActivity(new Intent(RegisterActivity.this, RegisterSuitActivity.class));
                                                finish();
                                            }else if (causes.size()<1){
                                                startActivity(new Intent(RegisterActivity.this, RegisterSuitActivity2.class));
                                                finish();
                                            } else {
                                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                                finish();
                                            }
                                        }else{
                                            String name = dName+" "+fName;
                                            registerUser(name,email);
                                        }
                                    }else{
                                        Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).getMessage() , Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Log.w("RegisterActivity", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private void firebaseAuthWithEmail() {
        final String fullNameTxt = nameEditTxt.getText().toString().trim();
        final String emailTxt = emailEditTxt.getText().toString().trim();
        final String passwordTxt = passwordEditTxt.getText().toString().trim();
        final String confirmPasswordTxt = confirm_passwordEditTxt.getText().toString().trim();

        if (fullNameTxt.isEmpty()){
            nameEditTxt.setError("Full name is required!");
            nameEditTxt.requestFocus();
            return;
        }

        if (emailTxt.isEmpty()){
            emailEditTxt.setError("Email address is required!");
            emailEditTxt.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches()){
            emailEditTxt.setError("Please provide a valid email!");
            emailEditTxt.requestFocus();
            return;
        }

        if (passwordTxt.isEmpty()){
            passwordEditTxt.setError("Password is required!");
            passwordEditTxt.requestFocus();
            return;
        }

        if (passwordTxt.length() < 6){
            passwordEditTxt.setError("Password must be 6 characters or more!");
            passwordEditTxt.requestFocus();
            return;
        }

        if (confirmPasswordTxt.isEmpty()){
            confirm_passwordEditTxt.setError("Please confirm your password!");
            confirm_passwordEditTxt.requestFocus();
            return;
        }

        if (!passwordTxt.equals(confirmPasswordTxt)){
            confirm_passwordEditTxt.setError("Passwords don't match!");
            confirm_passwordEditTxt.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(emailTxt,passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    registerUser(fullNameTxt,emailTxt);

                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, "Some error has occurred! " + task.getException().getMessage(), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

    }

    private void registerUser(String fullName, String email) {
        CurrentUser = mAuth.getCurrentUser();

        Date currentDate = Calendar.getInstance().getTime();
        ArrayList<String> followings = new ArrayList<>();
        ArrayList<String> followers = new ArrayList<>();
        ArrayList<String> causes = new ArrayList<>();

        Map<String, Object> user = new HashMap<>();
        user.put("UserId",CurrentUser.getUid());
        user.put("Name",fullName);
        user.put("Email",email);
        user.put("Username",null);
        user.put("About",null);
        user.put("Date",currentDate);
        user.put("Location",null);
        user.put("Phone",null);
        user.put("IsOrganisation",null);
        user.put("ProfilePic",null);
        user.put("Banner",null);
        user.put("Followers",followers);
        user.put("Followings",followings);
        user.put("Causes",causes);

        DocumentReference df = db.collection("users").document(CurrentUser.getUid());
        df.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(RegisterActivity.this,RegisterSuitActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this, "Some error has occured! " + e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        });

    }
}