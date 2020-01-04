package pharamacy.eg.sala.ui2;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import pharamacy.eg.sala.MyItemList;
import pharamacy.eg.sala.R;
import pharamacy.eg.sala.ReceyPro;

public class Home_pharm extends Fragment {
    AsyncQueryHandler handler;
    private FirebaseUser user;
    Button local_medicines, imported_medicines, accessories, button2;
    ArrayList<String> nameProductL, nameProductImported, nameProductAccessories, ownerProduct, nameCompany, PhoneNumber;
    ArrayList<String> country_workU;
    private InterstitialAd mInterstitialAd;
    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    ListView listProductInfo;
    RecyclerView ProductInfo;
    public String name_company;
    public String nameInList;
    ArrayList<MyItemList> listZeft;
    BottomSheetBehavior behavior;
    private NavigationView navigationView;
    SearchView search;
    String text;
    ArrayAdapter<String> adapter;
    TextView typeSearch;
    ImageView call, checkProduct;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home_pharmcy, container, false);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //ـــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        nameProductL = new ArrayList<>();
        nameProductImported = new ArrayList<>();
        nameProductAccessories = new ArrayList<>();
        listZeft = new ArrayList<>();
        //ـــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        nameCompany = new ArrayList<>();
        PhoneNumber = new ArrayList<>();
        ownerProduct = new ArrayList<>();
        local_medicines = view.findViewById(R.id.Local_medicines);
        imported_medicines = view.findViewById(R.id.Imported_medicines);
        accessories = view.findViewById(R.id.Accessories);
        View BottomSheet = view.findViewById(R.id.design_bottom_sheet);
        listProductInfo = view.findViewById(R.id.list_main);
        ProductInfo = view.findViewById(R.id.list_main2);
        navigationView = view.findViewById(R.id.nav_view);
        typeSearch = view.findViewById(R.id.typeSearch);
        call = view.findViewById(R.id.callIcon);
        checkProduct = view.findViewById(R.id.checkProduct);
        //ـــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        behavior = BottomSheetBehavior.from(BottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Toast.makeText(getActivity(), "STATE_DRAGGING", Toast.LENGTH_LONG).show();
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Toast.makeText(getActivity(), "STATE_SETTLING", Toast.LENGTH_LONG).show();
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                        Toast.makeText(getActivity(), "STATE_EXPANDED", Toast.LENGTH_LONG).show();
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Toast.makeText(getActivity(), "STATE_COLLAPSED", Toast.LENGTH_LONG).show();
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        Toast.makeText(getActivity(), "STATE_HIDDEN", Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        //ـــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        //ads
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("BFE5A6AC72AC4A402AFDA3209FDB660A").build());
        // TODO: Add adView to your view hierarchy.
        //ـــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("product").child("أدويةمحلية");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                nameProductL.add(dataSnapshot.getKey());
                mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("BFE5A6AC72AC4A402AFDA3209FDB660A").build());
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, nameProductL);
                listProductInfo.setVisibility(View.VISIBLE);
                ProductInfo.setVisibility(View.GONE);
                listProductInfo.setAdapter(adapter);
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                user = FirebaseAuth.getInstance().getCurrentUser();
                listProductInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), ReceyPro.class);
                        intent.putExtra("nameInlist", nameInList);
                        intent.putExtra("local_medicines", "أدويةمحلية");
                        intent.putExtra("accessories", "");
                        intent.putExtra("imported_medicines", "");
                        startActivity(intent);
                    }
                });
                //                listProductInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    //todo عيزين ناخد position قبل ما يتغير في السيرش
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        listZeft.clear();
//                        nameInList = parent.getItemAtPosition(position).toString();
//                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("product").child("أدويةمحلية").child(nameInList);
//                        if (reference2 != null) {
//                            reference2.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    if (dataSnapshot.exists()) {
//                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                            ownerProduct.add(ds.getKey());
//                                            ///////////////////////////////////////////////////////////////
//                                            //check country , get name company, get phone number of company
//                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("Offices").child(ds.getKey());
//                                            databaseReference.addValueEventListener(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                    PhoneNumber.add(dataSnapshot.child("phoneNumber").getValue().toString());
//                                                    nameCompany.add(dataSnapshot.child("nameU").getValue().toString());
//                                                    sleep(300);
//                                                    country_workU = (ArrayList<String>) dataSnapshot.child("country_work").getValue();
//
//
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                                                }
//                                            });
//                                            DatabaseReference databaseReferencePh = FirebaseDatabase.getInstance().getReference().child("users").child("pharmacies").child(user.getPhoneNumber());
//                                            databaseReferencePh.addValueEventListener(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                                    DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference().child("product").child("أدويةمحلية").child(nameInList).child(ds.getKey());
//                                                    name_company = dataSnapshot.child("country_chooser").getValue().toString();
//                                                    if (country_workU != null)
//                                                        if (country_workU.contains(name_company)) {
//                                                            if (reference3 != null) {
//                                                                reference3.addValueEventListener(new ValueEventListener() {
//                                                                    String name3 = nameInList;
//
//                                                                    @Override
//                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                                        if (dataSnapshot.exists()) {
////                                                    for (int i =0 ;i<ownerProduct.size();i++) {
//                                                                            MyItemList myItemList = dataSnapshot.getValue(MyItemList.class);
//                                                                            listZeft.add(myItemList);
//                                                                            listProductInfo.setVisibility(View.GONE);
//                                                                            ProductInfo.setVisibility(View.VISIBLE);
//                                                                            MyAdapterList myAdapterList = new MyAdapterList(listZeft, nameCompany, name3, PhoneNumber, getContext());
//                                                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//                                                                            ProductInfo.setLayoutManager(linearLayoutManager);
//                                                                            ProductInfo.setAdapter(myAdapterList);
//                                                                            ProductInfo.setItemAnimator(new DefaultItemAnimator());
//                                                                        }
//                                                                    }
//
//                                                                    @Override
//                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                                    }
//                                                                });
//                                                            }
//                                                        } else {
//                                                            Toast.makeText(getActivity(), "لا يوجد مخزن يخدم محافظتك ", Toast.LENGTH_LONG).show();
//                                                        }
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                                                }
//                                            });
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                                }
//                            });
//                        }
//                    }
//                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //ـــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        DatabaseReference referenceImported = FirebaseDatabase.getInstance().getReference().child("product").child("أدوية مستوردة");
        referenceImported.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                nameProductImported.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //ـــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        DatabaseReference referenceAccessories = FirebaseDatabase.getInstance().getReference().child("product").child("مستلزمات");
        referenceAccessories.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                nameProductAccessories.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //ـــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        //Search
        search = view.findViewById(R.id.search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                text = newText;
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        //ـــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        //https://www.youtube.com/watch?v=PmqYd-AdmC0
        typeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            }
        });
        //ـــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        local_medicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, nameProductL);
                    listProductInfo.setVisibility(View.VISIBLE);
                    ProductInfo.setVisibility(View.GONE);
                    listProductInfo.setAdapter(adapter);

                    ////////////////////////////////
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                    /////////////////////////////////
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    //Todo بس لسا في لحطبة في الختة بتاعت المحافظات
                    listProductInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        //todo عيزين ناخد position قبل ما يتغير في السيرش
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            listZeft.clear();
                            nameInList = parent.getItemAtPosition(position).toString();
                            Intent intent = new Intent(getActivity(), ReceyPro.class);
                            intent.putExtra("nameInlist", nameInList);
                            intent.putExtra("local_medicines", "أدويةمحلية");
                            intent.putExtra("accessories", "");
                            intent.putExtra("imported_medicines", "");

                            startActivity(intent);
