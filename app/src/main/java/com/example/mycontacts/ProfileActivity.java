package com.example.mycontacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mycontacts.Model.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {



    MaterialEditText et_username, et_number;
    Button updateprofile;
    CircleImageView imageView;

    FirebaseUser firebaseUser;

    String username, usernumber;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        et_number = findViewById(R.id.update_number);
        et_username = findViewById(R.id.update_user_name);
        updateprofile = findViewById(R.id.update_profile_button);
        imageView = findViewById(R.id.update_profile_photo);

        pd = new ProgressDialog(this);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Users users = snapshot.getValue(Users.class);

                et_number.setText(users.getContactnumber());

                Glide.with(getApplicationContext()).load(users.getImageURL()).into(imageView);

                et_username.setText(users.getUsername());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = et_username.getText().toString();
                usernumber = et_number.getText().toString();

                updateProfile(usernumber, username);

            }
        });





    }

    private void updateProfile(String usernumber, String username) {


        pd.setMessage("Updating");
        pd.show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());


        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("username", username);
        hashMap.put("contactnumber", usernumber);

        reference.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                pd.dismiss();

                Toast.makeText(ProfileActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        });




    }


}