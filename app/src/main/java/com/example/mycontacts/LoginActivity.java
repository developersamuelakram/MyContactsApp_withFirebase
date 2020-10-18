package com.example.mycontacts;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {

    MaterialEditText et_password, et_email;
    Button signIn;
    String email, password;
    FirebaseAuth auth;

    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        et_email = findViewById(R.id.sign_email);
        et_password = findViewById(R.id.sign_password);
        signIn  = findViewById(R.id.signIn_account);


        pd = new ProgressDialog(this);



        auth = FirebaseAuth.getInstance();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = et_email.getText().toString();
                password = et_password.getText().toString();


                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {

                    Toast.makeText(LoginActivity.this, "Required", Toast.LENGTH_SHORT).show();


                } else {

                    LogmeIn(email, password);
                }


            }
        });





    }

    private void LogmeIn(String email, String password) {

        pd.setMessage("Signing In");
        pd.show();

        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                pd.dismiss();

                Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });

    }




}