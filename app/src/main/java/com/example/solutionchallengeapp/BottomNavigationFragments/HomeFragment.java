package com.example.solutionchallengeapp.BottomNavigationFragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.solutionchallengeapp.Adapters.EventsAdapter;
import com.example.solutionchallengeapp.GetStartedActivity;
import com.example.solutionchallengeapp.Models.EventModel;
import com.example.solutionchallengeapp.Models.UserModel;
import com.example.solutionchallengeapp.R;
import com.example.solutionchallengeapp.SearchActivity;
import com.example.solutionchallengeapp.SettingsActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private View root;
    private UserModel userModel;
    private TextView username;
    private ImageView settingsBtn;
    private ShapeableImageView userPicture;
    private RelativeLayout searchBtn;
    private RecyclerView recyclerView;
    private EventsAdapter eventsAdapter;
    private List<EventModel> EventsDataHolder;

    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;
    private FirebaseFirestore db;

    private GoogleSignInClient mGoogleSignInClient;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);

        searchBtn = root.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);

        settingsBtn = root.findViewById(R.id.settingsBtn);
        settingsBtn.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        username = root.findViewById(R.id.homeUsername);
        userPicture = root.findViewById(R.id.homeUserPicture);

        //Set User Infos
        Intent intent = getActivity().getIntent();
        userModel = (UserModel) intent.getSerializableExtra("UserInfos");

        if (userModel!=null){
            username.setText(userModel.getUsername());
            if (userModel.getProfilePic()!= null){
                Glide.with(getActivity()).load(userModel.getProfilePic()).into(userPicture);
            }else {
                //TODO glide with default picture1
            }
        }else{
            db.collection("users").document(CurrentUser.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot doc = task.getResult();
                                if (doc.exists()){
                                    userModel = doc.toObject(UserModel.class);
                                    assert userModel != null;
                                    username.setText(userModel.getUsername());
                                    if (userModel.getProfilePic() != null){
                                        Glide.with(getActivity()).load(userModel.getProfilePic()).into(userPicture);
                                    }else{
                                        //TODO glide with default picture2
                                    }
                                }
                            }
                        }
                    });
        }

        EventModel event1 = new EventModel("e01","u01","Food for Thought: Join the Drive to Stock Up for Those in Need","about1","Croissant Rouge Algerien","location1","https://firebasestorage.googleapis.com/v0/b/solution-challenge-2023.appspot.com/o/profileImages%2Fjoel-muniz-3k3l2brxmwQ-unsplash.jpg?alt=media&token=b08f1a5d-f766-44c1-bbf4-66797bd8bf49","https://firebasestorage.googleapis.com/v0/b/solution-challenge-2023.appspot.com/o/profileImages%2F%D8%A7%D9%84%D9%87%D9%84%D8%A7%D9%84_%D8%A7%D9%84%D8%A3%D8%AD%D9%85%D8%B1_%D8%A7%D9%84%D8%AC%D8%B2%D8%A7%D8%A6%D8%B1%D9%8A.jpg?alt=media&token=45409a20-1b15-4bc0-a384-07ac701801c6",120,"Feb 04th, 2023 • 9:30 AM");
        EventModel event2 = new EventModel("e02","u02","Beach Clean-Up Brigade: Join the Effort to Keep Our Coasts Beautiful","about2","Croissant Rouge Algerien","location2","https://firebasestorage.googleapis.com/v0/b/solution-challenge-2023.appspot.com/o/profileImages%2Fbrian-yurasits-PzQNdXw2a6g-unsplash.jpg?alt=media&token=99c42f38-846b-4f69-8443-23b52937e24f","https://firebasestorage.googleapis.com/v0/b/solution-challenge-2023.appspot.com/o/profileImages%2F%D8%A7%D9%84%D9%87%D9%84%D8%A7%D9%84_%D8%A7%D9%84%D8%A3%D8%AD%D9%85%D8%B1_%D8%A7%D9%84%D8%AC%D8%B2%D8%A7%D8%A6%D8%B1%D9%8A.jpg?alt=media&token=45409a20-1b15-4bc0-a384-07ac701801c6",120,"Feb 14th, 2023 • 8:00 AM");
        EventModel event3 = new EventModel("e03","u03","Help Distribute Food and Bring Smiles to Our Community","about3","Croissant Rouge Algerien","location3","https://firebasestorage.googleapis.com/v0/b/solution-challenge-2023.appspot.com/o/profileImages%2Fjoel-muniz-A4Ax1ApccfA-unsplash.jpg?alt=media&token=8bd45bc5-b874-454a-a25d-9cd5df659167","https://firebasestorage.googleapis.com/v0/b/solution-challenge-2023.appspot.com/o/profileImages%2F%D8%A7%D9%84%D9%87%D9%84%D8%A7%D9%84_%D8%A7%D9%84%D8%A3%D8%AD%D9%85%D8%B1_%D8%A7%D9%84%D8%AC%D8%B2%D8%A7%D8%A6%D8%B1%D9%8A.jpg?alt=media&token=45409a20-1b15-4bc0-a384-07ac701801c6",120,"Mar 31th, 2023 • 12:00 PM");
        EventsDataHolder = new ArrayList<EventModel>();
        EventsDataHolder.add(event1);
        EventsDataHolder.add(event2);
        EventsDataHolder.add(event3);

        BuildRecyclerView();

        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();

        mGoogleSignInClient= GoogleSignIn.getClient(getActivity(),gso);

        return root;

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.searchBtn:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.settingsBtn:
                logoutUser();
                //startActivity(new Intent(getActivity(), SettingsActivity.class));
        }
    }

    private void BuildRecyclerView() {
        recyclerView = root.findViewById(R.id.eventsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        eventsAdapter = new EventsAdapter(EventsDataHolder, getContext());
        recyclerView.setAdapter(eventsAdapter);
    }

    private void logoutUser() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getActivity(), GetStartedActivity.class));
                        getActivity().finish();
                    }
                });
    }
}