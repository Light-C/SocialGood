package touchfish.socialgood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class FollowActivity extends AppCompatActivity {

    static public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        user = (User)getIntent().getSerializableExtra("OBJECT");

        ImageButton back = findViewById(R.id.backToMine);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FollowActivity.this, MineActivity.class);
                intent.putExtra("TYPE", "User");
                intent.putExtra("OBJECT", user);
                startActivity(intent);
            }
        });
    }
}