package com.example.solutionchallengeapp.Models;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;

public class EventModel {

    private String EventId, UserId, Title, About, OrganisationName, Location, EventPicture, OrganisationPicture;
    private int Participants;
    private String Date;

    public EventModel(String eventId, String userId, String title, String about, String organisationName, String location, String eventPicture, String organisationPicture, int participants, String date) {
        EventId = eventId;
        UserId = userId;
        Title = title;
        About = about;
        OrganisationName = organisationName;
        Location = location;
        EventPicture = eventPicture;
        OrganisationPicture = organisationPicture;
        Participants = participants;
        Date = date;
    }

    /*public String ConvertDate() {
        SimpleDateFormat sfd = new SimpleDateFormat("MMM d, yyyy • hh:mm aaa");
        return sfd.format(getDate().toDate());
    }*/

    public EventModel() {
    }

    public String getEventId() {
        return EventId;
    }

    public void setEventId(String eventId) {
        EventId = eventId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        About = about;
    }

    public String getOrganisationName() {
        return OrganisationName;
    }

    public void setOrganisationName(String organisationName) {
        OrganisationName = organisationName;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getEventPicture() {
        return EventPicture;
    }

    public void setEventPicture(String eventPicture) {
        EventPicture = eventPicture;
    }

    public String getOrganisationPicture() {
        return OrganisationPicture;
    }

    public void setOrganisationPicture(String organisationPicture) {
        OrganisationPicture = organisationPicture;
    }

    public int getParticipants() {
        return Participants;
    }

    public void setParticipants(int participants) {
        Participants = participants;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
