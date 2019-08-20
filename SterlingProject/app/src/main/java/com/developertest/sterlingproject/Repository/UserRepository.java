package com.developertest.sterlingproject.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.developertest.sterlingproject.Model.User;

import java.util.List;

public class UserRepository {

    private UserDAO mUserDao;
    private static List<User> mUsers;
    private static User mUser;

    public UserRepository(Application application) {
        FixtureRoomDatabase db = FixtureRoomDatabase.getInstance(application);

        try {
            mUserDao = db.userDAO();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void insert(User user){
        new InsertAsyncTask(mUserDao).execute(user);
    }

    public void update(User user){
        new UpdateAsyncTask(mUserDao).execute(user);
    }

    public void delete(User user){
        new DeleteAsyncTask(mUserDao).execute(user);
    }

    public List<User> getAllUsers(){
        return mUserDao.GetAllUsers();
    }

    public User getUser(String emailAddress){
        return mUserDao.GetUser(emailAddress);
    }

    private static class InsertAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDAO mAsyncTaskDao;

        private InsertAsyncTask(UserDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... users) {
            mAsyncTaskDao.Insert(users[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDAO mAsyncTaskDao;

        private UpdateAsyncTask(UserDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... users) {
            mAsyncTaskDao.Update(users[0]);
            return null;
        }
    }
    private static class DeleteAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDAO mAsyncTaskDao;

        private DeleteAsyncTask(UserDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... users) {
            mAsyncTaskDao.Delete(users[0]);
            return null;
        }
    }
}