//                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("product").child("أدويةمحلية").child(nameInList);
//                            if (reference2 != null) {
//                                reference2.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        if (dataSnapshot.exists()) {
//                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                                ownerProduct.add(ds.getKey());
//                                                ///////////////////////////////////////////////////////////////
//                                                //check country , get name company, get phone number of company
//                                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("Offices").child(ds.getKey());
//                                                databaseReference.addValueEventListener(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                        PhoneNumber.add(dataSnapshot.child("phoneNumber").getValue().toString());
//                                                        nameCompany.add(dataSnapshot.child("nameU").getValue().toString());
//                                                        sleep(300);
//                                                        country_workU = (ArrayList<String>) dataSnapshot.child("country_work").getValue();
//
//
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                                                    }
//                                                });
//                                                DatabaseReference databaseReferencePh = FirebaseDatabase.getInstance().getReference().child("users").child("pharmacies").child(user.getPhoneNumber());
//                                                databaseReferencePh.addValueEventListener(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                                        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference().child("product").child("أدويةمحلية").child(nameInList).child(ds.getKey());
//                                                        name_company = dataSnapshot.child("country_chooser").getValue().toString();
//                                                        if (country_workU != null)
//                                                            if (country_workU.contains(name_company)) {
//                                                                if (reference3 != null) {
//                                                                    reference3.addValueEventListener(new ValueEventListener() {
//                                                                        String name3 = nameInList;
//
//                                                                        @Override
//                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                                            if (dataSnapshot.exists()) {
////                                                    for (int i =0 ;i<ownerProduct.size();i++) {
//                                                                                MyItemList myItemList = dataSnapshot.getValue(MyItemList.class);
//                                                                                listZeft.add(myItemList);
//                                                                                listProductInfo.setVisibility(View.GONE);
//                                                                                ProductInfo.setVisibility(View.VISIBLE);
//                                                                                MyAdapterList myAdapterList = new MyAdapterList(listZeft, nameCompany, name3, PhoneNumber, getContext());
//                                                                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//                                                                                ProductInfo.setLayoutManager(linearLayoutManager);
//                                                                                ProductInfo.setAdapter(myAdapterList);
//                                                                                ProductInfo.setItemAnimator(new DefaultItemAnimator());
//                                                                            }
//                                                                        }
//
//                                                                        @Override
//                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                                        }
//                                                                    });
//                                                                }
//                                                            } else {
//                                                                Toast.makeText(getActivity(), "لا يوجد مخزن يخدم محافظتك ", Toast.LENGTH_LONG).show();
//                                                            }
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                                                    }
//                                                });
//                                            }
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                                    }
//                                });
//                            }
                        }
                    });

                }
            }

        });
        accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, nameProductAccessories);
                listProductInfo.setVisibility(View.VISIBLE);
                ProductInfo.setVisibility(View.GONE);
                listProductInfo.setAdapter(adapter);

                ////////////////////////////////
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                /////////////////////////////////
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                user = FirebaseAuth.getInstance().getCurrentUser();
                //Todo بس لسا في لحطبة في الختة بتاعت المحافظات
                listProductInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        listZeft.clear();
                        nameInList = parent.getItemAtPosition(position).toString();
                        Intent intent = new Intent(getActivity(), ReceyPro.class);
                        intent.putExtra("nameInlist", nameInList);
                        intent.putExtra("accessories", "مستلزمات");
                        intent.putExtra("local_medicines", "");
                        intent.putExtra("imported_medicines", "");

                        startActivity(intent);
