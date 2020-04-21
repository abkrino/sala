package pharamacy.eg.sala;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class SignIN extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        super.onStart();
        checkPer();

    }

    private EditText meditText; // Take the number from the user
    //////////////////////////////////
    public String no;
    AdView adView;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        adView = findViewById(R.id.adView);
        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-1743625796086476/3426647804");
        showAd();

        // Initialize Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        /*save the number in the  Firebase and move to the data registration page*/
        Button signIn = findViewById(R.id.button);
        /*Back to previous activity*/
//        ImageView back = findViewById(R.id.back_arrow);
//        back.setVisibility(View.GONE);
        meditText = findViewById(R.id.editText);

        meditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                no = meditText.getText().toString();
                validNo(no);
                return false;
            }
        });
        /*Move to data entry  activity*/
        TextView signUP = findViewById(R.id.signUP_btn);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ads

                // TODO: Add adView to your view hierarchy.
                /////////////////////////////////////////////////////////////////////////////////////////

                no = meditText.getText().toString();
                if (currentUser == null) {
                    if (no.isEmpty() || no.length() <= 10) {
                        meditText.setError("من فضلك ادخل رقم صحيح");
                        meditText.requestFocus();
                      return;
                    }else {
                        Intent intent = new Intent(SignIN.this, Confirm.class);
                            intent.putExtra("meditText", no);
                        Toast.makeText(SignIN.this, no + "مرحبا بك يا ", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                        finish();

                    }
                }else {
                    if (no.isEmpty() || no.length() <= 10) {
                        meditText.setError("من فضلك ادخل رقم صحيح");
                        meditText.requestFocus();
                        return;
                    }else {
                        Intent intent = new Intent(SignIN.this, Confirm.class);
                        intent.putExtra("meditText", no);
                        Toast.makeText(SignIN.this, no + "مرحبا بك يا ", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                        finish();

                    }

                }

            }
        });

        signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                no = meditText.getText().toString();
                if (no.isEmpty() || no.length() <= 10) {
                    meditText.setError("من فضلك ادخل رقم صحيح");
                    meditText.requestFocus();
                    return;
                }else {
                    Intent intent = new Intent(SignIN.this, Confirm.class);
                    intent.putExtra("meditText", no);
                    startActivity(intent);
                    Toast.makeText(SignIN.this, no+ "مرحبا بك يا ", Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        });

    }

    private void validNo(String no) {
        if (no.isEmpty()) {
            meditText.setError("من فضلك ادخل رقم هاتف");
            meditText.requestFocus();
            return;
        }
    }
    public void showAd(){
        MobileAds.initialize(this, "ca-app-pub-1743625796086476~4917839514");
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkPer() {

        if (checkSelfPermission( Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.ACCESS_NETWORK_STATE}, 1);

        }


    }
}