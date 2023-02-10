package com.example.solutionchallengeapp;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backBtn;
    private TextView SignUpSwitch, forgotPasswordBtn, errorText;
    private TextInputLayout passwordLayout,usernameLayout;
    private AppCompatButton loginBtn;
    private TextInputEditText userEditTxt,passwordEditTxt;
    private ProgressBar progressBar;

    private LinearLayout googleAuthBtn;
    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        errorText = findViewById(R.id.errorText);
        progressBar = findViewById(R.id.loginProgressBar);
        userEditTxt = findViewById(R.id.userEditTxt);
        passwordEditTxt = findViewById(R.id.loginPasswordEditTxt);
        passwordLayout = findViewById(R.id.loginPasswordLayout);
        usernameLayout = findViewById(R.id.userLayout);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        SignUpSwitch = findViewById(R.id.sign_up_switch_button);
        SignUpSwitch.setOnClickListener(this);

        backBtn = findViewById(R.id.sign_in_back_button);
        backBtn.setOnClickListener(this);

        loginBtn = findViewById(R.id.sign_in_button);
        loginBtn.setOnClickListener(this);

        forgotPasswordBtn = findViewById(R.id.forgot_password_button);
        forgotPasswordBtn.setOnClickListener(this);

        googleAuthBtn = findViewById(R.id.login_continue_google_button);
        googleAuthBtn.setOnClickListener(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        userEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                usernameLayout.setError(null);
            }
        });
        passwordEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                passwordLayout.setError(null);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_up_switch_button:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
            case R.id.sign_in_back_button:
                onBackPressed();
                break;
            case R.id.sign_in_button:
                loginUser();
                break;
            case R.id.forgot_password_button:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;
            case R.id.login_continue_google_button:
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

                firebaseLoginWithGoogle(account.getIdToken(),account.getEmail(),account.getDisplayName(),account.getFamilyName());


            } catch (ApiException e) {
                Toast.makeText(this, "signInResult:failed code=" + e.getStatusCode() , Toast.LENGTH_SHORT).show();
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            }
        }else{
            Log.w("LoginActivity", exception.toString());
        }
    }

    private void firebaseLoginWithGoogle(String idToken,String email,String dName,String fName) {
        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = mAuth.getCurrentUser();
                            db.collection("users").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot doc = task.getResult();
                                        if(doc.exists()) {
                                            userModel = doc.toObject(UserModel.class);
                                            String username = userModel.getUsername();
                                            ArrayList<String> causes = userModel.getCauses();
                                            if (username == null) {
                                                startActivity(new Intent(LoginActivity.this, RegisterSuitActivity.class));
                                                finish();
                                            }else if(causes.size()<1){
                                                startActivity(new Intent(LoginActivity.this, RegisterSuitActivity2.class));
                                                finish();
                                            } else {
                                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                finish();
                                            }
                                        }else{
                                            String name = dName+" "+fName;
                                            registerUser(name,email,currentUser);
                                        }
                                    }else{
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }else {

                            Log.w("RegisterActivity", "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }

    private void loginUser() {
        final String user = userEditTxt.getText().toString().trim();
        final String password = passwordEditTxt.getText().toString().trim();

        if (user.isEmpty()){
            userEditTxt.setError("Username is required!");
            return;
        }

        if (password.isEmpty()){
            passwordEditTxt.setError("Password is required!");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        if (Patterns.EMAIL_ADDRESS.matcher(user).matches()){
            mAuth.signInWithEmailAndPassword(user,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        //Check if user has completed registration//
                        currentUser = mAuth.getCurrentUser();
                        db.collection("users").document(currentUser.getUid())
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            DocumentSnapshot doc = task.getResult();
                                            if(doc.exists()){
                                                userModel = doc.toObject(UserModel.class);
                                                String username = userModel.getUsername();
                                                ArrayList<String> causes = userModel.getCauses();
                                                progressBar.setVisibility(View.GONE);
                                                if (username == null) {
                                                    startActivity(new Intent(LoginActivity.this,RegisterSuitActivity.class));
                                                    finish();

                                                }else if(causes.size() < 1){
                                                    startActivity(new Intent(LoginActivity.this,RegisterSuitActivity2.class));
                                                    finish();
                                                }else{
                                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                    finish();
                                                }
                                            }
                                        }
                                    }
                                });

                    }else{
                        progressBar.setVisibility(View.GONE);
                        passwordLayout.setError(" ");
                        usernameLayout.setError(" ");
                        errorText.setText("Your password is incorrect or this account doesn't exist!");
                        errorText.setVisibility(View.VISIBLE);

                    }
                }
            });
        }else if (Patterns.PHONE.matcher(user).matches()){
            db.collection("users").whereEqualTo("Phone", user)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                List<DocumentSnapshot> docs = task.getResult().getDocuments();
                                if (docs.size() != 0) {
                                    for (DocumentSnapshot document : docs) {
                                        userModel = document.toObject(UserModel.class);
                                        String email = userModel.getEmail();

                                            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        ArrayList<String> causes = userModel.getCauses();
                                                        if (causes.size()>0){
                                                            progressBar.setVisibility(View.GONE);
                                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                            finish();
                                                        }else{
                                                            startActivity(new Intent(LoginActivity.this,RegisterSuitActivity2.class));
                                                            finish();
                                                        }

                                                    } else {
                                                        progressBar.setVisibility(View.GONE);
                                                        passwordLayout.setError(" ");
                                                        errorText.setText("You've entered an incorrect password. Please try again!");
                                                        errorText.setVisibility(View.VISIBLE);

                                                    }
                                                }
                                            });

                                    }
                                }else{
                                    progressBar.setVisibility(View.GONE);
                                    usernameLayout.setError(" ");
                                    errorText.setText("You've entered an incorrect phone number. Please try again!");
                                    errorText.setVisibility(View.VISIBLE);
                                }


                            }else{
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }else{
            db.collection("users").whereEqualTo("Username", user)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()){
                                List<DocumentSnapshot> docs = task.getResult().getDocuments();
                                if (docs.size() != 0) {
                                    for (DocumentSnapshot document : docs){
                                        userModel = document.toObject(UserModel.class);
                                        String email = userModel.getEmail();
                                        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()){
                                                    ArrayList<String> causes = userModel.getCauses();
                                                    if (causes.size()>0) {
                                                        progressBar.setVisibility(View.GONE);
                                                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                                        finish();
                                                    }else {
                                                        progressBar.setVisibility(View.GONE);
                                                        startActivity(new Intent(LoginActivity.this,RegisterSuitActivity2.class));
                                                        finish();
                                                    }

                                                }else{
                                                    progressBar.setVisibility(View.GONE);
                                                    passwordLayout.setError(" ");
                                                    errorText.setText("You've entered an incorrect password. Please try again!");
                                                    errorText.setVisibility(View.VISIBLE);

                                                }
                                            }
                                        });

                                    }

                                }else{
                                    progressBar.setVisibility(View.GONE);
                                    usernameLayout.setError(" ");
                                    errorText.setText("You've entered an incorrect username. Please try again!");
                                    errorText.setVisibility(View.VISIBLE);
                                }


                            }else{
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void registerUser(String fullNameTxt,String emailTxt,FirebaseUser currentUser){

        Date currentDate = Calendar.getInstance().getTime();
        ArrayList<String> followings = new ArrayList<>();
        ArrayList<String> followers = new ArrayList<>();
        ArrayList<String> causes = new ArrayList<>();

        Map<String, Object> user = new HashMap<>();
        user.put("UserId",currentUser.getUid());
        user.put("Name",fullNameTxt);
        user.put("Email",emailTxt);
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

        DocumentReference df = db.collection("users").document(currentUser.getUid());
        df.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(LoginActivity.this,RegisterSuitActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}