//
//                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("product").child("مستلزمات").child(nameInList);
//                        if (reference2 != null) {
//                            reference2.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    if (dataSnapshot.exists()) {
//                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                            ownerProduct.add(ds.getKey());
//                                            ///////////////////////////////////////////////////////////////
//                                            //check country , get name company, get phone number of company
//                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("Offices").child(ds.getKey());
//                                            databaseReference.addValueEventListener(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                    PhoneNumber.add(dataSnapshot.child("phoneNumber").getValue().toString());
//                                                    nameCompany.add(dataSnapshot.child("nameU").getValue().toString());
//                                                    sleep(300);
//                                                    country_workU = (ArrayList<String>) dataSnapshot.child("country_work").getValue();
//
//
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                                                }
//                                            });
//                                            DatabaseReference databaseReferencePh = FirebaseDatabase.getInstance().getReference().child("users").child("pharmacies").child(user.getPhoneNumber());
//                                            databaseReferencePh.addValueEventListener(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                                    DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference().child("product").child("مستلزمات").child(nameInList).child(ds.getKey());
//                                                    name_company = dataSnapshot.child("country_chooser").getValue().toString();
//                                                    if (country_workU != null)
//                                                        if (country_workU.contains(name_company)) {
//                                                            if (reference3 != null) {
//                                                                reference3.addValueEventListener(new ValueEventListener() {
//                                                                    String name3 = nameInList;
//
//                                                                    @Override
//                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                                        if (dataSnapshot.exists()) {
////                                                    for (int i =0 ;i<ownerProduct.size();i++) {
//                                                                            MyItemList myItemList = dataSnapshot.getValue(MyItemList.class);
//                                                                            listZeft.add(myItemList);
//                                                                            listProductInfo.setVisibility(View.GONE);
//                                                                            ProductInfo.setVisibility(View.VISIBLE);
//                                                                            MyAdapterList2 myAdapterList2 = new MyAdapterList2(listZeft, nameCompany, name3, PhoneNumber, getContext());
//                                                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//                                                                            ProductInfo.setLayoutManager(linearLayoutManager);
//                                                                            ProductInfo.setAdapter(myAdapterList2);
//                                                                            ProductInfo.setItemAnimator(new DefaultItemAnimator());
//                                                                        }
//                                                                    }
//
//                                                                    @Override
//                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                                    }
//                                                                });
//                                                            }
//                                                        } else {
//                                                            Toast.makeText(getActivity(), "لا يوجد مخزن يخدم محافظتك ", Toast.LENGTH_LONG).show();
//                                                        }
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                                                }
//                                            });
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                                }
//                            });
//                        }
                    }
                });

            }
        });
        imported_medicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, nameProductImported);
                listProductInfo.setVisibility(View.VISIBLE);
                ProductInfo.setVisibility(View.GONE);
                listProductInfo.setAdapter(adapter);

                ////////////////////////////////
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                /////////////////////////////////
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                user = FirebaseAuth.getInstance().getCurrentUser();
                //Todo بس لسا في لحطبة في الختة بتاعت المحافظات
                listProductInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    //todo عيزين ناخد position قبل ما يتغير في السيرش
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        listZeft.clear();
                        nameInList = parent.getItemAtPosition(position).toString();
                        Intent intent = new Intent(getActivity(), ReceyPro.class);
                        intent.putExtra("nameInlist", nameInList);
                        intent.putExtra("imported_medicines", "أدوية مستوردة");
                        intent.putExtra("accessories", "");
                        intent.putExtra("local_medicines", "");
                        startActivity(intent);
                        //                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("product").child("أدوية مستوردة").child(nameInList);
