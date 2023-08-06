package touchfish.socialgood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ActivityActivity extends AppCompatActivity {

    public static User user;
    public static Publisher publisher;
    public static String type;
    public ActivityClass activityClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);
        // initialize firebase
        FirebaseApp.initializeApp(getBaseContext());
        // getStringExtra
        type = getIntent().getStringExtra("TYPE");
        if(type == null){
            Log.d("(activity) type","null");
        }
        else if(type.equals("User")){
            Log.d("(activity) type","User");
            user = (User)getIntent().getSerializableExtra("OBJECT");
        }
        else if(type.equals("Publisher")){
            Log.d("(activity) type","Publisher");
            publisher = (Publisher)getIntent().getSerializableExtra("OBJECT");
        }
        activityClass = (ActivityClass) getIntent().getSerializableExtra("ACTIVITY");
        // change activity
        ImageButton btn_back  = findViewById(R.id.backToHome);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityActivity.this, HomeActivity.class);
                intent.putExtra("TYPE",type);
                if(type == null){
                    Log.d("(activity->home) type","null");
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
        // get view
        TextView text_title = findViewById(R.id.activityDetailName);
        text_title.setText(activityClass.title);
        TextView text_publisher = findViewById(R.id.activityDetailProvider);
        text_publisher.setText(activityClass.publisher);
        TextView text_credit = findViewById(R.id.activityCredit);
        text_credit.setText(activityClass.credit+"");
        TextView text_start = findViewById(R.id.activityDetailStartDate);
        text_start.setText(activityClass.start);
        TextView text_end = findViewById(R.id.activityDetailEndDate);
        text_end.setText(activityClass.end);
        TextView text_description = findViewById(R.id.activityDetailDescription);
        text_description.setText(activityClass.description);
        TextView text_like = findViewById(R.id.likeNumber);
        text_like.setText(activityClass.likes+"");
        TextView text_dislike = findViewById(R.id.dislikeNumber);
        text_dislike.setText(activityClass.dislikes+"");
        ImageView image_detail = findViewById(R.id.activityDetailPicture);

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference("activity");

        Task<Uri> uri = storageReference.child(activityClass.pictureName).getDownloadUrl();
        while ((!uri.isComplete()));
        Uri downloadURI = uri.getResult();
        Picasso.with(ActivityActivity.this).load(downloadURI.toString()).fit().centerCrop().into(image_detail);


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        ImageButton like_button = findViewById(R.id.likeButton);
        like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityClass.likes = activityClass.likes +1;
                text_like.setText(activityClass.likes+"");
                DatabaseReference activityReference = firebaseDatabase.getReference("Activity");
                activityReference.orderByChild("title").equalTo(activityClass.title).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        String id = snapshot.getKey();
                        Log.d("(activity) like+1",id);
                        activityReference.child(id).child("likes").setValue(activityClass.likes);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        ImageButton dislike_button = findViewById(R.id.dislikeButton);
        dislike_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityClass.dislikes = activityClass.dislikes +1;
                text_dislike.setText(activityClass.dislikes+"");
                DatabaseReference activityReference = firebaseDatabase.getReference("Activity");
                activityReference.orderByChild("title").equalTo(activityClass.title).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        String id = snapshot.getKey();
                        Log.d("(activity) dislike+1",id);
                        activityReference.child(id).child("dislikes").setValue(activityClass.dislikes);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }
}