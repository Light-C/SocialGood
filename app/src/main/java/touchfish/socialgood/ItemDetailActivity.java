package touchfish.socialgood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ItemDetailActivity extends AppCompatActivity {
    ItemModel item;
    User user;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Intent intent= getIntent();
        item = (ItemModel)intent.getSerializableExtra("itemDetail");
        user = (User) intent.getSerializableExtra("ItemUser");
        firebaseDatabase = FirebaseDatabase.getInstance();



//        ImageView img = findViewById(R.id.productDetailPicture);
        TextView itemName = findViewById(R.id.productDetailName);
        TextView itemPrice = findViewById(R.id.productDetailPrice);
        TextView itemSurplus = findViewById(R.id.productDetailAmount);
        TextView itemPublisher = findViewById(R.id.productDetailProvider);
        TextView itemInfo = findViewById(R.id.productDetailDescription);
        Button itemBuy = (Button)findViewById(R.id.productDetailBuy);
        ImageButton itemBack = findViewById(R.id.productDetailBack) ;
//        img.setImageResource(item.getItemFirstImg());
        itemName.setText(item.getItemName());
        itemPrice.setText(String.valueOf(item.getItemPrice()));
        itemSurplus.setText(String.valueOf(item.getItemSurplus()));
        itemPublisher.setText(item.getPublisher());
        itemInfo.setText(item.getItemInfo());

        itemBuy.setOnClickListener(itemBuyListener);
        itemBack.setOnClickListener(itemBackListener);

    }

    private View.OnClickListener itemBuyListener = new View.OnClickListener() {
        public void onClick(View v) {
            // do something when the button is clicked
//            Intent intent = new Intent(getApplicationContext(),ActivityWeb.class);
//            startActivity(intent);
            if (user.credit>= item.getItemPrice() && item.getItemSurplus()>0){
                user.credit = (int) (user.credit-item.getItemPrice());
                item.setItemSurplus(item.getItemSurplus()-1);
                TextView itemSurplus = findViewById(R.id.productDetailAmount);
                itemSurplus.setText(String.valueOf(item.getItemSurplus()));
                Toast.makeText(ItemDetailActivity.this,"You bought this product\n"+
                                "You still have:"+user.credit+"credit!",
                        Toast.LENGTH_SHORT).show();
                DatabaseReference itemReference = firebaseDatabase.getReference("Item");
                itemReference.orderByChild("itemName").equalTo(item.getItemName())
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                String id = snapshot.getKey();
                                Log.d("ItemSurplus-1",id);
                                itemReference.child(id).child("itemSurplus").setValue(item.getItemSurplus());
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
                DatabaseReference userReference = firebaseDatabase.getReference("User");
                userReference.orderByChild("email").equalTo(user.email)
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                String id = snapshot.getKey();
                                Log.d("UserCredichange",id);
                                userReference.child(id).child("credit").setValue(user.credit);
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

            }else {
                Toast.makeText(ItemDetailActivity.this,"You have not enough credit!",
                        Toast.LENGTH_SHORT).show();
            }


        }
    };












    private View.OnClickListener itemBackListener = new View.OnClickListener() {
        public void onClick(View v) {
            // do something when the button is clicked
//            ShopFragment shopFragment = new ShopFragment();
//            FragmentManager fm = getFragmentManager();
//            FragmentTransaction ft = fm.beginTransaction();
//            ft.replace(R.id.nav_host_fragment_activity_main, shopFragment);
//            ft.commit();

            Intent intent = new Intent(ItemDetailActivity.this, ShopActivity.class);
            startActivity(intent);

        }
    };


}