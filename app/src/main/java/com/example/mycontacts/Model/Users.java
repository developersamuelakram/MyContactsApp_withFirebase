package com.example.mycontacts.Model;

public class Users {

    String username, imageURL, contactnumber;

    public Users(String username, String imageURL, String contactnumber) {
        this.username = username;
        this.imageURL = imageURL;
        this.contactnumber = contactnumber;
    }

    public Users() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }
}
