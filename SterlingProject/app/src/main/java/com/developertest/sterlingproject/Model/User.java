package com.developertest.sterlingproject.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "tbl_user") //to give the table a name instead of letting have the default class name
public class User {
    @PrimaryKey(autoGenerate = true) //to autogenerate your own primayr key
    private int Id;
    @NonNull
    private String EmailAddress;
    private String FirstName;
    private String LastName;
    private String UserName;
    private String Password;

    public User(@NonNull String EmailAddress, String FirstName, String LastName, String UserName, String Password) {
        this.EmailAddress = EmailAddress;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.UserName = UserName;
        this.Password = Password;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    @NonNull
    public String getEmailAddress() {
        return EmailAddress;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getUserName() {
        return UserName;
    }

    public String getPassword() {
        return Password;
    }
}
