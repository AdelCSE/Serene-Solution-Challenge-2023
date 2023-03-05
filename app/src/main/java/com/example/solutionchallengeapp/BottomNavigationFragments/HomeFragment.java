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
import com.example.solutionchallengeapp.Models.EventModel;
import com.example.solutionchallengeapp.Models.UserModel;
import com.example.solutionchallengeapp.R;
import com.example.solutionchallengeapp.SearchActivity;
import com.example.solutionchallengeapp.SettingsActivity;
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

        EventModel event1 = new EventModel("e01","u01","title1","about1","name1","location1","https://firebasestorage.googleapis.com/v0/b/solution-challenge-2023.appspot.com/o/profileImages%2Fi3byz6zfDuNZ2uT9skLbx5NqjQ93?alt=media&token=8924c6c8-1731-4e9e-9212-564e03623c72",null,120,"Feb 14th, 2023");
        EventModel event2 = new EventModel("e02","u02","title2","about2","name2","location2","https://firebasestorage.googleapis.com/v0/b/solution-challenge-2023.appspot.com/o/profileImages%2Fi3byz6zfDuNZ2uT9skLbx5NqjQ93?alt=media&token=8924c6c8-1731-4e9e-9212-564e03623c72",null,120,"Feb 14th, 2023");
        EventModel event3 = new EventModel("e03","u03","title3","about3","name3","location3","https://firebasestorage.googleapis.com/v0/b/solution-challenge-2023.appspot.com/o/profileImages%2Fi3byz6zfDuNZ2uT9skLbx5NqjQ93?alt=media&token=8924c6c8-1731-4e9e-9212-564e03623c72",null,120,"Feb 14th, 2023");
        EventModel event4 = new EventModel("e04","u04","title4","about4","name4","location4","https://firebasestorage.googleapis.com/v0/b/solution-challenge-2023.appspot.com/o/profileImages%2Fi3byz6zfDuNZ2uT9skLbx5NqjQ93?alt=media&token=8924c6c8-1731-4e9e-9212-564e03623c72",null,120,"Feb 14th, 2023");
        EventsDataHolder = new ArrayList<EventModel>();
        EventsDataHolder.add(event1);
        EventsDataHolder.add(event2);
        EventsDataHolder.add(event3);
        EventsDataHolder.add(event4);

        BuildRecyclerView();

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
                startActivity(new Intent(getActivity(), SettingsActivity.class));
        }
    }

    private void BuildRecyclerView() {
        recyclerView = root.findViewById(R.id.eventsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        eventsAdapter = new EventsAdapter(EventsDataHolder, getContext());
        recyclerView.setAdapter(eventsAdapter);
    }
}