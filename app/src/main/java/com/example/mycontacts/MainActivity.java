package com.example.mycontacts;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mycontacts.Model.Contacts;
import com.example.mycontacts.Model.Users;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    Toolbar toolbar;
    FirebaseUser firebaseUser;
    CircleImageView imageontoOLBAR;
    TextView usernameOnToolbar;
    RecyclerView recyclerView;
    List<Contacts> contactsList;
    ContactAdapter mAdapter;

    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbarmain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");


        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);



        displayContacts();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        imageontoOLBAR = findViewById(R.id.imageViewInToolbar);
        usernameOnToolbar = findViewById(R.id.userName_toolbar);

        imageontoOLBAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });

        floatingActionButton = findViewById(R.id.floating);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddContact.class));
            }
        });


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference
                ("Users").child(firebaseUser.getUid());


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Users users = snapshot.getValue(Users.class);

                usernameOnToolbar.setText(users.getUsername());

                Glide.with(getApplicationContext()).load(users.getImageURL()).into(imageontoOLBAR);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        mAuth = FirebaseAuth.getInstance();
    }

    private void displayContacts() {

        contactsList = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Contacts")
                .child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                contactsList.clear();

                for (DataSnapshot ds: snapshot.getChildren()) {

                    Contacts contacts = ds.getValue(Contacts.class);

                    contactsList.add(contacts);

                    mAdapter = new ContactAdapter(MainActivity.this, contactsList );
                    recyclerView.setAdapter(mAdapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {



        if (item.getItemId() == R.id.logout) {

            mAuth.signOut();
            finish();
            Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, StartActivity.class));




        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}