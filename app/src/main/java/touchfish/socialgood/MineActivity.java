package touchfish.socialgood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class MineActivity extends AppCompatActivity {

    static public User user;
    static public Publisher publisher;
    static public String type;

    public ListView setting;
    public ArrayAdapter myList_adapter;
    ArrayList<String> userFunction = new ArrayList<>();
    ArrayList<String> publisherFunction = new ArrayList<>();

    private ImageView profilePic;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);


        TextView textProfileName = findViewById(R.id.textProfileName);
        TextView textCredit = findViewById(R.id.textCredit);

        setting = findViewById(R.id.settingList);
        userFunction.add("History");
        userFunction.add("Follow");
        userFunction.add("Log out");
        publisherFunction.add("Publish Activity");
        publisherFunction.add("Publish Product");
        publisherFunction.add("Log out");

        type = getIntent().getStringExtra("TYPE");
        if(type == null){
            Log.d("(mine) type","null");
        }
        else if(type.equals("User")){
            Log.d("(mine) type","User");
            user = (User)getIntent().getSerializableExtra("OBJECT");
            textProfileName.setText(user.username);
            textCredit.setText("Credit: "+user.credit);

            myList_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, userFunction);
            setting.setAdapter(myList_adapter);
            setting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i == 0) {
                        Intent intent = new Intent(MineActivity.this, HistoryActivity.class);
                        intent.putExtra("OBJECT", user);
                        startActivity(intent);
                    } else if (i == 1) {
                        Intent intent = new Intent(MineActivity.this, FollowActivity.class);
                        intent.putExtra("OBJECT", user);
                        startActivity(intent);
                    } else if (i == 2) {
                        Intent intent = new Intent(MineActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
            });

        }
        else if(type.equals("Publisher")){
            Log.d("(mine) type","Publisher");
            publisher = (Publisher)getIntent().getSerializableExtra("OBJECT");
            textProfileName.setText(publisher.username);

            myList_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, publisherFunction);
            setting.setAdapter(myList_adapter);
            setting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(i == 0){
                        Intent intent = new Intent(MineActivity.this, PublishActivityActivity.class);
                        intent.putExtra("OBJECT", publisher);
                        startActivity(intent);
                    }
                    else if(i == 1){
                        Intent intent = new Intent(MineActivity.this, PublishProductActivity.class);
                        intent.putExtra("OBJECT", publisher);
                        startActivity(intent);
                    }
                    else if(i == 2){
                        Intent intent = new Intent(MineActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }

        profilePic = findViewById(R.id.profilePic);


        ImageButton home = findViewById(R.id.bottom_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MineActivity.this, HomeActivity.class);
                intent.putExtra("TYPE",type);
                if(type == null){
                    Log.d("(mine->home) type","null");
                }
                else if(type.equals("User")){
                    intent.putExtra("OBJECT",user);
                }
                else if(type.equals("Publisher")){
                    intent.putExtra("OBJECT",publisher);
                }
                startActivity(intent);
            }
        });

        ImageButton shop = findViewById(R.id.bottom_shop);
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MineActivity.this, ShopActivity.class);
                intent.putExtra("TYPE",type);
                if(type == null){
                    Log.d("(mine->shop) type","null");
                }
                else if(type.equals("User")){
                    intent.putExtra("OBJECT",user);
                }
                else if(type.equals("Publisher")){
                    intent.putExtra("OBJECT",publisher);
                }
                startActivity(intent);
            }
        });

        profilePic = findViewById(R.id.profilePic);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
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
            profilePic.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference ref = storageReference.child("profile/"+randomKey);

        ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Toast.makeText(MineActivity.this, "Image upload", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(MineActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
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