//                        if (reference2 != null) {
//                            reference2.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    if (dataSnapshot.exists()) {
//                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                            ownerProduct.add(ds.getKey());
//                                            ///////////////////////////////////////////////////////////////
//                                            //check country , get name company, get phone number of company
//                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("Offices").child(ds.getKey());
//                                            databaseReference.addValueEventListener(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                    PhoneNumber.add(dataSnapshot.child("phoneNumber").getValue().toString());
//                                                    nameCompany.add(dataSnapshot.child("nameU").getValue().toString());
//                                                    sleep(300);
//                                                    country_workU = (ArrayList<String>) dataSnapshot.child("country_work").getValue();
//
//
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                                                }
//                                            });
//                                            DatabaseReference databaseReferencePh = FirebaseDatabase.getInstance().getReference().child("users").child("pharmacies").child(user.getPhoneNumber());
//                                            databaseReferencePh.addValueEventListener(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                                    DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference().child("product").child("أدوية مستوردة").child(nameInList).child(ds.getKey());
//                                                    name_company = dataSnapshot.child("country_chooser").getValue().toString();
//                                                    if (country_workU != null)
//                                                        if (country_workU.contains(name_company)) {
//                                                            if (reference3 != null) {
//                                                                reference3.addValueEventListener(new ValueEventListener() {
//                                                                    String name3 = nameInList;
//
//                                                                    @Override
//                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                                        if (dataSnapshot.exists()) {
////                                                    for (int i =0 ;i<ownerProduct.size();i++) {
//                                                                            MyItemList myItemList = dataSnapshot.getValue(MyItemList.class);
//                                                                            listZeft.add(myItemList);
//                                                                            listProductInfo.setVisibility(View.GONE);
//                                                                            ProductInfo.setVisibility(View.VISIBLE);
//                                                                            MyAdapterList2 myAdapterList2 = new MyAdapterList2(listZeft, nameCompany, name3, PhoneNumber, getContext());
//                                                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//                                                                            ProductInfo.setLayoutManager(linearLayoutManager);
//                                                                            ProductInfo.setAdapter(myAdapterList2);
//                                                                            ProductInfo.setItemAnimator(new DefaultItemAnimator());
//                                                                        }
//                                                                    }
//
//                                                                    @Override
//                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                                    }
//                                                                });
//                                                            }
//                                                        } else {
//                                                            Toast.makeText(getActivity(), "لا يوجد مخزن يخدم محافظتك ", Toast.LENGTH_LONG).show();
//                                                        }
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                                                }
//                                            });
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                                }
//                            });
//                        }
                    }
                });

            }

        });

    }
}


//    public void goSearchImported_medicines(View view) {
//        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, nameProductImported);
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
//                                                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
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
//                                                Toast.makeText(getActivity(), "لا يوجد مخزن يخدم محافظتك ", Toast.LENGTH_LONG).show();
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
//        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, nameProductAccessories);
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
//                                                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
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
//                                                Toast.makeText(getActivity(), "لا يوجد مخزن يخدم محافظتك ", Toast.LENGTH_LONG).show();
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