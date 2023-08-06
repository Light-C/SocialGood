package touchfish.socialgood;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import touchfish.socialgood.search.Exp;
import touchfish.socialgood.search.Parser;
import touchfish.socialgood.search.Tokenizer;




public class ShopActivity extends AppCompatActivity {

    static public User user;
    static public Publisher publisher;
    static public String type;

    ArrayList<ItemModel> itemTest;
    BufferedReader bufferedReader;
    ArrayList<ItemModel> searchItem;
    private int[] firstPicture;

    private int[] ids ={0001,0002,0003,0004,0005,0006,0007};
    private String[] items = {"happy","money","mark","mc","kfc","tired","fat"};
    double[] prices = {1000,2000,3000,111,7888,2323,8882};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        type = getIntent().getStringExtra("TYPE");
        if(type == null){
            Log.d("(shop) type","null");
        }
        else if(type.equals("User")){
            Log.d("(shop) type","User");
            user = (User)getIntent().getSerializableExtra("OBJECT");
        }
        else if(type.equals("Publisher")){
            Log.d("(shop) type","Publisher");
            publisher = (Publisher)getIntent().getSerializableExtra("OBJECT");
        }

        ImageButton home = findViewById(R.id.bottom_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShopActivity.this, HomeActivity.class);
                intent.putExtra("TYPE",type);
                if(type == null){
                    Log.d("(shop->shop) type","null");
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

        ImageButton mine = findViewById(R.id.bottom_mine);
        mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShopActivity.this, MineActivity.class);
                intent.putExtra("TYPE",type);
                if(type == null){
                    Log.d("(shop->mine) type","null");
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

       itemTest = new ArrayList();
//       itemTest= readItemData();
//        for (int i =0;i< ids.length;i++){
//            ItemModel temp = new ItemModel();
//            temp.setItemName(items[i]);
//            temp.setItemPrice(prices[i]);
//            itemTest.add(temp);
//        }


//        itemTest = readItemData();
                readItemData();

//        ListView list_store = findViewById(R.id.list_store);
//
//        StoreAdapter storeAdapter = new StoreAdapter();
//
//        list_store.setAdapter(storeAdapter);
//        list_store.setOnItemClickListener(listStoreListener);
//
//        Button searchButton = (Button) findViewById(R.id.search_button);
//        searchButton.setOnClickListener(searchListener);
//
//

    }

    //    Initialize data
//    public ArrayList<ItemModel> readItemData() {
    public void readItemData() {
        ArrayList<ItemModel> itemsadd = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference itemReference = firebaseDatabase.getReference("Item");


        itemReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                int i =0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ItemModel itemTemp = dataSnapshot.getValue(ItemModel.class);
                    //start to show items
                    itemsadd.add(itemTemp);
                }







                itemTest.addAll(itemsadd);
                ListView list_store = findViewById(R.id.list_store);
                StoreAdapter storeAdapter = new StoreAdapter();
                list_store.setAdapter(storeAdapter);
                list_store.setOnItemClickListener(listStoreListener);
                Button searchButton = (Button) findViewById(R.id.search_button);
                searchButton.setOnClickListener(searchListener);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ShopFragment", "onCancelled");
            }
        });



