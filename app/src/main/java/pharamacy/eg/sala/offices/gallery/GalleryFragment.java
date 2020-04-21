package pharamacy.eg.sala.offices.gallery;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
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
import java.util.Objects;

//import pharamacy.eg.sala.Class.GlideApp;
import pharamacy.eg.sala.EditProfile;
import pharamacy.eg.sala.R;

public class GalleryFragment extends Fragment {
    private TextView name, city, neighborhood, country_chooser, Specia_work, phoneNumber, country_work;
    public String nameU, cityU, neighborhoodU, country_chooserU, Specia_workU, phoneNumberU;
    public ArrayList<String> country_workU;
    public Uri a;
    RelativeLayout background;
    boolean flag = false;
    private FirebaseUser user;
    public String userId;
    private com.mikhaellopez.circularimageview.CircularImageView userPic;
    private StorageReference mStorage;
    AdView adView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adView = view.findViewById(R.id.adView);
        adView = new AdView(getActivity());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-1743625796086476/3426647804");
        showAd();

        name = view.findViewById(R.id.nameC);
        city = view.findViewById(R.id.city);
        country_work = view.findViewById(R.id.multispinner);
        neighborhood = view.findViewById(R.id.neighborhood);
        country_chooser = view.findViewById(R.id.spinner1);
        Specia_work = view.findViewById(R.id.spinner2);
        phoneNumber = view.findViewById(R.id.number);
        background = view.findViewById(R.id.background);

        userPic = view.findViewById(R.id.profile_pic);
        Button edit = view.findViewById(R.id.edit_profile_btn);
        country_workU = new ArrayList<>();
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
                       if (dataSnapshot.getValue() != null) {
                           if (userId != null) {
                               nameU = dataSnapshot.child("nameU").getValue().toString();
                               cityU = dataSnapshot.child("cityU").getValue().toString();
                               neighborhoodU = dataSnapshot.child("neighborhoodU").getValue().toString();
                               country_chooserU = dataSnapshot.child("country_chooser").getValue().toString();
                               Specia_workU = dataSnapshot.child("Specia_work").getValue().toString();
                               phoneNumberU = dataSnapshot.child("phoneNumber").getValue().toString();
                               country_workU = (ArrayList<String>) dataSnapshot.child("country_work").getValue();
                               flag = true;
                           }

                           name.setText( nameU);
                           city.setText(  cityU);
                           neighborhood.setText( neighborhoodU);
                           country_chooser.setText(country_chooserU);
                           country_work.setText(showCountry());
                           Specia_work.setText(Specia_workU);
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
                                   Picasso.get()
                                           .load(R.mipmap.user_foreground)
                                           .into(userPic);
                               }
                           });
                       } else {

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
                                       flag = true;
                                   }

                                   name.setText("الاسم: " + nameU);
                                   city.setText("المدينة: " + cityU);
                                   neighborhood.setText("المنطقة: " + neighborhoodU);
                                   country_chooser.setText(country_chooserU);
                                   phoneNumber.setText(phoneNumberU);
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
                                           Picasso.get()
                                                   .load(R.mipmap.user_foreground)
                                                   .into(userPic);
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
                Intent intent = new Intent(getActivity(), EditProfile.class);
                intent.putExtra("name", nameU);
                intent.putExtra("city", cityU);
                intent.putExtra("neighborhood", neighborhoodU);
                intent.putExtra("country_chooser", country_chooserU);
                intent.putStringArrayListExtra("country_work", country_workU);
                intent.putExtra("Specia_workU", Specia_workU);
                intent.putExtra("phoneNumber", phoneNumberU);
                intent.putExtra("imageUrl", a);
                startActivity(intent);
            }
        });


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    public String showCountry() {
        String str;
        if (country_workU.get(0).equals("أختر محافظة")) {
            str = country_chooserU;
        } else {
            str = TextUtils.join("\n", country_workU);
        }
        return str;
    }
    public void showAd(){
        MobileAds.initialize(getActivity(), "ca-app-pub-1743625796086476~4917839514");
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);
    }
}