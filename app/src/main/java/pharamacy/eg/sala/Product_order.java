
package pharamacy.eg.sala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pharamacy.eg.sala.Class.MainAdapter;
import pharamacy.eg.sala.Class.Product;
import pharamacy.eg.sala.adpter.AdapterPro;
import pharamacy.eg.sala.sql.DataBaseHelper;
import pharamacy.eg.sala.ui2.OrderPharmacy;

public class Product_order extends AppCompatActivity {
    RecyclerView listView;
    Button call, cancel;
    Bundle bundle;
    String date, number, phoneOwner, nameProductS, nameCompany, numberPharmacy, numberOffices;
    String type;
    Cursor res;
    DataBaseHelper myDataBase;
    ArrayList<Product> myOrder;
    double prices, discounts;
    Product productOrder;
    DatabaseReference reference;
    MainAdapter adapter;
    TextView nameCompanyorder, dateOrder, numberpro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("قائمة الاصناف المطلوبة ");
        setContentView(R.layout.activity_product_order2);
        listView = findViewById(R.id.listPro);
        call = findViewById(R.id.btCall);
        cancel = findViewById(R.id.cancel);
        nameCompanyorder = findViewById(R.id.nameCompanyOrder);
//        numberoffces = findViewById(R.id.nu);
        dateOrder = findViewById(R.id.dateOrder);
        numberpro = findViewById(R.id.numberOfOrder);


        myDataBase = new DataBaseHelper(getBaseContext());
        res = myDataBase.getAllData();

        myOrder = new ArrayList<>();
        bundle = getIntent().getExtras();
        date = bundle.getString("dateOrder");
        number = bundle.getString("numberOwner");
        numberOffices = bundle.getString("numberOffices");
        type = bundle.getString("type");
        numberPharmacy = bundle.getString("numberPharmacy");
        ArrayList<Product> pro = getProductCompany(date, number);

        dateOrder.setText(date);
            adapter = new MainAdapter(getBaseContext(), pro);
            numberpro.setText(pro.size() + "");
            nameCompanyorder.setText(nameCompany);
            listView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);




        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushAndcall();

            }
        });

    }

    public ArrayList<Product> getProductCompany(String date, String numberOffice) {

        switch (type) {
            case "Pharmacy":
                if (res != null && res.getCount() > 0) {
                    if (res.moveToFirst()) {
                        if (date.equals(res.getString(8)) && numberOffice.equals(res.getString(6))) {
                            nameCompany = res.getString(2);

                            nameProductS = res.getString(1);
                            prices = res.getDouble(3);
                            discounts = res.getDouble(4);
                            productOrder = new Product(nameProductS, prices, discounts);
                            myOrder.add(productOrder);
                        }
                        while (res.moveToNext()) {
                            if (date.equals(res.getString(8)) && numberOffice.equals(res.getString(6))) {
                                nameCompany = res.getString(2);

                                nameProductS = res.getString(1);
                                prices = res.getDouble(3);
                                discounts = res.getDouble(4);
                                phoneOwner = res.getString(5);
                                productOrder = new Product(nameProductS, prices, discounts);
                                myOrder.add(productOrder);

                            }

                        }

                    }


                }
            break;
            case "Offices":
                productOrder = new Product(nameProductS, prices, discounts);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("Offices").child(numberOffices).child("order").child(date).child(numberOffice);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<ArrayList<Product>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Product>>() {
                        };

//                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        // TODO: handle the post
                        ArrayList<Product> pp = dataSnapshot.getValue(genericTypeIndicator);
                        myOrder = pp;
                        adapter = new MainAdapter(getBaseContext(), myOrder);
                        numberpro.setText(myOrder.size() + "");

                        listView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);
//                        notifyDataSetChanged();
//                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                DatabaseReference asd = FirebaseDatabase.getInstance().getReference().child("users").child("pharmacies").child(numberOffice).child("nameU");
                asd.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    nameCompany =dataSnapshot.getValue().toString();
                        nameCompanyorder.setText("صيدلية :"+nameCompany);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;

        }


//        return myOrder;
        return myOrder;
    }

    public void pushAndcall() {
        switch (type) {
            case "Pharmacy":
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        reference = FirebaseDatabase.getInstance().getReference();

//                    ProductOrder newOrder = new ProductOrder(nameProductS, prices, discounts);
                        reference.child("users").child("Offices").child(number).child("order").child(date).child(numberPharmacy).setValue(myOrder);

                    }
                }).start();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneOwner));
                startActivity(intent);
                finish();
                break;
            case "Offices":
                Intent intent1 = new Intent(Intent.ACTION_DIAL);
                intent1.setData(Uri.parse("tel:" + number));
                startActivity(intent1);
                finish();
                break;
        }


    }


    public void goback(View view) {
        finish();
    }
}
