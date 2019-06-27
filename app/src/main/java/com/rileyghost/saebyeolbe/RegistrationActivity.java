package com.rileyghost.saebyeolbe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RegistrationActivity extends AppCompatActivity {

    private static final  String TAG = "RegistrationActivity";

    private EditText txtEmailAddress;
    private EditText EdTxtSetEmailOnLogin; // test
    private EditText txtPassword;
    private FirebaseAuth firebaseAuth;

    private FirebaseAuth mAuth; // on test
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        txtEmailAddress = (EditText) findViewById(R.id.txtEmailRegister);
        txtPassword = (EditText) findViewById(R.id.txtPasswordRegister);
        firebaseAuth = FirebaseAuth.getInstance();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

    }


    public void btnUserRegister_Click (View v){

        if (txtEmailAddress.length() == 0){
            txtEmailAddress.setError("Enter your email address");
        }
        else if (txtPassword.length() == 0){
            txtPassword.setError("Enter your password");
        }
        else {


            (firebaseAuth.createUserWithEmailAndPassword(txtEmailAddress.getText().toString(), txtPassword.getText().toString()))
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            // ProgressDialog dissmiss code
                            if (task.isSuccessful()) {

                                // ==========================================================================  on test
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userID = user.getUid();
                                String email = user.getEmail();

                                UserProData newUserProData = new UserProData ("", "",
                                        "", "", "Please update your name");
                                myRef.child("users").child(userID).setValue(newUserProData);
                                myRef.child("users").child(userID).child("email").setValue(email);

                                // ========================================================================== on test

                                Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_LONG).show();

                                Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                                startActivity(i);
                            } else {
                                Log.e("Error", task.getException().toString());
                                Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                    });


        }
        }


    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();

    }

}

