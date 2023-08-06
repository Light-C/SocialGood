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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class PublishProductActivity extends AppCompatActivity {

    static public Publisher publisher;

    private ImageView productPicture;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_product);

        publisher = (Publisher)getIntent().getSerializableExtra("OBJECT");

        ImageButton back = findViewById(R.id.backToMine);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PublishProductActivity.this, MineActivity.class);
                intent.putExtra("TYPE", "Publisher");
                intent.putExtra("OBJECT", publisher);
                startActivity(intent);
            }
        });

        productPicture = findViewById(R.id.productPicture);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        productPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        Button btn_publish = findViewById(R.id.productPublishBtn);
        btn_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editActivityTitle = findViewById(R.id.editProductTitle);
                String activityTitle = editActivityTitle.getText().toString();

                EditText editActivityDescription =  findViewById(R.id.editProductDescription);
                String activityDescription = editActivityDescription.getText().toString();

                EditText editActivityStartDate = findViewById(R.id.editProductPrice);
                String activityStartDate = editActivityStartDate.getText().toString();
                Double itemPrice = Double.parseDouble(activityStartDate);

                EditText editActivityEndDate = findViewById(R.id.editProductAmount);
                String activityEndDate = editActivityEndDate.getText().toString();
                int itemSurplus = Integer.parseInt(activityEndDate);

                if(activityTitle.isEmpty() || activityDescription.isEmpty()
                        || activityStartDate.isEmpty() || activityEndDate.isEmpty()) {
                    Toast.makeText(PublishProductActivity.this
                            , "Please fill in all EditText",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                String content = activityTitle+"|"+activityDescription+"|"+activityStartDate+"|"+activityEndDate+"|";
                Log.d("(publish good)",content);

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference activityReference = firebaseDatabase.getReference("Item");
                ItemModel itemClass = new ItemModel(0001, activityTitle, itemPrice, itemSurplus, activityDescription, publisher.username, 0001);
                itemClass.setPictureName(pictureName);
                activityReference.push().setValue(itemClass);
                Toast.makeText(PublishProductActivity.this
                        , "Publish Product Successfully",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    private String pictureName= "d76f5eb8-7af7-4e94-9e2b-c0da07b25dda";
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
            productPicture.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        setPictureName(randomKey);
        StorageReference ref = storageReference.child("product/"+randomKey);

        ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Toast.makeText(PublishProductActivity.this, "Image upload", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(PublishProductActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercentage = (100.00 * snapshot.getBytesTransferred()/ snapshot.getTotalByteCount());
                pd.setMessage("Progress: " + (int) progressPercentage + "%");
            }
        });
    }
//    ##!
}