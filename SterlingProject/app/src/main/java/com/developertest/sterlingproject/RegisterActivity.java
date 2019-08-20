package com.developertest.sterlingproject;

import android.arch.lifecycle.ViewModelProvider;
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
import android.widget.TextView;
import android.widget.Toast;

import com.developertest.sterlingproject.Model.User;
import com.developertest.sterlingproject.ViewModel.UserViewModel;

public class RegisterActivity extends AppCompatActivity {

    private EditText FirstName, LastName, Email, UserName, Password, ConfirmPassword;
    Button btn_register, btn_backtoSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        InitVariables();

        btn_backtoSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ValidateEntries()) {
                    DismissKeyboard(FirstName);
                    Register();
                }
            }
        });
    }

    private void InitVariables() {
        FirstName = (EditText) findViewById(R.id.et_first_name);
        LastName = (EditText) findViewById(R.id.et_last_name);
        Email = (EditText) findViewById(R.id.et_email_address);
        UserName = (EditText) findViewById(R.id.et_user_name);
        Password = (EditText) findViewById(R.id.et_password);
        ConfirmPassword = (EditText) findViewById(R.id.et_confirm_password);
        btn_register = (Button) findViewById(R.id.btn_save_register);
        btn_backtoSignin = (Button) findViewById(R.id.btn_cancel_register);
    }

    private boolean ValidateEntries() {
        boolean proceedToRegister = true;
        if (TextUtils.isEmpty(FirstName.getText())) {
            FirstName.setError(getString(R.string.required_field));
            FirstName.requestFocus();
            proceedToRegister = false;
        }
        if (TextUtils.isEmpty(LastName.getText())) {
            LastName.setError(getString(R.string.required_field));
            LastName.requestFocus();
            proceedToRegister = false;
        }
        if (TextUtils.isEmpty(Email.getText())) {
            Email.setError(getString(R.string.required_field));
            Email.requestFocus();
            proceedToRegister = false;
        }
        if (TextUtils.isEmpty(UserName.getText())) {
            UserName.setError(getString(R.string.required_field));
            UserName.requestFocus();
            proceedToRegister = false;
        }
        if (TextUtils.isEmpty(Password.getText())) {
            Password.setError(getString(R.string.required_field));
            Password.requestFocus();
            proceedToRegister = false;
        }
        if (TextUtils.isEmpty(ConfirmPassword.getText())) {
            ConfirmPassword.setError(getString(R.string.required_field));
            ConfirmPassword.requestFocus();
            proceedToRegister = false;
        }
        if (!(Password.getText().toString()).equals(ConfirmPassword.getText().toString())) {
            ConfirmPassword.setError("Password and confirm password does not match");
            ConfirmPassword.requestFocus();
            proceedToRegister = false;
        }
        return proceedToRegister;
    }

    private void DismissKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void Register() {
        try {
            User user = new User(Email.getText().toString(), FirstName.getText().toString(),
                    LastName.getText().toString(), UserName.getText().toString(), Password.getText().toString());
            UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
            userViewModel.insert(user);
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, "Unable to register, check data and try again", Toast.LENGTH_LONG).show();
        }
    }
}
