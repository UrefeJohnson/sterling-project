package com.developertest.sterlingproject.Repository;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.developertest.sterlingproject.Model.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert
    void Insert(User user);

    @Update
    void Update(User user);

    @Delete
    void Delete(User user);

    @Query("SELECT * FROM tbl_user ORDER BY FirstName DESC")
    List<User> GetAllUsers();

    @Query("SELECT * FROM tbl_user WHERE EmailAddress = :emailAddress")
    User GetUser(String emailAddress);

}
