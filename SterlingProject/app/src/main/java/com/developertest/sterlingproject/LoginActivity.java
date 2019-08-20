package com.developertest.sterlingproject;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.developertest.sterlingproject.Model.User;
import com.developertest.sterlingproject.ViewModel.UserViewModel;

public class LoginActivity extends AppCompatActivity {

    private EditText Email, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InitVariables();

        Button btn_cancelLogin = (Button) findViewById(R.id.btn_cancel_login);
        Button btn_login = (Button) findViewById(R.id.btn_login);
        btn_cancelLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ValidateEntries()) {
                    DismissKeyboard(Email);
                    Register();
                }
                ;
            }
        });
    }


    private void InitVariables() {
        Email = (EditText) findViewById(R.id.et_email_address);
        Password = (EditText) findViewById(R.id.et_password_login);
    }

    private boolean ValidateEntries() {
        boolean proceedToLogin = true;
        if (TextUtils.isEmpty(Email.getText())) {
            Email.setError(getString(R.string.required_field));
            Email.requestFocus();
            proceedToLogin = false;
        }
        if (TextUtils.isEmpty(Password.getText())) {
            Password.setError(getString(R.string.required_field));
            Password.requestFocus();
            proceedToLogin = false;
        }
        return proceedToLogin;
    }

    private void DismissKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void Register() {
        try {
            User user = new User(Email.getText().toString(), "","", "", Password.getText().toString());
            UserViewModel userViewModel = ViewModelProviders.of(LoginActivity.this).get(UserViewModel.class);
            User loginUser = userViewModel.getUser(user.getEmailAddress());
            if(loginUser != null){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, "Unable to login, check data and try again", Toast.LENGTH_LONG).show();
        }
    }
}
