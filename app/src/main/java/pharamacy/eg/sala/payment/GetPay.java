package pharamacy.eg.sala.payment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActivityChooserView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import pharamacy.eg.sala.MainOffices;
import pharamacy.eg.sala.R;
import pharamacy.eg.sala.ReceyPro;
import pharamacy.eg.sala.SearchProduct;
import pharamacy.eg.sala.Start_Activity;
import pharamacy.eg.sala.offices.home.HomeFragment;

public class GetPay extends AppCompatActivity {
    WebView webView;
    ProgressBar progressBar;
    private WebView mWebviewPop;
    private AlertDialog builder;
    private Context mContext;
    private DatabaseReference mDatabase;
    private String phoneNumberPharmacy, phoneNumberOfficess, type;
    DatePicker picker;
    int month, endMonth;
    int day, endDay;
    VideoView videoView;
    Activity activity;
    LinearLayout linearWeb;
    AdView adView;
    private Bundle rx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_pay);
        calling();
        handlling();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void calling() {
        webView = findViewById(R.id.webView1);
        progressBar = findViewById(R.id.progressBar);
        videoView = findViewById(R.id.videoView);
        videoView = findViewById(R.id.videoView);
        linearWeb = findViewById(R.id.linearWeb);
        adView = findViewById(R.id.adView);
        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-1743625796086476/3426647804");
        activity = this;
        final MediaController mediacontroller = new MediaController(this);

        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("video/").child("InShot_20200412_204213021.mp4");
        mStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'

                mediacontroller.setAnchorView(videoView);


                videoView.setMediaController(mediacontroller);
                videoView.setVideoURI(uri);
//                videoView.requestFocus();
                videoView.start();
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Toast.makeText(getApplicationContext(), "Video over", Toast.LENGTH_SHORT).show();
                        videoView.setVisibility(View.GONE);
                        linearWeb.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


        rx = getIntent().getExtras();
        phoneNumberPharmacy = rx.getString("phoneNumberPharmacy");
        phoneNumberOfficess = rx.getString("phoneNumberOfficess");
        type = rx.getString("type");
        picker = findViewById(R.id.dataoo);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);

        webView.setWebViewClient(new UriWebViewClient());
        webView.setWebChromeClient(new UriChromeClient());

        mContext = this.getApplicationContext();


    }

    private void handlling() {
        openPay();
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})

    public void openPay() {
        month = (picker.getMonth() + 1);
        day = picker.getDayOfMonth();
        endDay = checkSubscribe(day, month);
        endMonth = month + 1;
        if (endMonth == 13) {
            endMonth = 1;
        }
        if (type.equals("pharmacies")) {
            //payment pharmacy
            webView.loadUrl("https://app.vapulus.com/website/?siteId=6e2add7e-3697-4d6c-b6b3-e65e2b803027&amount=35&link=http:%2F%2Fsalaa.epizy.com&pageTitle=matrix%20mobile%20app&onaccept=http:%2F%2Fwww.google.com%2Fncr&onfail=http:%2F%2Fwww.msn.com%2F");

        } else {
            //payment offices
            webView.loadUrl("https://app.vapulus.com/website/?siteId=6e2add7e-3697-4d6c-b6b3-e65e2b803027&amount=220&link=http:%2F%2Fsalaa.epizy.com&pageTitle=matrix%20mobile%20app&onaccept=http:%2F%2Fwww.google.com%2Fncr&onfail=http:%2F%2Fwww.msn.com%2F");

        }

    }

    private class UriWebViewClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {


            progressBar.setVisibility(View.VISIBLE);
            if (url.startsWith("http://www.google.com")) {
                Toast.makeText(GetPay.this, "عملية مقبولة ", Toast.LENGTH_LONG).show();
                webView.endViewTransition(webView);
                switch (type) {
                    case "pharmacies":
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("users").child("pharmacies").child(phoneNumberPharmacy).child("resultPay").setValue("accept");
                                mDatabase.child("users").child("pharmacies").child(phoneNumberPharmacy).child("datePayment").child("start day").setValue(day);
                                mDatabase.child("users").child("pharmacies").child(phoneNumberPharmacy).child("datePayment").child("end day").setValue(endDay);
                                mDatabase.child("users").child("pharmacies").child(phoneNumberPharmacy).child("datePayment").child("start month").setValue(month);
                                mDatabase.child("users").child("pharmacies").child(phoneNumberPharmacy).child("datePayment").child("end month").setValue(endMonth).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                startActivity(new Intent(GetPay.this, Start_Activity.class));
                                                finish();

                                            }
                                        });
                                    }
                                });
                            }
                        }).start();
                        break;
                    case "offices":
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("users").child("Offices").child(phoneNumberOfficess).child("resultPay").setValue("accept");
                                mDatabase.child("users").child("Offices").child(phoneNumberOfficess).child("datePayment").child("start day").setValue(day);
                                mDatabase.child("users").child("Offices").child(phoneNumberOfficess).child("datePayment").child("end day").setValue(endDay);
                                mDatabase.child("users").child("Offices").child(phoneNumberOfficess).child("datePayment").child("start month").setValue(month);
                                mDatabase.child("users").child("Offices").child(phoneNumberOfficess).child("datePayment").child("end month").setValue(endMonth).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                startActivity(new Intent(GetPay.this, MainOffices.class));
                                                finish();

                                            }
                                        });
                                    }
                                });

                            }
                        }).start();
                        break;
                }


                return false;
            } else if (url.startsWith("http://www.msn.com")) {
                webView.endViewTransition(webView);

                Toast.makeText(GetPay.this, "عملية غير مقبولة", Toast.LENGTH_LONG).show();
                mDatabase = FirebaseDatabase.getInstance().getReference();

                switch (type) {
                    case "pharmacies":
                        mDatabase.child("users").child("pharmacies").child(phoneNumberPharmacy).child("resultPay").setValue("failed");
                        mDatabase.child("users").child("pharmacies").child(phoneNumberPharmacy).child("datePayment").child("day").setValue(day);
                        mDatabase.child("users").child("pharmacies").child(phoneNumberPharmacy).child("datePayment").child("month").setValue(month);
                        startActivity(new Intent(GetPay.this, SearchProduct.class));
                        finish();

                        break;

                    case "offices":

                        mDatabase.child("users").child("Offices").child(phoneNumberOfficess).child("resultPay").setValue("failed");
                        mDatabase.child("users").child("Offices").child(phoneNumberOfficess).child("datePayment").child("day").setValue(day);
                        mDatabase.child("users").child("Offices").child(phoneNumberOfficess).child("datePayment").child("month").setValue(month);
                        startActivity(new Intent(GetPay.this, Start_Activity.class));
                        finish();
                        break;


                }
                return false;
            } else {
                view.loadUrl(url);
                webView.requestFocus(View.FOCUS_DOWN);
                return true;
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            Log.d("onReceivedSslError", "onReceivedSslError");
            //super.onReceivedSslError(view, handler, error);
        }
    }

    class UriChromeClient extends WebChromeClient {

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {
            mWebviewPop = new WebView(mContext);
            mWebviewPop.setVerticalScrollBarEnabled(false);
            mWebviewPop.setHorizontalScrollBarEnabled(false);
            mWebviewPop.setWebViewClient(new UriWebViewClient());
            mWebviewPop.setWebChromeClient(new UriChromeClient());
            mWebviewPop.getSettings().setJavaScriptEnabled(true);
            mWebviewPop.getSettings().setSavePassword(true);
            mWebviewPop.getSettings().setSaveFormData(true);
            builder = new AlertDialog.Builder(GetPay.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();
            builder.setTitle("");
            builder.setView(mWebviewPop);
            builder.setButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    mWebviewPop.destroy();
                    dialog.dismiss();
                }
            });
            builder.show();
            builder.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(mWebviewPop);
            resultMsg.sendToTarget();

            return true;
        }


        @Override
        public void onCloseWindow(WebView window) {
            try {
                mWebviewPop.destroy();
            } catch (Exception e) {

            }

            try {
                builder.dismiss();

            } catch (Exception e) {

            }


        }
    }

    public int checkSubscribe(int day, int month) {
        int n1;
        switch (month) {
            case 1:
                n1 = day + 30;
                endDay = n1 - 31;

                break;
            case 2:
                n1 = day + 30;
                endDay = n1 - 29;

                break;
            case 3:
                n1 = day + 30;
                endDay = n1 - 31;

                break;
            case 4:
                n1 = day + 30;
                endDay = n1 - 30;

                break;
            case 5:
                n1 = day + 30;
                endDay = n1 - 31;

                break;
            case 6:
                n1 = day + 30;
                endDay = n1 - 30;
                break;
            case 7:
                n1 = day + 30;
                endDay = n1 - 31;
                break;
            case 8:
                n1 = day + 30;
                endDay = n1 - 31;
                month = month + 1;
                break;
            case 9:
                n1 = day + 30;
                endDay = n1 - 30;
                break;
            case 10:
                n1 = day + 30;
                endDay = n1 - 31;
                break;
            case 11:
                n1 = day + 30;
                endDay = n1 - 30;
                break;
            case 12:
                n1 = day + 30;
                endDay = n1 - 31;
                break;
        }

        return endDay;
    }


}


