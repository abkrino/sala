package pharamacy.eg.sala.offices.send;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pharamacy.eg.sala.Product_order;
import pharamacy.eg.sala.R;
import pharamacy.eg.sala.ReceyPro;
import pharamacy.eg.sala.adpter.MyAdpterOrder;
import pharamacy.eg.sala.sql.DataBaseHelper;

public class SendFragment extends Fragment {
    private ExpandableListView myorderDate;
    AdView adView;
    MyAdpterOrder myAdpterOrder;
    List<String> numberpharmS;
    HashMap<String, List<String>> myOrderOwner;
    List<String> numberPharmacies;
    List<String> namePharmacies;
    private FirebaseUser user;
    String date, numberOffices, namePharmacy;
    String getNamePharmacy, numberPharmacy;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_send, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calling(view);
        getOrderOffices();
        showAd();




    }

    private void calling(View view) {
        adView = new AdView(getActivity());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-1743625796086476/3426647804");
        adView = view.findViewById(R.id.adView);
        myOrderOwner = new HashMap<>();
        numberPharmacies = new ArrayList<>();
        namePharmacies = new ArrayList<>();
        numberpharmS = new ArrayList<>(myOrderOwner.keySet());
        user = FirebaseAuth.getInstance().getCurrentUser();
        numberOffices = user.getPhoneNumber();
        myorderDate = view.findViewById(R.id.myorderDateOff);

    }

    public void showAd() {

        MobileAds.initialize(getActivity(), "ca-app-pub-1743625796086476~4917839514");
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public void getOrderOffices() {

        //Start//
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child("Offices").child(numberOffices).child("order");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                date = dataSnapshot.getKey();
                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("users").child("Offices").child(numberOffices).child("order").child(date);
                reference2.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        numberPharmacy = dataSnapshot.getKey();
                        getNamePharmacy(numberPharmacy);
                        numberPharmacies.add(numberPharmacy);
                        myOrderOwner.put(date, numberPharmacies);
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

    public void getNamePharmacy(String numberPharmacy) {

        //start//
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child("pharmacies").child(numberPharmacy).child("nameU");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                namePharmacy = dataSnapshot.getValue().toString();
                namePharmacies.add(namePharmacy);

                if (myOrderOwner.size() >= 1 && namePharmacies.size() >= 1) {
                    numberPharmacies = new ArrayList<>(myOrderOwner.keySet());

                    setReceyOrder(getActivity(), myOrderOwner, numberPharmacies, namePharmacies);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void setReceyOrder(Context context, HashMap<String, List<String>> header, List<String> child, List<String> nameCompany) {

        myAdpterOrder = new MyAdpterOrder(context, header, child, nameCompany);
        myorderDate.setAdapter(myAdpterOrder);
        myorderDate.setVisibility(View.VISIBLE);
        myorderDate.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String t = (String) myorderDate.getExpandableListAdapter().getGroup(groupPosition);
                String t1 = (String) myorderDate.getExpandableListAdapter().getChild(groupPosition, childPosition);
                startActivity(new Intent(getContext(), Product_order.class).putExtra("dateOrder", t).putExtra("numberOwner", t1).putExtra("numberOffices", numberOffices).putExtra("type","Offices"));


//                showdialog(getProductCompany(t, t1));

                return false;
            }

        });

    }


}