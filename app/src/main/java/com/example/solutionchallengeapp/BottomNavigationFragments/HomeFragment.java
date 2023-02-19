package com.example.solutionchallengeapp.BottomNavigationFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.solutionchallengeapp.Models.UserModel;
import com.example.solutionchallengeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class HomeFragment extends Fragment {

    private View root;
    private UserModel userModel;
    private TextView username;
    private ShapeableImageView userPicture;

    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;
    private FirebaseFirestore db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);

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

        return root;
    }
}