package pharamacy.eg.sala.ui2;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pharamacy.eg.sala.MyItemList;
import pharamacy.eg.sala.R;
import pharamacy.eg.sala.ReceyPro;

public class Home_pharm extends Fragment {
    public FirebaseUser user;
    private Button local_medicines, imported_medicines, accessories;
    private ArrayList<String> nameProductL, nameProductImported, nameProductAccessories;
    private InterstitialAd mInterstitialAd;
    ImageView akrino2;
    ConstraintLayout mylayout2;
    //ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    private ListView listProductInfo;
    private RecyclerView ProductInfo;
    private String name_company;
    private String nameInList, phoneNumberPharmacy;
    private ArrayList<MyItemList> listZeft;
    private BottomSheetBehavior behavior;
    private SearchView search;
    private String text;
    private ArrayAdapter<String> adapter;
    private TextView typeSearch;
    private ProgressDialog progressDialog;
    private Activity myActivityPh;
    AdView adView;
    private LayoutInflater factory;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    @Override
    public void onStart() {
        super.onStart();
        checkPer();
        SharedPreferences sharedPref = getActivity().getSharedPreferences("edue", Context.MODE_PRIVATE);
        if (sharedPref.getString("statu_edue", "yes").equals("yes") || sharedPref.getString("statu_edue", "yes") == null) {
            target();
        } else {
            akrino2.setVisibility(View.GONE);
            mylayout2.setVisibility(View.VISIBLE);
        }    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home_pharmcy, container, false);
        myActivityPh = getActivity();

        return root;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        akrino2 = view.findViewById(R.id.abkrino2);
        mylayout2 = view.findViewById(R.id.mylayout2);
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
        adView =view.findViewById(R.id.adView);
        listProductInfo = view.findViewById(R.id.list_main);
        ProductInfo = view.findViewById(R.id.list_main2);
        typeSearch = view.findViewById(R.id.typeSearch);
        user = FirebaseAuth.getInstance().getCurrentUser();
        factory = LayoutInflater.from(getActivity());
        adView = view.findViewById(R.id.adView);
        adView = new AdView(getActivity());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-1743625796086476/3426647804");
        startAd();
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
        search = view.findViewById(R.id.searchP);
        search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                search.setMaxWidth(4000);
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                text = newText;
                if (text != null) adapter.getFilter().filter(text);
                else {
                    return false;
                }
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
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_of_items, R.id.textViewList, nameProductL);

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

                    }
                });

            }

        });
        accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_of_items, R.id.textViewList, nameProductAccessories);
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
                        intent.putExtra("name_company", name_company);
                        startActivity(intent);
                    }
                });
            }
        });
        imported_medicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_of_items, R.id.textViewList, nameProductImported);
                listProductInfo.setVisibility(View.VISIBLE);
                ProductInfo.setVisibility(View.GONE);
                listProductInfo.setAdapter(adapter);

                ////////////////////////////////
                /////////////////////////////////
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                listProductInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        listZeft.clear();
                        nameInList = parent.getItemAtPosition(position).toString();
                        Intent intent = new Intent(getActivity(), ReceyPro.class);
                        intent.putExtra("nameInlist", nameInList);
                        intent.putExtra("imported_medicines", "أدوية مستوردة");
                        intent.putExtra("accessories", "");
                        intent.putExtra("local_medicines", "");
                        intent.putExtra("name_company", name_company);
                        startActivity(intent);
                    }
                });

            }

        });

    }

    //end on ctreated view
    //ـــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
    public void getLocal_medicines() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("product").child("أدويةمحلية");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                nameProductL.add(dataSnapshot.getKey());
                if (nameProductL.size() > 50) {
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_of_items, R.id.textViewList, nameProductL);
                    listProductInfo.setVisibility(View.VISIBLE);
                    ProductInfo.setVisibility(View.GONE);
                    listProductInfo.setAdapter(adapter);
                    progressDialog.dismiss();

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

                Intent intent = new Intent(getActivity(), ReceyPro.class);
                intent.putExtra("nameInlist", nameInList);
                intent.putExtra("local_medicines", "أدويةمحلية");
                intent.putExtra("accessories", "");
                intent.putExtra("imported_medicines", "");
                intent.putExtra("name_company", name_company);
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

    public void checkPer() {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.ACCESS_NETWORK_STATE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            checkPer();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    private void startAd() {
        MobileAds.initialize(getActivity(), "ca-app-pub-1743625796086476~4917839514");
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }
    public void target() {

        final SpannableString spannedDesc = new SpannableString("ابحث عن الصنف الذي تحتاجه من هذه القائمة ");
        final SpannableString spannedDesc2 = new SpannableString(" يمكنك ايضا البحث باسم الصنف علي الصنف الذي تريده");
        final SpannableString spannedDesc3 = new SpannableString("يمكنك ان تختار من ثلاث فئات (ادوية محلية - ادوية مستوردة - مستلزمات ) ");

        final TapTargetSequence sequence = new TapTargetSequence(getActivity())
                .targets(
                        TapTarget.forView(getActivity().findViewById(R.id.searchP), " زر البحث ", spannedDesc2)
                                // All options below are optional
                                .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.70f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.White_transparent_white_hex_5)   // Specify a color for the target circle
                                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.White_White)      // Specify the color of the title text
                                .descriptionTextSize(15)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.White_transparent_white_hex_13)  // Specify the color of the description text
                                .textColor(R.color.Blue_Lavender)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .dimColor(R.color.Black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(false)                   // Whether to tint the target view's color
                                .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(50)
                                .id(1),          // Specify the target radius (in dp)

                        TapTarget.forView(getActivity().findViewById(R.id.list_main), " قائمة الاصناف المتاحة  ", spannedDesc)
                                // All options below are optional
                                .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.70f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.White_transparent_white_hex_5)   // Specify a color for the target circle
                                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.White_White)      // Specify the color of the title text
                                .descriptionTextSize(15)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.White_transparent_white_hex_13)  // Specify the color of the description text
                                .textColor(R.color.Blue_Lavender)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .dimColor(R.color.Black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(false)                   // Whether to tint the target view's color
                                .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(170)
                                .id(2),            // Specify the target radius (in dp)
                        TapTarget.forView(getActivity().findViewById(R.id.typeSearch), " زر البحث عن فئات المنتجات ", spannedDesc3)
                                // All options below are optional
                                .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.70f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.White_transparent_white_hex_5)   // Specify a color for the target circle
                                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.White_White)      // Specify the color of the title text
                                .descriptionTextSize(15)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.White_transparent_white_hex_13)  // Specify the color of the description text
                                .textColor(R.color.Blue_Lavender)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .dimColor(R.color.Black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(false)                   // Whether to tint the target view's color
                                .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                                .targetRadius(80)
                                .id(3)

                )
                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
                        final View deleteDialogView = factory.inflate(R.layout.dialog_edu, null);
                        final AlertDialog deleteDialog = new AlertDialog.Builder(getActivity()).create();
                        deleteDialog.setView(deleteDialogView);
                        deleteDialog.setCancelable(false);
                        deleteDialog.show();
                        deleteDialogView.findViewById(R.id.alert_btn_no).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                sharedPref = getActivity().getSharedPreferences("edue", Context.MODE_PRIVATE);
                                editor = sharedPref.edit();
                                editor.putString("statu_edue", "no");
                                editor.apply();
                                deleteDialog.dismiss();
                                Toast.makeText(getActivity(), "لن يتم عرض الشرح في كل مرة ", Toast.LENGTH_SHORT).show();

                            }
                        });
                        deleteDialogView.findViewById(R.id.alert_yes).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                sharedPref = getActivity().getSharedPreferences("edue", Context.MODE_PRIVATE);
                                editor = sharedPref.edit();
                                editor.putString("statu_edue", "yes");
                                editor.apply();
                                deleteDialog.dismiss();
                                Toast.makeText(getActivity(), "سيتم عرض الشرح في كل مرة ", Toast.LENGTH_SHORT).show();

                            }
                        });


                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {

                    }
                });


        final SpannableString spannedDesc1 = new SpannableString("تم تطوير التطبيق من شركة عبقرينو لنظم المعلومات و تطوير وادارة المشروعات " +
                "تطبيق sala يتيح لك مقارنة اسعار جميع المنتجاتك التي تحتاجها " +
                "ويظهر لك افضل خصم وافضل سعر  مقدم من الشركات العارضة للمنتجات " +
                " هيا بينا ");
