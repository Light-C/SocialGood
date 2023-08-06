package touchfish.socialgood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private Button login;
    private TextView signUp;
    public boolean successful = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(getBaseContext());

        email = (EditText) findViewById(R.id.editTextLoginE);
        password = (EditText) findViewById(R.id.editTextLoginPass);
        login = (Button) findViewById(R.id.buttonLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                loginCheck();
            }
        });

        signUp = (TextView) findViewById(R.id.textViewSignUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(LoginActivity.this,
                        RegisterActivity.class);
                startActivity(register);
            }
        });
    }

    private void loginCheck() {
        String getEmail = email.getText().toString().trim();
        String getPassword = password.getText().toString().trim();

        if (getEmail.isEmpty()) {
            email.setError("Please fill in the email address");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(getEmail).matches()) {
            email.setError("Please fill in the valid email address");
            email.requestFocus();
            return;
        }
        if (getPassword.isEmpty()) {
            password.setError("Please fill in the password");
            password.requestFocus();
            return;
        }
        if (getPassword.length() < 6) {
            password.setError("Password length must greater than 6");
            password.requestFocus();
            return;
        }

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userReference = firebaseDatabase.getReference("User");
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    if(getEmail.equals(user.email)){
                        if( getPassword.equals(user.password)) {
                            Log.d("user login", "find dataSnapshot password right");
                            Toast.makeText(LoginActivity.this
                                    , "User Login successful!", Toast.LENGTH_LONG)
                                    .show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.putExtra("TYPE","User");
                            intent.putExtra("OBJECT",user);
                            startActivity(intent);
                            return;
                        }
                        else {
                            Log.d("user login", "find dataSnapshot password wrong");
                            Toast.makeText(LoginActivity.this
                                    , "User Login Failed. Check your password", Toast.LENGTH_LONG)
                                    .show();
                            return;
                        }
                    }
                }
                Log.d("user login","email not found");
                DatabaseReference publisherReference = firebaseDatabase.getReference("Publisher");
                publisherReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Publisher publisher = dataSnapshot.getValue(Publisher.class);

                            if (getEmail.equals(publisher.email)) {
                                if (getPassword.equals(publisher.password)) {
                                    Log.d("publisher login", "find dataSnapshot password right");
                                    Toast.makeText(LoginActivity.this
                                            , "Publisher Login successful!", Toast.LENGTH_LONG)
                                            .show();
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    intent.putExtra("TYPE","Publisher");
                                    intent.putExtra("OBJECT",publisher);
                                    startActivity(intent);
                                    return;
                                } else {
                                    Log.d("publisher login", "find dataSnapshot password wrong");
                                    Toast.makeText(LoginActivity.this
                                            , "Publisher Login Failed. Check your password", Toast.LENGTH_LONG)
                                            .show();
                                    return;
                                }
                            }
                        }
                        Log.d("publisher login", "email not found");
                        Toast.makeText(LoginActivity.this
                                , "Login Failed. This email has not been registered", Toast.LENGTH_LONG)
                                .show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("publisher login", "onCancelled");
                        Toast.makeText(LoginActivity.this
                                , "Publisher Login Failed. Check your email and password", Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("login","onCancelled");
                Toast.makeText(LoginActivity.this
                                    , "Login Failed. Check your email and password", Toast.LENGTH_LONG)
                                    .show();
            }
        });


    }
}