package com.rileyghost.saebyeolbe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyProfileActivity extends AppCompatActivity {

    private static final String TAG = "MyProfileActivity";

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;

    private ListView mListView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        mListView1 = (ListView) findViewById(R.id.myProfileListView1);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //       toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
                // IM22
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

    }


    private void showData(DataSnapshot dataSnapshot) {

        for(DataSnapshot ds : dataSnapshot.getChildren()){

            UserProfileInfo uInfo = new UserProfileInfo();

            uInfo.setName(ds.child(userID).getValue(UserProfileInfo.class).getName());
            uInfo.setAge(ds.child(userID).getValue(UserProfileInfo.class).getAge());
            uInfo.setPhone(ds.child(userID).getValue(UserProfileInfo.class).getPhone());
            uInfo.setCity(ds.child(userID).getValue(UserProfileInfo.class).getAddress());
            uInfo.setPostal(ds.child(userID).getValue(UserProfileInfo.class).getPostal());
            uInfo.setEmail(ds.child(userID).getValue(UserProfileInfo.class).getEmail());

            Log.d(TAG, "showData: name: " + uInfo.getName());
            Log.d(TAG, "showData: age: " + uInfo.getAge());
            Log.d(TAG, "showData: phone: "+ uInfo.getPhone());
            Log.d(TAG, "showData: address: "+ uInfo.getAddress());
            Log.d(TAG, "showData: postal: "+ uInfo.getPostal());
            Log.d(TAG, "showData: email: "+ uInfo.getEmail());

            ArrayList<String> array  = new ArrayList<>();
            array.add("Name : "+ uInfo.getName());
            array.add("Email : "+ uInfo.getEmail());
            array.add("Birthday : "+ uInfo.getAge());
            array.add("Phone : "+ uInfo.getPhone());
            array.add("Address : "+ uInfo.getAddress());
            array.add("Zip code : "+ uInfo.getPostal());

            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,array);
            mListView1.setAdapter(adapter);

        }
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
