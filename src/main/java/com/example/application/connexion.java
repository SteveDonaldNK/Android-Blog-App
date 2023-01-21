package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Type;

public class connexion extends AppCompatActivity {

    Button loginBtn;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    EditText userEmail, userPassword;
    ImageButton showPass;
    LinearLayout emailError, passError;
    TextView createAccountBtn, passErrorMessage, emailErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        varInit();

        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), accueil.class));
            overridePendingTransition(0,0);
            finish();
        }
        loginUser(false);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(true);
            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), inscription.class));
                overridePendingTransition(0,0);
                finish();
            }
        });

        userEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    resetErrors("email");
                    userEmail.setBackground(getDrawable(R.drawable.input_bg_focus));
                } else {
                    resetErrors("password");
                    userEmail.setBackground(getDrawable(R.drawable.input_bg));
                }
            }
        });

        userPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    resetErrors("password");
                    userPassword.setBackground(getDrawable(R.drawable.input_bg_focus));
                } else {
                    resetErrors("email");
                    userPassword.setBackground(getDrawable(R.drawable.input_bg));
                }
            }
        });

        showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userPassword.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    userPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPass.setColorFilter(getResources().getColor(R.color.primary));
                } else {
                    userPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPass.setColorFilter(getResources().getColor(R.color.icon_color));
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        passError.setVisibility(View.GONE);
        emailError.setVisibility(View.GONE);
        //check if user's email and password are stored in SharedPreferencies

        SharedPreferences pref = getApplicationContext().getSharedPreferences("user", 0);
        String email = pref.getString("email", null);
        String password = pref.getString("password", null);

        if (email != null && password != null) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        startActivity(new Intent(getApplicationContext(), accueil.class));
                    } else {
                        startActivity(new Intent(getApplicationContext(), connexion.class));
                        passError.setVisibility(View.GONE);
                        emailError.setVisibility(View.GONE);
                    }
                    overridePendingTransition(0,0);
                    finish();
                }
            });
        }
    }

    public void varInit() {
        auth = FirebaseAuth.getInstance();
        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        createAccountBtn = findViewById(R.id.createAccountBtn);
        passError = findViewById(R.id.passError);
        emailError = findViewById(R.id.emailError);
        showPass = findViewById(R.id.eyeIcon);
        passErrorMessage = findViewById(R.id.passErrorMessage);
        emailErrorMessage = findViewById(R.id.emailErrorMessage);
        currentUser = auth.getCurrentUser();
    }

    public void loginUser(Boolean clicked) {
        String email, password;
        email = userEmail.getText().toString();
        password = userPassword.getText().toString();
        userEmail.clearFocus();
        userPassword.clearFocus();

        if (!email.contains("@") && clicked) {
            emailErrorMessage.setText("Invalid email");
            emailError.setVisibility(View.VISIBLE);
            userEmail.setBackground(getDrawable(R.drawable.input_bg_error));

        }
        if ((password.length() < 8) && clicked) {
            passErrorMessage.setText("Password should be at least 8 characters");
            passError.setVisibility(View.VISIBLE);
            userPassword.setBackground(getDrawable(R.drawable.input_bg_error));
        }
        if(!email.isEmpty() && !password.isEmpty() && password.length() > 7 && email.contains("@")) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "login successful", Toast.LENGTH_LONG).show();
                        FirebaseUser user = auth.getCurrentUser();

                        //save user email and password to sharedpreferences
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("user", 0);

                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.apply();

                        //open new activity
                        startActivity(new Intent(getApplicationContext(), accueil.class));
                        overridePendingTransition(0,0);
                        finish();
                    } else {
                        if (task.getException().toString().contains("password is invalid")) { //display error message if password is incorrect
                            passErrorMessage.setText("Incorrect password");
                            passError.setVisibility(View.VISIBLE);
                            userPassword.setBackground(getDrawable(R.drawable.input_bg_error));
                        } else if (task.getException().toString().contains("no user record")){
                            emailErrorMessage.setText("User not found");
                            emailError.setVisibility(View.VISIBLE);
                            userEmail.setBackground(getDrawable(R.drawable.input_bg_error));
                        }
                    }
                }
            });
        }
    }

    public void resetErrors(String field) {
        if (field.equals("email") && emailError.getVisibility() == View.VISIBLE) {
            emailError.setVisibility(View.GONE);
            userEmail.setBackground(getDrawable(R.drawable.input_bg));
        } else {
            passError.setVisibility(View.GONE);
            userPassword.setBackground(getDrawable(R.drawable.input_bg));
        }
    }
}