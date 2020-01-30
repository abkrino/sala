package pharamacy.eg.sala;

import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import pharamacy.eg.sala.Class.GlideApp;

public class SearchProduct extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener  {
    @Override
    protected void onResume() {
        checkConnection();
        super.onResume();
    }

    private AppBarConfiguration mAppBarConfiguration;
    private StorageReference mStorage;
    public String userId;
    String nameU;
    private FirebaseUser user;
    View headerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout2);
        NavigationView navigationView = findViewById(R.id.nav_view2);
        headerView = navigationView.getHeaderView(0);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home2, R.id.nav_profile2, R.id.nav_order2,
                R.id.nav_connectUs2, R.id.nav_about2, R.id.nav_signOut2)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment2);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getPhoneNumber();
        }
        mStorage = FirebaseStorage.getInstance().getReference().child("images/").child(userId);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child("pharmacies").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                nameU = dataSnapshot.child("nameU").getValue().toString();
                    TextView name = headerView.findViewById(R.id.nameHeadr);
                    name.setText("صيدلية : "+nameU);
                }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ImageView imageView= headerView.findViewById(R.id.imageViewHeader);
        TextView number =headerView.findViewById(R.id.numberheadr);

       number.setText(userId);
        mStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                GlideApp.with(SearchProduct.this)
                        .load(uri)
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_offices, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment2);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }
    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        Snackbar snackbar;
        if (isConnected) {

        } else {
            snackbar  = Snackbar.make(findViewById(R.id.drawer_layout2), Html.fromHtml("<font color=\"#D81B60\">Sorry! Not connected to internet</font>") , Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }


//    Home_pharm home_pharm = new Home_pharm();



//    public void goCall(View view) {
//        MyAdapterList adapterList = new MyAdapterList();
//        phone=adapterList.getNumoo();
//        Intent intent = new Intent(Intent.ACTION_DIAL);
//        intent.setData(Uri.parse("tel:" + phone));
//        startActivity(intent);
//    }
}
class ListOfPrice extends Fragment{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}

