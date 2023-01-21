package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class inscription extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseUser user;
    CollectionReference collectionRef;
    Button createAccountBtn;
    ImageButton showPass1, showPass2;
    EditText userName, userEmail, userPassword, confirmPassword;
    LinearLayout nameError, emailError, passError, confirmError;
    TextView connectionBtn, nameErrorMessage, emailErrorMessage, passErrorMessage, confirmErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        varInit();

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        connectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), connexion.class));
                overridePendingTransition(0,0);
                finish();
            }
        });

        userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    resetErrors("name");
                    userName.setBackground(getDrawable(R.drawable.input_bg_focus));
                } else {
                    userName.setBackground(getDrawable(R.drawable.input_bg));
                }
            }
        });

        userEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    resetErrors("email");
                    userEmail.setBackground(getDrawable(R.drawable.input_bg_focus));
                } else {
                    userEmail.setBackground(getDrawable(R.drawable.input_bg));
                }
            }
        });

        userPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    resetErrors("password");
                    userName.setBackground(getDrawable(R.drawable.input_bg_focus));
                } else {
                    userName.setBackground(getDrawable(R.drawable.input_bg));
                }
            }
        });

        confirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    resetErrors("confirm");
                    userName.setBackground(getDrawable(R.drawable.input_bg_focus));
                } else {
                    userName.setBackground(getDrawable(R.drawable.input_bg));
                }
            }
        });

        showPass1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userPassword.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    userPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPass1.setColorFilter(getResources().getColor(R.color.primary));
                } else {
                    userPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPass1.setColorFilter(getResources().getColor(R.color.icon_color));
                }
            }
        });

        showPass2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(confirmPassword.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    confirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPass2.setColorFilter(getResources().getColor(R.color.primary));
                } else {
                    confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPass2.setColorFilter(getResources().getColor(R.color.icon_color));
                }
            }
        });
    }

    public void varInit() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        collectionRef = db.collection("users");
        showPass1 = findViewById(R.id.eyeIcon);
        userName = findViewById(R.id.userName);
        showPass2 = findViewById(R.id.eyeIcon2);
        userEmail = findViewById(R.id.userEmail);
        nameError = findViewById(R.id.nameError);
        passError = findViewById(R.id.passError);
        emailError = findViewById(R.id.emailError);
        userPassword = findViewById(R.id.password);
        confirmError = findViewById(R.id.confirmError);
        connectionBtn = findViewById(R.id.connectionBtn);
        confirmPassword = findViewById(R.id.confirmPassword);
        nameErrorMessage = findViewById(R.id.nameErrorMessage);
        createAccountBtn = findViewById(R.id.createAccountBtn);
        passErrorMessage = findViewById(R.id.passErrorMessage);
        emailErrorMessage = findViewById(R.id.emailErrorMessage);
        confirmErrorMessage = findViewById(R.id.confirmErrorMessage);

        nameError.setVisibility(View.GONE);
        emailError.setVisibility(View.GONE);
        passError.setVisibility(View.GONE);
        confirmError.setVisibility(View.GONE);
    }

    public void registerUser() {
        String email, password, name, confirm;
        name = userName.getText().toString();
        email = userEmail.getText().toString();
        password = userPassword.getText().toString();
        confirm = confirmPassword.getText().toString();

        userName.clearFocus();
        userEmail.clearFocus();
        userPassword.clearFocus();
        confirmPassword.clearFocus();

        if(name.isEmpty()) {
            nameErrorMessage.setText("This field is required");
            nameError.setVisibility(View.VISIBLE);
            userName.setBackground(getDrawable(R.drawable.input_bg_error));
        }
        if(!email.contains("@")) {
            emailErrorMessage.setText("Invalid email");
            emailError.setVisibility(View.VISIBLE);
            userName.setBackground(getDrawable(R.drawable.input_bg_error));
        }
        if(password.length() < 8) {
            passErrorMessage.setText("Password should be at least 8 characters");
            passError.setVisibility(View.VISIBLE);
            userName.setBackground(getDrawable(R.drawable.input_bg_error));
        }
        if(confirm.isEmpty()) {
            confirmErrorMessage.setText("This field is required");
            confirmError.setVisibility(View.VISIBLE);
            userName.setBackground(getDrawable(R.drawable.input_bg_error));
        }
        if(!confirm.equals(password)) {
            confirmErrorMessage.setText("Does not match password");
            confirmError.setVisibility(View.VISIBLE);
            userName.setBackground(getDrawable(R.drawable.input_bg_error));
        }

        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirm.isEmpty() && confirm.equals(password) && password.length() > 7 && email.contains("@")) {

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        user = auth.getCurrentUser();
                        Map<String, Object> data = new HashMap<>();
                        data.put( "user", name);
                        data.put("uid", user.getUid());
                        collectionRef.add(data);
                        startActivity(new Intent(getApplicationContext(), connexion.class));
                        overridePendingTransition(0,0);
                        finish();
                    } else if (task.getException().toString().contains("email address is already in use")){
                        emailErrorMessage.setText("This email already has an account");
                        emailError.setVisibility(View.VISIBLE);
                        userName.setBackground(getDrawable(R.drawable.input_bg_error));
                    }
                }
            });
        }
    }

    public void resetErrors(String field) {
        if (field.equals("email") && emailError.getVisibility() == View.VISIBLE) {
            emailError.setVisibility(View.GONE);
            userName.setBackground(getDrawable(R.drawable.input_bg));
        } else if(field.equals("password") && passError.getVisibility() == View.VISIBLE) {
            passError.setVisibility(View.GONE);
            userName.setBackground(getDrawable(R.drawable.input_bg));
        } else if (field.equals("confirm") && confirmError.getVisibility() == View.VISIBLE) {
            confirmError.setVisibility(View.GONE);
            userName.setBackground(getDrawable(R.drawable.input_bg));
        } else {
            nameError.setVisibility(View.GONE);
            userName.setBackground(getDrawable(R.drawable.input_bg));
        }
    }
}