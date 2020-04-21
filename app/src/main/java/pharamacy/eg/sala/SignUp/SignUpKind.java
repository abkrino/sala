package pharamacy.eg.sala.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import pharamacy.eg.sala.R;

public class SignUpKind extends AppCompatActivity {
    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kind);

        adView =findViewById(R.id.adView);
        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-1743625796086476/3426647804");
        showAd();
        Button maktab = findViewById(R.id.maktab_btn);
        Button saydalya = findViewById(R.id.saydalya_btn);
        maktab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpKind.this, SignUP.class);
                startActivity(intent);
                finish();

            }
        });

        saydalya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpKind.this, SignUP_ph.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void showAd(){
        MobileAds.initialize(this, "ca-app-pub-1743625796086476~4917839514");
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}
