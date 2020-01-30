package pharamacy.eg.sala;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIN extends AppCompatActivity {


    private EditText meditText; // Take the number from the user
    //////////////////////////////////
    public String no;
    private InterstitialAd mInterstitialAd;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
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
                mInterstitialAd = new InterstitialAd(SignIN.this);
                mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
                mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("BFE5A6AC72AC4A402AFDA3209FDB660A").build());
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

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
}