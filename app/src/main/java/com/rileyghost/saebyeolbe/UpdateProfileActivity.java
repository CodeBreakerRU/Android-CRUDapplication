package com.rileyghost.saebyeolbe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfileActivity extends AppCompatActivity {

    private Button btnUpdateProfile;
    private EditText txtName;
    private EditText txtAge;
    private EditText txtAddress;
    private EditText txtPostal;
    private EditText txtPhone;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    private static final  String TAG = "UpdateProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        btnUpdateProfile = (Button) findViewById(R.id.btnUpdateProfile);
        txtName = (EditText) findViewById(R.id.txtNameUpdateProfile);
        txtAge = (EditText) findViewById(R.id.txtAgeUpdateProfile);
        txtAddress = (EditText) findViewById(R.id.txtCityUpdateProfile);
        txtPostal = (EditText) findViewById(R.id.txtPostalCodeUpdateProfile);
        txtPhone = (EditText) findViewById(R.id.txtPhoneUpdateProfile);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    // toastMessage("Successfully signed in with: " + user.getEmail());
                } else {

                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
            }
        };

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Object value = dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Attempting to add object to database.");

                String name = txtName.getText().toString();
                String age = txtAge.getText().toString();
                String address = txtAddress.getText().toString();
                String postal = txtPostal.getText().toString();
                String phone = txtPhone.getText().toString();



                if(!name.equals("")){
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userID = user.getUid();
                    //String email = user.getEmail();
                    myRef.child("users").child(userID).child("name").setValue(name);
                    myRef.child("users").child(userID).child("age").setValue(age);
                    myRef.child("users").child(userID).child("address").setValue(address);
                    myRef.child("users").child(userID).child("postal").setValue(postal);
                    myRef.child("users").child(userID).child("phone").setValue(phone);

                   /* myRef.child(userID).child("User").child("ViewProfile").child("name").setValue(name);
                    myRef.child(userID).child("User").child("ViewProfile").child("age").setValue(age);
                    myRef.child(userID).child("User").child("ViewProfile").child("city").setValue(city);
                    myRef.child(userID).child("User").child("ViewProfile").child("phone").setValue(phone);

                    myRef.child(userID).child("User").child("ViewProfile").child(name).setValue("true");
                    myRef.child(userID).child("User").child("ViewProfile").child(age).setValue("true");
                    myRef.child(userID).child("User").child("ViewProfile").child(city).setValue("true");
                    myRef.child(userID).child("User").child("ViewProfile").child(phone).setValue("true");*/

                    // toastMessage("Adding " + name + " to database...");

                    toastMessage("Profile Saved");

                    txtName.setText("");
                    txtAge.setText("");
                    txtAddress.setText("");
                    txtPostal.setText("");
                    txtPhone.setText("");
                }
            }
        });

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

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
