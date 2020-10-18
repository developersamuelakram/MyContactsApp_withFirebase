package com.example.mycontacts.Model;

public class Contacts {


    String contactemail, contactname, contactnumber, imageURL;

    public Contacts(String contactemail, String contactname, String contactnumber, String imageURL) {
        this.contactemail = contactemail;
        this.contactname = contactname;
        this.contactnumber = contactnumber;
        this.imageURL = imageURL;
    }



    public Contacts() {
    }

    public String getContactemail() {
        return contactemail;
    }

    public void setContactemail(String contactemail) {
        this.contactemail = contactemail;
    }

    public String getContactname() {
        return contactname;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}

