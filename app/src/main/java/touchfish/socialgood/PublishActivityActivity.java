package touchfish.socialgood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class PublishActivityActivity extends AppCompatActivity {

    static public Publisher publisher;

    private ImageView activityPicture;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_activity);

        publisher = (Publisher)getIntent().getSerializableExtra("OBJECT");

        ImageButton back = findViewById(R.id.backToMine);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PublishActivityActivity.this, MineActivity.class);
                intent.putExtra("TYPE", "Publisher");
                intent.putExtra("OBJECT", publisher);
                startActivity(intent);
            }
        });

        Button btn_publish = findViewById(R.id.ActivityPublishBtn);
        btn_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editActivityTitle = findViewById(R.id.editActivityTitle);
                String activityTitle = editActivityTitle.getText().toString();

                EditText editActivityCredit = findViewById(R.id.editActivityCredit);
                String activityCredit = editActivityCredit.getText().toString();
                int credit = Integer.parseInt(activityCredit);

                EditText editActivityDescription =  findViewById(R.id.editActivityDescription);
                String activityDescription = editActivityDescription.getText().toString();

                EditText editActivityStartDate = findViewById(R.id.editActivityStartDate);
                String activityStartDate = editActivityStartDate.getText().toString();

                EditText editActivityEndDate = findViewById(R.id.editActivityEndDate);
                String activityEndDate = editActivityEndDate.getText().toString();


                if(activityTitle.isEmpty() || activityCredit.isEmpty() || activityDescription.isEmpty()
                        || activityStartDate.isEmpty() || activityEndDate.isEmpty()){
                    Toast.makeText(PublishActivityActivity.this
                            , "Please fill in all EditText",
                            Toast.LENGTH_LONG).show();
                    return;
                }


                String content = publisher.username+"|"+activityTitle+"|"+activityCredit+"|"+activityDescription+"|"+activityStartDate+"|"+activityEndDate;
                Log.d("(publish activity)",content);

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference activityReference = firebaseDatabase.getReference("Activity");
                ActivityClass activityClass = new ActivityClass(publisher.username,activityTitle,credit,activityDescription,activityStartDate,activityEndDate,pictureName);
                activityReference.push().setValue(activityClass);
                Toast.makeText(PublishActivityActivity.this
                        , "Publish Activity Successfully",
                        Toast.LENGTH_LONG).show();
            }
        });
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        activityPicture = findViewById(R.id.activityPicture);

        activityPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

    }

    private String pictureName= "9a097f0a-3721-4894-a2c6-372b54f396f3";
    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            activityPicture.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        setPictureName(randomKey);
        StorageReference ref = storageReference.child("activity/"+randomKey);

        ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Toast.makeText(PublishActivityActivity.this, "Image upload", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(PublishActivityActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercentage = (100.00 * snapshot.getBytesTransferred()/ snapshot.getTotalByteCount());
                pd.setMessage("Progress: " + (int) progressPercentage + "%");
            }
        });
    }
}