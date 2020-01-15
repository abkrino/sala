package pharamacy.eg.sala.ui2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pharamacy.eg.sala.MyItemList;
import pharamacy.eg.sala.R;
import pharamacy.eg.sala.ReceyPro;

public class Home_pharm extends Fragment {
    public FirebaseUser user;
    private Button local_medicines, imported_medicines, accessories;
    private ArrayList<String> nameProductL, nameProductImported, nameProductAccessories;
    private InterstitialAd mInterstitialAd;
    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private ListView listProductInfo;
    private RecyclerView ProductInfo;
    public String name_company;
    private String nameInList , phoneNumberPharmacy;
    private ArrayList<MyItemList> listZeft;
    private BottomSheetBehavior behavior;
    private SearchView search;
    private String text;
    private ArrayAdapter<String> adapter;
    private TextView typeSearch;
    private ProgressDialog progressDialog;
    private Activity myActivityPh;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home_pharmcy, container, false);
        myActivityPh = getActivity();
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
        local_medicines = view.findViewById(R.id.Local_medicines);
        imported_medicines = view.findViewById(R.id.Imported_medicines);
        accessories = view.findViewById(R.id.Accessories);
        View BottomSheet = view.findViewById(R.id.design_bottom_sheet);
        listProductInfo = view.findViewById(R.id.list_main);
        ProductInfo = view.findViewById(R.id.list_main2);
        typeSearch = view.findViewById(R.id.typeSearch);
        user = FirebaseAuth.getInstance().getCurrentUser();
        phoneNumberPharmacy = user.getPhoneNumber();
        getCounteryOfpharmacies(phoneNumberPharmacy);
        //ـــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        behavior = BottomSheetBehavior.from(BottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        //ـــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        //ads
        showAd();
        // TODO: Add adView to your view hierarchy.
        //ـــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        showProg("loading", " لحظات لتحميل الاصناف ....");
        new Thread(new Runnable() {
            @Override
            public void run() {

                myActivityPh.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getLocal_medicines();
                    }
                });
            }
        }).start();
        //ـــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        getAccessories();
        getImported_medicines();
        //ـــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
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
                switch (behavior.getState()) {
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                        break;
                }
            }
        });
        //ـــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        //todo make thread to loading prices
        local_medicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_of_items,R.id.textViewList, nameProductL);
                    listProductInfo.setVisibility(View.VISIBLE);
                    ProductInfo.setVisibility(View.GONE);
                    listProductInfo.setAdapter(adapter);
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


                    listProductInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            listZeft.clear();
                            nameInList = parent.getItemAtPosition(position).toString();
                            Intent intent = new Intent(getActivity(), ReceyPro.class);
                            intent.putExtra("nameInlist", nameInList);
                            intent.putExtra("local_medicines", "أدويةمحلية");
                            intent.putExtra("accessories", "");
                            intent.putExtra("imported_medicines", "");
                            intent.putExtra("name_company", name_company);

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

        });
        accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                adapter = new ArrayAdapter<String>(getActivity(),R.layout.list_of_items, R.id.textViewList,nameProductAccessories);
                listProductInfo.setVisibility(View.VISIBLE);
                ProductInfo.setVisibility(View.GONE);
                listProductInfo.setAdapter(adapter);
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_of_items,R.id.textViewList, nameProductImported);
                listProductInfo.setVisibility(View.VISIBLE);
                ProductInfo.setVisibility(View.GONE);
                listProductInfo.setAdapter(adapter);

                ////////////////////////////////
                /////////////////////////////////
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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

    public void getLocal_medicines() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("product").child("أدويةمحلية");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                nameProductL.add(dataSnapshot.getKey());
                if (nameProductL.size()==1500) {
                    adapter = new ArrayAdapter<String>(getActivity(),R.layout.list_of_items,R.id.textViewList, nameProductL);
                    listProductInfo.setVisibility(View.VISIBLE);
                    ProductInfo.setVisibility(View.GONE);
                    listProductInfo.setAdapter(adapter);
                    progressDialog.dismiss();
                    showAd();
                }

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



        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        listProductInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                nameInList = parent.getItemAtPosition(position).toString();
                showAd();
                Intent intent = new Intent(getActivity(), ReceyPro.class);
                intent.putExtra("nameInlist", nameInList);
                intent.putExtra("local_medicines", "أدويةمحلية");
                intent.putExtra("accessories", "");
                intent.putExtra("imported_medicines", "");
                startActivity(intent);
            }

        });
    }

    public void getAccessories() {
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

    }

    public void getImported_medicines() {
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
    }

    public void showAd() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                myActivityPh.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mInterstitialAd = new InterstitialAd(getActivity());
                        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
                        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("BFE5A6AC72AC4A402AFDA3209FDB660A").build());
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            Log.d("TAG", "The interstitial wasn't loaded yet.");
                        }
                    }
                });
            }
        }).start();
    }

    public void showProg(String title, String message) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false); //To prevent the user dismiss progressDialog
        progressDialog.show();

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
}

