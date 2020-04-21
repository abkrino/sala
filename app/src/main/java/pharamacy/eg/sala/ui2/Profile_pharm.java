package pharamacy.eg.sala.ui2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//import pharamacy.eg.sala.Class.GlideApp;
import pharamacy.eg.sala.EditeProfilePh;
import pharamacy.eg.sala.R;

public class Profile_pharm extends Fragment {

    private TextView name, city, neighborhood, country_chooser,  phoneNumber,address ;
    public String nameU, cityU, neighborhoodU, country_chooserU,phoneNumberU,addressU;
    public ArrayList<String> country_workU;
    public Uri a;
    boolean flag = false;

    private FirebaseUser user;
    public String userId;
    private ImageView userPic;
    private StorageReference mStorage;
    AdView adView;
    public View onCreateView(@NonNull LayoutInflater inflater,
        ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile_fragment, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adView =view.findViewById(R.id.adView);
        name = view.findViewById(R.id.nameC);
        city = view.findViewById(R.id.city);
        neighborhood = view.findViewById(R.id.neighborhood);
        country_chooser = view.findViewById(R.id.spinner1);
        phoneNumber = view.findViewById(R.id.number);
        showAd();
        address = view.findViewById(R.id.addressP);
        userPic = view.findViewById(R.id.profile_pic);
        Button edit = view.findViewById(R.id.edit_profile_btn);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getPhoneNumber();
        }
        mStorage = FirebaseStorage.getInstance().getReference().child("images/").child(userId);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child("Offices").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userId = user.getPhoneNumber();
                if(dataSnapshot.getValue()!=null){
                    if (userId != null) {
                        nameU = dataSnapshot.child("nameU").getValue().toString();
                        cityU = dataSnapshot.child("cityU").getValue().toString();
                        neighborhoodU = dataSnapshot.child("neighborhoodU").getValue().toString();
                        country_chooserU = dataSnapshot.child("country_chooser").getValue().toString();
                        phoneNumberU = dataSnapshot.child("phoneNumber").getValue().toString();
                        flag = true;
                    }

                    name.setText( nameU);
                    city.setText( cityU);
                    neighborhood.setText( neighborhoodU);
                    country_chooser.setText(country_chooserU);
                    phoneNumber.setText(phoneNumberU);

                    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    mStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'
                            a = uri;
                            Picasso.get()
                                    .load(uri)
                                    .into(userPic);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                }else{

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child("pharmacies").child(userId);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            userId = user.getPhoneNumber();
                            if (userId != null) {
                                nameU = dataSnapshot.child("nameU").getValue().toString();
                                cityU = dataSnapshot.child("cityU").getValue().toString();
                                neighborhoodU = dataSnapshot.child("neighborhoodU").getValue().toString();
                                country_chooserU = dataSnapshot.child("country_chooser").getValue().toString();
                                phoneNumberU = dataSnapshot.child("phoneNumber").getValue().toString();
                                addressU = dataSnapshot.child("address").getValue().toString();

                                flag = true;
                            }

                            name.setText( nameU);
                            city.setText(cityU);
                            neighborhood.setText(neighborhoodU);
                            country_chooser.setText(country_chooserU);
                            phoneNumber.setText(phoneNumberU);
                            address.setText(addressU);

                            mStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Got the download URL for 'users/me/profile.png'
                                    a = uri;
                                    Picasso.get()
                                            .load(uri)
                                            .into(userPic);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , EditeProfilePh.class);
                intent.putExtra("name",nameU);
                intent.putExtra("city",cityU);
                intent.putExtra("neighborhood",neighborhoodU);
                intent.putExtra("country_chooser",country_chooserU);
                intent.putExtra("phoneNumber",phoneNumberU);
                intent.putExtra("address",addressU);

                intent.putExtra("imageUrl",a);
                startActivity(intent);
            }
        });


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
    public String showCountry() {

        String str = TextUtils.join("\n", country_workU);
        return str;
    }
    public void showAd(){

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);
    }

}