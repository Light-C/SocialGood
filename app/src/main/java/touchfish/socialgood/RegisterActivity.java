package touchfish.socialgood;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText email, username, password;
    private Button signUp, signUp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // initialization of the connect to Firebase
        FirebaseApp.initializeApp(getBaseContext());

        // initialization of all resources in Register Activity
        email = (EditText) findViewById(R.id.editTextRegE);
        username = (EditText) findViewById(R.id.editTextRegUserName);
        password = (EditText) findViewById(R.id.editTextRegPassword);
        signUp = (Button) findViewById(R.id.buttonRegSignUp);
        signUp2 = (Button) findViewById(R.id.buttonRegSignUp2);

        // First signup button for Publishers
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register("Publisher");
            }
        });

        // Second signup button for Users
        signUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register("User");
            }
        });

        // back to login page
        ImageButton btn_back  = findViewById(R.id.backToLogin);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    /**
     * Request users input with email, username, and password
     * Check the email, username and password are valid
     * Write data to the Firebase with different path: User or Publisher
     * @param tag get the input from the buttons that user choose to be a User or Publisher
     */
    private void register(String tag) {
        // initialization for three EditText to get user input
        String getEmail = email.getText().toString().trim();
        String getUsername = username.getText().toString().trim();
        String getPassword = password.getText().toString().trim();

        // check the input for email is not empty
        if (getEmail.isEmpty()) {
            email.setError("Please fill in the email address");
            email.requestFocus();
            return;
        }
        // check the user use valid email address
        if (!Patterns.EMAIL_ADDRESS.matcher(getEmail).matches()) {
            email.setError("Please fill in the valid email address");
            email.requestFocus();
            return;
        }
        // check the input for username is not empty
        if (getUsername.isEmpty()) {
            username.setError("Please fill in the username");
            username.requestFocus();
            return;
        }
        // check the input for password is not empty
        if (getPassword.isEmpty()) {
            password.setError("Please fill in the password");
            password.requestFocus();
            return;
        }
        // check the password length should greater than 6
        if (getPassword.length() < 6) {
            password.setError("Password length must greater than 6");
            password.requestFocus();
            return;
        }
        // begin to write data to the Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        if(tag.equals("User")){
            User user = new User(getEmail, getPassword, getUsername);
            DatabaseReference userReference = firebaseDatabase.getReference("User");
            userReference.push().setValue(user);
            Toast.makeText(RegisterActivity.this
                    , "Register User Successfully",
                    Toast.LENGTH_LONG).show();
        }
        else{
            Publisher publisher = new Publisher(getEmail, getPassword, getUsername);
            DatabaseReference publisherReference = firebaseDatabase.getReference("Publisher");
            publisherReference.push().setValue(publisher);
            Toast.makeText(RegisterActivity.this
                    , "Register Publisher Successfully",
                    Toast.LENGTH_LONG).show();
        }
    }
}
