package com.example.solutionchallengeapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;

public class UserModel implements Serializable, Parcelable {
    private String UserId, Name, Username, Email, Location, Phone, ProfilePic, About;
    private ArrayList<String> Followings,Followers,Causes;
    private Boolean IsOrganisation;
    private Timestamp Date;

    public UserModel(String userId, String name, String username, String email, String location, String phone, String profilePic, String about, ArrayList<String> followings, ArrayList<String> followers, ArrayList<String> causes, Boolean isOrganisation, Timestamp date) {
        UserId = userId;
        Name = name;
        Username = username;
        Email = email;
        Location = location;
        Phone = phone;
        ProfilePic = profilePic;
        About = about;
        Followings = followings;
        Followers = followers;
        Causes = causes;
        IsOrganisation = isOrganisation;
        Date = date;
    }

    public UserModel(){}

    protected UserModel(Parcel in) {
        UserId = in.readString();
        Name = in.readString();
        Username = in.readString();
        Email = in.readString();
        Location = in.readString();
        Phone = in.readString();
        ProfilePic = in.readString();
        About = in.readString();
        Followings = in.createStringArrayList();
        Followers = in.createStringArrayList();
        Causes = in.createStringArrayList();
        byte tmpIsOrganisation = in.readByte();
        IsOrganisation = tmpIsOrganisation == 1;
        Date = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getAbout() { return About; }

    public void setAbout(String about) {
        About = about;
    }

    public ArrayList<String> getFollowers() {
        return Followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        Followers = followers;
    }

    public ArrayList<String> getCauses() {
        return Causes;
    }

    public void setCauses(ArrayList<String> causes) {
        Causes = causes;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }

    public ArrayList<String> getFollowings() {
        return Followings;
    }

    public void setFollowings(ArrayList<String> followings) {
        Followings = followings;
    }

    public Boolean getOrganisation() {
        return IsOrganisation;
    }

    public void setOrganisation(Boolean organisation) {
        IsOrganisation = organisation;
    }

    public Timestamp getDate() {
        return Date;
    }

    public void setDate(Timestamp date) {
        Date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(UserId);
        dest.writeString(Name);
        dest.writeString(Username);
        dest.writeString(Email);
        dest.writeString(Location);
        dest.writeString(Phone);
        dest.writeString(ProfilePic);
        dest.writeString(About);
        dest.writeParcelable(Date, flags);
        dest.writeStringList(Followings);
        dest.writeStringList(Followers);
        dest.writeStringList(Causes);

    }
}
