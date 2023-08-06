package touchfish.socialgood;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class RegisterTest {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Test(timeout = 1000)
    public void userRegisterTest() {
        String email = "userTest@Test.com";
        String password = "password";
        String username = "userRegisterTest";
        final String[] getEmail = new String[1];
        final String[] getPassword = new String[1];
        final String[] getUsername = new String[1];

        User user = new User(email, password, username);
        DatabaseReference userReference = firebaseDatabase.getReference("User");
        userReference.push().setValue(user);

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User temp = dataSnapshot.getValue(User.class);
                    if (temp.email.equals(email)) {
                        getEmail[0] = temp.email;
                    }
                    if (temp.password.equals(password)) {
                        getPassword[0] = temp.password;
                    }
                    if (temp.username.equals(username)) {
                        getUsername[0] = temp.username;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                return;
            }
        });

        assertEquals(email, getEmail[0]);
        assertEquals(password, getPassword[0]);
        assertEquals(username, getUsername[0]);
    }

        @Test(timeout = 1000)
    public void pubRegisterTest() {
        String email = "pubTest@Test.com";
        String password = "password";
        String username = "pubRegisterTest";
        final String[] getEmail = new String[1];
        final String[] getPassword = new String[1];
        final String[] getUsername = new String[1];

        User user = new User(email, password, username);
        DatabaseReference userReference = firebaseDatabase.getReference("Publisher");
        userReference.push().setValue(user);

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User temp = dataSnapshot.getValue(User.class);
                    if (temp.email.equals(email)) {
                        getEmail[0] = temp.email;
                    }
                    if (temp.password.equals(password)) {
                        getPassword[0] = temp.password;
                    }
                    if (temp.username.equals(username)) {
                        getUsername[0] = temp.username;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                return;
            }
        });

        assertEquals(email, getEmail[0]);
        assertEquals(password, getPassword[0]);
        assertEquals(username, getUsername[0]);
    }
}
