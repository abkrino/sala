package pharamacy.eg.sala;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.os.SystemClock.sleep;

public class ReceyPro extends AppCompatActivity {
    ArrayList<String> nameProductL, nameProductImported, nameProductAccessories, ownerProduct, nameCompany, PhoneNumber, country_workU;
    ArrayList<MyItemList> listZeft;
    String name_company, nameInList, textSearch;
    RecyclerView ProductInfo;
    SearchView searchCompany;
    MyAdapterList myAdapterList;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recey_pro);
        String type1, type2, type3;
        nameInList = getIntent().getStringExtra("nameInlist");
        type1 = getIntent().getStringExtra("local_medicines");
        type2 = getIntent().getStringExtra("accessories");
        type3 = getIntent().getStringExtra("imported_medicines");
        nameProductL = new ArrayList<>();
        nameProductImported = new ArrayList<>();
        nameProductAccessories = new ArrayList<>();
        listZeft = new ArrayList<>();
        //ـــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        nameCompany = new ArrayList<>();
        PhoneNumber = new ArrayList<>();
        ownerProduct = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        ProductInfo = findViewById(R.id.list_main2);
        if (type1.equals("أدويةمحلية") && type2.isEmpty() && type3.isEmpty()) {
            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("product").child("أدويةمحلية").child(nameInList);
            if (reference2 != null) {
                reference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                ownerProduct.add(ds.getKey());
                                ///////////////////////////////////////////////////////////////
                                //check country , get name company, get phone number of company
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("Offices").child(ds.getKey());
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        PhoneNumber.add(dataSnapshot.child("phoneNumber").getValue().toString());
                                        nameCompany.add(dataSnapshot.child("nameU").getValue().toString());
//                                        sleep(300);
                                        country_workU = (ArrayList<String>) dataSnapshot.child("country_work").getValue();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                                DatabaseReference databaseReferencePh = FirebaseDatabase.getInstance().getReference().child("users").child("pharmacies").child(user.getPhoneNumber());
                                databaseReferencePh.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference().child("product").child("أدويةمحلية").child(nameInList).child(ds.getKey());
                                        name_company = dataSnapshot.child("country_chooser").getValue().toString();
                                        if (country_workU != null)
                                            if (country_workU.contains(name_company)) {
                                                if (reference3 != null) {
                                                    reference3.addValueEventListener(new ValueEventListener() {
                                                        String name3 = nameInList;
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists()) {
//                                                    for (int i =0 ;i<ownerProduct.size();i++) {
                                                                MyItemList myItemList = dataSnapshot.getValue(MyItemList.class);
                                                                listZeft.add(myItemList);
                                                                ProductInfo.setVisibility(View.VISIBLE);
                                                                myAdapterList = new MyAdapterList(listZeft, nameCompany, name3, PhoneNumber, ReceyPro.this);
                                                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ReceyPro.this);
                                                                ProductInfo.setLayoutManager(linearLayoutManager);
                                                                ProductInfo.setAdapter(myAdapterList);
                                                                ProductInfo.setItemAnimator(new DefaultItemAnimator());
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            } else {
                                                Toast.makeText(ReceyPro.this, "لا يوجد مخزن يخدم محافظتك ", Toast.LENGTH_LONG).show();
                                            }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        } else {
            if (type2.equals("مستلزمات") && type1.isEmpty() && type3.isEmpty()) {
                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("product").child("مستلزمات").child(nameInList);
                if (reference2 != null) {
                    reference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    ownerProduct.add(ds.getKey());
                                    ///////////////////////////////////////////////////////////////
                                    //check country , get name company, get phone number of company
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("Offices").child(ds.getKey());
                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            PhoneNumber.add(dataSnapshot.child("phoneNumber").getValue().toString());
                                            nameCompany.add(dataSnapshot.child("nameU").getValue().toString());
//                                            sleep(300);
                                            country_workU = (ArrayList<String>) dataSnapshot.child("country_work").getValue();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                    DatabaseReference databaseReferencePh = FirebaseDatabase.getInstance().getReference().child("users").child("pharmacies").child(user.getPhoneNumber());
                                    databaseReferencePh.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference().child("product").child("مستلزمات").child(nameInList).child(ds.getKey());
                                            name_company = dataSnapshot.child("country_chooser").getValue().toString();
                                            if (country_workU != null)
                                                if (country_workU.contains(name_company)) {
                                                    if (reference3 != null) {
                                                        reference3.addValueEventListener(new ValueEventListener() {
                                                            String name3 = nameInList;

                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.exists()) {
//                                                    for (int i =0 ;i<ownerProduct.size();i++) {
                                                                    MyItemList myItemList = dataSnapshot.getValue(MyItemList.class);
                                                                    listZeft.add(myItemList);
                                                                    ProductInfo.setVisibility(View.VISIBLE);
                                                                    MyAdapterList2 myAdapterList2 = new MyAdapterList2(listZeft, nameCompany, name3, PhoneNumber, ReceyPro.this);
                                                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ReceyPro.this);
                                                                    ProductInfo.setLayoutManager(linearLayoutManager);
                                                                    ProductInfo.setAdapter(myAdapterList2);
                                                                    ProductInfo.setItemAnimator(new DefaultItemAnimator());
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                } else {
                                                    Toast.makeText(ReceyPro.this, "لا يوجد مخزن يخدم محافظتك ", Toast.LENGTH_LONG).show();
                                                }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            } else {
                if (type3.equals("أدوية مستوردة") && type1.isEmpty() && type2.isEmpty()) {
                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("product").child("أدوية مستوردة").child(nameInList);
                    if (reference2 != null) {
                        reference2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        ownerProduct.add(ds.getKey());
                                        ///////////////////////////////////////////////////////////////
                                        //check country , get name company, get phone number of company
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("Offices").child(ds.getKey());
                                        databaseReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                PhoneNumber.add(dataSnapshot.child("phoneNumber").getValue().toString());
                                                nameCompany.add(dataSnapshot.child("nameU").getValue().toString());
                                                sleep(300);
                                                country_workU = (ArrayList<String>) dataSnapshot.child("country_work").getValue();


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });
                                        DatabaseReference databaseReferencePh = FirebaseDatabase.getInstance().getReference().child("users").child("pharmacies").child(user.getPhoneNumber());
                                        databaseReferencePh.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference().child("product").child("أدوية مستوردة").child(nameInList).child(ds.getKey());
                                                name_company = dataSnapshot.child("country_chooser").getValue().toString();
                                                if (country_workU != null)
                                                    if (country_workU.contains(name_company)) {
                                                        if (reference3 != null) {
                                                            reference3.addValueEventListener(new ValueEventListener() {
                                                                String name3 = nameInList;

                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    if (dataSnapshot.exists()) {
//                                                    for (int i =0 ;i<ownerProduct.size();i++) {
                                                                        MyItemList myItemList = dataSnapshot.getValue(MyItemList.class);
                                                                        listZeft.add(myItemList);

                                                                        ProductInfo.setVisibility(View.VISIBLE);
                                                                        MyAdapterList2 myAdapterList2 = new MyAdapterList2(listZeft, nameCompany, name3, PhoneNumber, ReceyPro.this);
                                                                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ReceyPro.this);
                                                                        ProductInfo.setLayoutManager(linearLayoutManager);
                                                                        ProductInfo.setAdapter(myAdapterList2);
                                                                        ProductInfo.setItemAnimator(new DefaultItemAnimator());
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });
                                                        }
                                                    } else {
                                                        Toast.makeText(ReceyPro.this, "لا يوجد مخزن يخدم محافظتك ", Toast.LENGTH_LONG).show();
                                                    }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
            }
        }

    }
}
