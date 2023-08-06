package touchfish.socialgood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class HistoryActivity extends AppCompatActivity {

    static public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        user = (User)getIntent().getSerializableExtra("OBJECT");

        ImageButton back = findViewById(R.id.backToMine);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryActivity.this, MineActivity.class);
                intent.putExtra("TYPE", "User");
                intent.putExtra("OBJECT", user);
                startActivity(intent);
            }
        });

    }
}