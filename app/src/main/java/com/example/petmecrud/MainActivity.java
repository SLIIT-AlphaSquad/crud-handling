package com.example.petmecrud;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.function.IntToDoubleFunction;

public class MainActivity extends AppCompatActivity {

    ImageButton imageButton;
    EditText edicourseid,edtcoursename,edtcoursefee,edtcourseduration;
    Button btnaddcourse, btnshowcourse;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    StorageReference mStorage;

    private static final int Gallery_code = 1;
    private Uri imageUri=null;
    ProgressDialog mprograss;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButton =findViewById(R.id.btn_courseImagBtn);
        edicourseid=findViewById(R.id.edt_courseId);
        edtcoursename=findViewById(R.id.edt_courseName);
        edtcoursefee=findViewById(R.id.edt_courseFees);
        edtcourseduration=findViewById(R.id.edt_courseDuration);

        mprograss=new ProgressDialog(this);

        btnaddcourse=findViewById(R.id.btn_addedCourse);
        btnshowcourse=findViewById(R.id.btn_showCourse);
        mDatabase=FirebaseDatabase.getInstance();
        mRef=mDatabase.getReference().child("Languages");
        mStorage= FirebaseStorage.getInstance().getReference();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,Gallery_code);

            }

        });

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_code && resultCode == RESULT_OK)
        {
           imageUri = data.getData();
           imageButton.setImageURI(imageUri);
        }

        btnaddcourse.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id=edicourseid.getText().toString().trim();
                String name=edtcoursename.getText().toString().trim();
                String fee=edtcoursefee.getText().toString().trim();
                String duration=edtcourseduration.getText().toString().trim();

                if(!id.isEmpty() && !name.isEmpty() && !fee.isEmpty() && !duration.isEmpty() && imageUri!=null)
                {

                    mprograss.setMessage("uploading....");
                    mprograss.show();

                    StorageReference fillpath = mStorage.child("image_post").child(imageUri.getLastPathSegment());
                    fillpath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {


                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                  String t = task.getResult().toString();
                                  DatabaseReference newPost=mRef.push();
                                  newPost.child("courseId").setValue(id);
                                  newPost.child("courseName").setValue(name);
                                  newPost.child("courseFee").setValue(fee);
                                  newPost.child("courseDuration").setValue(duration);
                                  newPost.child("image").setValue(task.getResult().toString());

                                  mprograss.dismiss();


                                  Intent intent = new Intent(MainActivity.this,RecycleViewList.class);
                                  startActivity(intent);


                                }
                            });
                        }
                    });
                }
            }
        }));


}

    public void sendMessage(View view) {
        Intent intent = new Intent(MainActivity.this, RecycleViewList.class);
        startActivity(intent);
    }

}