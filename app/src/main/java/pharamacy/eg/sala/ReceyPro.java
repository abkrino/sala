package pharamacy.eg.sala;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import pharamacy.eg.sala.Class.Product;
import pharamacy.eg.sala.adpter.MyAdapterList;
import pharamacy.eg.sala.adpter.MyAdapterList2;

public class ReceyPro extends AppCompatActivity {
    ArrayList<String> nameProductL, nameProductImported, nameProductAccessories,
            ownerProduct, nameCompany, PhoneNumber, country_workU, phoneNumberIdOffeise;
    ArrayList<MyItemList> listZeft, listKhara;
    String name_company, nameInList, textSearch, officePhoneNumber, phoneNumberPharmcy;
    String resultPay;
    RecyclerView ProductInfo;
    SearchView searchCompany;
    MyAdapterList myAdapterList;
    MyAdapterList2 myAdapterList2;
    private FirebaseUser user;
    private InterstitialAd mInterstitialAd;
    private ProgressDialog progressDialog;
    Activity myActivityProduct;
    Bundle bundle;
    String type1 = "أدويةمحلية", type2 = "", type3 = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recey_pro);
        nameProductL = new ArrayList<>();
        nameProductImported = new ArrayList<>();
        nameProductAccessories = new ArrayList<>();
        listZeft = new ArrayList<>();
        listKhara = new ArrayList<>();
        myActivityProduct = ReceyPro.this;
        //ـــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        showAdBar();
        //ـــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        bundle = getIntent().getExtras();
        //todo make thread to loading company
        nameInList = bundle.getString("nameInlist");
        type1 = bundle.getString("local_medicines");
        type2 = bundle.getString("accessories");
        type3 = bundle.getString("imported_medicines");
        name_company = bundle.getString("name_company");
        //ـــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        nameCompany = new ArrayList<>();
        PhoneNumber = new ArrayList<>();
        ownerProduct = new ArrayList<>();
        phoneNumberIdOffeise = new ArrayList<>();
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
            showProg("loading", "لحظات لتحميل منتجات الشركات...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    myActivityProduct.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getAccessories();
                        }
                    });
                }
            }).start();
        } else if (type3.equals("أدوية مستوردة") && type1.isEmpty() && type2.isEmpty()) {
            showProg("loading", "لحظات لتحميل منتجات الشركات...");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    myActivityProduct.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getImported_medicines();

                        }
                    });
                }
            }).start();
        }
        phoneNumberPharmcy = user.getPhoneNumber();
    }

    public void getLocal_medicines() {
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("product").child("أدويةمحلية").child(nameInList);
        if (reference2 != null) {
            reference2.addValueEventListener(new ValueEventListener() {
                int i = 0;

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ownerProduct.add(ds.getKey());
                            //todo الرقم بيطلع اخر واحد وبيجيب الدتا بتعته
                            officePhoneNumber = ds.getKey();
                            getInformtionOffice(ownerProduct.get(i), "أدويةمحلية");

                            i++;
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
                int i = 0;

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ownerProduct.add(ds.getKey());
                            ///////////////////////////////////////////////////////////////
                            //check country , get name company, get phone number of company
                            officePhoneNumber = ds.getKey();
                            getInformtionOffice(officePhoneNumber, "مستلزمات");
                            i++;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    // to find phone number offices's
    public void getImported_medicines() {
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("product").child("أدوية مستوردة").child(nameInList);
        try {
            reference2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            ownerProduct.add(ds.getKey());
                            officePhoneNumber = ds.getKey();
                            getInformtionOffice(officePhoneNumber, "أدوية مستوردة");
                            ///////////////////////////////////////////////////////////////
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } catch (NullPointerException e) {
            Toast.makeText(myActivityProduct, "please check internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
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

    //to find name , country work , number to call
    public void getInformtionOffice(String phone, String type) {
        ///////////////////////////////////////////////////////////////
        //check country , get name company, get phone number of company
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("Offices").child(phone);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    country_workU = (ArrayList<String>) dataSnapshot.child("country_work").getValue();
                    if (comperBetweenCountery()) {

                        PhoneNumber.add(Objects.requireNonNull(dataSnapshot.child("phoneNumber").getValue()).toString());
                        nameCompany.add(Objects.requireNonNull(dataSnapshot.child("nameU").getValue()).toString());
                        phoneNumberIdOffeise.add(phone);
//                        int size = (+(1));
//                        for (int i = 0; i < ownerProduct.size() - 1; i++) {

                            getListPriceToAdapter(phone, type);

//                        }


                    } else {

                        progressDialog.dismiss();
                        Toast.makeText(myActivityProduct, "لا يوجد من يخدم محافظتك", Toast.LENGTH_SHORT).show();
                    }
                } catch (NullPointerException e) {
                    country_workU = new ArrayList<>();

                    String aya7aga = (String) dataSnapshot.child("country_chooser").getValue();
                    country_workU.add(aya7aga);
                    databaseReference.child("country_work").setValue(country_workU);
                    getInformtionOffice(phone,type);
//                    startActivity(new Intent(ReceyPro.this, SearchProduct.class));
                }
                // stop here and i'm tierd to comper between counrty

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    // to mark if offic = phrmacy accurding to country work
    public boolean comperBetweenCountery() {
        if (country_workU.contains(name_company)) {
            return true;
//            getInformtionOffice();
        } else {
            return false;
        }

    }

    // set data to recycel view
    public void getListPriceToAdapter(String phone, String typeWork) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                myActivityProduct.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (typeWork.equals("أدويةمحلية")) {
                            DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference().child("product").child(typeWork).child(nameInList).child(phone);
                            reference3.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        MyItemList myItemList = dataSnapshot.getValue(MyItemList.class);
                                        listKhara.add(myItemList);
                                        if (ownerProduct.size() == listKhara.size()) {
                                            for (int i = 0; i < listKhara.size(); i++) {
                                                MyItemList listPro = new MyItemList(listKhara.get(i).getDiscount(), listKhara.get(i).getPrice(), nameCompany.get(i), nameInList, PhoneNumber.get(i), ownerProduct.get(i));

                                                listZeft.add(listPro);

                                            }


                                            ProductInfo.setVisibility(View.VISIBLE);
                                            //todo بيرتب الاسعار بس وبقيت الحاجة زي ما هيا
                                            myAdapterList = new MyAdapterList(listZeft, ReceyPro.this);
                                            myAdapterList.notifyDataSetChanged();
                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ReceyPro.this);
                                            ProductInfo.setLayoutManager(linearLayoutManager);
//                                        Collections.sort(listZeft);
                                            ProductInfo.setAdapter(myAdapterList);
                                            ProductInfo.setItemAnimator(new DefaultItemAnimator());
                                            progressDialog.dismiss();

                                            Toast.makeText(myActivityProduct, "جميع من يخدم محافظتك", Toast.LENGTH_SHORT).show();
                                        }
                                        //todo thinking about this .....
                                    }


                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }

                            });


                        } else {
                            DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference().child("product").child(typeWork).child(nameInList).child(phone);
                            reference3.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        MyItemList myItemList = dataSnapshot.getValue(MyItemList.class);
                                        listKhara.add(myItemList);
                                        if (ownerProduct.size() == listKhara.size()) {
                                            for (int i = 0; i < listKhara.size(); i++) {
                                                MyItemList listPro = new MyItemList(listKhara.get(i).getDiscount(), listKhara.get(i).getPrice(), nameCompany.get(i), nameInList, PhoneNumber.get(i), ownerProduct.get(i));

                                                listZeft.add(listPro);
                                            }
                                        }

                                        ProductInfo.setVisibility(View.VISIBLE);

                                        myAdapterList2 = new MyAdapterList2(listZeft, ReceyPro.this);
                                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ReceyPro.this);
                                        myAdapterList2.notifyDataSetChanged();

                                        ProductInfo.setLayoutManager(linearLayoutManager);
                                        ProductInfo.setAdapter(myAdapterList2);
                                        ProductInfo.setItemAnimator(new DefaultItemAnimator());
                                        progressDialog.dismiss();
                                        Toast.makeText(myActivityProduct, "جميع من يخدم محافظتك", Toast.LENGTH_SHORT).show();


                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }


                    }
                });
            }
        }).start();

    }

    public void showAdBar(){
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);
    }
}
