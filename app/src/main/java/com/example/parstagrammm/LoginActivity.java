package com.example.parstagrammm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

//a login screen that offers login via username/password.
public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "Login Activity";

    private ImageView ivTitle;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        ivTitle = findViewById(R.id.ivTitle);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick login button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                loginUser(username, password);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick sign up button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                createUser(username, password);
            }
        });
    }

    private void createUser(String username, String password) {
        //create a new parse user
        ParseUser newUser = new ParseUser();

        //set properties
        newUser.setUsername(username);
        newUser.setPassword(password);

        //invoke signupinbackground
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) {
                    Log.e(TAG, "Issue signing up new user..." , e);
                    Toast.makeText(LoginActivity.this, "Issue signing up new user...", Toast.LENGTH_SHORT).show();
                    return;
                }

                //navigate to main activity if the user has signed up properly
                goMainActivity();
                Toast.makeText(LoginActivity.this, "New user sign up Success!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to Login User " + username);
        
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null) {
                    Log.e(TAG, "Issue with login.", e);
                    Toast.makeText(LoginActivity.this, "Issue with login...", Toast.LENGTH_SHORT).show();
                    return;
                }

                //navigate to the main activity if the user has signed in properly
                goMainActivity();
                Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}