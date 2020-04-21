package pharamacy.eg.sala;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;

//import pharamacy.eg.sala.Class.GlideApp;

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
    private String resultPay;
    private boolean result;
    private int datePayDay;
    private int datePayMonth;
    private String date;
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
                Picasso.get()
                        .load(uri)
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
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

