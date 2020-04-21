package pharamacy.eg.sala;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavAction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
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
import com.squareup.picasso.Picasso;

import pharamacy.eg.sala.offices.home.HomeFragment;

//import pharamacy.eg.sala.Class.GlideApp;

public class MainOffices extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private AppBarConfiguration mAppBarConfiguration;
    private StorageReference mStorage;
    public String userId;
    public String nameU;
    private FirebaseUser user;
    View headerView;
    private String resultPay;
    private boolean result;
    private int datePayDay;
    private int datePayMonth;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_offices);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_send, R.id.nav_signOut)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        checkConnection();




        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getPhoneNumber();
        }
        mStorage = FirebaseStorage.getInstance().getReference().child("images/").child(userId);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child("Offices").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    nameU = dataSnapshot.child("nameU").getValue().toString();
                    TextView name = headerView.findViewById(R.id.nameHeadr);
                    name.setText("شركة : " + nameU);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ImageView imageView = headerView.findViewById(R.id.imageViewHeader);
        TextView number = headerView.findViewById(R.id.numberheadr);

        number.setText(userId);
        mStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Picasso.get()
                        .load(uri)
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Picasso.get()
                        .load(R.mipmap.user_foreground)
                        .into(imageView);
            }
        });

        LinearLayout layoutPaid = headerView.findViewById(R.id.layoutPaid);
        TextView datePaid = headerView.findViewById(R.id.DatePaid);
        if (checkPayment()) {
            layoutPaid.setVisibility(View.VISIBLE);
            date = "" + datePayMonth + "\\" + datePayDay + "";
            datePaid.setText(date);
        }

    }

    public boolean checkPayment() {
        String defult = "def";
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("my payment", Context.MODE_PRIVATE);
        resultPay = sharedPref.getString("resultPay", defult);
        if (resultPay != null) {
            switch (resultPay) {

                case "def":
                    result = false;

                    break;
                case "failed":
                    result = false;
                    break;
                case "accept":
                    result = true;
//                Toast.makeText(context, "انت تستمتع بالمزايا المدفوعة ", Toast.LENGTH_SHORT).show();
                    datePayDay = sharedPref.getInt("datePayDay", 0);
                    datePayMonth = sharedPref.getInt("datePayMonth", 0);
                    break;
            }
        }


        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_offices, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
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
            snackbar = Snackbar.make(findViewById(R.id.drawer_layout), Html.fromHtml("<font color=\"#FFFFFF\">جيد ! انت متصل بالانترنت</font>"), Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            snackbar = Snackbar.make(findViewById(R.id.drawer_layout), Html.fromHtml("<font color=\"#D81B60\">نأسف ! لا يوجد اتصال بالانترنت</font>"), Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    public void showVideoAd() {
        InterstitialAd mInterstitialAd2 = new InterstitialAd(this);
        mInterstitialAd2.setAdUnitId(getString(R.string.codeAdVideo));
        mInterstitialAd2.loadAd(new AdRequest.Builder().build());
        if (mInterstitialAd2.isLoaded()) {
            mInterstitialAd2.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");

        }
    }
}
