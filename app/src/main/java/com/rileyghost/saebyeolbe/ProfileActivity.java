package com.rileyghost.saebyeolbe;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ProfileActivity";

    public TextView textViewEmail; // profile content

    public TextView txtEmailP;      // oncreate options menu
    public TextView txtNameP;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    private String userID;
    private ListView mListViewP;    // profile data list view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mListViewP = (ListView) findViewById(R.id.ProfileListViewP);  // profile data list view

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        // ===================================================== //test
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //       toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }

            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                toastMessage("Error connecting to database");
            }
        });

        // ========================================================

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "In development mode :| Contact RILEYGHOST", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        textViewEmail = (TextView) findViewById(R.id.userEmailProfile);       // profile content
        textViewEmail.setText(getIntent().getExtras().getString("Email"));

    }

    private void showData(DataSnapshot dataSnapshot) {

        for(DataSnapshot ds : dataSnapshot.getChildren()){
            UserProfileInfo uInfo = new UserProfileInfo();
            uInfo.setName(ds.child(userID).getValue(UserProfileInfo.class).getName());
            uInfo.setAge(ds.child(userID).getValue(UserProfileInfo.class).getAge());
            uInfo.setPhone(ds.child(userID).getValue(UserProfileInfo.class).getPhone());
            uInfo.setCity(ds.child(userID).getValue(UserProfileInfo.class).getAddress());
            uInfo.setPostal(ds.child(userID).getValue(UserProfileInfo.class).getPostal());

            Log.d(TAG, "showData: name: " + uInfo.getName());
            Log.d(TAG, "showData: age: " + uInfo.getAge());
            Log.d(TAG, "showData: phone: "+ uInfo.getPhone());
            Log.d(TAG, "showData: address: "+ uInfo.getAddress());
            Log.d(TAG, "showData: postal: "+ uInfo.getPostal());

            ArrayList<String> array  = new ArrayList<>();
            array.add("Name : "+ uInfo.getName());
            array.add("Birthday : "+ uInfo.getAge());
            array.add("Phone : "+ uInfo.getPhone());
            array.add("Address : "+ uInfo.getAddress());
            array.add("Zip code : "+ uInfo.getPostal());

            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,array);
            mListViewP.setAdapter(adapter);

            txtNameP = (TextView)  findViewById(R.id.txtNameP); // display name on the navigation bar get from db
            txtNameP.setText(uInfo.getName());
        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        txtEmailP = (TextView)  findViewById(R.id.txtEmailP);
        txtEmailP.setText(getIntent().getExtras().getString("Email")); // display email on the navigation bar get from authentication

        getMenuInflater().inflate(R.menu.profile, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

                        Intent i = new Intent(ProfileActivity.this, MyProfileActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_gallery) {

            Intent i = new Intent(ProfileActivity.this, UpdateProfileActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_slideshow) {

            Intent i = new Intent(ProfileActivity.this, ImageActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

            Intent i = new Intent(ProfileActivity.this, TestData.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}

