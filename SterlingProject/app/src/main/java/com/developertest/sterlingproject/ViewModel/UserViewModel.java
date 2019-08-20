package com.developertest.sterlingproject.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.developertest.sterlingproject.Model.User;
import com.developertest.sterlingproject.Repository.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    private UserRepository userRepository;

    public void insert(User user) {
        userRepository.insert(user);
    }

    public void update(User user) {
        userRepository.update(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public List<User> getAllUsers(){
        return userRepository.getAllUsers();
    }

    public User getUser(String emailAddress){
        return userRepository.getUser(emailAddress);
    }
}
