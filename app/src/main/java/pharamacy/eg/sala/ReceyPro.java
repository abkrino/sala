package pharamacy.eg.sala;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReceyPro extends AppCompatActivity {
    ArrayList<String> nameProductL, nameProductImported, nameProductAccessories, ownerProduct, nameCompany, PhoneNumber, country_workU;
    ArrayList<MyItemList> listZeft;
    String name_company, nameInList, textSearch, officePhoneNumber, phoneNumberPharmcy;
    RecyclerView ProductInfo;
    SearchView searchCompany;
    MyAdapterList myAdapterList;
    private FirebaseUser user;
    private InterstitialAd mInterstitialAd;
    private ProgressDialog progressDialog;
    Activity myActivityProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recey_pro);
        String type1, type2, type3;
        Bundle rx = getIntent().getExtras();
        //todo make thread to loading company
        nameInList = rx.getString("nameInlist");
        type1 = rx.getString("local_medicines");
        type2 = rx.getString("accessories");
        type3 = rx.getString("imported_medicines");
        nameProductL = new ArrayList<>();
        nameProductImported = new ArrayList<>();
        nameProductAccessories = new ArrayList<>();
        listZeft = new ArrayList<>();
        myActivityProduct = ReceyPro.this;
        //ـــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        nameCompany = new ArrayList<>();
        PhoneNumber = new ArrayList<>();
        ownerProduct = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        ProductInfo = findViewById(R.id.list_main2);
        if (type1.equals("أدويةمحلية") && type2.isEmpty() && type3.isEmpty()) {
            showProg("loading", "لحظات لتحميل منتجات الشركات...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    myActivityProduct.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getLocal_medicines();
                        }
                    });
                }
            }).start();
        } else if (type2.equals("مستلزمات") && type1.isEmpty() && type3.isEmpty()) {

                getAccessories();
            } else if (type3.equals("أدوية مستوردة") && type1.isEmpty() && type2.isEmpty()) {
                    getImported_medicines();
                }
        phoneNumberPharmcy = user.getPhoneNumber();
    }

    public void getLocal_medicines() {
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("product").child("أدويةمحلية").child(nameInList);
        if (reference2 != null) {
            reference2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ownerProduct.add(ds.getKey());
                            officePhoneNumber = ds.getKey();
                            getInformtionOffice(officePhoneNumber);
                            getCounteryOfpharmacies(phoneNumberPharmcy);
                            getListPriceToAdapter("أدويةمحلية");
                            //todo

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    public void getAccessories() {
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
                            officePhoneNumber = ds.getKey();
                            getInformtionOffice(officePhoneNumber);
                            getCounteryOfpharmacies(phoneNumberPharmcy);
                            getListPriceToAdapter("مستلزمات");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }
    public void getImported_medicines() {
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("product").child("أدوية مستوردة").child(nameInList);
        if (reference2 != null) {
            reference2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ownerProduct.add(ds.getKey());
                            officePhoneNumber = ds.getKey();
                            getInformtionOffice(officePhoneNumber);
                            getCounteryOfpharmacies(phoneNumberPharmcy);
                            getListPriceToAdapter("أدوية مستوردة");
                            ///////////////////////////////////////////////////////////////
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

    }

    public void showAd() {
        mInterstitialAd = new InterstitialAd(ReceyPro.this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("BFE5A6AC72AC4A402AFDA3209FDB660A").build());
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    public void showProg(String title, String message) {
        progressDialog = new ProgressDialog(ReceyPro.this);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false); //To prevent the user dismiss progressDialog
        progressDialog.show();

    }

    public void getInformtionOffice(String phone) {
        ///////////////////////////////////////////////////////////////
        //check country , get name company, get phone number of company
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("Offices").child(phone);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PhoneNumber.add(dataSnapshot.child("phoneNumber").getValue().toString());
                nameCompany.add(dataSnapshot.child("nameU").getValue().toString());
                country_workU = (ArrayList<String>) dataSnapshot.child("country_work").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void getCounteryOfpharmacies(String phone) {
        DatabaseReference databaseReferencePh = FirebaseDatabase.getInstance().getReference().child("users").child("pharmacies").child(phone);
        databaseReferencePh.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name_company = dataSnapshot.child("country_chooser").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //todo انا وقفت هنا لسا فاضل اجيب بقيت الداتا بتاعت المنتج واقرنها بالمحافظة بتاعة الصيدلية وشكرا لحسن استماعكم
    public void getListPriceToAdapter(String typeWork) {

        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference().child("product").child(typeWork).child(nameInList).child(officePhoneNumber);
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

        progressDialog.dismiss();


    }
}
