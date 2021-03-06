package pharamacy.eg.sala.offices.tools;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import pharamacy.eg.sala.R;

public class ToolsFragment extends Fragment {
    TextView textView1, textView2, textView3, textView4, textView5, textView6,text11;
    AdView adView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adView = view.findViewById(R.id.adView);
        textView1 = view.findViewById(R.id.text1);
        textView2 = view.findViewById(R.id.text2);
        textView3 = view.findViewById(R.id.text3);
        textView4 = view.findViewById(R.id.text4);
        textView5 = view.findViewById(R.id.text5);
        textView6 = view.findViewById(R.id.text6);
        text11 = view.findViewById(R.id.text11);
        adView = view.findViewById(R.id.adView);
        adView = new AdView(getActivity());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-1743625796086476/3426647804");
        showAd();
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("email"));
                intent.setType("massage/rfc822");
                String [] s ={"abkrino.suport@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL,s);
                intent.putExtra(Intent.EXTRA_SUBJECT,"Enter subject");
                intent.putExtra(Intent.EXTRA_TEXT,"Enter your email body");
                Intent choser = Intent.createChooser(intent,"launch Email");
                startActivity(choser);

            }
        });
        text11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);

                intent.setData(Uri.parse("tel:" + "01273686030"));

                startActivity(intent);
            }
        });
        textView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);

                intent.setData(Uri.parse("tel:" + "01554046584"));

                startActivity(intent);
            }
        });
    }
    public void showAd(){
        MobileAds.initialize(getActivity(), "ca-app-pub-1743625796086476~4917839514");
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);
    }
}