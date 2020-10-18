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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddContact extends AppCompatActivity {

    EditText et_name, et_number, et_email;
    CircleImageView imageView;
    String email, phonenumber, name;
    Button addcountact;


    Uri imageUri = null;
    FirebaseUser firebaseUser;

    ProgressDialog pd;


    public static final int GALLERY_IMAGE_CODE = 100;
    public static final int CAMERA_IMAGE_CODE = 200;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        et_name = findViewById(R.id.contactName);
        et_number = findViewById(R.id.contactNumber);
        et_email = findViewById(R.id.emailContact);
        imageView = findViewById(R.id.add_imageaccount);
        addcountact = findViewById(R.id.addcontact);

        Permissions();

        pd = new ProgressDialog(this);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialog();
            }
        });




        addcountact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = et_email.getText().toString();
                phonenumber = et_number.getText().toString();
                name = et_name.getText().toString();



                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(phonenumber) || TextUtils.isEmpty(name)) {

                    Toast.makeText(AddContact.this, "Type Something Honey", Toast.LENGTH_SHORT).show();


                } else {


                    addContact(name, email, phonenumber);
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



    private void addContact(final String name, final String email, final String phonenumber) {

        pd.setMessage("Adding Contact");
        pd.show();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        String filepath = "ContactPhotos/" + "Photos/" + "User_";

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();


        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(filepath);

        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                pd.dismiss();

                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();

                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        pd.dismiss();

                        String imageURL = uri.toString();


                        HashMap<String, Object> hashMap = new HashMap<>();

                        hashMap.put("contactname", name);
                        hashMap.put("contactemail", email);
                        hashMap.put("imageURL", imageURL);
                        hashMap.put("contactnumber", phonenumber);

                        reference.child("Contacts").child(firebaseUser.getUid()).push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                pd.dismiss();

                                Toast.makeText(AddContact.this, "Contact Added", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                    }
                });

            }
        });








    }


}