//        return itemsadd;
    }



    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener searchListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void onClick(View v) {
            // do something when the button is clicked
//            Intent intent = new Intent(getApplicationContext(),ActivityWeb.class);
//            startActivity(intent);
            searchItem = new ArrayList<>();
            EditText  searchText= (EditText) findViewById(R.id.shop_search);
            String searchString = searchText.getText().toString();

            if (searchString.length()!=0){
                Tokenizer tokenizer = new Tokenizer(searchString);
                Tokenizer valueTokenizer = Parser.valueTokenizer(tokenizer);
                ArrayList searchObject = new ArrayList();
                Exp searchExp = new Parser(valueTokenizer).parseExp();
                ArrayList searchValueObject = new ArrayList();
                System.out.println(searchExp.show().length());
                if (searchExp.show().length()!=0){
                    if (searchExp.show().length()==1){

                        char single =  searchExp.show().charAt(0);

                        if (single>=48 && single<=57){
                            Double d = Double.valueOf(searchExp.show());
                            for (int i =0;i<itemTest.size();i++){
                                if (itemTest.get(i).getItemPrice().equals(d)){
                                    searchValueObject.add(itemTest.get(i));
                                }
                            }
                        }else {
                            for (int i =0;i<itemTest.size();i++){
                                if (itemTest.get(i).getItemName().indexOf(single)!=-1){
                                    searchValueObject.add(itemTest.get(i));
                                }
                            }
                        }

                    }else {
                        ArrayList temp = (ArrayList) searchExp.identify(searchObject);
                        for (int i =0;i< temp.size();i++){
                            System.out.println(temp.get(i).getClass());
                            if (temp.get(i).getClass().getTypeName().equals("java.lang.Character")){
                                char charElement = (char) temp.get(i);
//                            if ((int)charElement<=91){
//                                int temp11 =(int) charElement+32;
//                                charElement = (char) temp11;
//                            }

                                for (int j = 0;j<itemTest.size();j++){
                                    if (itemTest.get(j).getItemName().indexOf(charElement)!=-1){
                                        if (!searchValueObject.contains(itemTest.get(j))){
                                            searchValueObject.add(itemTest.get(j));
//                                        System.out.println(itemTest.get(j).getItemName());
                                        }
                                    }
                                }
                            }
                            else if (temp.get(i).getClass().getTypeName().equals("java.lang.Double")){
                                Double doubleElement = (Double) temp.get(i);
                                for (int j =0 ;j<itemTest.size();j++){
                                    if (itemTest.get(j).getItemPrice().equals(doubleElement)){
                                        if (!searchValueObject.contains(itemTest.get(j))){
                                            searchValueObject.add(itemTest.get(j));
//                                        System.out.println(itemTest.get(j).getItemName());
                                        }
                                    }
                                }
                            }
                        }
                    }

                }

//                System.out.println(searchValueObject.size());

                searchItem.addAll(searchValueObject);


                String[] searchResultName = new String[searchItem.size()];
                for (int i =0;i<searchItem.size();i++ ){
                    searchResultName[i] = searchItem.get(i).getItemName();
                }
//                System.out.println("okokokokokok11111");
                if (searchItem.size()!=0){
//
                    AlertDialog.Builder selectItem = new AlertDialog.Builder(ShopActivity.this);
                    selectItem.setTitle("Please select the item you are looking for");
                    selectItem.setItems(searchResultName, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(ShopActivity.this,ItemDetailActivity.class);
                            intent.putExtra("itemDetail", searchItem.get(i));
                            intent.putExtra("ItemUser",user);
                            startActivity(intent);
                        }
                    });
                    AlertDialog searchShow = selectItem.create();
                    searchShow.show();
                }else {
                    Toast.makeText(getApplicationContext(), "Can't find the item you need", Toast.LENGTH_SHORT).show();
                }
            }

            searchText.setText("");

        }
    };



    //        onClickListener
    private AdapterView.OnItemClickListener listStoreListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Intent intent = new Intent(ShopActivity.this,ItemDetailActivity.class);
            intent.putExtra("itemDetail", itemTest.get(i));
            intent.putExtra("ItemUser",user);
            startActivity(intent);

        }
    };


    class StoreAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return itemTest.size();
        }

        @Override
        public Object getItem(int i) {
            return itemTest.get(i);
        }

        @Override
        public long getItemId(int i) {
            return itemTest.get(i).getId();
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            ViewHolder holder;
            if (view==null){
                view = View.inflate(ShopActivity.this,R.layout.item_model,null);
                holder = new ViewHolder();
                holder.item = view.findViewById(R.id.item_name);
                holder.price = view.findViewById(R.id.item_price);
                holder.photo = view.findViewById(R.id.good_picture);
                view.setTag(holder);
            }else {
                holder = (ViewHolder) view.getTag();
            }
            holder.item.setText(itemTest.get(position).getItemName());
            holder.price.setText(String.valueOf(itemTest.get(position).getItemPrice()));
            holder.photo.setImageResource(itemTest.get(position).getItemFirstImg());


            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference("product");

            Task<Uri> uri = storageReference.child(itemTest.get(position).getPictureName()).getDownloadUrl();
            while ((!uri.isComplete()));
            Uri downloadURI = uri.getResult();
            Picasso.with(ShopActivity.this).load(downloadURI.toString()).fit().centerCrop().into(holder.photo);


            return view;
        }
    }


    class ViewHolder{
        TextView item;
        TextView price;
        ImageView photo;
    }




}