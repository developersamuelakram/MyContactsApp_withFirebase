package com.example.mycontacts;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterAccount extends AppCompatActivity {

    MaterialEditText et_username, et_password, et_email;
    Button registeraccount;
    CircleImageView imageView;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    ProgressDialog pd;

    Uri imageUri = null;

    public static final int GALLERY_IMAGE_CODE = 100;
    public static final int CAMERA_IMAGE_CODE = 200;

    String username, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);


        et_username = findViewById(R.id.reg_username);
        et_email = findViewById(R.id.reg_email);
        et_password = findViewById(R.id.reg_password);
        registeraccount = findViewById(R.id.register_Account_btn);
        imageView = findViewById(R.id.reg_image_account);



        pd = new ProgressDialog(this);

        Permissions();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialog();
            }
        });


        mAuth = FirebaseAuth.getInstance();

        registeraccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = et_username.getText().toString();
                password = et_password.getText().toString();
                email = et_email.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    et_username.setError("Required");

                } else if (TextUtils.isEmpty(password)) {
                    et_password.setError("Required");


                } else if (TextUtils.isEmpty(email)) {
                    et_email.setError("Required");



                } else if (password.toString().length() < 6) {
                    et_password.setError("Password must be more than 6 or 6");


                } else {

                    SignUpUser(username, password, email);
                }






            }
        });


    }

    private void imagePickDialog() {

        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                if (i==0) {


                    openCamera();
                }

                if (i==1) {
                    openGallery();

                }
            }
        });

        builder.create().show();


    }

    private void openGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_IMAGE_CODE);

    }

    private void openCamera() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pick");
        values.put(MediaStore.Images.Media.TITLE, "Temp Desc");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_IMAGE_CODE);



    }


    private void Permissions() {


        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.RECORD_AUDIO
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == GALLERY_IMAGE_CODE && resultCode == RESULT_OK) {

            imageUri = data.getData();

            imageView.setImageURI(imageUri);



        }



        if (requestCode == CAMERA_IMAGE_CODE && resultCode == RESULT_OK) {


            imageView.setImageURI(imageUri);



        }



    }

    private void SignUpUser(final String username, String password, final String email) {

        pd.setMessage("Registering");
        pd.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();


                String filepath = "ContactImages/" + "Photos/" + "User_";

                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(filepath);

                storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();

                        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                pd.dismiss();

                                String imageURL = uri.toString();


                                HashMap<String, Object> hashMap = new HashMap<>();

                                hashMap.put("username", username);
                                hashMap.put("email", email);
                                hashMap.put("imageURL", imageURL);
                                hashMap.put("id", firebaseUser.getUid());
                                hashMap.put("contactnumber", "123456");

                                reference.child("Users").child(firebaseUser.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {


                                        pd.dismiss();
                                        Toast.makeText(RegisterAccount.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });

                            }
                        });

                    }
                });








            }
        });


    }

}