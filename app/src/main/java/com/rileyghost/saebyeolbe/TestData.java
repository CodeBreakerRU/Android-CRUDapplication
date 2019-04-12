package com.rileyghost.saebyeolbe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TestData extends AppCompatActivity {

    private static final String TAG = "TestData";


    private DatabaseReference DatabaseReference;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_data);
        DatabaseReference = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        // ==========================

    }


    public void insertOnclick (View v){
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        String email = user.getEmail();

        UserProData newUserProData = new UserProData ("Update your address", "Update your birthday",
                "Update your phone", "Update your postal code", "Update your name");
        myRef.child("users").child(userID).setValue(newUserProData);
        myRef.child("users").child(userID).child("email").setValue(email);


    }


}
