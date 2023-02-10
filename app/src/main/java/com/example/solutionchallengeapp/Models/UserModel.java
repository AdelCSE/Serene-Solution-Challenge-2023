package com.example.solutionchallengeapp.Models;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;

public class UserModel implements Serializable {
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

    public String getAbout() {
        return About;
    }

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

    //This is for the first registration
    public UserModel(String userId, String name, String email) {
        UserId = userId;
        Name = name;
        Email = email;
    }

    public UserModel(){}

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
}