//        spannedDesc.setSpan(new UnderlineSpan(), spannedDesc1.length() - "TapTargetView".length(), spannedDesc.length(), 0);
        TapTargetView.showFor(getActivity(), TapTarget.forView(getActivity().findViewById(R.id.abkrino2), " فكرة التطبيق", spannedDesc1)
                // All options below are optional
                .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                .outerCircleAlpha(0.70f)            // Specify the alpha amount for the outer circle
                .targetCircleColor(R.color.White_transparent_white_hex_5)   // Specify a color for the target circle
                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                .titleTextColor(R.color.White_White)      // Specify the color of the title text
                .descriptionTextSize(15)            // Specify the size (in sp) of the description text
                .descriptionTextColor(R.color.White_transparent_white_hex_13)  // Specify the color of the description text
                .textColor(R.color.Blue_Lavender)            // Specify a color for both the title and description text
                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                .dimColor(R.color.Black)            // If set, will dim behind the view with 30% opacity of the given color
                .drawShadow(true)                   // Whether to draw a drop shadow or not
                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                .tintTarget(false)                   // Whether to tint the target view's color
                .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                .targetRadius(120), new TapTargetView.Listener() {
            @Override
            public void onTargetClick(TapTargetView view) {
                super.onTargetClick(view);
                // .. which evidently starts the sequence we defined earlier
                akrino2.setVisibility(View.GONE);
                mylayout2.setVisibility(View.VISIBLE);
                sequence.start();
            }

            @Override
            public void onOuterCircleClick(TapTargetView view) {
                super.onOuterCircleClick(view);
                Toast.makeText(view.getContext(), "اضغط علي العنصر ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                Log.d("TapTargetViewSample", "You dismissed me :(");
            }
        });

    }

}