//    Button local_medicines, imported_medicines, accessories, button2;
//    ArrayList<String> nameProductL, nameProductImported, nameProductAccessories, ownerProduct, nameCompany;
//    ArrayList<String> country_workU;
//    private InterstitialAd mInterstitialAd;
//    DataBaseHelper mydb = new DataBaseHelper(this);
//    private ProgressBar lodaer;
//    private FirebaseAuth mAuth;
//    ////////////////////////////////////////////////////////////////////////////////////////////
//    ListView listProductInfo;
//    RecyclerView ProductInfo;
//    String name_company, PhoneNumber, nameInList;
//    ArrayList<MyItemList> listZeft;
//    BottomSheetBehavior behavior;
//    private NavigationView navigationView;
//    SearchView search;
//    String text;
//    ArrayAdapter<String> adapter;
//    Intent intent1;
//    ///////////////////////////////////////////////////////////////////////////////////////////
//    //orderMap
//    String nameComMap;
//    ArrayList<String> productMap = new ArrayList<>();
//    Map<String, ArrayList<String>> order = new HashMap<>();
//
//    public Map<String, ArrayList<String>> getOrder() {
//        return order;
//    }
//
//    ///////////////////////////////////////////////////////////////////////////////////////////
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search_product);
//        //////////////////////////////////////////////////////////////////////////////////////////
//        nameProductL = new ArrayList<>();
//        nameProductImported = new ArrayList<>();
//        nameProductAccessories = new ArrayList<>();
//        listZeft = new ArrayList<>();
//        /////////////////////////////////////////////////////////////////////////////////////////
//        nameCompany = new ArrayList<>();
//        ownerProduct = new ArrayList<>();
//        local_medicines = findViewById(R.id.Local_medicines);
//        imported_medicines = findViewById(R.id.Imported_medicines);
//        accessories = findViewById(R.id.Accessories);
//        View BottomSheet = findViewById(R.id.design_bottom_sheet);
//        listProductInfo = findViewById(R.id.list_main);
//        ProductInfo = findViewById(R.id.list_main2);
//        navigationView = findViewById(R.id.nav_view);
////        button2 = findViewById(R.id.button2);
////        mAuth = FirebaseAuth.getInstance();
//        //order
//        View header = navigationView.getHeaderView(0);
////        ConstraintLayout orderbt = header.findViewById(R.id.order);
////        ConstraintLayout profile = header.findViewById(R.id.profile_btn);
////        ConstraintLayout contactUs = header.findViewById(R.id.contactUs);
////        ConstraintLayout signOut = header.findViewById(R.id.signOut);
////        signOut.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
//                mAuth.signOut();
////                startActivity(new Intent(SearchProduct.this, SignIN.class));
////                finish();
////            }
////        });
////        profile.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                startActivity(new Intent(SearchProduct.this,Profile.class));
////                finish();
////            }
////        });
////        contactUs.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////            startActivity(new Intent(SearchProduct.this,ContactUs.class));
////            }
////        });
////        orderbt.setVisibility(View.VISIBLE);
////        orderbt.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                intent1 = new Intent(SearchProduct.this, ProductOrder.class);
////                startActivity(intent1);
////
////            }
////        });
//        ///////////////////////////////////////////////////////////////////////////////////////////
//
//        behavior = BottomSheetBehavior.from(BottomSheet);
//        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View view, int i) {
//                switch (i) {
//                    case BottomSheetBehavior.STATE_DRAGGING:
//                        Toast.makeText(SearchProduct.this, "STATE_DRAGGING", Toast.LENGTH_LONG).show();
//                        break;
//                    case BottomSheetBehavior.STATE_SETTLING:
//                        Toast.makeText(SearchProduct.this, "STATE_SETTLING", Toast.LENGTH_LONG).show();
//                        break;
//                    case BottomSheetBehavior.STATE_EXPANDED:
//                        behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
//                        Toast.makeText(SearchProduct.this, "STATE_EXPANDED", Toast.LENGTH_LONG).show();
//                        break;
//                    case BottomSheetBehavior.STATE_COLLAPSED:
//                        Toast.makeText(SearchProduct.this, "STATE_COLLAPSED", Toast.LENGTH_LONG).show();
//                        break;
//                    case BottomSheetBehavior.STATE_HIDDEN:
//                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                        Toast.makeText(SearchProduct.this, "STATE_HIDDEN", Toast.LENGTH_LONG).show();
//                        break;
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View view, float v) {
//
//            }
//        });
//        /////////////////////////////////////////////////////////////////////////////////////////
//        /////////////////////////////////////////////////////////////////////////////////////////
//        //ads
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
//        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("BFE5A6AC72AC4A402AFDA3209FDB660A").build());
//        // TODO: Add adView to your view hierarchy.
//        /////////////////////////////////////////////////////////////////////////////////////////
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("product").child("أدوية");
//        reference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                nameProductL.add(dataSnapshot.getKey());
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//        //////////////////////////////////////////////////////////////////////////////////////////
//        DatabaseReference referenceImported = FirebaseDatabase.getInstance().getReference().child("product").child("أدوية مستوردة");
//        referenceImported.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                nameProductImported.add(dataSnapshot.getKey());
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        ///////////////////////////////////////////////////////////////////////////////////////////
//        DatabaseReference referenceAccessories = FirebaseDatabase.getInstance().getReference().child("product").child("مستلزمات");
//        referenceAccessories.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                nameProductAccessories.add(dataSnapshot.getKey());
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        //////////////////////////////////////////////
//        //Search
//        search = findViewById(R.id.search);
//        search.setOnQueryTextListener(this);
//        ///////////////////////////////////////////////////////////////////////////////////////////
//        //https://www.youtube.com/watch?v=PmqYd-AdmC0
//    }
//    public void goSearchLocal_medicines(View view) {
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameProductL);
//        listProductInfo.setVisibility(View.VISIBLE);
//        ProductInfo.setVisibility(View.GONE);
//        listProductInfo.setAdapter(adapter);
//
//        ////////////////////////////////
//        if (mInterstitialAd.isLoaded()) {
//            mInterstitialAd.show();
//        } else {
//            Log.d("TAG", "The interstitial wasn't loaded yet.");
//        }
//        /////////////////////////////////
//        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        //Todo بس لسا في لحطبة في الختة بتاعت المحافظات
//        listProductInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            //todo عيزين ناخد position قبل ما يتغير في السيرش
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                listZeft.clear();
//                nameInList = parent.getItemAtPosition(position).toString();
//                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("product").child("أدوية").child(nameInList);
//                if (reference2 != null) {
//                    reference2.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.exists()) {
//                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                    ownerProduct.add(ds.getKey());
//                                    ///////////////////////////////////////////////////////////////
//                                    //check country , get name company, get phone number of company
//                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("Offices").child(ds.getKey());
//                                    databaseReference.addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            nameCompany.add(dataSnapshot.child("nameU").getValue().toString());
//                                            sleep(300);
//                                            country_workU = (ArrayList<String>) dataSnapshot.child("country_work").getValue();
//                                            PhoneNumber = dataSnapshot.child("phoneNumber").getValue().toString();
//                                            if (PhoneNumber == null && PhoneNumber.isEmpty()) {
//                                                PhoneNumber = ds.getKey();
//                                            }
//
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                                        }
//                                    });
//                                    DatabaseReference databaseReferencePh = FirebaseDatabase.getInstance().getReference().child("users").child("pharmacies").child(user.getPhoneNumber());
//                                    databaseReferencePh.addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                            DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference().child("product").child("أدوية").child(nameInList).child(ds.getKey());
//                                            name_company = dataSnapshot.child("country_chooser").getValue().toString();
//                                           if(country_workU!=null)
//                                            if (country_workU.contains(name_company)) {
//                                                if (reference3 != null) {
//                                                    reference3.addValueEventListener(new ValueEventListener() {
//                                                        String name3 = nameInList;
//                                                        @Override
//                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                            if (dataSnapshot.exists()) {
////                                                    for (int i =0 ;i<ownerProduct.size();i++) {
//                                                                MyItemList myItemList = dataSnapshot.getValue(MyItemList.class);
//                                                                listZeft.add(myItemList);
//                                                                listProductInfo.setVisibility(View.GONE);
//                                                                ProductInfo.setVisibility(View.VISIBLE);
//
//                                                                MyAdapterList myAdapterList = new MyAdapterList(listZeft, nameCompany, name3,mydb);
//                                                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//                                                                ProductInfo.setLayoutManager(linearLayoutManager);
//                                                                ProductInfo.setAdapter(myAdapterList);
//                                                                ProductInfo.setItemAnimator(new DefaultItemAnimator());
//                                                            }
//                                                        }
//
//                                                        @Override
//                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                        }
//                                                    });
//                                                }
//                                            } else {
//                                                Toast.makeText(SearchProduct.this, "لا يوجد مخزن يخدم محافظتك ", Toast.LENGTH_LONG).show();
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                                        }
//                                    });
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                        }
//                    });
//                }
//            }
//        });
//
//    }
//
//    public void goSearchImported_medicines(View view) {
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameProductImported);
//        listProductInfo.setVisibility(View.VISIBLE);
//        ProductInfo.setVisibility(View.GONE);
//        listProductInfo.setAdapter(adapter);
//        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        listProductInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                listZeft.clear();
//                nameInList = parent.getItemAtPosition(position).toString();
//                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("product").child("أدوية مستوردة").child(nameInList);
//                if (reference2 != null) {
//                    reference2.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.exists()) {
//                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                    ownerProduct.add(ds.getKey());
//                                    ///////////////////////////////////////////////////////////////
//                                    //check country , get name company, get phone number of company
//                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("Offices").child(ds.getKey());
//                                    databaseReference.addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            nameCompany.add(dataSnapshot.child("nameU").getValue().toString());
//                                            sleep(200);
//                                            country_workU = (ArrayList<String>) dataSnapshot.child("country_work").getValue();
//                                            PhoneNumber = dataSnapshot.child("phoneNumber").getValue().toString();
//                                            if (PhoneNumber == null && PhoneNumber.isEmpty()) {
//                                                PhoneNumber = ds.getKey();
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                                        }
//                                    });
//                                    DatabaseReference databaseReferencePh = FirebaseDatabase.getInstance().getReference().child("users").child("pharmacies").child(user.getPhoneNumber());//todo a7a
//                                    databaseReferencePh.addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            name_company = dataSnapshot.child("country_chooser").getValue().toString();
//                                            DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference().child("product").child("أدوية مستوردة").child(nameInList).child(ds.getKey());
//                                            if (country_workU.contains(name_company)) {
//                                                if (reference3 != null) {
//                                                    reference3.addValueEventListener(new ValueEventListener() {
//                                                        String name =nameInList;
//                                                        @Override
//                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                            if (dataSnapshot.exists()) {
////                                                    for (int i =0 ;i<ownerProduct.size();i++) {
//                                                                MyItemList myItemList = dataSnapshot.getValue(MyItemList.class);
//                                                                listZeft.add(myItemList);
//                                                                listProductInfo.setVisibility(View.GONE);
//                                                                ProductInfo.setVisibility(View.VISIBLE);
//
//                                                                MyAdapterList myAdapterList = new MyAdapterList(listZeft, nameCompany, name,mydb);
//                                                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//                                                                ProductInfo.setLayoutManager(linearLayoutManager);
//                                                                ProductInfo.setAdapter(myAdapterList);
//                                                                ProductInfo.setItemAnimator(new DefaultItemAnimator());
//                                                            }
//                                                        }
//
//                                                        @Override
//                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                        }
//                                                    });
//                                                }
//                                            } else {
//                                                Toast.makeText(SearchProduct.this, "لا يوجد مخزن يخدم محافظتك ", Toast.LENGTH_LONG).show();
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                                        }
//                                    });
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                        }
//                    });
//                }
//            }
//        });
//
//    }
//
//    public void goSearchAccessories(View view) {
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameProductAccessories);
//        listProductInfo.setVisibility(View.VISIBLE);
//        ProductInfo.setVisibility(View.GONE);
//        listProductInfo.setAdapter(adapter);
//        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        listProductInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                listZeft.clear();
//                nameInList = parent.getItemAtPosition(position).toString();
//                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("product").child("مستلزمات").child(nameInList);
//                if (reference2 != null) {
//                    reference2.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.exists()) {
//                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                    ownerProduct.add(ds.getKey());
//                                    ///////////////////////////////////////////////////////////////
//                                    //check country , get name company, get phone number of company
//                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("Offices").child(ds.getKey());
//                                    databaseReference.addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            nameCompany.add(dataSnapshot.child("nameU").getValue().toString());
//                                            sleep(200);
//                                            country_workU = (ArrayList<String>) dataSnapshot.child("country_work").getValue();
//                                            PhoneNumber = dataSnapshot.child("phoneNumber").getValue().toString();
//                                            if (PhoneNumber == null && PhoneNumber.isEmpty()) {
//                                                PhoneNumber = ds.getKey();
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                                        }
//                                    });
//                                    DatabaseReference databaseReferencePh = FirebaseDatabase.getInstance().getReference().child("users").child("pharmacies").child(user.getPhoneNumber());
//                                    databaseReferencePh.addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            name_company = dataSnapshot.child("country_chooser").getValue().toString();
//                                            DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference().child("product").child("مستلزمات").child(nameInList).child(ds.getKey());
//                                            if (country_workU.contains(name_company)) {
//                                                if (reference3 != null) {
//                                                    reference3.addValueEventListener(new ValueEventListener() {
//                                                        @Override
//                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                            if (dataSnapshot.exists()) {
//                                                                MyItemList myItemList = dataSnapshot.getValue(MyItemList.class);
//                                                                listZeft.add(myItemList);
//                                                                listProductInfo.setVisibility(View.GONE);
//                                                                ProductInfo.setVisibility(View.VISIBLE);
//                                                                MyAdapterList myAdapterList = new MyAdapterList(listZeft, nameCompany, nameInList,mydb);
//                                                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//                                                                ProductInfo.setLayoutManager(linearLayoutManager);
//                                                                ProductInfo.setAdapter(myAdapterList);
//                                                                ProductInfo.setItemAnimator(new DefaultItemAnimator());
//                                                            }
//                                                        }
//
//                                                        @Override
//                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                        }
//                                                    });
//                                                }
//                                            } else {
//                                                Toast.makeText(SearchProduct.this, "لا يوجد مخزن يخدم محافظتك ", Toast.LENGTH_LONG).show();
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                                        }
//                                    });
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//            }
//        });
//    }
//
//    public void callCompany(View view) {
//        String phone;
//        Intent intent = new Intent(Intent.ACTION_DIAL);
//        phone = PhoneNumber;
//        if (phone == null && phone.isEmpty()) {
//            Toast.makeText(SearchProduct.this, "لايوجد ارقام للاتصال ", Toast.LENGTH_LONG).show();
//        } else {
//            intent.setData(Uri.parse("tel:" + phone));
//            Toast.makeText(this, "وبرده كس ام البرمجة", Toast.LENGTH_LONG).show();
//        }
//        startActivity(intent);
//    }
//
//    public void openSheet(View view) {
//        behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
//    }
//
//    public void openDrawer(View view) {
//        if (navigationView.getVisibility() == View.GONE) {
//            navigationView.setVisibility(View.VISIBLE);
//        } else {
//            navigationView.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//
//        return false;
//
//    }