package touchfish.socialgood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ImageSwitcher ad_switcher;
    private int[] arrayAd = new int[]{R.mipmap.home_page_ad1,R.mipmap.home_page_ad2,R.mipmap.home_page_ad3};
    private int index;
    private float touchDownX;
    private float touchUpX;
    private GridLayout gl;

    static public User user;
    static public Publisher publisher;
    static public String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // getStringExtra
        type = getIntent().getStringExtra("TYPE");
        if(type == null){
            Log.d("(home) type","null");
        }
        else if(type.equals("User")){
            Log.d("(home) type","User");
            user = (User)getIntent().getSerializableExtra("OBJECT");
        }
        else if(type.equals("Publisher")){
            Log.d("(home) type","Publisher");
            publisher = (Publisher)getIntent().getSerializableExtra("OBJECT");
        }
        // an imagines switcher
        ad_switcher = findViewById(R.id.ad_switcher);
        ad_switcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView ad =new ImageView(HomeActivity.this);
                ad.setImageResource(arrayAd[index]);
                return ad;
            }
        });
        ad_switcher.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    touchDownX = event.getX();
                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    touchUpX=event.getX();
                    if(touchUpX-touchDownX>100){//右滑，显示上一张
                        index=index==0?arrayAd.length-1:index-1;
                        ad_switcher.setInAnimation(AnimationUtils.loadAnimation(HomeActivity.this,R.anim.in_from_left));
                        ad_switcher.setOutAnimation(AnimationUtils.loadAnimation(HomeActivity.this,R.anim.out_to_right));
                        ad_switcher.setImageResource(arrayAd[index]);

                    }
                    else if(touchUpX-touchDownX<-100){//左滑，显示下一张
                        index=index==arrayAd.length-1?0:index+1;
                        ad_switcher.setInAnimation(AnimationUtils.loadAnimation(HomeActivity.this,R.anim.in_from_right));
                        ad_switcher.setOutAnimation(AnimationUtils.loadAnimation(HomeActivity.this,R.anim.out_to_left));
                        ad_switcher.setImageResource(arrayAd[index]);

                    }
                }
                return false;
            }
        });
        // grid layout
        gl = findViewById(R.id.activity_grid_layout);
        if(gl.getChildCount() == 0) {
            readActivitiesData();
        }
        // change activity
        ImageButton shop = findViewById(R.id.bottom_shop);
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ShopActivity.class);
                intent.putExtra("TYPE",type);
                if(type == null){
                    Log.d("(home->shop) type","null");
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
        // change activity
        ImageButton mine = findViewById(R.id.bottom_mine);
        mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MineActivity.class);
                intent.putExtra("TYPE",type);
                if(type == null){
                    Log.d("(home->mine) type","null");
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
    }

    @GlideModule
    public class MyAppGlideModule extends AppGlideModule {

        public void registerComponents(Context context, Registry registry) {
            // Register FirebaseImageLoader to handle StorageReference
            registry.append(StorageReference.class, InputStream.class,
                    new FirebaseImageLoader.Factory());
        }
    }

    public void readActivitiesData(){
        // firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference activityReference = firebaseDatabase.getReference("Activity");

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference("activity");

        activityReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // read data from firebase
                    ActivityClass activityClass = dataSnapshot.getValue(ActivityClass.class);
                    //show image
                    ImageView imageView = new ImageView(HomeActivity.this);
                    Task<Uri> uri = storageReference.child(activityClass.pictureName).getDownloadUrl();
                    while ((!uri.isComplete()));
                    Uri downloadURI = uri.getResult();
                    Picasso.with(HomeActivity.this).load(downloadURI.toString()).fit().centerCrop().into(imageView);

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(HomeActivity.this, ActivityActivity.class);
                            intent.putExtra("TYPE", type);
                            if (type == null) {
                                Log.d("(home->activity) type", "null");
                            } else if (type.equals("User")) {
                                intent.putExtra("OBJECT", user);
                            } else if (type.equals("Publisher")) {
                                intent.putExtra("OBJECT", publisher);
                            }
                            intent.putExtra("ACTIVITY", activityClass);
                            startActivity(intent);
                        }
                    });
                    TextView textView = new TextView(HomeActivity.this);
                    textView.setText(activityClass.title);
                    textView.setTextSize(18);
                    textView.setTextColor(Color.rgb(0,0, 0));
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(HomeActivity.this, ActivityActivity.class);
                            intent.putExtra("TYPE",type);
                            if(type == null){
                                Log.d("(home->activity) type","null");
                            }
                            else if(type.equals("User")){
                                intent.putExtra("OBJECT",user);
                            }
                            else if(type.equals("Publisher")){
                                intent.putExtra("OBJECT",publisher);
                            }
                            intent.putExtra("ACTIVITY",activityClass);
                            startActivity(intent);
                        }
                    });
                    gl.addView(imageView);
                    gl.addView(textView);
                    imageView.getLayoutParams().width = 600;
                    imageView.getLayoutParams().height = 400;
                    textView.getLayoutParams().width = 600;
                    textView.getLayoutParams().height = 400;
                    textView.setPadding(80, 0, 0, 0);
                    textView.setGravity(Gravity.CENTER_VERTICAL);
                    Log.d("(home) readActivities",activityClass.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("(home) readActivities", "onCancelled");
                Toast.makeText(HomeActivity.this
                        , "Read ActivitiesData Failed.", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }
}